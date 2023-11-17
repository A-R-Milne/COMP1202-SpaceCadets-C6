package cs.spacecadets;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GuiPanel extends JLabel {
    private final int operator;

    public GuiPanel(int operator) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.operator = operator;
    }
    
    public void showImage(BufferedImage image) {
        BufferedImage imgGraphics = switch (operator) {
            case 1 -> greyscale(image);
            case 2 -> sobel(greyscale(image), 0);
            case 3 -> sobel(greyscale(image), 1);
            case 4 -> sobel(greyscale(image), 2);
            default -> image;
        };
        ImageIcon icon = new ImageIcon(imgGraphics);
        this.setIcon(icon);
    }
    
    private BufferedImage greyscale(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for(int x=0; x<image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) { //(x,y) is the co-ordinates of the pixel in the image we are looking at
                newImage.setRGB(x,y,image.getRGB(x,y));
            }
        }
        return newImage;
    }
    
    private BufferedImage sobel(BufferedImage image, int axis) {
        HashMap<Integer,Integer> xMap1 = makeMap(1,2,1);
        HashMap<Integer,Integer> xMap2 = makeMap(-1,0,1);
        HashMap<Integer,Integer> yMap1 = makeMap(1,0,-1);
        HashMap<Integer,Integer> yMap2 = makeMap(1,2,1);
        
        int xVal;
        int yVal;
        int newVal;
        int finalA;
        int finalB;
        
        BufferedImage newImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
        
        for(int x=0; x<image.getWidth(); x++) {
            for(int y=0; y<image.getHeight(); y++) { //(x,y) is the co-ordinates of the pixel in the image we are looking at
                xVal = 0;
                yVal = 0;
                for (int a = -1; a < 2; a++) {
                    for (int b = -1; b < 2; b++) {
                        if (a + x < 0) {
                            finalA = 0;
                        } else {
                            finalA = Math.min(a + x, image.getWidth()-1); //Extend image horizontally 1 pixel on each side
                        }
                        if (b + y < 0) {
                            finalB = 0;
                        } else {
                            finalB = Math.min(b + y, image.getHeight()-1); //Extend image vertically 1 pixel on each side
                        }
                        if (axis == 0 || axis == 2) {
                            xVal += getABGR(image,a,b,finalA,finalB,xMap1,xMap2);
                        } //Sobel operator on x
                        if (axis == 1 || axis == 2) {
                            yVal += getABGR(image,a,b,finalA,finalB,yMap1,yMap2);
                        } //Sobel operator on y
                    }
                }
                newVal = switch (axis) {
                    case 0 -> xVal;
                    case 1 -> yVal;
                    case 2 -> getGradientMagnitude(xVal,yVal); //Gradient Magnitude
                    default -> 0;
                };
                newImage.setRGB(x,y,newVal);
            }
        }
        return newImage;
    }
    
    /**Quickly make hash maps with 3 integers and specific integer keys**/
    private HashMap<Integer,Integer> makeMap(int x, int y, int z) {
        HashMap<Integer,Integer> map = new HashMap<>();
        map.put(0,x);
        map.put(1,y);
        map.put(2,z);
        return map;
    }
    
    private int getABGR(BufferedImage image, int a, int b, int x, int y, HashMap<Integer,Integer> map1, HashMap<Integer,Integer> map2) {
        int abgr = image.getRGB(x, y);
        int red = abgr&0xff;
        int green = (abgr>>8)&0xff;
        int blue = (abgr>>16)&0xff;
        
        red*=(map1.get(1-a) * map2.get(1-b));
        green*=(map1.get(1-a) * map2.get(1-b));
        blue*=(map1.get(1-a) * map2.get(1-b));
        
        return (red)+(green<<8)+(blue<<16);
    }
    
    private int getGradientMagnitude(int x, int y) {
        int redX = x&0xff;
        int greenX = (x>>8)&0xff;
        int blueX = (x>>16)&0xff;

        int redY = y&0xff;
        int greenY = (y>>8)&0xff;
        int blueY = (y>>16)&0xff;
        
        int finalRed = (int) Math.sqrt(Math.pow(redX, 2) + Math.pow(redY, 2));
        int finalGreen = (int) Math.sqrt(Math.pow(greenX, 2) + Math.pow(greenY, 2));
        int finalBlue = (int) Math.sqrt(Math.pow(blueX, 2) + Math.pow(blueY, 2));
        
        return (finalRed)+(finalGreen<<8)+(finalBlue<<16);
    }
}
