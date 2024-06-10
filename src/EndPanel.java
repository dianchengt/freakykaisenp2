import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EndPanel extends JPanel {

    private JFrame enclosingFrame;
    private String winner;
    private BufferedImage sukunaWin;
    private BufferedImage gojoWin;

    public EndPanel(JFrame frame, String winner){
        enclosingFrame = frame;
        this.winner = winner;
        try {
            sukunaWin = ImageIO.read(new File("Assets/sukunaWin.png"));
            gojoWin = ImageIO.read(new File("Assets/gojoWin.png"));
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("GAME OVER", 130, 50);
        if (winner.equals("gojo")) {
            g.drawString("I am the Strongest...", 70, 70);
            g.drawImage(gojoWin, 70, 100, null);
        }
        if (winner.equals("sukuna")) {
            g.setFont(new Font("Courier New", Font.BOLD, 18));
            g.drawString("Stand Proud. You were Strong...", 35, 85);
            g.drawImage(sukunaWin, 120, 100, null);
        }
    }
}