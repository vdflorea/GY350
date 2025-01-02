import java.awt.*;
public class PlayerBullet extends Sprite2D {

    public PlayerBullet(Image i, Dimension ws) {
        super(i, ws);
        ySpeed = 1; // Adjust as desired (how fast bullets move)
    }

    public void move() {
        y -= ySpeed; // Moving upwards
    }
}
