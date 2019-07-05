package blackjack;

public class Dealer extends Player{
	public boolean isAllowedCard() {
		return this.getSplitHands().get(0).getHandValue() < 17;
	}
}
