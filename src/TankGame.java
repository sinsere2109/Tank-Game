import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 * 
 * @author Tyren Villanueva
 *	CECS 277 Project 5
 */

/**TankGame
 * Frame of the game
 */

public class TankGame extends JFrame {
	private DrawingPanel panel;
	public static int difficulty;

	public TankGame() {
		this.setTitle("Tanks");
		setSize(670, 670);
		panel = new DrawingPanel();
		this.setContentPane(panel);
		setVisible(true);
	}

	public static void main(String args[]) {
		boolean testInput = true;
		int x = 0;
		while (testInput) {
			Scanner in = new Scanner(System.in);
			System.out.println("Enter the difficulty of the game");
			System.out.println("1.Easy");
			System.out.println("2.Medium");
			System.out.println("3.Hard");
			try {
				x = in.nextInt();
				if (x > -1 && x < 4) {
					testInput = false;
				}
			} catch (InputMismatchException im) {
				in.next();
				System.out.println("Invalid Input");
			}
		}
		setDifficulty(x);

		new TankGame();

	}

	/**
	 * Sets the difficulty of the game
	 * 
	 * @param num- difficulty number
	 */
	public static void setDifficulty(int num) {
		difficulty = num;

	}

	/**
	 */
	public static int getDifficulty() {
		return difficulty;
	}
}
