package cs.spacecadets;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GuiPanel extends JPanel {
    private final int operator;

    public GuiPanel(int operator) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.operator = operator;
    }
    
    public void showImage(BufferedImage image) {
        BufferedImage imgGraphics;
        if (operator == 1) {imgGraphics = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_BYTE_GRAY);}
        else if (operator == 2) {imgGraphics = sobel(image,0);}
        else if (operator == 3) {imgGraphics = sobel(image,1);}
        else if (operator == 4) {imgGraphics = sobel(image,2);}
        else {imgGraphics = image;}
        Graphics graphics = imgGraphics.getGraphics();
        graphics.drawImage(image,0,0,null);
        paintComponent(graphics);
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
        
        BufferedImage newImage = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        
        for(int x=0; x<image.getWidth(); x++) {
            for(int y=0; y<image.getHeight(); y++) {
                xVal=0;
                yVal=0;
                for(int a=-1; a<2; a++) {
                    for(int b=-1; b<2; b++) {
                        if(a+x<0) {finalA=0;} else finalA = Math.min(a+x, image.getWidth()); //Extend image horizontally 1 pixel on each side
                        if(b+y<0) {finalB=0;} else finalB = Math.min(b+y, image.getWidth()); //Extend image vertically 1 pixel on each side
                        if(axis==0 || axis==2) {xVal+=(image.getRGB(finalA,finalB)*xMap1.get(a+1)*xMap2.get(b+1));} //Sobel operator on x
                        if(axis==1 || axis==2) {yVal+=(image.getRGB(finalA,finalB)*yMap1.get(a+1)*yMap2.get(b+1));} //Sobel operator on y
                    }
                }
                if(axis==0) {newVal = xVal;}
                else if(axis==1) {newVal = yVal;}
                else if(axis==2) {newVal = (int) Math.sqrt(Math.pow(xVal,2)+Math.pow(yVal,2));} //Gradient Magnitude
                else{newVal=0;}
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
}
