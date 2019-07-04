package blackjack;

import java.util.ArrayList;

public class Player {
	private ArrayList<Card> hand;
	private int handValue;
	private boolean isBusted;
	
	
	public Player() {
		this.setHand(new ArrayList<>());
		this.setHandValue(0);
		this.setBusted(false);
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public int getHandValue() {
		return handValue;
	}

	public void setHandValue(int handValue) {
		this.handValue = handValue;
	}

	public boolean isBusted() {
		return isBusted;
	}

	public void setBusted(boolean isBusted) {
		this.isBusted = isBusted;
	}

	public void calculateHand() {
		this.handValue = 0;
		for (int i = 0; i < this.hand.size(); i++) {
			this.handValue += this.hand.get(i).getCardValue();
		}
		if(!this.changeAceValue()) {
			this.setBusted(true);
		}
	}
	
	private boolean changeAceValue() {
		if(this.handValue > 21) {
			for (int i = 0; i < this.hand.size(); i++) {
				if (this.hand.get(i).getCardName().endsWith("A") && this.hand.get(i).getCardValue() == 11) {
					this.hand.get(i).setCardValue(1);
					this.handValue -= 10;
					
					System.out.println("Changed value of " + this.hand.get(i).getCardName() + " from 11 to 1.");
					
					if (this.handValue <= 21) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}
	
	public void printHand() {
		for (int i = 0; i < this.hand.size(); i++) {
			System.out.print(this.hand.get(i).getCardName() + " ");
		}
		System.out.print("\n");
	}
}
