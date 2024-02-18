import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {
    private static final Dimension WindowSize = new Dimension(1200, 600);
    private BufferStrategy strategy;

    private static final int NUMALIENS = 30;
    private Alien[] AliensArray = new Alien[NUMALIENS];

    private ArrayList<PlayerBullet> bulletsList = new ArrayList<>();
    final int PLAYER_SHIP_SPEED = 10; // Adjust as desired
    private final int BULLET_DELAY = 250; // 250 ms
    private long lastShotTime = 0;

    private Spaceship PlayerShip;

    private String workingDirectory;
    private ImageIcon alienIcon1;
    private ImageIcon alienIcon2;
    private ImageIcon playerShipIcon;
    private ImageIcon playerBulletIcon;
    private Image alienImage1;
    private Image alienImage2;
    private Image playerShipImage;
    private Image playerBulletImage;

    // Image sizes (for collision detection)
    private int alienWidth;
    private int alienHeight;
    private int playerBulletWidth;
    private int playerBulletHeight;
    private int playerShipWidth;
    private int playerShipHeight;


    private boolean isWaveOver = false;
    private boolean isGameOver = false;
    private boolean isPlaying = false;

    private boolean isLeftArrowKeyPressed = false;
    private boolean isRightArrowKeyPressed = false;

    private int score = 0;
    private int highScore = 0;
    private int wave = 1;

    public InvadersApplication() {
        // Implement key event-handling
        addKeyListener(this);

        // Extract necessary images from project 'src' directory
        workingDirectory = System.getProperty("user.dir");
        alienIcon1 = new ImageIcon(workingDirectory + "\\src\\alien_ship_1.png");
        alienIcon2 = new ImageIcon(workingDirectory + "\\src\\alien_ship_2.png");
        playerShipIcon = new ImageIcon(workingDirectory + "\\src\\player_ship.png");
        playerBulletIcon = new ImageIcon(workingDirectory + "\\src\\bullet.png");
        alienImage1 = alienIcon1.getImage();
        alienImage2 = alienIcon2.getImage();
        playerShipImage = playerShipIcon.getImage();
        playerBulletImage = playerBulletIcon.getImage();

        // Extract image information
        // --> Assume that alienImage1 & alienImage2 are same size
        alienWidth = alienImage1.getWidth(null);
        alienHeight = alienImage1.getHeight(null);
        playerBulletWidth = playerBulletImage.getWidth(null);
        playerBulletHeight = playerBulletImage.getHeight(null);
        playerShipWidth = playerShipImage.getWidth(null);
        playerShipHeight = playerShipImage.getHeight(null);

        // Create and set up the window
        this.setTitle("Space Invaders (Finished)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Display the window, centred on the screen
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);

        // Implement double buffering
        createBufferStrategy(2);
        strategy = getBufferStrategy();

    }

    public void run() {

        // Run until wave is over (all Aliens deceased
        while (!isGameOver && !isWaveOver) {
            int numDeceasedAliens = 0;
            boolean edgeHit = false;

            // Iterate through each Alien (checking logic along the way)
            for (int i = 0; i < NUMALIENS && !isGameOver && !isWaveOver; i++) {
                Alien a = AliensArray[i];
                Spaceship s = PlayerShip; // For easier readability


                // Check for bullet hitting an Alien (also move bullet)
                Iterator iterator = bulletsList.iterator();
                while(iterator.hasNext()){
                    PlayerBullet b = (PlayerBullet) iterator.next();

                    // Check for collision
                    if ((a.x < b.x && a.x + alienWidth > b.x) ||
                            (b.x < a.x && b.x + playerBulletWidth > a.x)) {
                        if ((a.y < b.y && a.y + alienHeight > b.y) ||
                                (b.y < a.y && b.y + playerBulletHeight > a.y)) {
                            // Only remove bullets after colliding with alive Aliens
                            if (a.isAlive()) {
                                score += 10;
                                a.setDeceased();
                                iterator.remove(); // Remove bullet from list
                            } else {
                                b.move();
                            }
                        }
                    } else {
                        b.move();
                    }
                    // If bullet is out of bounds, remove from the list (also subtract score)
                    if (b.y < -playerBulletHeight) {
                        score -= 5;
                        iterator.remove();
                    }
                }

                // Check for alive Alien hitting PlayerShip
                if ((a.x < s.x && a.x+alienWidth > s.x) ||
                        (s.x < a.x && s.x+playerShipWidth > a.x)) {
                    if ((a.y < s.y && a.y+alienHeight > s.y) ||
                            (s.y < a.y && s.y+playerShipHeight > a.y)) {
                        if (a.isAlive()) {
                            isGameOver = true;
                        }
                    }
                }

                if (!a.isAlive()) {
                    numDeceasedAliens++;
                }
                if (numDeceasedAliens == NUMALIENS) {
                    isWaveOver = true;
                }

                // Check if any alive Alien has hit an edge (set once)
                if (a.isAlive() && a.hasHitEdge() && !edgeHit) {
                    edgeHit = true;
                }
            }

            // If any alive Alien has hit an edge this round, reverse ALL of their directions
            if (edgeHit) {
                for (int i = 0; i < NUMALIENS; i++) {
                    AliensArray[i].flipDirection();
                    AliensArray[i].moveDown();
                }
            }

            // Move Aliens after all game logic has been executed
            for (int i = 0; i < NUMALIENS; i++) {

                // Change x and y coordinates of each Alien (updated on next repaint())
                AliensArray[i].move();
            }

            // Move Spaceship smoothly
            if (isLeftArrowKeyPressed || isRightArrowKeyPressed) {
                PlayerShip.move();
            }


            // Call repaint() after each Alien is assigned new coordinates
            this.repaint();

            try {
                Thread.sleep(10); // "Delay" of 20ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void keyPressed(KeyEvent e) {

        System.out.println("isPlaying: " +isPlaying);
        // Press any key to start (set once)
        if (!isPlaying) {
            isPlaying = true;
        }

        // Spacebar Pressed:
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            shootBullet();
        }

        // Right Arrow Key Pressed:
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isRightArrowKeyPressed = true;
            PlayerShip.setDirection("right");
        }
        // Left Arrow Key Pressed:
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isLeftArrowKeyPressed = true;
            PlayerShip.setDirection("left");
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isLeftArrowKeyPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isRightArrowKeyPressed = false;
        }
    }

    public void keyTyped(KeyEvent e) {
        // Not required
    }

    public void shootBullet() {
        // Add another bullet to bullets ArrayList
        // --> Will be drawn on next repaint()

        long currentTime = System.currentTimeMillis();

        // Shoot a new bullet if not on timeout (delay)
        if (currentTime - lastShotTime > BULLET_DELAY) {
            PlayerBullet b = new PlayerBullet(playerBulletImage, WindowSize);
            b.setPosition(PlayerShip.x+(double)54/2, PlayerShip.y);
            bulletsList.add(b);
            lastShotTime = currentTime; // Update the latest shot time
        }
    }

    public void paint(Graphics g) {
        // Implement double buffering
        g = strategy.getDrawGraphics();

        // Dynamically retrieve the size of the window as it is resized by the user
        // -> paint() gets called each time window size is adjusted (among other things)
        int newWindowWidth = getWidth();
        int newWindowHeight = getHeight();

        // Reset background dynamically (cover rectangles from previous paint() call)
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, newWindowWidth, newWindowHeight);

        if (isPlaying) {

            // Draw the score text at a fixed location
            g.setColor(Color.WHITE); // Set the text color to white
            g.setFont(new Font("Monospaced", Font.BOLD,  20)); // Set the font

            // Display score (for current game)
            String scoreString = "Score: " + score;
            g.drawString(scoreString,  20,  60);

            // Display high-score (across all games)
            highScore = Math.max(score, highScore); // Determine high-score in real-time
            String highScoreString = "Best: " + highScore;
            g.drawString(highScoreString,  220,  60);

            // Display current wave number
            String waveString = "Wave: " + wave;
            g.drawString(waveString,  420,  60);

            // Call each individual Bullet's paint() method
            Iterator iterator = bulletsList.iterator();

            while (iterator.hasNext()){
                PlayerBullet b = (PlayerBullet) iterator.next();
                b.paint(g);
            }

            // Call each individual Alien's paint() method
            for (int i = 0; i < NUMALIENS; i++) {
                AliensArray[i].paint(g);
            }

            // Call PlayerShip's paint() method
            PlayerShip.paint(g);
        } else {

            g.setColor(Color.WHITE); // Set all text color to white

            // "GAME OVER" text
            g.setFont(new Font("Arial", Font.BOLD,  75));
            g.drawString("GAME OVER", newWindowWidth/2 - 220, newWindowHeight/2);

            // "Press any key to play" text
            g.setFont(new Font("Arial", Font.BOLD,  25));
            g.drawString("Press any key to play", newWindowWidth/2 - 120, newWindowHeight/2 + 50);

            // Controls text
            g.setFont(new Font("Arial", Font.ITALIC,  15));
            g.drawString("[ Arrow Keys to move, Space to fire ]", newWindowWidth/2 - 115, newWindowHeight/2 + 80);
        }

        // Implement double buffering
        strategy.show();
    }

    public static void main(String[] args) {
        InvadersApplication w = new InvadersApplication();

        // while true (until application is exited)
        // while user has not pressed a key, display "press any key to start"
        // if user presses a key (startnewgame())

        while (true) {
            while (!w.isPlaying) {
                w.repaint();
            }
            startNewGame(w);
        }
    }

    public static void startNewGame(InvadersApplication w) {
        int alienSpeed = 3;

        w.isGameOver = false; // Ensure isGameOver boolean is reset from previous game (if applicable)

        startNewWave(w, alienSpeed);
        Thread t = new Thread(w);
        t.start();
        // Game has started

        while (!w.isGameOver) {
            if (!t.isAlive()) { // Once thread terminates (game/wave over), start new wave
                w.isWaveOver = false; // Start new wave
                w.wave += 1; // Increment wave counter
                alienSpeed += 2; // Increase speed each wave
                startNewWave(w, alienSpeed);
                t = new Thread(w);
                t.start();
            }
        }

        // Game has finished
        //w.highScore = Math.max(w.score, w.highScore); // Set new high score
        w.score = 0; // Reset current score
        w.isPlaying = false; // No longer playing after game is over
    }

    // Resets the playing field
    public static void startNewWave(InvadersApplication w, int alienSpeed) {

        // Reset/instantiate all Alien Objects
        for (int i = 0; i < NUMALIENS; i++) {
            w.AliensArray[i] = new Alien(w.alienImage1, w.alienImage2, WindowSize);
        }

        // Reset/instantiate PlayerShip
        w.PlayerShip = new Spaceship(w.playerShipImage, WindowSize);
        w.PlayerShip.setXSpeed(w.PLAYER_SHIP_SPEED);

        // Adjust depending on NUMALIENS
        // --> (ROWS * COLS must equal NUMALIENS)
        final int ROWS =  5;
        final int COLS =  6;

        // Spacing for Alien grid
        final int GAP =  15;

        int yy =  75; // Starting y coordinate

        // Move Aliens to grid formation at start of each wave
        for (int row = 0; row < ROWS; row++) {
            int xx = 25; // Reset x coordinate for each new row

            for (int col = 0; col < COLS; col++) {
                // Calculate index for each Alien in AliensArray
                int index = row * COLS + col;

                // Set speed of Aliens (depending on wave) and position in grid
                w.AliensArray[index].setXSpeed(alienSpeed);
                w.AliensArray[index].setPosition(xx, yy);

                // Increment x coordinate for next Alien
                xx += w.alienWidth + GAP;
            }

            // Increment y coordinate for next row
            yy += w.alienWidth + GAP;
        }

    }
}
