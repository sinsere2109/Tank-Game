import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * 
 * @author Tyren Villanueva CECS 277 Project 5
 */

public class Missile {
	/**
	 * wallhit- true if the missile hit a wall
	 */
	private boolean wallhit;
	/**
	 * tankhit-true if missile hits tank
	 */
	private boolean tankhit;
	/**
	 * enemtRect- array list of Rectangles of enemy locations
	 */
	private Rectangle[] enemyRect;
	/**
	 * location- location of the missile
	 */
	private Point location;
	/**
	 * deadtank- index of dead tank when missile hits tank
	 */
	private int deadTank;

	public Missile(int x, int y) {

		tankhit = false;
		wallhit = false;
		location = new Point(x, y);

	}

	/**
	 * Moves the missile
	 * 
	 * @param x direction towards other tank
	 * @param y direction towards other tank
	 */
	public void move(double x, double y) {

		int dx = (int) (x * 10);
		int dy = (int) (y * 10);

		location.translate(dx, dy);

	}

	/**
	 * Loops through enemy array and creates rectangles based on the location of the
	 * enemies
	 * 
	 * @param e
	 */
	public void setEnemyRect(ArrayList<Tank> e) {
		enemyRect = new Rectangle[e.size()];

		for (int i = 0; i < e.size(); i++) {
			Rectangle enemy = new Rectangle(e.get(i).getX(), e.get(i).getY(), 25, 25);
			enemyRect[i] = enemy;
		}

	}

	/**
	 * @returns true if missile hits tank
	 */
	public boolean checkTankCollisions() {

		Rectangle missle = new Rectangle(getX(), getY(), 10, 10);
		for (int i = 0; i < enemyRect.length; i++) {

			if (missle.intersects(enemyRect[i])) {

				tankhit = true;

				setTankhit(i);
			}

		}

		return tankhit;

	}

	/**
	 * Sets the deadtank variable to the index of the tank that was hit
	 * 
	 * @param num- the index of the dead tank
	 */
	public void setTankhit(int num) {

		deadTank = num;

	}

	/**
	 * @returns index of dead tank in enemyTanks array
	 */
	public int getTankhit() {

		return deadTank;
	}

	public boolean checkWallCollision(ArrayList<Rectangle> p) {

		Rectangle missle = new Rectangle(getX(), getY(), 10, 10);
		for (int i = 0; i < p.size(); i++) {
			if (missle.intersects(p.get(i))) {

				wallhit = true;

			}

		}

		return wallhit;
	}

	/**
	 * Draws the missile
	 * 
	 * @param g-graphics
	 */
	public void draw(Graphics g) {

		g.fillRect(getX(), getY(), 10, 10);

	}

	/**
	 * @returns x location of missile
	 */
	public int getX() {
		return location.x;
	}

	/**
	 * @returns Y location of the missile
	 */
	public int getY() {
		return location.y;
	}
}
