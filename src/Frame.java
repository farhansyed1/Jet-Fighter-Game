import javax.swing.*;
import java.awt.*;

/**
 * Creates the Frame for the game which the canvas is then created on.
 *
 * @author Farhan Syed
 * @author Isac Hassle
 * @version 2022-05-19
 *
 */
public class Frame extends JFrame {

    Panel panel;

    public Frame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();
        if(screenHeight < 1000 || screenWidth < 1000){
            panel = new Panel(800);
        }
        else{
            panel = new Panel(1000);
        }
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setResizable(false);
        this.setTitle("Jet Fighter");

        this.addKeyListener(panel);
        this.setFocusable(true);
        this.requestFocus();

        this.add(panel);
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}