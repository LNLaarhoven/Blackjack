package blackjack;

import java.util.Scanner;
import java.util.TimerTask;

public class Helper extends TimerTask{

	private boolean ranOutOfTime;
	private Scanner input;
	
	public Helper() {
		this(false);
	}
	
	public Helper(boolean ranOutOfTime) {
		this.ranOutOfTime = ranOutOfTime;
	}
	
	public Helper(Scanner input) {
		this.input = input;
	}
	
	public boolean getRanOutOfTime() {
		return this.ranOutOfTime;
	}
	
	public void setRanOutOfTime(boolean ranOutOfTime) {
		this.ranOutOfTime = ranOutOfTime;
	}
	
	@Override
	public void run() {
		this.setRanOutOfTime(true);
		this.input.close();
	}

}
