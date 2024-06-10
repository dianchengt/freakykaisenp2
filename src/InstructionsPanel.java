import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InstructionsPanel extends JPanel implements ActionListener {

    private JButton backButton;
    private JFrame enclosingFrame;
    private BufferedImage sukunaPlush, gojoPlush;

    public InstructionsPanel(JFrame frame) {
        try{
            gojoPlush = ImageIO.read(new File("assets/gojoPlush.png"));
            sukunaPlush = ImageIO.read(new File("assets/sukunaPlush.png"));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        enclosingFrame = frame;
        backButton = new JButton("Back");
        add(backButton);
        backButton.addActionListener(this);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        backButton.setLocation(515, 435);
        g.setFont(new Font("Aerial", Font.BOLD,  20));

        g.drawString("Description:", 10, 25);

        g.setFont(new Font("Aerial", Font.PLAIN, 15));

        g.drawString("This game is all about combat! Each player can move around the screen and", 10, 40);
        g.drawString("shoot attacks at the opponent. Landing a hit will reduce the opponent's", 10, 55);
        g.drawString("health while charging up your domain bar. Once it is filled, you can use", 10, 70);
        g.drawString("your domain expansion which gives you an advantage in battle.", 10, 85);
        g.drawString("Good Luck and Have Fun!", 10, 100);

        g.setFont(new Font("Daun Penh:", Font.ITALIC, 20));
        g.setColor(Color.RED);

        g.drawString("Sukuna:", 10 , 160);

        g.setFont(new Font("Aerial", Font.PLAIN, 14));
        g.setColor(Color.BLACK);

        g.drawString("Movement: I , J , K , L", 10 , 180);
        g.drawString("Attack: Button to Press is O", 10, 200);
        g.drawString("Domain: Button to Press is U", 10, 220);

        g.setFont(new Font("Daun Penh", Font.ITALIC, 20));
        g.setColor(Color.BLUE);

        g.drawString("Gojo:", 268, 160);

        g.setFont(new Font("Aerial", Font.PLAIN, 14));
        g.setColor(Color.BLACK);

        g.drawString("Movement: W , A , S , D", 268, 180);
        g.drawString("Attack: Button to Press is E", 268, 200);
        g.drawString("Domain: Button to Press is Q", 268, 220);

        g.drawImage(sukunaPlush, 25, 300, null);
        g.drawImage(gojoPlush, 283, 300, null);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() instanceof JButton){
            JButton button = (JButton) e.getSource();
            if(button == backButton){
                WelcomeFrame f = new WelcomeFrame();
                enclosingFrame.setVisible(false);
            }
        }
    }

}
