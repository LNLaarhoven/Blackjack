package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class BlackJack {
	
	private ArrayList<Card> deck;
	private char[] symbol = {'H', 'D', 'C', 'S'};
	private int amountOfPlayers = 0;
	
	private void initializeDeck() {
		this.deck = new ArrayList<>();
		String cardName;
		for (int i = 0; i < 4; i++) {
			for (int j = 2; j < 11; j++) {
				cardName = symbol[i] + Integer.toString(j);
				deck.add(new Card(cardName, j));
			}
			deck.add(new Card(symbol[i] + "J", 10));
			deck.add(new Card(symbol[i] + "Q", 10));
			deck.add(new Card(symbol[i] + "K", 10));
			deck.add(new Card(symbol[i] + "A", 11));
		}
	}
	
	public void printDeck() {
		for (int i = 0; i < 52; i++) {
			System.out.println(deck.get(i).getCardName());
		}
	}
	
	public BlackJack() {
		this.initializeDeck();
		Collections.shuffle(deck);
		this.startGame();
	}

	private void startGame() {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Type in a to add a player, then c to get an extra card, p to pass and q to quit.");
		String in = input.nextLine();
		do {
			if (this.amountOfPlayers > 0) {
				this.startRound(this.amountOfPlayers);
			}
			switch(in) {
			case "a":
				this.amountOfPlayers++;
				//@TODO add extra players and change while loop to check amount of players.
				break;
			case "c":
				//@TODO add card and value
			case "p":
				//@TODO players passes
			case "q":
				this.amountOfPlayers--;
			}
		} while(!(in.equals("q")));
	}

	private void startRound(int amountOfPlayers) {
		Dealer dealer = new Dealer();
		ArrayList<Player> players = new ArrayList<>();
		Collections.shuffle(deck);
		
		for (int i = 0; i < amountOfPlayers; i++) {
			Player player = new Player();
			players.add(player);
			player.getHand().add(this.drawCard());
			player.getHand().add(this.drawCard());
			player.calculateHand();
			
		}
		
	}
	
	private Card drawCard() {
		Card card = this.deck.get(0);
		this.deck.remove(card);
		return card;
	}
	
}
