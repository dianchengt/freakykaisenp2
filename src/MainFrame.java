import javax.swing.*;

public class MainFrame implements Runnable {

    private GraphicsPanel panel;

    public MainFrame(){
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1163, 650);
        frame.setLocationRelativeTo(null);
        panel =  new GraphicsPanel(frame);
        frame.add(panel);
        frame.setVisible((true));

        Thread thread = new Thread(this);
        thread.start();
    }

    public void run(){
        while(true){
            panel.repaint();
        }
    }
}
