import java.awt.*;

public abstract class Sprite2D {
    protected static Dimension WindowSize;
    protected double x,y;
    protected double xSpeed = 0;
    protected int ySpeed = 0;
    protected Image sprite;

    public Sprite2D (Image i, Dimension ws){
        sprite = i;
        WindowSize = ws;
    }

    public abstract void move();

    public void setPosition (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setXSpeed (double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void paint(Graphics g) {
        g.drawImage(sprite, (int)x, (int)y, null);
    }

}
