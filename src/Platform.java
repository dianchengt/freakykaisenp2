import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Platform implements methods {

    private BufferedImage platform;
    private double xCoord;
    private double yCoord;

    public Platform(double xCoord, double yCoord){
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        try{
            platform = ImageIO.read(new File("assets/platform.png"));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public double getXCoord(){
        return xCoord;
    }

    public double getYCoord(){
        return yCoord;
    }

    public BufferedImage getImage(){
        return platform;
    }

    public Rectangle rect() {
        int imageHeight = getImage().getHeight();
        int imageWidth = getImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord - 20, imageWidth - 20, imageHeight);
        return rect;
    }

}
