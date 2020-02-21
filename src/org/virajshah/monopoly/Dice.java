package org.virajshah.monopoly;

/**
 * The Dice class allows for realistic random dice rolls. Where two separate
 * events are used to roll a dice. This allows for catching events such as
 * rolling doubles, and a mode-average roll of 7
 * 
 * @author Viraj Shah
 */
class Dice {
	/**
	 * Roll a single die
	 *
	 * @return A random number between 1 and 6, inclusive
	 */
	static int roll1() {
		return (int) (Math.random() * 6) + 1;
	}

	/**
	 * Roll two dices and return the rolls and a 2-element int array.
	 *
	 * @return The results of rolling two dices; returns (new int[2]), where both
	 *         integers a between 1 and 6, inclusive
	 */
	static int[] roll2() {
		int r1 = roll1();
		int r2 = roll1();
		return new int[] { r1, r2 };
	}
}
