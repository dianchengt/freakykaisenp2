import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Character implements methods{

    private int health;
    private int domainBar;
    private BufferedImage charLeft, charRight;
    private double xCoord;
    private double yCoord;
    private boolean facingRight;
    private final double DISTANCE = 0.4;


    public Character(double xCoord, double yCoord, String leftFile, String rightFile, boolean facingRight) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.facingRight = facingRight;
        health = 10;
        domainBar = 0;
        try {
            charLeft = ImageIO.read(new File(leftFile));
            charRight = ImageIO.read(new File(rightFile));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public BufferedImage getImage() {
        if(facingRight){
            return charRight;
        }else{
            return charLeft;
        }
    }

    public BufferedImage getLeft() {
        return charLeft;
    }

    public BufferedImage getRight() {
        return charRight;
    }

    public double getXCoord() {
        return xCoord;
    }

    public double getYCoord() {
        return yCoord;
    }

    public int getHealth() {
        return health;
    }

    public int getDomainBar() {
        return domainBar;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void loseHealth() {
        health--;
    }

    public void gainHealth(){
        health ++;
    }

    public void chargeDomain() {
        domainBar++;
    }

    public void resetDomainBar() {
        domainBar = 0;
    }

    public void moveLeft() {
        if (!(xCoord < 0)) {
            xCoord -= DISTANCE;
        }
    }

    public void moveRight() {
        if (!(xCoord > 1080)) {
            xCoord += DISTANCE;
        }
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
    }

    public Rectangle rect() {
        int imageHeight = getImage().getHeight();
        int imageWidth = getImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }

    public boolean hasLanded(){
        return yCoord >= 300;
    }

    public void setYCoord(double yCoord){
        this.yCoord = yCoord;
    }

    public int getHeight(){
        return getImage().getHeight();
    }


}

