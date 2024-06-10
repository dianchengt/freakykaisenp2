import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WelcomePanel extends JPanel implements ActionListener {

    private JButton startGameButton;
    private JButton instructionsButton;
    private JFrame enclosingFrame;

    private BufferedImage gojo, sukuna, vs;

    public WelcomePanel(JFrame frame){
        try{
            gojo = ImageIO.read(new File("assets/gojo.png"));
            sukuna = ImageIO.read(new File("assets/sukuna.png"));
            vs = ImageIO.read(new File("assets/vs.png"));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        enclosingFrame = frame;
        startGameButton = new JButton("Start Game");
        instructionsButton = new JButton("How to Play");
        add(startGameButton);
        add(instructionsButton);
        startGameButton.addActionListener(this);
        instructionsButton.addActionListener(this);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Freaky Kaisen", 110, 50);
        startGameButton.setLocation(50, 100);
        instructionsButton.setLocation(250, 100);
        g.drawImage(gojo, 35, 150, null);
        g.drawImage(sukuna, 250, 140, null);
        g.drawImage(vs, 155, 155, null);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton){
            JButton button = (JButton) e.getSource();
            if(button == startGameButton){
                MainFrame f = new MainFrame();
                enclosingFrame.setVisible(false);
            }else{
                InstructionsFrame f = new InstructionsFrame();
                enclosingFrame.setVisible(false);
            }
        }
    }

}
