package commands.management.nicknameLock;

import static commands.CommandList.cmdNameLockSet;
import static messages.EmbedData.*;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.management.manager.MemberStatus;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import messages.IdExtracter;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class NicknameLockSet extends Statuses {

	public static Message nicknameLockSet() {
		title = "Nickname Lock";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();

		if (rawMessageContent.startsWith(cmdNameLockSet.getCommand())) {
			String filterCommandOut = rawMessageContent.replaceAll(cmdNameLockSet.getCommand(), "").trim();
			String userId = messages.IdExtracter.getIdFromMessage(filterCommandOut);
			if (userId != null) {
				if (!userId.equals(MessageInfo.botUser.getId())) {
					String nickname = IdExtracter.removeIdFromMessage(filterCommandOut).trim();
					if (nickname.length() > 32) {
						embContent = "Please enter a valid nickname. (1-32 characters)";
						return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
					}
					MemberStatus nameLockMember = new MemberStatus(userId);
					nameLockMember = applyExistingMemberStatus(nameLockMember);
					if (!affectedUsers.get(affectedUsers.indexOf(nameLockMember)).getGuildsNickNameLockIn()
							.contains(guildId)) {
						affectedUsers.remove(nameLockMember);
						nameLockMember = nameLockMember.nicknameLock(guildId, nickname);
						affectedUsers.add(nameLockMember);
						handler.CommandHandler.changeUserNick(userId, nickname);
						embContent = "User Locked: " + guild.getMemberById(userId).getEffectiveName();
					} else {
						embContent = "User is already locked in this server.";
					}
				} else {
					embContent = "Please don't try to lock yourself.";
				}
				enforceNameLock = true;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("nicknamelock set", cmdSign, adminCmdSign, CommandType.MANAGER,
				"will lock a given users name to a desired name. (No one can change it)",
				"thisCommand (@ Desired User) (Desired Name)").addFirstExample("thisCommand @G#7185 God")
						.addSecondExample("thisCommand @G#7185 OG")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName(),
								Permission.NICKNAME_MANAGE.getName() })
						.addMethod(NicknameLockSet.class.getMethod("nicknameLockSet")).build();

	}
}