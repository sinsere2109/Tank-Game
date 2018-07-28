import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;

/**
 * 
 * @author Tyren Villanueva CECS 277 Project 5
 */

public class DrawingPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener, Runnable {

	/**
	 * lives- The number of lives of the player
	 */
	private int lives;
	/**
	 * difficulty- The difficulty of the game
	 */
	private int difficulty;
	/**
	 * mouseX- the X- axis direction of the mouse
	 */
	private double mouseX;
	/**
	 * mouseY-the Y- axis direction of the mouse
	 */
	private double mouseY;
	/**
	 * t- Thread
	 */
	private Thread t;
	/**
	 * mapArray-Array holding values (1/0) from the text file
	 */
	private int[][] mapArray;
	/**
	 * boardsizei,boardsizej- The size of the row and column of the map
	 */
	private int boardsizei, boardsizej;
	/**
	 * dir- The direction of the tank
	 */
	private int dir;
	/**
	 * enemyTanks-The array of the enemy tanks
	 */
	private ArrayList<Tank> enemyTanks;
	/**
	 * playerTank- The users tank being controlled
	 */
	private Tank playerTank;
	/**
	 * enemyTanks 1-3- The enemy tanks the user is fighting and stored into the
	 * array
	 */
	private Tank enemyTank1;
	private Tank enemyTank2;
	private Tank enemyTank3;
	/**
	 * mapBorders- Array of rectangles for the map read from the file, this is used
	 * to check for collision using .intersects
	 */
	private ArrayList<Rectangle> mapBorders;

	public DrawingPanel() {

		/**
		 * Constructor initializes private members and Arrays
		 */
		mapBorders = new ArrayList<Rectangle>();
		lives = 5;
		difficulty = getDifficulty();
		this.setBackground(Color.WHITE);
		t = new Thread(this);
		playerTank = new Tank(120, 250, 25, 5, Color.RED);
		enemyTank1 = new Tank(500, 325, 25, 1, Color.ORANGE);
		enemyTank2 = new Tank(405, 95, 25, 1, Color.ORANGE);
		enemyTank3 = new Tank(405, 545, 25, 1, Color.ORANGE);
		enemyTanks = new ArrayList<Tank>();
		addTanks();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		t.start();
		/**
		 * Read map from array and add to arraylist
		 */
		try {
			Scanner read = new Scanner(new File("map1.txt"));
			boardsizei = read.nextInt();
			boardsizej = read.nextInt();
			mapArray = new int[boardsizei][boardsizej];
			for (int i = 0; i < boardsizei; i++) {
				for (int j = 0; j < boardsizej; j++) {
					mapArray[j][i] = read.nextInt();

				}

			}

		} catch (FileNotFoundException fnf) {
			System.out.println("File Not Found");
		}

		setBorderArray();
	}

	/**
	 * Creates the rectangles of the map
	 */
	public void setBorderArray() {
		for (int i = 0; i < boardsizei; i++) {
			for (int j = 0; j < boardsizej; j++) {
				if (mapArray[j][i] == 1) {
					Rectangle rect = new Rectangle(j * 50, i * 50, 50, 50);
					mapBorders.add(rect);
				}
			}
		}

	}

	/**
	 * Get mapBorders, array of rectangles used to check for collision
	 * 
	 * @return mapBorders array
	 */
	public ArrayList<Rectangle> getBorderArray() {
		return mapBorders;

	}

	/**
	 * @Override paintComponent used to paint/ draw to panel Called in run to repeat
	 *           generating animation of Tank Game
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (enemyTanks.size() == 0 || lives == 0) {

			resetGame();
		}

		for (int i = 0; i < boardsizei; i++) {
			for (int j = 0; j < boardsizej; j++) {
				if (mapArray[i][j] == 1) {
					g.setColor(Color.BLUE);
					g.fillRect(i * 50, j * 50, 50, 50);
				}

				else {
					g.setColor(Color.GRAY);
					g.fillRect(i * 50, j * 50, 50, 50);
				}
			}
		}

		playerTank.draw(g);
		playerTank.drawTurret(g, mouseX, mouseY);

		if (playerTank.gethasMissle()) {
			playerTank.moveplayerMissile((int) mouseX, (int) mouseY, getBorderArray(), enemyTanks);
			playerTank.drawMissile(g);
		}
		aiDirection();
		if (playerTank.getKilledTank() == true) {
			removeDeadTank();

		}

		for (int i = 0; i < enemyTanks.size(); i++) {

			enemyTanks.get(i).move(enemyTanks.get(i).getDirection(), getBorderArray());
			enemyTanks.get(i).draw(g);
			enemyTanks.get(i).drawEnemyTurret(g, playerTank.getX(), playerTank.getY());
			if (enemyTanks.get(i).gethasMissle() == false) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(800);
				if (randomInt < 10) {

					enemyTanks.get(i).createMissle();
				}

			} else if (enemyTanks.get(i).gethasMissle()) {
				enemyTanks.get(i).moveEnemyMissile(playerTank.getX(), playerTank.getY(), getBorderArray(), playerTank);
				enemyTanks.get(i).drawMissile(g);
			}
			if (enemyTanks.get(i).getKilledTank() == true) {
				removePlayerTank();
				enemyTanks.get(i).setKilledTank(false);

			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	/**
	 * Gets x and y value of mouse location
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();

	}

	/**
	 * Gets key pressed on keyboard for tank movement W-UP A-LEFT S-DOWN D-RIGHT
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_W:
			// handle up
			dir = 1;
			break;
		case KeyEvent.VK_S:
			dir = 2;
			// handle down
			break;
		case KeyEvent.VK_A:
			dir = 3;
			// handle left
			break;
		case KeyEvent.VK_D:
			dir = 4;
			// handle right
			break;

		}

		playerTank.move(dir, getBorderArray());
	}

	/**
	 * Adds tanks to array list depending on difficulty Difficulty 1-adds one tank
	 * Difficulty 2-adds two tanks Difficulty 3 adds three tanks
	 */
	public void addTanks() {
		if (difficulty == 1) {
			enemyTanks.add(enemyTank1);

		} else if (difficulty == 2) {
			enemyTanks.add(enemyTank1);
			enemyTanks.add(enemyTank2);

		} else if (difficulty == 3) {
			enemyTanks.add(enemyTank1);
			enemyTanks.add(enemyTank2);
			enemyTanks.add(enemyTank3);
		}

	}

	/**
	 * Resets player tanks to initial position and decrement lives
	 */
	public void removePlayerTank() {
		playerTank = new Tank(120, 250, 25, 5, Color.RED);
		lives--;

	}

	/**
	 * Remove dead enemy tank from game
	 */
	public void removeDeadTank() {
		enemyTanks.remove(playerTank.getdeadTank());
		playerTank.setKilledTank(false);
	}

	/**
	 * Resets game by setting tanks to starting locations and clearing enemy array
	 * list
	 */
	public void resetGame() {

		lives = 5;
		difficulty = getDifficulty();
		playerTank = new Tank(120, 250, 25, 5, Color.RED);
		enemyTank1 = new Tank(500, 325, 25, 1, Color.ORANGE);
		enemyTank2 = new Tank(405, 95, 25, 1, Color.ORANGE);
		enemyTank3 = new Tank(405, 545, 25, 1, Color.ORANGE);
		enemyTanks = new ArrayList<Tank>();
		addTanks();

	}

	/**
	 * Creates the direction for the enemies
	 *
	 */
	public void aiDirection() {

		Point playerloc = playerTank.getTankLocation();
		int playerX = playerloc.x;
		int playerY = playerloc.y;
		for (int i = 0; i < enemyTanks.size(); i++) {

			if (enemyTanks.get(i).getDirection() != 0 && enemyTanks.get(i).getCounter() < 60) {
				enemyTanks.get(i).setDirection(enemyTanks.get(i).getDirection());
				enemyTanks.get(i).incCounter();

			}

			if (enemyTanks.get(i).getCounter() == 60) {
				enemyTanks.get(i).setCounter(0);
				enemyTanks.get(i).setDirection(0);

			}

			if (enemyTanks.get(i).getX() < playerX && enemyTanks.get(i).getDirection() == 0) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(81);

				if (randomInt < 51) {
					enemyTanks.get(i).setDirection(4);
				} else if (randomInt > 50 && randomInt < 61) {
					enemyTanks.get(i).setDirection(3);
				} else if (randomInt > 60 && randomInt < 71) {
					enemyTanks.get(i).setDirection(2);
				} else if (randomInt > 70 && randomInt < 81) {
					enemyTanks.get(i).setDirection(1);
				}

			}

			else if (enemyTanks.get(i).getX() > playerX && enemyTanks.get(i).getDirection() == 0) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(81);
				if (randomInt < 51) {
					enemyTanks.get(i).setDirection(3);
				} else if (randomInt > 50 && randomInt < 61) {
					enemyTanks.get(i).setDirection(4);
				} else if (randomInt > 60 && randomInt < 71) {
					enemyTanks.get(i).setDirection(2);
				} else if (randomInt > 70 && randomInt < 81) {
					enemyTanks.get(i).setDirection(1);
				}

			}

			if (enemyTanks.get(i).getY() < playerY && enemyTanks.get(i).getDirection() == 0) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(81);
				if (randomInt < 51) {
					enemyTanks.get(i).setDirection(2);
				} else if (randomInt > 50 && randomInt < 61) {
					enemyTanks.get(i).setDirection(3);
				} else if (randomInt > 60 && randomInt < 71) {
					enemyTanks.get(i).setDirection(4);
				} else if (randomInt > 70 && randomInt < 81) {
					enemyTanks.get(i).setDirection(1);
				}
			} else if (enemyTanks.get(i).getY() > playerY && enemyTanks.get(i).getDirection() == 0) {

				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(81);
				if (randomInt < 51) {
					enemyTanks.get(i).setDirection(1);
				} else if (randomInt > 50 && randomInt < 61) {
					enemyTanks.get(i).setDirection(3);
				} else if (randomInt > 60 && randomInt < 71) {
					enemyTanks.get(i).setDirection(4);
				} else if (randomInt > 70 && randomInt < 81) {
					enemyTanks.get(i).setDirection(2);
				}
			}

		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (playerTank.gethasMissle() == false) {
			playerTank.createMissle();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	/**
	 * 
	 * @return the difficulty of the game
	 */
	public int getDifficulty() {
		return TankGame.getDifficulty();
	}

	/**
	 * @Override run()- infinite loop calling repaint() to create animation and put
	 *           thread to sleep(16)~ 60 fps
	 */
	@Override
	public void run() {
		while (true) {
			repaint();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {

			}

		}
	}

}
