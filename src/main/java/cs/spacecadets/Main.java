package cs.spacecadets;
import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Main {
    private final static int WIDTH = 320;
    private final static int HEIGHT = 240;
    private final static int OFFSET = 30;
    
    public static void main(String[] args) {
        Webcam camera = Webcam.getDefault();
        camera.setViewSize(new Dimension(WIDTH,HEIGHT));
        camera.open();

        JFrame frame = new JFrame("Circle Detector");
        Container panel = frame.getContentPane();
        panel.setLayout(new GridLayout(2,3));
        frame.setSize(3*(WIDTH+OFFSET),2*(HEIGHT+OFFSET));
        frame.setResizable(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        HashMap<Integer, GuiPanel> panelMap = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            GuiPanel guiPanel = new GuiPanel(i);
            panel.add(guiPanel);
            guiPanel.setSize(WIDTH+OFFSET, HEIGHT+OFFSET);
            panelMap.put(i, guiPanel);
        }
        
        BufferedImage image = camera.getImage();
        for (int i = 0; i < 6; i++) {
            panelMap.get(i).showImage(image);
        }
        
        frame.setVisible(true);
    }
}
