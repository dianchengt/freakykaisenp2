import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EndPanel extends JPanel implements ActionListener {

    private JFrame enclosingFrame;
    private String winner;
    private BufferedImage sukunaWin;
    private BufferedImage gojoWin;
    private JButton returnButton;

    public EndPanel(JFrame frame, String winner){
        enclosingFrame = frame;
        returnButton = new JButton("Main Menu");
        add(returnButton);
        returnButton.addActionListener(this);
        this.winner = winner;
        try {
            sukunaWin = ImageIO.read(new File("assets/sukuna.png"));
            gojoWin = ImageIO.read(new File("assets/gojo.png"));
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("GAME OVER", 135, 75);
        if (winner.equals("gojo")) {
            g.drawImage(gojoWin, 25, 150, null);
            g.drawString("Nah,", 150, 150);
            g.drawString("I'd win", 150, 175);
            //g.drawImage(gojoWin, 70, 100, null);
        }
        if (winner.equals("sukuna")) {
            g.setFont(new Font("Courier New", Font.BOLD, 18));
            g.drawImage(sukunaWin, 25, 150, null);
            g.drawString("Stand Proud.", 150, 150);
            g.drawString("You were Strong...", 150,175);
            //g.drawImage(sukunaWin, 120, 100, null);
        }
        returnButton.setLocation(0,0);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if(button == returnButton){
                WelcomeFrame w = new WelcomeFrame();
                enclosingFrame.setVisible(false);
            }
        }
    }
}