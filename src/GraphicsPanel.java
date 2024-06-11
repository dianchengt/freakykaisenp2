import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class GraphicsPanel extends JPanel implements KeyListener, ActionListener {

    // general instance variables
    private JFrame enclosingFrame;
    private BufferedImage background, gojoIcon, sukunaIcon, backupBackground, gojoDomain, sukunaDomain, domainClash, heartImage;
    private Character playerOne, playerTwo;
    private boolean[] pressedKeys;
    private Timer timer;
    private int time;

    // instance variables for shooting
    private boolean rightPurpleHollowActive, leftPurpleHollowActive, rightDismantleActive, leftDismantleActive;
    private Projectile purpleHollow, rightDismantle, leftDismantle;
    private static int gojoIncrementer = 0;
    private static int sukunaIncrementer = 0;
    private long gojoShootCooldown, sukunaShootCooldown;
    private boolean gojoCheckRight, gojoCheckLeft;
    private boolean sukunaCheckRight, sukunaCheckLeft;

    // instance variables for jumping + platforms
    private boolean wKeyPressed, iKeyPressed;
    private boolean isGojoJumping, isSukunaJumping;
    private boolean isGojoFalling, isSukunaFalling;
    private long gojoJumpStartTime, sukunaJumpStartTime;
    private long gojoSlowJumpStartTime, sukunaSlowJumpStartTime;
    private boolean gojoSlowJump, sukunaSlowJump;
    private boolean gojoFirstTimeJump, sukunaFirstTimeJump;
    private double gojoStartJump, sukunaStartJump;
    private long gojoSlowFallStartTime, sukunaSlowFallStartTime;
    private boolean gojoSlowFall, sukunaSlowFall;
    private boolean gojoFirstTimeFall, sukunaFirstTimeFall;
    private Platform platformOne, platformTwo;
    private boolean gojoOnPlatform, sukunaOnPlatform;

    // instance variables for domain activation
    private boolean sukunaDomainActive, gojoDomainActive, bothDomainActive;
    private boolean gojoFirstLoop, sukunaFirstLoop;
    private Clip gojoClip, sukunaClip;
    private boolean pause, gojoPause, sukunaPause;
    private boolean playGojoDomain, playSukunaDomain;
    private long gojoDomainStartTime, sukunaDomainStartTime;

    // instance variables for health regeneration implementation
    private Projectile heart;
    private long heartStartTime;
    private boolean isHeartVisible;

    // constructor sets all instance variables to default states
    public GraphicsPanel(JFrame frame) {
        enclosingFrame = frame;
        playerOne = new Character(10, 380,  "assets/gojoleft.png", "assets/gojoright.png", true);
        playerTwo = new Character(1063, 380, "assets/sukunaleft.png", "assets/sukunaright.png", false);
        addKeyListener(this);
        pressedKeys = new boolean[128];
        setFocusable(true);
        time = 0;
        timer = new Timer(1000, this);
        playGojoDomain = true;
        playSukunaDomain = true;
        try {
            background = ImageIO.read(new File("assets/background.png"));
            backupBackground = ImageIO.read(new File("assets/background.png"));
            gojoIcon = ImageIO.read(new File("assets/gojoicon.png"));
            sukunaIcon = ImageIO.read(new File("assets/sukunaicon.png"));
            gojoDomain = ImageIO.read(new File("assets/unlimitedvoid.png"));
            sukunaDomain = ImageIO.read(new File("assets/malevolentkitchen.png"));
            domainClash = ImageIO.read(new File("assets/domainclash.png"));
            heartImage = ImageIO.read(new File("assets/heart.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        heart = new Projectile(559, 100, "assets/heart.png");

        gojoFirstLoop = true;
        rightPurpleHollowActive = false;
        leftPurpleHollowActive = false;
        purpleHollow = new Projectile(playerOne.getXCoord(), playerOne.getYCoord(), "assets/purplehollow.png");

        sukunaFirstLoop = true;
        rightDismantleActive = false;
        leftDismantleActive = false;
        leftDismantle = new Projectile(playerTwo.getXCoord(), playerTwo.getYCoord(), "assets/dismantleleft.png");
        rightDismantle = new Projectile(playerTwo.getXCoord(), playerTwo.getYCoord(), "assets/dismantleright.png");

        isGojoJumping = false;
        isGojoFalling = false;
        gojoJumpStartTime = 0;
        gojoSlowJumpStartTime = 0;
        gojoSlowJump = false;
        gojoFirstTimeJump = true;
        gojoStartJump = 0;
        gojoSlowFallStartTime = 0;
        gojoSlowFall = false;
        gojoFirstTimeFall = true;

        isSukunaJumping = false;
        isSukunaFalling = false;
        sukunaJumpStartTime = 0;
        sukunaSlowJumpStartTime = 0;
        sukunaSlowJump = false;
        sukunaFirstTimeJump = true;
        sukunaStartJump = 0;
        sukunaSlowFallStartTime = 0;
        sukunaSlowFall = false;
        sukunaFirstTimeFall = true;

        platformOne = new Platform(130, 300);
        platformTwo = new Platform(755, 300);

        pause = false;
        gojoPause = false;
        sukunaPause = false;

        gojoShootCooldown = 0;
        sukunaShootCooldown = 0;

        wKeyPressed = false;
        iKeyPressed = false;

        gojoCheckLeft = true;
        gojoCheckRight = true;
        sukunaCheckLeft = true;
        sukunaCheckRight = true;

        sukunaOnPlatform = false;
        gojoOnPlatform = false;

        heartStartTime = 0;
        isHeartVisible = false;

        gojoDomainStartTime = 0;
        sukunaDomainStartTime = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // drawing the background
        g.drawImage(background, 0, 0, null);
        if (!pause) {
            // ending the game and deciding the winner
            if (playerOne.getHealth() <= 0) {
                EndFrame e = new EndFrame("sukuna");
                enclosingFrame.setVisible(false);
            } else if (playerTwo.getHealth() <= 0) {
                EndFrame e = new EndFrame("gojo");
                enclosingFrame.setVisible(false);
            }
            // setting up the health bar, domain bar, and characters
            if (playerOne.isFacingRight()) {
                g.drawImage(playerOne.getRight(), (int) playerOne.getXCoord(), (int) playerOne.getYCoord(), null);
            } else {
                g.drawImage(playerOne.getLeft(), (int) playerOne.getXCoord(), (int) playerOne.getYCoord(), null);
            }
            if (playerTwo.isFacingRight()) {
                g.drawImage(playerTwo.getRight(), (int) playerTwo.getXCoord(), (int) playerTwo.getYCoord(), null);
            } else {
                g.drawImage(playerTwo.getLeft(), (int) playerTwo.getXCoord(), (int) playerTwo.getYCoord(), null);
            }
            g.drawImage(gojoIcon, 10, 20, null);
            g.drawImage(sukunaIcon, 1078, 20, null);
            g.setFont(new Font("Courier New", Font.ITALIC, 24));

            // drawing platforms
            g.drawImage(platformOne.getImage(), (int) platformOne.getXCoord(), (int) platformOne.getYCoord(), null);
            g.drawImage(platformTwo.getImage(), (int) platformTwo.getXCoord(), (int) platformTwo.getYCoord(), null);

            // writing names
            g.setColor(Color.BLUE);
            g.drawString("Gojo Satoru", 100, 40);
            g.drawRect(10, 20, 75, 73);
            g.drawRect(9, 19, 76, 74);
            g.drawRect(10, 20, 76, 74);
            g.setColor(Color.RED);
            g.drawString("Ryomen Sukuna", 863, 40);
            g.drawRect(1078, 20, 75, 76);
            g.drawRect(1077, 19, 76, 77);
            g.drawRect(1078, 20, 76, 77);

            // drawing health bars
            g.setColor(Color.BLACK);
            g.drawRect(100, 50, 200, 20);
            g.drawRect(863, 50, 200, 20);
            g.setColor(Color.RED);
            int startXOne = 100;
            for (int i = 0; i < playerOne.getHealth(); i++) {
                g.fillRect(startXOne, 50, 20, 20);
                startXOne += 20;
            }
            int startXTwo = 1043;
            for (int i = 0; i < playerTwo.getHealth(); i++) {
                g.fillRect(startXTwo, 50, 20, 20);
                startXTwo -= 20;
            }

            // drawing domain bars
            g.setColor(Color.BLACK);
            g.drawRect(100, 75, 200, 20);
            g.setColor(Color.YELLOW);
            startXOne = 100;
            for (int i = 0; i < playerOne.getDomainBar(); i++) {
                g.fillRect(startXOne, 75, 65, 20);
                startXOne += 45;
            }
            g.setColor(Color.BLACK);
            g.drawRect(863, 75, 200, 20);
            g.setColor(Color.YELLOW);
            startXTwo = 998;
            for (int i = 0; i < playerTwo.getDomainBar(); i++) {
                g.fillRect(startXTwo, 75, 65, 20);
                startXTwo -= 45;
            }

            // heart regeneration
            long currTime = System.currentTimeMillis();
            if (!isHeartVisible && currTime - heartStartTime >= 30000) {
                isHeartVisible = true;
                heart.setImage(heartImage);
                heartStartTime = currTime;
            }
            if (isHeartVisible) {
                if (heart.getImage() != null) {
                    g.drawImage(heart.getImage(), (int) heart.getXCoord(), (int) heart.getYCoord(), null);
                    if (playerOne.rect().intersects(heart.rect()) && playerOne.getHealth() < 10) {
                        playerOne.gainHealth();
                        heart.setImage(null);
                        isHeartVisible = false;
                        heartStartTime = currTime;
                    } else if (playerTwo.rect().intersects(heart.rect()) && playerTwo.getHealth() < 10) {
                        playerTwo.gainHealth();
                        heart.setImage(null);
                        isHeartVisible = false;
                        heartStartTime = currTime;
                    }
                }
            }
            // playerOne movement keys
            // a key: move left
            if (pressedKeys[65]) {
                playerOne.faceLeft();
                playerOne.moveLeft();
            }
            // d key: move right
            if (pressedKeys[68]) {
                playerOne.faceRight();
                playerOne.moveRight();
            }
            // w key: jump
            if (pressedKeys[87] && !isGojoJumping && !gojoSlowJump && !isGojoFalling && !gojoSlowFall) {
                wKeyPressed = true;
            }
            if (wKeyPressed && !isGojoJumping && !isGojoFalling) {
                gojoSlowJump = true;
                gojoJumpStartTime = System.currentTimeMillis();
                gojoSlowJumpStartTime = gojoJumpStartTime;
                wKeyPressed = false;
            }
            if (gojoSlowJump) {
                long slowCurrentTime = System.currentTimeMillis();
                if (gojoFirstTimeJump) {
                    gojoStartJump = playerOne.getYCoord();
                    gojoFirstTimeJump = false;
                }
                if (playerOne.getYCoord() < -50) {
                    gojoFirstTimeJump = true;
                    gojoSlowJump = false;
                    isGojoJumping = true;
                } else if (playerOne.getYCoord() <= gojoStartJump - 300) {
                    gojoFirstTimeJump = true;
                    gojoSlowJump = false;
                    isGojoJumping = true;
                } else if (slowCurrentTime - gojoSlowJumpStartTime >= 50) {
                    playerOne.setYCoord(playerOne.getYCoord() - 30);
                    gojoSlowJumpStartTime = slowCurrentTime;
                }
            }
            if (isGojoJumping) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - gojoJumpStartTime >= 500) {
                    isGojoJumping = false;
                    gojoSlowFall = true;
                    gojoSlowFallStartTime = currentTime;
                    gojoFirstTimeFall = true;
                }
            }
            if (gojoSlowFall) {
                long slowCurrentTime = System.currentTimeMillis();
                if (gojoFirstTimeFall) {
                    gojoFirstTimeFall = false;
                }
                if (playerOne.rect().intersects(platformTwo.rect())) {
                    playerOne.setYCoord(platformTwo.getYCoord() - playerOne.getHeight());
                    gojoSlowFall = false;
                    isGojoFalling = false;
                    gojoFirstTimeFall = true;
                    gojoOnPlatform = true;
                } else if (playerOne.rect().intersects((platformOne.rect()))) {
                    playerOne.setYCoord(platformTwo.getYCoord() - playerOne.getHeight());
                    gojoSlowFall = false;
                    isGojoFalling = false;
                    gojoFirstTimeFall = true;
                    gojoOnPlatform = true;
                } else if (playerOne.getYCoord() >= 380) {
                    gojoFirstTimeFall = true;
                    gojoSlowFall = false;
                    isGojoFalling = true;
                } else if (slowCurrentTime - gojoSlowFallStartTime >= 50) {
                    playerOne.setYCoord(playerOne.getYCoord() + 30);
                    gojoSlowFallStartTime = slowCurrentTime;
                }
            }
            if (gojoOnPlatform && pressedKeys[87]) {
                gojoSlowJump = true;
                gojoOnPlatform = false;
            } else if (gojoOnPlatform && !playerOne.rect().intersects(platformTwo.rect())) {
                gojoSlowFall = true;
                gojoOnPlatform = false;
            } else if (gojoOnPlatform && !playerOne.rect().intersects(platformOne.rect())) {
                gojoSlowFall = true;
                gojoOnPlatform = false;
            }
            if (isGojoFalling) {
                if (playerOne.hasLanded()) {
                    isGojoFalling = false;
                }
            }

            if(!gojoDomainActive) {
                // playerTwo movement keys
                // j key: move left
                if (pressedKeys[74]) {
                    playerTwo.faceLeft();
                    playerTwo.moveLeft();
                }
                // l key: move right
                if (pressedKeys[76]) {
                    playerTwo.faceRight();
                    playerTwo.moveRight();
                }
                // i key: jump
                if (pressedKeys[73] && !isSukunaJumping && !sukunaSlowJump && !isSukunaFalling && !sukunaSlowFall) {
                    iKeyPressed = true;
                }
                if (iKeyPressed && !isSukunaJumping && !isSukunaFalling) {
                    sukunaSlowJump = true;
                    sukunaJumpStartTime = System.currentTimeMillis();
                    sukunaSlowJumpStartTime = sukunaJumpStartTime;
                    iKeyPressed = false;
                }
                if (sukunaSlowJump) {
                    long slowCurrentTime = System.currentTimeMillis();
                    if (sukunaFirstTimeJump) {
                        sukunaStartJump = playerTwo.getYCoord();
                        sukunaFirstTimeJump = false;
                    }
                    if (playerTwo.getYCoord() < -50) {
                        sukunaFirstTimeJump = true;
                        sukunaSlowJump = false;
                        isSukunaJumping = true;
                    } else if (playerTwo.getYCoord() <= sukunaStartJump - 300) {
                        sukunaFirstTimeJump = true;
                        sukunaSlowJump = false;
                        isSukunaJumping = true;
                    } else if (slowCurrentTime - sukunaSlowJumpStartTime >= 50) {
                        playerTwo.setYCoord(playerTwo.getYCoord() - 30);
                        sukunaSlowJumpStartTime = slowCurrentTime;
                    }
                }
                if (isSukunaJumping) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - sukunaJumpStartTime >= 500) {
                        isSukunaJumping = false;
                        sukunaSlowFall = true;
                        sukunaSlowFallStartTime = currentTime;
                        sukunaFirstTimeFall = true;
                    }
                }
                if (sukunaSlowFall) {
                    long slowCurrentTime = System.currentTimeMillis();
                    if (sukunaFirstTimeFall) {
                        sukunaFirstTimeFall = false;
                    }
                    if (playerTwo.rect().intersects(platformTwo.rect())) {
                        playerTwo.setYCoord(platformTwo.getYCoord() - playerTwo.getHeight());
                        sukunaSlowFall = false;
                        isSukunaFalling = false;
                        sukunaFirstTimeFall = true;
                        sukunaOnPlatform = true;
                    } else if (playerTwo.rect().intersects((platformOne.rect()))) {
                        playerTwo.setYCoord(platformTwo.getYCoord() - playerTwo.getHeight());
                        sukunaSlowFall = false;
                        isSukunaFalling = false;
                        sukunaFirstTimeFall = true;
                        sukunaOnPlatform = true;
                    } else if (playerTwo.getYCoord() >= 380) {
                        sukunaFirstTimeFall = true;
                        sukunaSlowFall = false;
                        isSukunaFalling = true;
                    } else if (slowCurrentTime - sukunaSlowFallStartTime >= 50) {
                        playerTwo.setYCoord(playerTwo.getYCoord() + 30);
                        sukunaSlowFallStartTime = slowCurrentTime;
                    }
                }
                if (sukunaOnPlatform && pressedKeys[73]) {
                    sukunaSlowJump = true;
                    sukunaOnPlatform = false;
                } else if (sukunaOnPlatform && !playerTwo.rect().intersects(platformTwo.rect())) {
                    sukunaSlowFall = true;
                    sukunaOnPlatform = false;
                } else if (sukunaOnPlatform && !playerTwo.rect().intersects(platformOne.rect())) {
                    sukunaSlowFall = true;
                    sukunaOnPlatform = false;
                }
                if (isSukunaFalling) {
                    if (playerTwo.hasLanded()) {
                        isSukunaFalling = false;
                    }
                }
            }else{
                //controls get reversed bc of gojo's domain
                // l key: move left
                if (pressedKeys[76]) {
                    playerTwo.faceLeft();
                    playerTwo.moveLeft();
                }
                // j key: move right
                if (pressedKeys[74]) {
                    playerTwo.faceRight();
                    playerTwo.moveRight();
                }
                // k key: jump
                if (pressedKeys[75] && !isSukunaJumping && !sukunaSlowJump && !isSukunaFalling && !sukunaSlowFall) {
                    iKeyPressed = true;
                }
                if (iKeyPressed && !isSukunaJumping && !isSukunaFalling) {
                    sukunaSlowJump = true;
                    sukunaJumpStartTime = System.currentTimeMillis();
                    sukunaSlowJumpStartTime = sukunaJumpStartTime;
                    iKeyPressed = false;
                }
                if (sukunaSlowJump) {
                    long slowCurrentTime = System.currentTimeMillis();
                    if (sukunaFirstTimeJump) {
                        sukunaStartJump = playerTwo.getYCoord();
                        sukunaFirstTimeJump = false;
                    }
                    if (playerTwo.getYCoord() < -50) {
                        sukunaFirstTimeJump = true;
                        sukunaSlowJump = false;
                        isSukunaJumping = true;
                    } else if (playerTwo.getYCoord() <= sukunaStartJump - 300) {
                        sukunaFirstTimeJump = true;
                        sukunaSlowJump = false;
                        isSukunaJumping = true;
                    } else if (slowCurrentTime - sukunaSlowJumpStartTime >= 50) {
                        playerTwo.setYCoord(playerTwo.getYCoord() - 30);
                        sukunaSlowJumpStartTime = slowCurrentTime;
                    }
                }
                if (isSukunaJumping) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - sukunaJumpStartTime >= 500) {
                        isSukunaJumping = false;
                        sukunaSlowFall = true;
                        sukunaSlowFallStartTime = currentTime;
                        sukunaFirstTimeFall = true;
                    }
                }
                if (sukunaSlowFall) {
                    long slowCurrentTime = System.currentTimeMillis();
                    if (sukunaFirstTimeFall) {
                        sukunaFirstTimeFall = false;
                    }
                    if (playerTwo.rect().intersects(platformTwo.rect())) {
                        playerTwo.setYCoord(platformTwo.getYCoord() - playerTwo.getHeight());
                        sukunaSlowFall = false;
                        isSukunaFalling = false;
                        sukunaFirstTimeFall = true;
                        sukunaOnPlatform = true;
                    } else if (playerTwo.rect().intersects((platformOne.rect()))) {
                        playerTwo.setYCoord(platformTwo.getYCoord() - playerTwo.getHeight());
                        sukunaSlowFall = false;
                        isSukunaFalling = false;
                        sukunaFirstTimeFall = true;
                        sukunaOnPlatform = true;
                    } else if (playerTwo.getYCoord() >= 380) {
                        sukunaFirstTimeFall = true;
                        sukunaSlowFall = false;
                        isSukunaFalling = true;
                    } else if (slowCurrentTime - sukunaSlowFallStartTime >= 50) {
                        playerTwo.setYCoord(playerTwo.getYCoord() + 30);
                        sukunaSlowFallStartTime = slowCurrentTime;
                    }
                }
                if (sukunaOnPlatform && pressedKeys[75]) {
                    sukunaSlowJump = true;
                    sukunaOnPlatform = false;
                } else if (sukunaOnPlatform && !playerTwo.rect().intersects(platformTwo.rect())) {
                    sukunaSlowFall = true;
                    sukunaOnPlatform = false;
                } else if (sukunaOnPlatform && !playerTwo.rect().intersects(platformOne.rect())) {
                    sukunaSlowFall = true;
                    sukunaOnPlatform = false;
                }
                if (isSukunaFalling) {
                    if (playerTwo.hasLanded()) {
                        isSukunaFalling = false;
                    }
                }
            }

            // player one attack key
            if (gojoCheckLeft) {
                if (pressedKeys[69] && !playerOne.isFacingRight() && System.currentTimeMillis() - gojoShootCooldown >= 750) {
                    leftPurpleHollowActive = true;
                    gojoShootCooldown = System.currentTimeMillis();
                    gojoCheckRight = false;
                }
            }
            if (gojoCheckRight) {
                if (pressedKeys[69] && playerOne.isFacingRight() && System.currentTimeMillis() - gojoShootCooldown >= 750) {
                    rightPurpleHollowActive = true;
                    gojoShootCooldown = System.currentTimeMillis();
                    gojoCheckLeft = false;
                }
            }
            //player one shooting to the left
            if (!rightPurpleHollowActive) {
                if (leftPurpleHollowActive) {
                    if (gojoFirstLoop) {
                        purpleHollow.setXCoord(playerOne.getXCoord());
                        purpleHollow.setYCoord(playerOne.getYCoord());
                        gojoFirstLoop = false;
                    }
                    purpleHollow.setYCoord(purpleHollow.getYCoord());
                    g.drawImage(purpleHollow.getImage(), (int) purpleHollow.getXCoord() + gojoIncrementer, (int) purpleHollow.getYCoord(), null);
                    gojoIncrementer -= 1;
                    if (purpleHollow.rectOne().intersects(playerTwo.rect())) {
                        leftPurpleHollowActive = false;
                        gojoIncrementer = 0;
                        playerTwo.loseHealth();
                        if (playerOne.getDomainBar() < 4) {
                            playerOne.chargeDomain();
                        }
                        gojoFirstLoop = true;
                        purpleHollow.setXCoord(0);
                        purpleHollow.setYCoord(0);
                        gojoCheckLeft = true;
                        gojoCheckRight = true;
                    } else if (purpleHollow.getXCoord() + gojoIncrementer < 10) {
                        leftPurpleHollowActive = false;
                        gojoIncrementer = 0;
                        gojoFirstLoop = true;
                        purpleHollow.setXCoord(0);
                        purpleHollow.setYCoord(0);
                        gojoCheckLeft = true;
                        gojoCheckRight = true;
                    } else if (purpleHollow.rectOne().intersects(leftDismantle.rectTwo()) || purpleHollow.rectOne().intersects(rightDismantle.rectTwo())) {
                        leftPurpleHollowActive = false;
                        gojoIncrementer = 0;
                        gojoFirstLoop = true;
                        purpleHollow.setXCoord(0);
                        purpleHollow.setYCoord(0);
                        rightDismantleActive = false;
                        leftDismantleActive = false;
                        sukunaIncrementer = 0;
                        sukunaFirstLoop = true;
                        rightDismantle.setXCoord(0);
                        rightDismantle.setYCoord(0);
                        gojoCheckLeft = true;
                        gojoCheckRight = true;
                    }
                }
            }
            // player one shooting to the right
            if (!leftPurpleHollowActive) {
                if (rightPurpleHollowActive) {
                    if (gojoFirstLoop) {
                        purpleHollow.setXCoord(playerOne.getXCoord());
                        purpleHollow.setYCoord(playerOne.getYCoord());
                        gojoFirstLoop = false;
                    }
                    purpleHollow.setYCoord(purpleHollow.getYCoord());
                    g.drawImage(purpleHollow.getImage(), (int) purpleHollow.getXCoord() + gojoIncrementer, (int) purpleHollow.getYCoord(), null);
                    gojoIncrementer += 1;
                    if (purpleHollow.rectOne().intersects(playerTwo.rect())) {
                        rightPurpleHollowActive = false;
                        gojoIncrementer = 0;
                        playerTwo.loseHealth();
                        if (playerOne.getDomainBar() < 4) {
                            playerOne.chargeDomain();
                        }
                        gojoFirstLoop = true;
                        purpleHollow.setXCoord(0);
                        purpleHollow.setYCoord(0);
                        gojoCheckLeft = true;
                        gojoCheckRight = true;
                    } else if (purpleHollow.getXCoord() + gojoIncrementer > 1100) {
                        rightPurpleHollowActive = false;
                        gojoIncrementer = 0;
                        gojoFirstLoop = true;
                        purpleHollow.setXCoord(0);
                        purpleHollow.setYCoord(0);
                        gojoCheckLeft = true;
                        gojoCheckRight = true;
                    } else if (purpleHollow.rectOne().intersects(leftDismantle.rectTwo()) || purpleHollow.rectOne().intersects(rightDismantle.rectTwo())) {
                        rightPurpleHollowActive = false;
                        gojoIncrementer = 0;
                        gojoFirstLoop = true;
                        purpleHollow.setXCoord(0);
                        purpleHollow.setYCoord(0);
                        rightDismantleActive = false;
                        leftDismantleActive = false;
                        sukunaIncrementer = 0;
                        sukunaFirstLoop = true;
                        rightDismantle.setXCoord(0);
                        rightDismantle.setYCoord(0);
                        gojoCheckLeft = true;
                        gojoCheckRight = true;
                    }
                }
            }

            // player two attack key
            if (sukunaCheckLeft) {
                if (pressedKeys[79] && !playerTwo.isFacingRight() && System.currentTimeMillis() - sukunaShootCooldown >= 750) {
                    leftDismantleActive = true;
                    sukunaShootCooldown = System.currentTimeMillis();
                    sukunaCheckRight = false;
                }
            }
            if (sukunaCheckRight) {
                if (pressedKeys[79] && playerTwo.isFacingRight() && System.currentTimeMillis() - sukunaShootCooldown >= 750) {
                    rightDismantleActive = true;
                    sukunaShootCooldown = System.currentTimeMillis();
                    sukunaCheckLeft = false;
                }
            }
            // player two shooting to the right
            if (!leftDismantleActive) {
                if (rightDismantleActive) {
                    if (sukunaFirstLoop) {
                        rightDismantle.setXCoord(playerTwo.getXCoord());
                        rightDismantle.setYCoord(playerTwo.getYCoord());
                        sukunaFirstLoop = false;
                    }
                    rightDismantle.setYCoord(rightDismantle.getYCoord());
                    if(sukunaDomainActive){
                        g.drawImage(rightDismantle.getImage(), (int) rightDismantle.getXCoord() + sukunaIncrementer, (int) rightDismantle.getYCoord() - 65, null);
                        g.drawImage(rightDismantle.getImage(), (int) rightDismantle.getXCoord() + sukunaIncrementer, (int) rightDismantle.getYCoord(), null);
                        g.drawImage(rightDismantle.getImage(), (int) rightDismantle.getXCoord() + sukunaIncrementer, (int) rightDismantle.getYCoord() + 65, null);
                    }else {
                        g.drawImage(rightDismantle.getImage(), (int) rightDismantle.getXCoord() + sukunaIncrementer, (int) rightDismantle.getYCoord(), null);
                    }
                    sukunaIncrementer += 1;
                    if (rightDismantle.rectTwo().intersects(playerOne.rect())) {
                        rightDismantleActive = false;
                        sukunaIncrementer = 0;
                        playerOne.loseHealth();
                        if (playerTwo.getDomainBar() < 4) {
                            playerTwo.chargeDomain();
                        }
                        sukunaFirstLoop = true;
                        rightDismantle.setXCoord(0);
                        rightDismantle.setYCoord(0);
                        sukunaCheckLeft = true;
                        sukunaCheckRight = true;
                    } else if (rightDismantle.getXCoord() + sukunaIncrementer > 1100) {
                        rightDismantleActive = false;
                        sukunaIncrementer = 0;
                        sukunaFirstLoop = true;
                        rightDismantle.setXCoord(0);
                        rightDismantle.setYCoord(0);
                        sukunaCheckLeft = true;
                        sukunaCheckRight = true;
                    } else if (rightDismantle.rectTwo().intersects(purpleHollow.rectOne())) {
                        rightDismantleActive = false;
                        sukunaIncrementer = 0;
                        sukunaFirstLoop = true;
                        rightDismantle.setXCoord(0);
                        rightDismantle.setYCoord(0);
                        leftPurpleHollowActive = false;
                        rightPurpleHollowActive = false;
                        gojoIncrementer = 0;
                        gojoFirstLoop = true;
                        purpleHollow.setXCoord(0);
                        purpleHollow.setYCoord(0);
                        sukunaCheckLeft = true;
                        sukunaCheckRight = true;
                    }
                }
            }
            // player two shooting to the left
            if (!rightDismantleActive) {
                if (leftDismantleActive) {
                    if (sukunaFirstLoop) {
                        leftDismantle.setXCoord(playerTwo.getXCoord());
                        leftDismantle.setYCoord(playerTwo.getYCoord());
                        sukunaFirstLoop = false;
                    }
                    leftDismantle.setYCoord(leftDismantle.getYCoord());
                    if(sukunaDomainActive){
                        g.drawImage(leftDismantle.getImage(), (int) leftDismantle.getXCoord() + sukunaIncrementer, (int) leftDismantle.getYCoord() - 65, null);
                        g.drawImage(leftDismantle.getImage(), (int) leftDismantle.getXCoord() + sukunaIncrementer, (int) leftDismantle.getYCoord(), null);
                        g.drawImage(leftDismantle.getImage(), (int) leftDismantle.getXCoord() + sukunaIncrementer, (int) leftDismantle.getYCoord() + 65, null);
                    }else {
                        g.drawImage(leftDismantle.getImage(), (int) leftDismantle.getXCoord() + sukunaIncrementer, (int) leftDismantle.getYCoord(), null);
                    }
                    sukunaIncrementer -= 1;
                    if (leftDismantle.rectTwo().intersects(playerOne.rect())) {
                        leftDismantleActive = false;
                        sukunaIncrementer = 0;
                        playerOne.loseHealth();
                        if (playerTwo.getDomainBar() < 4) {
                            playerTwo.chargeDomain();
                        }
                        sukunaFirstLoop = true;
                        leftDismantle.setXCoord(0);
                        leftDismantle.setYCoord(0);
                        sukunaCheckLeft = true;
                        sukunaCheckRight = true;
                    } else if (leftDismantle.getXCoord() + sukunaIncrementer < 10) {
                        leftDismantleActive = false;
                        sukunaIncrementer = 0;
                        sukunaFirstLoop = true;
                        leftDismantle.setXCoord(0);
                        leftDismantle.setYCoord(0);
                        sukunaCheckLeft = true;
                        sukunaCheckRight = true;
                    } else if (leftDismantle.rectTwo().intersects(purpleHollow.rectOne())) {
                        leftDismantleActive = false;
                        sukunaIncrementer = 0;
                        sukunaFirstLoop = true;
                        leftDismantle.setXCoord(0);
                        leftDismantle.setYCoord(0);
                        leftPurpleHollowActive = false;
                        rightPurpleHollowActive = false;
                        gojoIncrementer = 0;
                        gojoFirstLoop = true;
                        purpleHollow.setXCoord(0);
                        purpleHollow.setYCoord(0);
                        sukunaCheckLeft = true;
                        sukunaCheckRight = true;
                    }
                }
            }
        }

        // playerOne domain keys
        if(!sukunaDomainActive && !sukunaPause) {
            if (pressedKeys[81] && playerOne.getDomainBar() == 4) {
                pause = true;
                gojoPause = true;
                gojoDomainStartTime = System.currentTimeMillis();
                playerOne.resetDomainBar();
            }
        }
        if (gojoPause) {
            if (playGojoDomain) {
                playGojoMusic();
                playGojoDomain = false;
            }
            // gojo's domain animation!!!
            if (System.currentTimeMillis() - gojoDomainStartTime >= 10500) {
                gojoDomainActive = true;
                gojoPause = false;
                pause = false;
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 10150) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo26.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 10000) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo25.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 9850) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo24.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 9600) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo23.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 9400) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo22.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 9200) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo21.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 9000) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo20.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 8500) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo19.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 8250) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo18.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 8000) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo17.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 7750) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo16.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 6750) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo15.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 6500) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo14.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 6250) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo13.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 6000) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo12.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 5750) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo11.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 5500) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo10.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 5250) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo9.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 5000) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo8.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 3000) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo7.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - gojoDomainStartTime >= 1150) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo6.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if(System.currentTimeMillis() - gojoDomainStartTime >= 800){
                try{
                    background = ImageIO.read(new File("gojoAnimation/gojo5.png"));
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }else if(System.currentTimeMillis() - gojoDomainStartTime >= 600){
                try{
                    background = ImageIO.read(new File("gojoAnimation/gojo4.png"));
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }else if(System.currentTimeMillis() - gojoDomainStartTime >= 400){
                try{
                    background = ImageIO.read(new File("gojoAnimation/gojo3.png"));
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            } else if(System.currentTimeMillis() - gojoDomainStartTime >= 200){
                try{
                    background = ImageIO.read(new File("gojoAnimation/gojo2.png"));
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }else if(System.currentTimeMillis() - gojoDomainStartTime >= 0) {
                try {
                    background = ImageIO.read(new File("gojoAnimation/gojo1.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if (gojoDomainActive) {
            background = gojoDomain;
            timer.start();
            if (time == 10) {
                timer.stop();
                time = 0;
                background = backupBackground;
                gojoDomainActive = false;
                playGojoDomain = true;
            }
        }

        // playerTwo domain keys
        if(!gojoDomainActive && !gojoPause) {
            if (pressedKeys[85] && playerTwo.getDomainBar() == 4) {
                pause = true;
                sukunaPause = true;
                sukunaDomainStartTime = System.currentTimeMillis();
                playerTwo.resetDomainBar();
            }
        }
        if(sukunaPause){
            if (playSukunaDomain) {
                playSukunaMusic();
                playSukunaDomain = false;
            }
            // sukuna's domain animation!!!
            if (System.currentTimeMillis() - sukunaDomainStartTime >= 14300) {
                sukunaDomainActive = true;
                sukunaPause = false;
                pause = false;
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 13500) {
                try {
                    background = ImageIO.read(new File("assets/malevolentkitchen.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 11800) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna32.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 9800) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna31.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 9600) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna30.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 9400) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna29.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 9200) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna28.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 9000) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna27.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 8700) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna26.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 8500) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna25.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 8300) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna24.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 8100) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna23.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 7900) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna22.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 7700) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna21.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 7500) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna20.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 7300) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna19.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 7100) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna18.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 6700) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna17.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 6250) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna16.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 5800) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna15.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 5350) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna14.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else if (System.currentTimeMillis() - sukunaDomainStartTime >= 4900) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna13.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 4450) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna12.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 4000) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna11.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 3500) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna10.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 3050) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna9.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 2650) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna8.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 2250) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna7.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 1850) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna6.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 1450) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna5.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 450) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna4.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 300) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna3.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 150) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna2.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (System.currentTimeMillis() - sukunaDomainStartTime >= 0) {
                try {
                    background = ImageIO.read(new File("sukunaAnimation/sukuna1.png"));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if (sukunaDomainActive) {
            background = sukunaDomain;
            timer.start();
            if (time == 7) {
                timer.stop();
                time = 0;
                background = backupBackground;
                sukunaDomainActive = false;
                playSukunaDomain = true;
            }
        }
    }

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() instanceof Timer) {
            time++;
        }
    }

    public static int getGojoIncrementer(){
        return gojoIncrementer;
    }

    public static int getSukunaIncrementer(){
        return sukunaIncrementer;
    }

    // methods to play sounds
    private void playGojoMusic(){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("assets/gojodomainmusic.wav").getAbsoluteFile());
            gojoClip = AudioSystem.getClip();
            gojoClip.open(audioInputStream);
            gojoClip.start();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void playSukunaMusic(){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("assets/sukunadomainmusic.wav").getAbsoluteFile());
            sukunaClip = AudioSystem.getClip();
            sukunaClip.open(audioInputStream);
            sukunaClip.start();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
