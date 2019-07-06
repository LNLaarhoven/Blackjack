package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Timer;

/*
 * Class that holds the state of the game.
 */
public class BlackJack {

	private ArrayList<Card> deck;						//Deck of cards that holds the cards that aren't in the player's or dealer's hands
	private ArrayList<Player> players;					//List of the player (excluding dealer)
	private Dealer dealer;								//Dealer object
	private char[] symbol = { 'H', 'D', 'C', 'S' };		//Array that represents the 4 symbols on cards: Hearts, Diamonds, Clubs, Spades

	/*
	 * Constructor class. It initialises the deck and shuffles it. It then starts the game.
	 */
	public BlackJack() {
		this.initializeDeck();
		Collections.shuffle(deck);
		this.playGame();
	}

	/*
	 * Method to initialise the deck. The outer loop loops through the symbols and the inner loop loops through the different cards.
	 * It concatenates the character of the symbol with the right number and then adds the Jack, the Queen, the King and the ace. 
	 * It also adds the right values for the cards
	 */
	private void initializeDeck() {
		this.deck = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			for (int j = 2; j < 11; j++) {
				String cardName = symbol[i] + Integer.toString(j);
				deck.add(new Card(cardName, j));
			}
			deck.add(new Card(symbol[i] + "J", 10));
			deck.add(new Card(symbol[i] + "Q", 10));
			deck.add(new Card(symbol[i] + "K", 10));
			deck.add(new Card(symbol[i] + "A", 11));
		}
	}

	/*
	 * Debug method to print the deck.
	 */
	public void printDeck() {
		for (int i = 0; i < 52; i++) {
			System.out.println(deck.get(i).getCardName());
		}
	}

	/*
	 * Method that plays the game
	 */
	private void playGame() {
		/* Initialises the starting objects */
		Scanner input = new Scanner(System.in);
		this.players = new ArrayList<>();
		this.dealer = new Dealer();

		/* Initialise the input string to avoid skipping the while loop*/
		String in = "";

		while (!in.equals("q")) {
			/* Read the next line of input.*/
			System.out.println(
					"Type in a to add a player, r to remove a player, s to start a round and q to quit the game.");
			in = input.nextLine();
			
			/* Determine the next course of action? */
			switch (in) {
			case "a":
				/* Add a new player*/
				this.players.add(new Player());
				break;
			case "r":
				/* Remove a player */
				if (this.players.size() > 0) {
					this.players.remove(0);
				} else {
					System.out.println("Can't remove any more players.");
				}
				break;
			case "q":
				/* Quits the game*/
				System.out.println("Quitting game");
				break;
			case "s":
				/* starts a round */
				this.playRound(input);
				break;
			default:
				/* Catch invalid input */
				System.out.println("Please do type a, r, s or q.");

			}
		}
	}

	/*
	 * Method for playing a round of blackjack
	 */
	private void playRound(Scanner input) {
		/* Shuffle the deck */
		Collections.shuffle(deck);

		/* Let every player draw his/her starting hand */
		for (Player player : this.players) {
			this.drawStartingHand(player);
		}

		this.drawStartingHand(dealer);

		/* The dealer reveals one card */
		System.out.println(
				"The dealer reveals the card " + this.dealer.getSplitHands().get(0).getHand().get(0).getCardName());

		/* Play every player's hand */
		for (Player player : this.players) {
			this.playerTurn(player, input);
		}

		/* Dealer's turn */
		this.resolveDealerActions();
		
		/* Print the results */
		this.printResults();

		/* Put everything back for the start of another round */
		this.resetForNextRound();

	}

	/*
	 * Method to determine the course of action in a player's turn
	 */
	private void playerTurn(Player player, Scanner input) {
		
		/* Loop through the hand of a player, if any splitting occurred, otherwise it just goes through it once. */
		for (Hand hand : player.getSplitHands()) {
			
			/* Print hand and value */
			System.out.println("Player " + (this.players.indexOf(player) + 1)
					+ " has the cards the following cards in hand: " + (player.getSplitHands().indexOf(hand) + 1));
			hand.printHand();
			System.out.println("You have a value of " + hand.getHandValue()
					+ ". Type c for a card and p to pass");

			/* Check if the player is allowed to split cards.*/
			int handIndex = player.isAllowedToSplit();
			if (handIndex >= 0) {
				System.out.println("You are allowed to split your cards, type d and hit enter to split your cards.");
			}

			/*Set the timer for a player to respond to 30 seconds*/
			Timer timer = new Timer();
			Helper task = new Helper();
			timer.schedule(task, 30000);

			/* Read the next line */
			String in = input.nextLine();
			
			/* If d has been typed it splits the hand. */
			if (in.equals("d")) {
				this.splitHands(player, player.getSplitHands().indexOf(hand));
				System.out.println("You split your hand and now hand " + (player.getSplitHands().indexOf(hand) + 1) + "contains:");
				hand.printHand();
				System.out.println("You have a value of " + hand.getHandValue()
						+ ". Type c for a card and p to pass");
			}
			
			/* While loop that continues for as long as the player doesn't pass or runs out of time. */
			while (!in.equals("p") && !task.getRanOutOfTime()) {

				/* Player has responded, so reset the timer.*/
				task.cancel();
				task = new Helper();
				timer.schedule(task, 30000);

				/* Check if the player asked for a card or split before otherwise we have a wrong input.*/
				if (in.equals("c")) {
					/*Player asked for a card and gets one, hand value gets recalculated.*/
					Card drawnCard = this.drawCard();
					hand.getHand().add(drawnCard);
					hand.calculateHand();

					/* Check if the player's hand is busted.*/
					if (hand.getHandValue() > 21) {
						hand.setBusted(true);
						System.out.println("You drew: " + drawnCard.getCardName() + ". You went over 21, you lost");
						break;
					}

					/* Reveal the card that has been drawn and the new value.*/
					System.out.println("You drew: " + drawnCard.getCardName() + ". You now have a value of "
							+ hand.getHandValue() + ".");
				} else if (!in.equals("d")){
					/* Wrong input, didn't receive c,d or p*/
					System.out.println("Please type c to draw a card or p to pass.");
				}
				
				/* Read the next instruction.*/
				in = input.nextLine();
			}
			/* Clean up the timer */
			timer.cancel();
		}
	}

	/*
	 * Method to determine the course of action for the dealer.
	 */
	private void resolveDealerActions() {
		
		/* Reveal the other card in the dealer's hand */
		System.out.println("The dealer reveals his other card: "
				+ this.dealer.getSplitHands().get(0).getHand().get(1).getCardName());
		
		/* Dealer draws a card for as long as its hand value is below 17 */
		while (dealer.isAllowedCard()) {
			
			/* Draw a new card and calculate new hand value.*/
			Card drawnCard = this.drawCard();
			this.dealer.getSplitHands().get(0).getHand().add(drawnCard);
			this.dealer.getSplitHands().get(0).calculateHand();
			System.out.println("The dealer draws " + drawnCard.getCardName() + " and has now a value of "
					+ this.dealer.getSplitHands().get(0).getHandValue() + ".");
			
			/* Check if the dealer's hand is busted.*/
			if (this.dealer.getSplitHands().get(0).getHandValue() > 21) {
				this.dealer.getSplitHands().get(0).setBusted(true);
				System.out.println(
						"Dealer went over 21! Other players that also lost will get their money back and other players will earn money!");
			}
		}
	}

	/*
	 * Method to print the end results of a round
	 */
	private void printResults() {
		
		if (this.dealer.getSplitHands().get(0).isBusted()) {
			/* The dealer is busted */
			for (Player player : this.players) {
				for (Hand hand : player.getSplitHands()) {
					if (hand.isBusted()) {
						/*Player was also busted on a hand*/
						System.out.println("Player " + (this.players.indexOf(player) + 1) + " ties on hand "
								+ (player.getSplitHands().indexOf(hand) + 1) + "!");
					} else {
						/*Player wasn't busted on a hand*/
						System.out.println("Player " + (this.players.indexOf(player) + 1) + " wins on hand +"
								+ (player.getSplitHands().indexOf(hand) + 1) + "!");
					}
				}
			}
		} else {
			/* The dealer isn't busted. */
			for (Player player : this.players) {
				for (Hand hand : player.getSplitHands()) {
					if (hand.isBusted()) {
						/* Player's hand is busted. */
						System.out.println("Player " + (this.players.indexOf(player) + 1) + " is busted with hand "
								+ (player.getSplitHands().indexOf(hand) + 1) + " so the dealer wins!");
					} else {
						/* Player's hand is ready to be compared. */
						if (hand.getHandValue() > this.dealer.getSplitHands().get(0).getHandValue()) {
							/* Player wins with a hand */
							System.out.println("Player " + (this.players.indexOf(player) + 1) + " wins with hand "
									+ (player.getSplitHands().indexOf(hand) + 1) + "!");
						} else if (hand.getHandValue() == this.dealer.getSplitHands().get(0).getHandValue()) {
							/* Player ties with a hand */
							System.out.println("Player " + (this.players.indexOf(player) + 1) + " ties with hand "
									+ (player.getSplitHands().indexOf(hand) + 1) + "!");
						} else {
							/* Player loses with a hand */
							System.out.println("Player " + (this.players.indexOf(player) + 1) + " loses with hand "
									+ (player.getSplitHands().indexOf(hand) + 1)+ "!");
						}
					}
				}
			}
		}
	}

	/*
	 * Method to draw the starting hand for a Player object (this includes the dealer).
	 */
	private void drawStartingHand(Player player) {
		player.getSplitHands().get(0).getHand().add(this.drawCard());
		player.getSplitHands().get(0).getHand().add(this.drawCard());
		player.getSplitHands().get(0).calculateHand();
	}

	/*
	 * Method to draw a card from the deck
	 */
	private Card drawCard() {
		Card card = this.deck.get(0);
		this.deck.remove(card);
		return card;
	}

	/*
	 * Method to take all the card from all the players, including the dealer, and put it back into the deck and shuffle them again.
	 */
	private void resetForNextRound() {
		
		/* Loop through all the players and all their hand to add their card back to the deck */
		for (Player player : this.players) {
			for (Hand hand : player.getSplitHands()) {
				this.deck.addAll(hand.getHand());		
			}
			/* Restore the player's variables. */
			player.resetPlayer();
		}
		
		/* Add the cards back into the deck from the dealer.*/
		this.deck.addAll(this.dealer.getSplitHands().get(0).getHand());
		this.dealer.resetPlayer();
		
		/* Shuffle the deck. */
		Collections.shuffle(this.deck);
	}

	/*
	 * Method to split the hand of a player
	 */ 
	private void splitHands(Player player, int index) {
		
		/* Create a new hand, add the second card of a split, draw a new card with it and calculate the value*/
		Hand newHand = new Hand();
		newHand.getHand().add(player.getSplitHands().get(index).getHand().get(1));
		player.getSplitHands().get(index).getHand().remove(1);
		newHand.getHand().add(this.drawCard());
		newHand.calculateHand();
		player.getSplitHands().add(newHand);
		
		/* Draw a new card for the first hand and calculate the value. */
		player.getSplitHands().get(index).getHand().add(this.drawCard());
		player.getSplitHands().get(index).calculateHand();
	}
}
