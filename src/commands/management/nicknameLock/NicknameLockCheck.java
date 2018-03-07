package commands.management.nicknameLock;

import static commands.CommandList.cmdNameLockCheck;
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

public class NicknameLockCheck extends Statuses {

	public static Message nameLockCheck() {
		title = "Nickname Lock";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();

		if (rawMessageContent.startsWith(cmdNameLockCheck.getCommand())) {
			for (int i = 0; i < affectedUsers.size(); i++) {
				if (affectedUsers.get(i).getGuildsNickNameLockIn().contains(guildId)) {
					if (embContent.contains("  ")) {
						embContent = embContent.replaceFirst("  ", ", ");
					}
					embContent += guild.getMemberById(affectedUsers.get(i).getUserId()).getAsMention() + "  ";
				}
			}

			if (embContent.isEmpty()) {
				embContent = "No users are currently nickname locked in this guild.";
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("nicknamelock check", cmdSign, adminCmdSign, CommandType.MANAGER,
			"displays the users currently muted", "thisCommand")
					.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() }).addMethod(NicknameLockCheck.class.getMethod("nameLockCheck")).build();
	}
}
