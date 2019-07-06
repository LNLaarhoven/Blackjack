package blackjack;

import java.util.ArrayList;

/*
 * Class that represents the player. It hold one or more hands in an ArrayList.
 */
public class Player {
	private ArrayList<Hand> splitHands;

	public Player() {
		this.setSplitHands(new ArrayList<>());
	}
	
	/*
	 * Resets the player by initialising new objects. 
	 */
	public void resetPlayer() {
		this.splitHands = new ArrayList<>();
		this.splitHands.add(new Hand());
	}
	
	public ArrayList<Hand> getSplitHands() {
		return splitHands;
	}

	public void setSplitHands(ArrayList<Hand> splitHands) {
		this.splitHands = splitHands;
		this.splitHands.add(new Hand());
	}
	
	/* Check whether the player is allowed to split. It returns the index of the hand that can be split or it return -1 if it can't be split.*/
	public int isAllowedToSplit() {
		for (Hand hand: this.splitHands) {
			if (hand.canSplit()) {
				return this.splitHands.indexOf(hand);
			}
		}
		return -1;
	}
}
