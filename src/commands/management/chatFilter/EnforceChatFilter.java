package commands.management.chatFilter;

import commands.management.chatFilter.bannedWordList.BannedWordList;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class EnforceChatFilter extends Statuses {
	public static boolean activeCommand() {
		if (enforceChatFilter == true) {
			Message message = MessageInfo.message;
			String rawMessageContent = message.getContentRaw();
			Guild guild = MessageInfo.guild;
			for (int i = 0; i < affectedGuilds.size(); i++) {
				if (affectedGuilds.get(i).getGuildId().equals(guild.getId())) {
					if (!affectedGuilds.get(i).getChatFilterChannels().isEmpty()) {
						if (!(message.isEdited())) {
							rawMessageContent = org.apache.commons.lang3.StringUtils
									.stripAccents(rawMessageContent.replaceAll("\\.", "").replaceAll(" ", "")
											.replaceAll("_", "").replaceAll(",", "").replaceAll("~", ""));
							if (BannedWordList.BWL.contains(rawMessageContent)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
}
