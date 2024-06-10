import javax.swing.*;

public class EndFrame implements Runnable {

    private EndPanel panel;
    private String winner;

    public EndFrame(String winner) {
        JFrame frame = new JFrame("End Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        panel = new EndPanel(frame, winner);
        frame.add(panel);
        frame.setVisible(true);

        Thread thread = new Thread(this);
        thread.start();
    }

    public void run(){
        while(true){
            panel.repaint();
        }
    }
}
