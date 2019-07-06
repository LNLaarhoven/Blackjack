package blackjack;

/*
 * Class that hold the name of a card and the value of it.
 */
public class Card {
	private String cardName;
	private int cardValue;
	
	public Card() {
		this("H2");
	}
	
	public Card(String cardName) {
		this(cardName, 2);
	}
	
	public Card(String cardName, int cardValue) {
		this.cardName = cardName;
		this.cardValue = cardValue;
	}

	public String getCardName() {
		return this.cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public int getCardValue() {
		return this.cardValue;
	}

	public void setCardValue(int cardValue) {
		this.cardValue = cardValue;
	}
	
	
}
