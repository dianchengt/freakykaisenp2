import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Projectile {

    private double xCoord;
    private double yCoord;
    private BufferedImage image;

    public Projectile(double xCoord, double yCoord, String file) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        try {
            image = ImageIO.read(new File(file));
        } catch (IOException e) {
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
        return image;
    }

    public void setXCoord(double newXCoord){
        xCoord = newXCoord;
    }

    public void setYCoord(double newYCoord){
        yCoord = newYCoord;
    }

    public Rectangle rect(){
        int imageHeight = getImage().getHeight();
        int imageWidth = getImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int)yCoord, imageWidth, imageHeight);
        return rect;
    }

    public Rectangle rectOne(){
        int imageHeight = getImage().getHeight();
        int imageWidth = getImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord + GraphicsPanel.getGojoIncrementer(), (int) yCoord, imageWidth - 40, imageHeight);
        return rect;
    }

    public Rectangle rectTwo(){
        int imageHeight = getImage().getHeight();
        int imageWidth = getImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord + GraphicsPanel.getSukunaIncrementer(), (int) yCoord, imageWidth - 45, imageHeight);
        return rect;
    }


    public void setImage(BufferedImage img){
        image = img;
    }
}

