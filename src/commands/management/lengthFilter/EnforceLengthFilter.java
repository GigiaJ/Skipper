package commands.management.lengthFilter;

import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class EnforceLengthFilter extends Statuses {
	public static boolean activeCommand() {
		if (enforceLengthFilter == true) {
			Message message = MessageInfo.message;
			String rawMessageContent = message.getContentRaw();
			Guild guild = MessageInfo.guild;
			for (int i = 0; i < affectedGuilds.size(); i++) {
				if (affectedGuilds.get(i).getGuildId().equals(guild.getId())) {
					if (!affectedGuilds.get(i).getLengthFilterChannels().isEmpty()) {
						if (!(message.isEdited())) {
							rawMessageContent = org.apache.commons.lang3.StringUtils
									.stripAccents(rawMessageContent.replaceAll("\\.", "").replaceAll(" ", "")
											.replaceAll("_", "").replaceAll(",", "").replaceAll("~", ""));
							if (rawMessageContent.length() < 2) {
								if (!(rawMessageContent.toLowerCase().contains("k"))
										&& !(rawMessageContent.toLowerCase().contains("y"))
										&& !(rawMessageContent.toLowerCase().contains("?"))
										&& !(rawMessageContent.toLowerCase().contains("o"))) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}
