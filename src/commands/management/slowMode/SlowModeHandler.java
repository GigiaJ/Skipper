package commands.management.slowMode;

import commands.management.manager.MemberStatus;
import commands.management.manager.SlowModeData;
import handler.CommandHandler;

public class SlowModeHandler implements Runnable {

	private long delay;
	private String userId;
	private String guildId;

	public SlowModeHandler(MemberStatus memberStatus, SlowModeData slowModeData) {
		this.delay = slowModeData.getDelay();
		this.userId = memberStatus.getUserId();
		this.guildId = slowModeData.getGuildId();
	}

	public void run() {
		CommandHandler.disableChatFor(userId, guildId);
		try {
			Thread.sleep(delay * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
			return;
		}
		CommandHandler.enableChatFor(userId, guildId);
		Thread.currentThread().interrupt();
		return;
	}
}
