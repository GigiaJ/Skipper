package commands.management.muter;

import static commands.CommandList.cmdMute;
import static messages.EmbedData.*;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.management.manager.MemberStatus;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class Mute extends Statuses {

	public static Message mute() {
		title = "Chat Management";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();

		if (rawMessageContent.startsWith(cmdMute.getCommand())) {
			String filterCommandOut = rawMessageContent.replaceAll(cmdMute.getCommand() + " ", "");
			String userId = messages.IdExtracter.getIdFromMessage(filterCommandOut);
			if (userId != null) {
				if (!userId.equals(MessageInfo.botUser.getId())) {
					MemberStatus mutedMember = new MemberStatus(userId);
					mutedMember = applyExistingMemberStatus(mutedMember);

					// Checks whether the user is muted in the current guild and will fail to apply
					// the mute if so
					if (!affectedUsers.get(affectedUsers.indexOf(mutedMember)).getGuildsMutedIn().contains(guildId)) {
						affectedUsers.remove(mutedMember);
						mutedMember = mutedMember.mute(guildId);
						affectedUsers.add(mutedMember);
						handler.CommandHandler.disableChatFor(mutedMember.getUserId(), guildId);
						embContent = "User Muted: " + guild.getMemberById(userId).getEffectiveName();
					} else {
						embContent = "User is already muted in this server.";
					}
				} else {
					embContent = "Please don't try to mute yourself.";
				}
				enforceMute = true;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("mute", cmdSign, adminCmdSign, CommandType.MANAGER,
				"mutes the indicated user in the current guild.", "thisCommand (@ Desired User)")
						.addFirstExample("thisCommand @G#7185")
						.addPermissionRequirements(new String[] { Permission.MANAGE_CHANNEL.getName(),
								Permission.MESSAGE_MANAGE.getName(), Permission.MESSAGE_HISTORY.getName() })
						.addMethod(Mute.class.getMethod("mute")).build();

	}
}
