package blackjack;

import java.util.ArrayList;

/*
 * Class to represent one of the Player's hand
 */
public class Hand {
	private ArrayList<Card> hand;
	private int handValue;
	private boolean busted;

	public Hand() {
		this(new ArrayList<Card>());
	}

	public Hand(ArrayList<Card> hand) {
		this.hand = hand;
		this.busted = false;
		this.calculateHand();
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
	
	/*
	 * Method that calculates the new value of the hand.
	 */
	public void calculateHand() {
		this.handValue = 0;
		
		/* Add all the card values of all the cards together.*/
		for (Card card: this.hand) {
			this.handValue += card.getCardValue();
		}
		
		/* Change the value of an Ace if the value of the hand is over 21 (if possible)*/
		if(!this.changeAceValue()) {
			this.setBusted(true);
		}
	}
	
	/*
	 * Method that changes the ace values in the hand and returns true if the value then changes to less or equal to 21.
	 */
	private boolean changeAceValue() {
		if(this.handValue > 21) {
			/* Hand is maybe busted, loop through all the cards in the hand to search for an ace. */
			for (Card card: this.hand) {
				if (card.getCardName().endsWith("A") && card.getCardValue() == 11) {
					/* An ace is found, lower the value of the hand and the card*/
					card.setCardValue(1);
					this.handValue -= 10;
					
					System.out.println("Changed value of " + card.getCardName() + " from 11 to 1.");
					
					/*If the value is now less or equal to 21 the hand is still valid to play with. */
					if (this.handValue <= 21) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}
	
	/*
	 * Method to print the hand
	 */
	public void printHand() {
		for (Card card: this.hand) {
			System.out.print(card.getCardName() + " ");
		}
		System.out.print("\n");
	}
	
	/*
	 * Method to check if it is possible to split hands.
	 */
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
