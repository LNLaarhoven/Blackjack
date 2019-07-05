package blackjack;

import java.util.TimerTask;

public class Helper extends TimerTask{

	@Override
	public void run() {
		System.out.println("Time has ran out!");
	}

}
