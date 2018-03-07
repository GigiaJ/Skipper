package commands.management.nameFilter;

import static commands.CommandList.cmdNameFilterCheck;
import static messages.EmbedData.*;
import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class NameFilterCheck extends Statuses {
	public static Message nameFilterCheck() {
		title = "Name Filter";
		embColor = Color.GREEN;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		JDA jda = MessageInfo.jda;
		if (rawMessageContent.startsWith(cmdNameFilterCheck.getCommand())) {
			embContent += "**Guilds:** ";
			for (int i = 0; i < affectedGuilds.size(); i++) {
				if (!affectedGuilds.get(i).getNameFilter()) {
					embContent += jda.getGuildById((affectedGuilds.get(i).getGuildId())).getName() + ", ";
				}
			}

			if (embContent.lastIndexOf(":**") + 4 == embContent.length()) {
				embContent += "No Guilds Being Filtered";
			} else {
				embContent = embContent.substring(0, embContent.length() - 2);
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("namefilter check", cmdSign, adminCmdSign, CommandType.MANAGER,
				"displays the current guilds which the name filter is active.", "thisCommand")
				.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
				.addMethod(NameFilterCheck.class.getMethod("nameFilterCheck")).build();
	}
}
