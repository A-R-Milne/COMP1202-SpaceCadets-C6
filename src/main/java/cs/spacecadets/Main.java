package cs.spacecadets;

import com.github.sarxos.webcam.Webcam;
import org.bytedeco.javacv.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws FrameGrabber.Exception {
        FrameGrabber frameGrabber = new OpenCVFrameGrabber(0); //Default camera is 0
        frameGrabber.start();
        Webcam camera = Webcam.getDefault();
        camera.open();

        JFrame window = new JFrame("Circle Detector");
        HashMap<Integer,GuiPanel> panelMap = new HashMap<>();
        for (int i=0; i<6; i++) {
            GuiPanel panel = new GuiPanel(i);
            window.add(panel);
            panel.setSize(300,300);
            panel.setLocation(300*(i/2),300*(i%2));
            panelMap.put(i,panel);
        }
        
        BufferedImage image = camera.getImage();
        for (int i=0; i<6; i++) {
            panelMap.get(i).showImage(image);
        }
    }
}
