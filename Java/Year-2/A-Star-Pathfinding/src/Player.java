
import java.awt.Graphics;
import java.awt.Image;


public class Player {

	Image myImage;
	int x=0,y=0;
	int xSpeed=0, ySpeed=0;
	boolean hasMoved = false;

	public Player( Image i ) {
		myImage=i;
		x=10;
		y=35;
	}
	
	public void setXSpeed( int x ) {
		xSpeed=x;
	}
	
	public void setYSpeed( int y ) {
		ySpeed=y;
	}
	
	public void move(boolean map[][]) {
		int newx=x+xSpeed;
		int newy=y+ySpeed;
		if (newx == x && newy == y) {
			// Player has not moved
			hasMoved = false;
		} else {
			// Player has moved

			if (!map[newx][newy]) {
				x=newx;
				y=newy;
			}
			hasMoved = true;
		}
	}

	public boolean hasMoved() {
		//System.out.println(hasMoved);
		return hasMoved;
	}
	
	public void paint(Graphics g) {
		g.drawImage(myImage, x*20, y*20, null);
	}
	
}
