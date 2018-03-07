package commands.management.nicknameLock;

import commands.management.manager.Statuses;
import eventInfo.GuildMemberNickChangeInfo;
import net.dv8tion.jda.core.entities.Guild;

public class EnforceNicknameLock extends Statuses {
	private static String nickname = "";
	
	
	public static boolean activeCommand() {
		Guild guild = GuildMemberNickChangeInfo.guild;
		String userId = GuildMemberNickChangeInfo.member.getUser().getId();
		if (enforceNameLock == true) {
			for (int i = 0; i < affectedUsers.size(); i++) {
				if (affectedUsers.get(i).getUserId().equals(userId)) {
					if (affectedUsers.get(i).getGuildsNickNameLockIn().contains(guild.getId())) {
						nickname = affectedUsers.get(i).getNickNames().get(affectedUsers.get(i).getGuildsNickNameLockIn().indexOf(guild.getId()));
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static String setNickname() {
		return nickname;
	}
}
