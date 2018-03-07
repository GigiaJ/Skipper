package commands.management.nicknameLock;

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

public class NicknameLockUnsetAll extends Statuses {
	public static Message nicknameLockUnsetAll() {
		title = "Chat Management";
		embColor = Color.GREEN;
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();

		if (rawMessageContent.startsWith(cmdNameLockUnsetAll.getCommand())) {
			for (int i = 0; i < affectedUsers.size(); i++) {
				for (int t = 0; t < affectedUsers.get(i).getGuildsNickNameLockIn().size(); t++) {
					if (affectedUsers.get(i).getGuildsNickNameLockIn().get(t).equals(guildId)) {
						MemberStatus nameLockMember = affectedUsers.get(i);
						affectedUsers.remove(i);
						nameLockMember = nameLockMember.removeNicknameLock(guildId);
						affectedUsers.add(nameLockMember);
						embContent = "All users in this guild are no longer nickname locked.";
						enforceNameLock = false;
						i = 0;
						break;
					}
				}
			}
			if (enforceNameLock == true) {
			memberStatusHasChanged();
			}
			if (embContent.isEmpty()) {
				embContent = "Please lock a user first.";
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("nicknamelock unset all", cmdSign, adminCmdSign, CommandType.MANAGER,
				"removes the nickname locks in this guild.", "thisCommand (@ Desired User)").addPermissionRequirements(
						new String[] { Permission.MESSAGE_EMBED_LINKS.getName(), Permission.NICKNAME_MANAGE.getName() })
						.addMethod(NicknameLockUnsetAll.class.getMethod("nicknameLockUnsetAll")).build();

	}
}
