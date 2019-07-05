package blackjack;

import java.util.ArrayList;

public class Hand {
	private ArrayList<Card> hand;
	private int handValue;
	private boolean busted;

	public Hand() {
		this(new ArrayList<Card>());
	}

	public Hand(ArrayList<Card> hand) {
		this.hand = hand;
		this.calculateHand();
		this.busted = false;
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
		return busted;
	}

	public void setBusted(boolean busted) {
		this.busted = busted;
	}
	
	public void calculateHand() {
		this.handValue = 0;
		for (Card card: this.hand) {
			this.handValue += card.getCardValue();
		}
		if(!this.changeAceValue()) {
			this.setBusted(true);
		}
	}
	
	private boolean changeAceValue() {
		if(this.handValue > 21) {
			for (Card card: this.hand) {
				if (card.getCardName().endsWith("A") && card.getCardValue() == 11) {
					card.setCardValue(1);
					this.handValue -= 10;
					
					System.out.println("Changed value of " + card.getCardName() + " from 11 to 1.");
					
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
		for (Card card: this.hand) {
			System.out.print(card.getCardName() + " ");
		}
		System.out.print("\n");
	}
	
	public boolean canSplit() {
		if (this.hand.size() == 2) {
			if (this.hand.get(0).getCardName().substring(1, 2).equals(this.hand.get(1).getCardName().substring(1, 2))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
