package commands.management.muter;

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

public class UnmuteAll extends Statuses {

	public static Message unmuteAll() {
		title = "Chat Management";
		embColor = Color.GREEN;
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();

		if (rawMessageContent.startsWith(cmdUnmuteAll.getCommand())) {
			for (int i = 0; i < affectedUsers.size(); i++) {
				for (int t = 0; t < affectedUsers.get(i).getGuildsMutedIn().size(); t++) {
					if (affectedUsers.get(i).getGuildsMutedIn().get(t).equals(guildId)) {
						MemberStatus mutedMember = affectedUsers.get(i);
						affectedUsers.remove(i);
						mutedMember = mutedMember.unmute(guildId);
						affectedUsers.add(mutedMember);
						CommandHandler.enableChatFor(affectedUsers.get(i).getUserId(), guildId);
						embContent = "All users in this guild are no longer muted.";
						enforceMute = false;
						i = 0;
						break;
					}
				}
			}
			if (enforceMute == false) {
				memberStatusHasChanged();
			}
			if (embContent.isEmpty()) {
				embContent = "Please mute a user first.";
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("unmute all", cmdSign, adminCmdSign, CommandType.MANAGER,
				"unmutes all users muted in the current guild.", "thisCommand")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(commands.management.muter.Unmute.class.getMethod("unmute")).build();
	}
}
