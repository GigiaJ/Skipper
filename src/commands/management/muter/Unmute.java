package commands.management.muter;

import static commands.CommandList.cmdUnmute;
import static commands.CommandList.cmdUnmuteAll;
import static messages.EmbedData.*;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.management.manager.MemberStatus;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import handler.CommandHandler;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class Unmute extends Statuses {

	public static Message unmute() {
		title = "Chat Management";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();

		if (rawMessageContent.startsWith(cmdUnmute.getCommand())
				&& (!rawMessageContent.startsWith(cmdUnmuteAll.getCommand()))) {
			String filterCommandOut = rawMessageContent.replaceAll(cmdUnmute.getCommand() + " ", "");
			String userId = messages.IdExtracter.getIdFromMessage(filterCommandOut);
			if (userId != null) {
				for (int i = 0; i < affectedUsers.size(); i++) {
					for (int t = 0; t < affectedUsers.get(i).getGuildsMutedIn().size(); t++) {
						if (affectedUsers.get(i).getGuildsMutedIn().get(t).equals(guildId)) {
							if (affectedUsers.get(i).getUserId().equals(userId)) {
								MemberStatus mutedMember = affectedUsers.get(i);
								affectedUsers.remove(mutedMember);
								mutedMember = mutedMember.unmute(guildId);
								affectedUsers.add(mutedMember);
								CommandHandler.enableChatFor(affectedUsers.get(i).getUserId(), guildId);
								memberStatusHasChanged();
								embContent = "User has been unmuted.";
								break;
							}
						}
					}
				}
				if (embContent.isEmpty()) {
					embContent = "Please mute a user first.";
				}
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}

		if (rawMessageContent.startsWith(cmdUnmuteAll.getCommand())) {
			return UnmuteAll.unmuteAll();
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("unmute", cmdSign, adminCmdSign, CommandType.MANAGER,
				"unmutes the indicated user in the current guild.", "thisCommand (@ Desired User)")
						.addFirstExample("thisCommand @G#7185")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(Unmute.class.getMethod("unmute")).build();
	}
}
