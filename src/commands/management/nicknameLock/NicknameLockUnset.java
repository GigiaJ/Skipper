package commands.management.nicknameLock;

import static commands.CommandList.cmdNameLockUnset;
import static commands.CommandList.cmdNameLockUnsetAll;
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

public class NicknameLockUnset extends Statuses {
	public static Message nicknameLockUnset() {
		title = "Nickname Lock";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		if (rawMessageContent.startsWith(cmdNameLockUnset.getCommand())
				&& (!(rawMessageContent.startsWith(cmdNameLockUnsetAll.getCommand())))) {
			String filterCommandOut = rawMessageContent.replaceAll(cmdNameLockUnset.getCommand() + " ", "");
			String userId = messages.IdExtracter.getIdFromMessage(filterCommandOut);
			if (userId != null) {
				MemberStatus nameLockMember = new MemberStatus(userId);
				nameLockMember = applyExistingMemberStatus(nameLockMember);
				affectedUsers.remove(nameLockMember);
				nameLockMember = nameLockMember.removeNicknameLock(guildId);
				affectedUsers.add(nameLockMember);
				memberStatusHasChanged();
				embContent = "Nickname lock has been unset.";
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		if (rawMessageContent.startsWith(cmdNameLockUnsetAll.getCommand())) {
			return NicknameLockUnsetAll.nicknameLockUnsetAll();
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("nicknamelock unset", cmdSign, adminCmdSign, CommandType.MANAGER,
				"removes the user nickname lock.", "thisCommand (@ Desired User)")
						.addFirstExample("thisCommand @G#7185")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName(),
								Permission.NICKNAME_MANAGE.getName() })
						.addMethod(NicknameLockUnset.class.getMethod("nicknameLockUnset")).build();

	}
}
