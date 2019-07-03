package blackjack;

public class Dealer extends Player{
	public boolean isAllowedCard() {
		return this.getHandValue() < 17;
	}
}
