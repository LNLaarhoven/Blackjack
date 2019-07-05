package blackjack;

import java.util.ArrayList;

public class Player {
	private boolean isBusted;
	private ArrayList<Hand> splitHands;

	public Player() {
		this.setSplitHands(new ArrayList<>());
	}
	
	public void resetPlayer() {
		this.splitHands = new ArrayList<>();
		this.splitHands.add(new Hand());
		this.isBusted = false;
	}
	
	public ArrayList<Hand> getSplitHands() {
		return splitHands;
	}

	public void setSplitHands(ArrayList<Hand> splitHands) {
		this.splitHands = splitHands;
		this.splitHands.add(new Hand());
	}
	
	public int isAllowedToSplit() {
		for (Hand hand: this.splitHands) {
			if (hand.canSplit()) {
				return this.splitHands.indexOf(hand);
			}
		}
		return -1;
	}
}
