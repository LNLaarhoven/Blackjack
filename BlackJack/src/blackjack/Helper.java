package blackjack;

import java.util.TimerTask;

public class Helper extends TimerTask{

	private boolean ranOutOfTime;
	
	public Helper() {
		this(false);
	}
	
	public Helper(boolean ranOutOfTime) {
		this.ranOutOfTime = ranOutOfTime;
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
		System.out.println("The player took too long to respond and automatically passes, hit return to continue.");
	}

}
