package commands.management.slowMode;

import static commands.CommandList.cmdSlowModeUser;
import static messages.EmbedData.*;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.management.manager.MemberStatus;
import commands.management.manager.SlowModeData;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import handler.CommandHandler;
import messages.EmbedMessage;
import messages.IdExtracter;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class SlowModeUser extends Statuses {

	public static Message slowModeUser() {
		title = "Slow Mode";
		embColor = Color.GREEN;

		String rawMessageContent = MessageInfo.message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();

		if (rawMessageContent.startsWith(cmdSlowModeUser.getCommand())) {
			String filterCommandOut = rawMessageContent.replaceAll(cmdSlowModeUser.getCommand(), "");
			String userId = IdExtracter.getIdFromMessage(filterCommandOut);
			if (userId != null) {
				filterCommandOut = IdExtracter.removeIdFromMessage(filterCommandOut);
				MemberStatus slowModeMember = new MemberStatus(userId);
				slowModeMember = applyExistingMemberStatus(slowModeMember);
				if (!userId.equals(MessageInfo.botUser.getId())) {
					if (alreadyAffected(slowModeMember, guildId) == false) {
						int delay = 5;
						int duration = -1;
						if (filterCommandOut.contains(" ")) {
							for (int i = 0; i < filterCommandOut.length(); i++) {
								if (filterCommandOut.charAt(0) == ' ') {
									filterCommandOut = filterCommandOut.replaceFirst(" ", "");
								} else {
									break;
								}
							}
							if (!filterCommandOut.isEmpty()) {
								String[] numbers = filterCommandOut.split(" ");

								if (!numbers[0].isEmpty() || numbers[0] != null) {
									delay = Integer.valueOf(numbers[0].replaceAll(" ", ""));
									if (numbers.length > 1) {
										if (numbers[1] != null || !numbers[1].isEmpty()) {
											duration = Integer.valueOf(numbers[1].replaceAll(" ", ""));
										}
									}
								} else {
									delay = Integer.valueOf(filterCommandOut.replaceAll(" ", ""));
								}
							}
						}
						affectedUsers.remove(slowModeMember);
						slowModeMember.slowMode(guildId, delay, duration);
						SlowModeThreading slowModeThread = new SlowModeThreading(slowModeMember,
								new SlowModeData(guildId, delay, duration));
						Thread t = new Thread(slowModeThread);
						t.setName(userId);
						t.start();
						affectedUsers.add(slowModeMember);
						enforceSlowMode = true;
						embContent = "Slowmode activated on " + guild.getMemberById(userId).getEffectiveName();
					}
				} else {
					embContent = "Please don't try to slowmode yourself.";
				}
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	static boolean alreadyAffected(MemberStatus slowModeMember, String guildId) {
		for (int i = 0; i < slowModeMember.getGuildsSlowModeIn().size(); i++) {
			if (affectedUsers.get(affectedUsers.indexOf(slowModeMember)).getGuildsSlowModeIn().get(i).getGuildId()
					.equals(guildId)) {
				if (slowModeMember.getGuildsSlowModeIn().get(i).getGuildId().equals(guildId)) {
					affectedUsers.remove(affectedUsers.indexOf(slowModeMember));
					slowModeMember.removeSlowMode(slowModeMember.getGuildsSlowModeIn().get(i));
					affectedUsers.add(slowModeMember);
					memberStatusHasChanged();
					CommandHandler.enableChatFor(slowModeMember.getUserId(), guildId);
					embContent = "User is no longer slowmoded.";
					return true;
				}
			}
		}
		return false;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("slowmode user", cmdSign, adminCmdSign, CommandType.MANAGER,
				"slows down chat for a user.",
				"thisCommand (@ Desired User) (*delay in seconds) (*duration in seconds)")
						.addFirstExample("thisCommand @G#7185").addSecondExample("thisCommand @G#7185 5 100")
						.addPermissionRequirements(new String[] { Permission.MANAGE_CHANNEL.getName(),
								Permission.MESSAGE_MANAGE.getName(), Permission.MESSAGE_HISTORY.getName() })
						.addMethod(commands.management.slowMode.SlowModeUser.class.getMethod("slowModeUser")).build();
	}
}
