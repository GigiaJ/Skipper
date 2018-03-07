package commands.management.muter;

import static commands.CommandList.cmdCheckMutes;
import static messages.EmbedData.*;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class CheckMutes extends Statuses {

	public static Message checkMutes() {
		title = "Chat Management";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();

		if (rawMessageContent.startsWith(cmdCheckMutes.getCommand())) {
			for (int i = 0; i < affectedUsers.size(); i++) {
				if (affectedUsers.get(i).getGuildsMutedIn().contains(guildId)) {
					if (embContent.contains("  ")) {
						embContent = embContent.replaceFirst("  ", ", ");
					}
					embContent += guild.getMemberById(affectedUsers.get(i).getUserId()).getAsMention() + "  ";
				}
			}

			if (embContent.isEmpty()) {
				embContent = "No users are currently muted in this guild.";
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("checkmutes", cmdSign, adminCmdSign, CommandType.MANAGER,
			"displays the users currently muted", "thisCommand")
					.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() }).addMethod(CheckMutes.class.getMethod("checkMutes")).build();
	}
}
