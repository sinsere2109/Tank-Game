import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * 
 * @author Tyren Villanueva CECS 277 Project 5
 */

public class Tank {
	/**
	 * hasMissile- True if tanks has a missile
	 */
	private boolean hasMissile;
	/**
	 * killedTank-True if tanks has killed another tank
	 */
	private boolean killedTank;
	/**
	 * deadtank-If enemy tank is dead, deadtank is the index of the enemy in the
	 * array list
	 */
	private int deadtank;
	/**
	 * tankLoc-Location of the tank
	 */
	private Point tankLoc;
	/**
	 * tankColor- Color of the tank
	 */
	private Color tankColor;
	/**
	 * direction- direction of the tanks size- length and width of the tank
	 * rectangle
	 */
	private int direction, size;
	/**
	 * speed- speed of the tanks
	 */
	private int speed;
	/**
	 * turrentTipx-X location of the tank turrentTipy-Y location of the tank
	 */
	private int turretTipx;
	private int turretTipy;
	private int directionCounter;
	/**
	 * Array list of the missiles
	 */
	private ArrayList<Missile> missiles;

	public Tank(int x, int y, int radius, int s, Color c) {
		hasMissile = false;
		missiles = new ArrayList<Missile>();
		speed = s;
		tankLoc = new Point(x, y);
		tankColor = c;
		size = radius;

	}

	/**
	 * Draws the missile on the panel
	 * 
	 * @param g- graphics
	 */
	public void drawMissile(Graphics g) {
		if (hasMissile == true) {
			missiles.get(0).draw(g);
		}
	}

	/**
	 * Creates a missile for the tank and adds it to the array list
	 */
	public void createMissle() {
		hasMissile = true;
		Missile tankMissile = new Missile(turretTipx, turretTipy);
		missiles.add(0, tankMissile);
	}

	/**
	 * Moves the player missile
	 * 
	 * @param mouseX-The x location of the mouse
	 * @param mouseY- The y location of the mouse
	 * @param points- The array list of rectangles of the map borders
	 * @param enemyTanks- array list of the enemy tanks
	 */
	public void moveplayerMissile(int mouseX, int mouseY, ArrayList<Rectangle> points, ArrayList<Tank> enemyTanks) {
		missiles.get(0).setEnemyRect(enemyTanks);
		int midx = (int) tankLoc.getX() + 12;
		int midy = (int) tankLoc.getY() + 12;
		int dx = (mouseX - midx);
		int dy = (mouseY - midy);
		double magnitude = Math.sqrt((dx * dx) + (dy * dy));
		if (missiles.get(0).checkWallCollision(points) == false && missiles.get(0).checkTankCollisions() == false) {
			missiles.get(0).move(dx / magnitude, dy / magnitude);
		} else if (missiles.get(0).checkTankCollisions() == true) {
			killedTank = true;
			deadtank = missiles.get(0).getTankhit();
			missiles.clear();
			hasMissile = false;
		} else {
			missiles.clear();
			hasMissile = false;

		}
	}

	/**
	 * Moves the enemy missile
	 * 
	 * @param playerX -The x location of the player
	 * @param         playerY- The y location of the player
	 * @param         points- The array list of rectangles of the map borders
	 * @param t       -Player tank
	 */
	public void moveEnemyMissile(int playerX, int playerY, ArrayList<Rectangle> points, Tank t) {
		ArrayList<Tank> playerTank = new ArrayList<Tank>();
		playerTank.add(t);
		missiles.get(0).setEnemyRect(playerTank);
		int midx = (int) tankLoc.getX() + 12;
		int midy = (int) tankLoc.getY() + 12;
		int dx = (playerX - midx);
		int dy = (playerY - midy);
		double magnitude = Math.sqrt((dx * dx) + (dy * dy));
		if (missiles.get(0).checkWallCollision(points) == false && missiles.get(0).checkTankCollisions() == false) {
			missiles.get(0).move(dx / magnitude, dy / magnitude);
		} else if (missiles.get(0).checkTankCollisions() == true) {
			killedTank = true;
			missiles.clear();
			hasMissile = false;
		}

		else {
			missiles.clear();
			hasMissile = false;
		}

	}

	/**
	 * 
	 * @returns true if the tank has killed another tank
	 */
	public boolean getKilledTank() {

		return killedTank;
	}

	/**
	 * Sets the boolean killedTank
	 * 
	 * @param bool
	 */
	public void setKilledTank(boolean bool) {
		killedTank = bool;

	}

	/**
	 * 
	 * @returns the index of the dead tank
	 */
	public int getdeadTank() {
		return deadtank;
	}

	/**
	 * 
	 * @return true if the tanks has a missile
	 */
	public boolean gethasMissle() {

		return hasMissile;

	}

	/**
	 * @return the location of the tank
	 */
	public Point getTankLocation() {
		return tankLoc;
	}

	/**
	 * 
	 * @return the X location of the tank
	 */
	public int getX() {
		return tankLoc.x;
	}

	/**
	 * @returns the Y location of the tank
	 */
	public int getY() {
		return tankLoc.y;
	}

	/**
	 * @return the color of the tank
	 */
	public Color getTankColor() {
		return tankColor;

	}

	/**
	 * Set the direction
	 * 
	 * @param num - the direction of the tank
	 */
	public void setDirection(int num) {

		direction = num;
	}

	/**
	 * @returns the direction
	 */
	public int getDirection() {

		return direction;
	}

	/**
	 * Draws the tank
	 * 
	 * @param g- graphics
	 */
	public void draw(Graphics g) {
		g.setColor(tankColor);
		g.fillRect((int) tankLoc.getX(), (int) tankLoc.getY(), size, size);

	}

	/**
	 * Draws the turret of the tank
	 * 
	 * @param g- graphics
	 * @param mouseX- The X location of the mouse
	 * @param mouseY- The Y location of the mouse
	 */
	public void drawTurret(Graphics g, double mouseX, double mouseY) {

		double dx = mouseX - tankLoc.getX() + 12;
		double dy = mouseY - tankLoc.getY() + 12;
		double magnitude = Math.sqrt((dx * dx) + (dy * dy));
		int newY = (int) ((dy) * (40 / magnitude) + (tankLoc.getY() + 12));
		int newX = (int) ((dx) * (40 / magnitude) + (tankLoc.getX() + 12));
		g.setColor(Color.BLACK);
		g.drawLine((int) (tankLoc.getX() + 12), (int) (tankLoc.getY() + 12), newX, newY);
		turretTipx = newX;
		turretTipy = newY;
	}

	/**
	 * Draws the turret for the enemy
	 * 
	 * @param g- graphics
	 * @param playerLocationx-the x location of the player
	 * @param playerLocationy- the y location of the player
	 */
	public void drawEnemyTurret(Graphics g, double playerLocationx, double playerLocationy) {
		double dx = playerLocationx - tankLoc.getX() + 12;
		double dy = playerLocationy - tankLoc.getY() + 12;
		double magnitude = Math.sqrt((dx * dx) + (dy * dy));
		int newY = (int) ((dy) * (40 / magnitude) + (tankLoc.getY() + 12));
		int newX = (int) ((dx) * (40 / magnitude) + (tankLoc.getX() + 12));
		g.setColor(Color.BLACK);
		g.drawLine((int) (tankLoc.getX() + 12), (int) (tankLoc.getY() + 12), newX, newY);
		turretTipx = newX;
		turretTipy = newY;
	}

	/**
	 * @returns the X location of the tip of the turret
	 */
	public int getTurretX() {
		return turretTipx;
	}

	/**
	 * @returns the Y location of the tip of the turret
	 */
	public int getTurretY() {
		return turretTipy;
	}

	/**
	 * Moves the player's tank
	 * 
	 * @param dir direction of the tank
	 * @param p   Array list of rectangles to set as boundaries the the player
	 */
	public void move(int dir, ArrayList<Rectangle> p) {
		boolean hit = false;
		if (dir == 1) {
			Rectangle playerTank = new Rectangle((int) tankLoc.getX(), (int) tankLoc.getY() - speed, size, size);
			for (int i = 0; i < p.size(); i++) {
				if (playerTank.intersects(p.get(i))) {
					hit = true;
				}

			}

			if (hit == false) {
				tankLoc.y = tankLoc.y - speed;
			}

		}

		else if (dir == 2) {
			Rectangle playerTank = new Rectangle((int) tankLoc.getX(), (int) tankLoc.getY() + speed, size, size);
			for (int i = 0; i < p.size(); i++) {
				if (playerTank.intersects(p.get(i))) {
					hit = true;
				}

			}
			if (hit == false) {
				tankLoc.y = tankLoc.y + speed;
			}

		}

		else if (dir == 3) {
			Rectangle playerTank = new Rectangle((int) tankLoc.getX() - speed, (int) tankLoc.getY(), size, size);
			for (int i = 0; i < p.size(); i++) {
				if (playerTank.intersects(p.get(i))) {
					hit = true;
				}

			}
			if (hit == false) {
				tankLoc.x = tankLoc.x - speed;
			}

		}

		else if (dir == 4) {

			Rectangle playerTank = new Rectangle((int) tankLoc.getX() + speed, (int) tankLoc.getY(), size, size);
			for (int i = 0; i < p.size(); i++) {

				if (playerTank.intersects(p.get(i))) {
					hit = true;
				}

			}
			if (hit == false) {
				tankLoc.x = tankLoc.x + speed;
			}

		}
	}

	/**
	 * Increments the counter of the direction of the ai movement Ai travels in the
	 * same direction until counter is zero
	 */
	public void incCounter() {
		directionCounter++;
	}

	/**
	 * @returns Counter of ai movement direction
	 */
	public int getCounter() {
		return directionCounter;

	}

	/**
	 * @param num- the counter
	 */
	public void setCounter(int num) {
		directionCounter = num;
	}
}
