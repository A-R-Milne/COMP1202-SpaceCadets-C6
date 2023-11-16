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
            case 2 -> sobel(image, 0);
            case 3 -> sobel(image, 1);
            case 4 -> sobel(image, 2);
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
        HashMap<Integer,Integer> xMap2 = makeMap(1,0,-1);
        HashMap<Integer,Integer> yMap1 = makeMap(1,0,-1);
        HashMap<Integer,Integer> yMap2 = makeMap(1,2,1);
        
        int xVal;
        int yVal;
        int newVal;
        int finalA;
        int finalB;
        
        BufferedImage newImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
        
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
                            xVal += (image.getRGB(finalA, finalB) * xMap1.get(a + 1) * xMap2.get(b + 1));
                        } //Sobel operator on x
                        if (axis == 1 || axis == 2) {
                            yVal += (image.getRGB(finalA, finalB) * yMap1.get(a + 1) * yMap2.get(b + 1));
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
    
    private int getGradientMagnitude(int x, int y) {
        int redX = x%256;
        int greenX = x%(256*256)-redX;
        int blueX = x%(256*256*256)-greenX-redX;
        int alphaX = x-blueX-greenX-redX;

        int redY = y%256;
        int greenY = y%(256*256)-redY;
        int blueY = y%(256*256*256)-greenY-redY;
        int alphaY = y-blueY-greenY-redY;
        
        int finalRed = (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        int finalGreen = (int) Math.sqrt(Math.pow(greenX, 2) + Math.pow(greenY, 2));
        int finalBlue = (int) Math.sqrt(Math.pow(blueX, 2) + Math.pow(blueY, 2));
        int finalAlpha = (int) Math.sqrt(Math.pow(alphaX, 2) + Math.pow(alphaY, 2));

        System.out.println(finalRed+finalGreen+finalBlue+finalAlpha);
        return finalRed+finalGreen+finalBlue+finalAlpha;
    }
}
