package cs.spacecadets;
import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Main {
    private final static int IMAGES = 6;
    private final static int WIDTH = 320;
    private final static int HEIGHT = 240;
    private final static int OFFSET = 30;
    
    public static void main(String[] args) {
        Webcam camera = Webcam.getDefault();
        camera.setViewSize(new Dimension(WIDTH,HEIGHT));
        camera.open();

        JFrame frame = new JFrame("Circle Detector");
        Container bigPanel = frame.getContentPane();
        bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));
        frame.setSize(900,600);
        frame.setResizable(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        Container leftPanel = frame.getContentPane();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        Container rightPanel = frame.getContentPane();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
        
        HashMap<Integer, GuiPanel> panelMap = new HashMap<>();
        addPanels(0, panelMap, leftPanel);
        addPanels(3, panelMap, rightPanel);
        
        BufferedImage image = camera.getImage();
        for (int i = 0; i < 6; i++) {
            panelMap.get(i).showImage(image);
        }
        
        frame.setVisible(true);
    }
    
    private static void addPanels(int add, HashMap<Integer, GuiPanel> panelMap, Container jPanel) {
        for (int j = 0; j < 3; j++) {
            int i = j+add;
            GuiPanel panel = new GuiPanel(i);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setSize(300, 300);
            panel.setLocation(300 * (i/2), 300 * (i%2));
            panelMap.put(i, panel);
            jPanel.add(panel);
        }
    }
}
