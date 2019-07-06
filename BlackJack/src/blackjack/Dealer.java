package blackjack;

/*
 * Class that represents the dealer. It inherits from Player and has got a method to determine if it is allowed to draw a card.
 */
public class Dealer extends Player{
	public boolean isAllowedCard() {
		return this.getSplitHands().get(0).getHandValue() < 17;
	}
}
