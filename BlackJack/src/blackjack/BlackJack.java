package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Timer;

public class BlackJack {

	private ArrayList<Card> deck;
	private ArrayList<Player> players;
	private Dealer dealer;
	private char[] symbol = { 'H', 'D', 'C', 'S' };

	public BlackJack() {
		this.initializeDeck();
		Collections.shuffle(deck);
		this.playGame();
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

	private void playGame() {
		Scanner input = new Scanner(System.in);
		this.players = new ArrayList<>();
		this.dealer = new Dealer();
		
		String in = "";
		
		while(!in.equals("q")) {
			System.out
					.println("Type in a to add a player, r to remove a player, s to start a round and q to quit the game.");
			in = input.nextLine();
			switch (in) {
			case "a":
				this.players.add(new Player());
				break;
			case "r":
				if (this.players.size() > 0) {
					this.players.remove(0);
				} else {
					System.out.println("Can't remove any more players.");
				}
				break;
			case "q":
				System.out.println("Quitting game");
				break;
			case "s":
				this.playRound(input);
				break;
			default:
				System.out.println("Please do type a, r, s or q.");

			}
		}
	}

	private void playRound(Scanner input) {
		Collections.shuffle(deck);

		for (Player player : this.players) {
			this.drawStartingHand(player);
		}

		this.drawStartingHand(dealer);

		System.out.println("The dealer reveals the card " + this.dealer.getHand().get(0).getCardName());

		for (Player player: this.players) {
			this.playerTurn(player, input);
		}

		this.resolveDealerActions();
		this.printResults();
		
		this.resetForNextRound();

	}
	
	private void playerTurn(Player player, Scanner input) {
		System.out.println("Player " + (this.players.indexOf(player) + 1) + " has the cards the following cards:");
		player.printHand();
		System.out.println(
				"You have a value of " + player.getHandValue() + ". Type c for a card and p to pass");
		Timer timer = new Timer();
		Helper task = new Helper();
		timer.schedule(task, 10000);
		String in = input.nextLine();
		while (!in.equals("p") && !task.getRanOutOfTime()) {
			task.cancel();
			task = new Helper();
			timer.schedule(task, 10000);
			if (in.equals("c")) {
				Card drawnCard = this.drawCard();
				player.getHand().add(drawnCard);
				player.calculateHand();
				if (player.getHandValue() > 21) {
					player.setBusted(true);
					System.out.println("You drew: " + drawnCard.getCardName() + ". You went over 21, you lost");
					break;
				}
				System.out.println("You drew: " + drawnCard.getCardName() + ". You now have a value of "
						+ player.getHandValue() + ".");
				in = input.nextLine();
			} else {
				System.out.println("Please type c to draw a card or p to pass.");
				in = input.nextLine();
			}
		}
		timer.cancel();
	}
	
	private void resolveDealerActions() {
		System.out.println("The dealer reveals his other card: " + this.dealer.getHand().get(1).getCardName());
		while (dealer.isAllowedCard()) {
			Card drawnCard = this.drawCard();
			this.dealer.getHand().add(drawnCard);
			this.dealer.calculateHand();
			System.out.println("The dealer draws " + drawnCard.getCardName() + " and has now a value of "
					+ this.dealer.getHandValue() + ".");
			if (this.dealer.getHandValue() > 21) {
				this.dealer.setBusted(true);
				System.out.println(
						"Dealer went over 21! Other players that also lost will get their money back and other players will earn money!");
			}
		}
	}

	private void printResults() {
		if (this.dealer.isBusted()) {
			for (Player player : this.players) {
				if (player.isBusted()) {
					System.out.println("Player " + (this.players.indexOf(player) + 1)
							+ " is also busted so the player gets his/her money back.");
				} else {
					System.out.println("Player " + (this.players.indexOf(player) + 1) + " wins!");
				}
			}
		} else {
			for (Player player : this.players) {
				if (player.isBusted()) {
					System.out
							.println("Player " + (this.players.indexOf(player) + 1) + " is busted so the dealer wins.");
				} else {
					if (player.getHandValue() > this.dealer.getHandValue()) {
						System.out.println("Player " + (this.players.indexOf(player) + 1) + " wins!");
					} else if (player.getHandValue() == this.dealer.getHandValue()) {
						System.out.println("Player " + (this.players.indexOf(player) + 1) + " draws!");
					} else {
						System.out.println("Player " + (this.players.indexOf(player) + 1) + " loses.");
					}
				}
			}
		}
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

	private void resetForNextRound() {
		for (Player player : this.players) {
			this.deck.addAll(player.getHand());
			player.resetPlayer();
		}
		this.deck.addAll(this.dealer.getHand());
		this.dealer.resetPlayer();
	}
}
