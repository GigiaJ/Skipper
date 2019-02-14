package commands.management.slowMode;

import commands.management.manager.MemberStatus;
import commands.management.manager.SlowModeData;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

public class EnforceSlowModes extends Statuses {

	public static boolean activeCommand() {
		if (enforceSlowMode == true) {
			if (hasSlowMode() == false) {
				applySlowMode();
			} else {
				return true;
			}
		}
		return false;
	}

	private static boolean hasSlowMode() {
		String authorId = MessageInfo.author.getId();
		String guildId = MessageInfo.guild.getId();
		for (int i = 0; i < affectedUsers.size(); i++) {
			if (affectedUsers.get(i).getUserId().equals(authorId)) {
				if (!affectedUsers.get(i).getGuildsSlowModeIn().isEmpty()) {
					for (int t = 0; t < affectedUsers.get(i).getGuildsSlowModeIn().size(); t++) {
						if (affectedUsers.get(i).getGuildsSlowModeIn().get(t).getGuildId().equals(guildId)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static void applySlowMode() {
		Guild guild = MessageInfo.guild;
		MessageChannel channel = MessageInfo.channel;
		String channelId = channel.getId();
		String guildId = guild.getId();
		String authorId = MessageInfo.author.getId();
		for (int i = 0; i < affectedUsers.size(); i++) {
			if (affectedUsers.get(i).getUserId().equals(authorId)) {
				for (int t = 0; t < affectedUsers.get(i).getGuildsSlowModeIn().size(); t++) {
					if (affectedUsers.get(i).getGuildsSlowModeIn().get(t).getGuildId().equals(guildId)) {
						slowModeStarted(affectedUsers.get(i), affectedUsers.get(i).getGuildsSlowModeIn().get(t));
						return;
					}
				}
			}
		}
		for (int i = 0; i < affectedGuilds.size(); i++) {
			for (int t = 0; t < affectedGuilds.get(i).getSlowModeChannels().size(); t++) {
				if (affectedGuilds.get(i).getSlowModeChannels().get(t).equals(channelId)) {
					MemberStatus memberToSlowMode = new MemberStatus(authorId);
					memberToSlowMode = applyExistingMemberStatus(memberToSlowMode);
					affectedUsers.remove(memberToSlowMode);
					memberToSlowMode = memberToSlowMode.slowMode(guildId, affectedGuilds.get(i).getSlowModeDelay(),
							affectedGuilds.get(i).getSlowModeDelay());
					affectedUsers.add(memberToSlowMode);
					SlowModeThreading slowModeThread = new SlowModeThreading(memberToSlowMode, new SlowModeData(guildId,
							affectedGuilds.get(i).getSlowModeDelay(), affectedGuilds.get(i).getSlowModeDelay()));
					Thread thread = new Thread(slowModeThread);
					thread.setName(authorId);
					thread.start();
					slowModeStarted(memberToSlowMode, new SlowModeData(guildId,
							affectedGuilds.get(i).getSlowModeDelay(), affectedGuilds.get(i).getSlowModeDelay()));
					return;
				}
			}
		}
	}

	public static void slowModeStarted(MemberStatus memberStatus, SlowModeData slowModeData) {
		SlowModeHandler userTimerThread = new SlowModeHandler(memberStatus, slowModeData);
		Thread slowModeThread = new Thread(userTimerThread);
		slowModeThread.setName(memberStatus.getUserId());
		slowModeThread.start();
	}

	public static void slowModeEnded(MemberStatus member, SlowModeData slowModeData) {
		affectedUsers.remove(member);
		member = member.removeSlowMode(slowModeData);
		affectedUsers.add(member);
		memberStatusHasChanged();
	}

}
