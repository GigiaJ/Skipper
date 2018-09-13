package commands.management.slowMode;

import java.util.List;

import commands.management.manager.MemberStatus;
import commands.management.manager.SlowModeData;
import handler.CommandHandler;
import net.dv8tion.jda.core.entities.Member;
import commands.management.manager.Statuses;

public class SlowModeHandler extends Statuses implements Runnable {

	private long delay;
	private String userId;
	private String guildId;

	public SlowModeHandler(MemberStatus memberStatus, SlowModeData slowModeData) {
		this.delay = slowModeData.getDelay();
		this.userId = memberStatus.getUserId();
		this.guildId = slowModeData.getGuildId();
	}

	public void run() {
		boolean outranks = false;
		List<Member> higherRankedUsers = commands.RetrieveUsers.listMembersHigherRanked();
		for (int i = 0; i < higherRankedUsers.size(); i++) {
			if (higherRankedUsers.get(i).getUser().getId().equals(userId)) {
				outranks = true;
				break;
			}
		}
		if (outranks == false) {
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
		} else {
			MemberStatus mutedMember = new MemberStatus(userId);
			mutedMember = applyExistingMemberStatus(mutedMember);
			// Checks whether the user is muted in the current guild and will fail to apply
			// the mute if so
			if (!affectedUsers.get(affectedUsers.indexOf(mutedMember)).getGuildsMutedIn().contains(guildId)) {
				affectedUsers.remove(mutedMember);
				mutedMember = mutedMember.mute(guildId);
				affectedUsers.add(mutedMember);
			}
			try {
				Thread.sleep(delay * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
				return;
			}
			if (affectedUsers.get(affectedUsers.indexOf(mutedMember)).getGuildsMutedIn().contains(guildId)) {
				affectedUsers.remove(mutedMember);
				mutedMember = mutedMember.unmute(guildId);
				affectedUsers.add(mutedMember);
				memberStatusHasChanged();
			}
			Thread.currentThread().interrupt();
			return;
		}
	}
}
