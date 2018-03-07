package commands.management.slowMode;

import commands.management.manager.MemberStatus;
import commands.management.manager.SlowModeData;

public class SlowModeThreading implements Runnable {

	private int duration;
	private MemberStatus member;
	private SlowModeData slowModeData;

	public SlowModeThreading(MemberStatus member, SlowModeData slowModeData) {
		this.duration = slowModeData.getDuration();
		this.member = member;
		this.slowModeData = slowModeData;
	}

	public void run() {
		try {
			if (duration > 0) {
				Thread.sleep(duration * 1000);
			}
			else {
				Thread.currentThread().interrupt();
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		EnforceSlowModes.slowModeEnded(member, slowModeData);
		Thread.currentThread().interrupt();
		return;
	}
}