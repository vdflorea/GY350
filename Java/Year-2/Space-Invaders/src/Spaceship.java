import java.awt.*;

public class Spaceship extends Sprite2D {
    private int direction;

    public Spaceship(Image i, Dimension ws) {
        super(i, ws);

        // Spawn the playership in the middle of the player area
        x = (double)WindowSize.width / 2;

        // Spawn the playership in the middle of the bottom 1/4 of the window height
        double playerArea = WindowSize.height - (0.75* WindowSize.height); // Size of player area
        y = WindowSize.height - (playerArea / 2);
    }

    public void move() {
        if (direction == 1) { // Move RIGHT
            if (x + xSpeed < WindowSize.width - sprite.getWidth(null)) { // Prevent PlayerShip from going out of bounds
                x += xSpeed;
            }
        } else if (direction == -1) { // Move LEFT
            if (x - xSpeed > 0) { // Prevent PlayerShip from going out of bounds
                x -= xSpeed;
            }
        }
    }

    public void setDirection(String direction) {
        // Used "right"/"left" strings for code readability purposes
        if (direction.equals("right")) {
            this.direction = 1;
        } else if (direction.equals("left")) {
            this.direction = -1;
        }
    }

}
