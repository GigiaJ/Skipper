package commands.management.muter;

import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class EnforceMutes extends Statuses {
	public static boolean activeCommand() {
		Guild guild = MessageInfo.guild;
		User author = MessageInfo.author;
		if (enforceMute == true) {
			for (int i = 0; i < affectedUsers.size(); i++) {
				for (int t = 0; t < affectedUsers.get(i).getGuildsMutedIn().size(); t++) {
					if (affectedUsers.get(i).getGuildsMutedIn().get(t).equals(guild.getId())) {
						if (affectedUsers.get(i).getUserId().equals(author.getId())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
