import java.awt.*;

public class Alien extends Sprite2D {
    private int direction = 1; // Each Alien will move in the right direction at first

    // Store Alien's images within its class
    private Image image1;
    private Image image2;

    private int framesDrawn = 0; // Used for animation
    private boolean isAlive = true;

    public Alien(Image i, Image i2, Dimension ws) {
        super(i, ws); // Set Alien's image to 'i' at first

        // Store these images for animation later
        image1 = i;
        image2 = i2;

        ySpeed = 15; // Adjust as necessary (How many pixels Aliens move down IF edge is hit)
    }

    public void move() {
        if (direction == 1) { // Move RIGHT
            x += xSpeed;
        } else if (direction == -1) { // Move LEFT
            x -= xSpeed;
        }
    }

    public void moveDown() {
        y += ySpeed;
    }

    public boolean hasHitEdge() {
        if (x >= WindowSize.width - sprite.getWidth(null)) {
            return true;
        }
        if (x <= 0) {
            return true;
        }
        return false;
    }

    public void setDeceased() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void flipDirection() {
        direction *= -1;
    }

    @Override
    public void paint (Graphics g) {
        framesDrawn++;

        if (isAlive) {
            if ( framesDrawn%100<50 ) {
                g.drawImage(image1, (int) x, (int) y, null);
            } else {
                g.drawImage(image2, (int) x, (int) y, null);
            }
        }
    }

    public int getDirection() {
        return direction;
    }
}
