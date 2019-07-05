package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class BlackJack {
	
	private ArrayList<Card> deck;
	private ArrayList<Player> players;
	private Dealer dealer;
	private char[] symbol = {'H', 'D', 'C', 'S'};
	private int amountOfPlayers = 0;
	
	public BlackJack() {
		this.initializeDeck();
		Collections.shuffle(deck);
		this.startGame();
	}
	
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

	private void startGame() {
		Scanner input = new Scanner(System.in);
		Timer timer = new Timer();
		TimerTask task = new Helper();
		
		System.out.println("Type in a to add a player, r to remove a player, s to start a round and q to quit the game.");
		String in = input.nextLine();
		do {
			switch(in) {
			case "a":
				this.amountOfPlayers++;
				break;
			case "r":
				this.amountOfPlayers--;
				break;
			case "q":
				System.out.println("Quitting game");
				break;
			case "s":
				this.playRound(this.amountOfPlayers, input);
				break;
			default:
				System.out.println("Please do type a, r, s or q.");
				
			}
			in = input.nextLine();
		} while(!(in.equals("q")));
	}

	private void playRound(int amountOfPlayers, Scanner input) {
		this.dealer = new Dealer();
		this.players = new ArrayList<>();
		Collections.shuffle(deck);
		
		for (int i = 0; i < amountOfPlayers; i++) {
			Player player = new Player();
			players.add(player);
			this.drawStartingHand(player);
		}
		
		this.drawStartingHand(dealer);
		
		System.out.println("The dealer reveals the card " + this.dealer.getHand().get(0).getCardName());
		
		for (int i = 0; i < amountOfPlayers; i++) {
			System.out.println("Player " + (i + 1) + " has the cards the following cards:");
			this.players.get(i).printHand();
			System.out.println("You have a value of " + this.players.get(i).getHandValue() + ". Type c for a card and p to pass");
			String in = input.nextLine();
			while(!in.equals("p")) {
				Card drawnCard = this.drawCard();
				this.players.get(i).getHand().add(drawnCard);
				this.players.get(i).calculateHand();
				if (this.players.get(i).getHandValue() > 21) {
					this.players.get(i).setBusted(true);
					System.out.println("You drew: " + drawnCard.getCardName() + ". You went over 21, you lost");
					break;
				}
				System.out.println("You drew: " + drawnCard.getCardName() + ". You now have a value of " + this.players.get(i).getHandValue() + ".");
				in = input.nextLine();
			}
		}
		
		System.out.println("The dealer reveals his other card: " + this.dealer.getHand().get(1).getCardName());
		while(dealer.isAllowedCard()) {
			Card drawnCard = this.drawCard();
			this.dealer.getHand().add(drawnCard);
			this.dealer.calculateHand();
			System.out.println("The dealer draws " + drawnCard.getCardName() + "and has now a value of " + this.dealer.getHandValue() + ".");
			if (this.dealer.getHandValue() > 21) {
				this.dealer.setBusted(true);
				System.out.println("Dealer went over 21! Other players that also lost will get their money back and other players will earn money!");
			}
		}
		
		if (this.dealer.isBusted()) {
			for (int i = 0; i < this.amountOfPlayers; i++) {
				if (this.players.get(i).isBusted()) {
					System.out.println("Player " + (i + 1) + " is also busted so the player gets his/her money back.");
				} else {
					System.out.println("Player " + (i + 1) + " wins!");
				}
			}
		} else {
			for (int i = 0; i < this.amountOfPlayers; i++) {
				if (this.players.get(i).isBusted()) {
					System.out.println("Player " + (i + 1) + " is busted so the dealer wins.");
				} else {
					if (this.players.get(i).getHandValue() > this.dealer.getHandValue()) {
						System.out.println("Player " + (i + 1) + " wins!");
					} else {
						System.out.println("Player " + (i + 1) + " loses.");
					}
				}
			}
		}
		this.resetDeck();
		
	}
	
	private void drawStartingHand(Player player) {
		player.getHand().add(this.drawCard());
		player.getHand().add(this.drawCard());
		player.calculateHand();
	}
	
	private Card drawCard() {
		Card card = this.deck.get(0);
		this.deck.remove(card);
		return card;
	}
	
	private void resetDeck() {
		for(Player player: this.players) {
			this.deck.addAll(player.getHand());
			player.setHand(null);
		}
	}
}
