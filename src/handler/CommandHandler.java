package handler;

import static commands.CommandList.cmdNext;
import static commands.CommandList.cmdBack;
import static commands.CommandList.cmdSign;
import static main.Main.settings;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import commands.color.ColorSetter;
import commands.color.colorchat.ChatColorSetter;
import commands.general.pageChange.Pagenator;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import macro.Macros;
import messages.MessageEffects;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import commands.RetrieveUsers;

public class CommandHandler extends MessageInfo {
	public static Color embedMessagingColor;
	public static Color chatColor;

	public static void handler() throws InterruptedException, ExecutionException {
		if (settings.getAutoColorStatus() == false) {
			embedMessagingColor = ColorSetter.color;
			chatColor = ChatColorSetter.color;
		}

		if (settings.getAutoColorStatus() == true && channelType.equals(ChannelType.TEXT)) {
			Color color = guild.getMemberById(botUser.getId()).getColor();
			embedMessagingColor = color;
			chatColor = color;
		}
		activeCommands();
		if (author.equals(botUser)) {
			if (channelType.equals(ChannelType.PRIVATE)) {
				channel = message.getPrivateChannel();
			}
			if (channelType.equals(ChannelType.GROUP)) {
				channel = message.getGroup();
			}
			if (channelType.equals(ChannelType.TEXT)) {
				channel = message.getChannel();
			}
			userIcon = jda.getSelfUser().getAvatarUrl();
			if (channel.getType().isGuild()) {
				authorName = message.getGuild().getSelfMember().getEffectiveName();
			} else {
				authorName = jda.getSelfUser().getName();
			}
			checkForCommands(channel);
			checkForMacros();
			// Checks for any effects applied to the message and then resets the embed
			// settings so they can be used properly for the next message
			MessageEffects.messageEffects();
		}
	}

	public static void activeCommands() {
		
		if (!author.equals(botUser)) {
			if (commands.management.chatFilter.EnforceChatFilter.activeCommand() == true) {
				message.delete().complete();
			}
			
			if (commands.management.spamFilter.EnforceSpamFilter.activeCommand() == true) {
				message.delete().complete();
			}

			if (commands.management.muter.EnforceMutes.activeCommand() == true) {
				if (RetrieveUsers.listMembersHigherRanked().contains(message.getMember())) {
					message.delete().complete();
				}
			}

			if (commands.management.lengthFilter.EnforceLengthFilter.activeCommand() == true) {
				message.delete().complete();
			}

			if (commands.management.slowMode.EnforceSlowModes.activeCommand() == true) {
				for (int i = 0; i < Statuses.affectedUsers.size(); i++) {
					if (Statuses.affectedUsers.get(i).getUserId().equals(message.getAuthor().getId())) {
						for (int t = 0; t < Statuses.affectedUsers.get(i).getGuildsSlowModeIn().size(); t++) {
							if (Statuses.affectedUsers.get(i).getGuildsSlowModeIn().get(t).getGuildId()
									.equals(message.getGuild().getId())) {
								commands.management.slowMode.EnforceSlowModes.slowModeStarted(
										Statuses.affectedUsers.get(i),
										Statuses.affectedUsers.get(i).getGuildsSlowModeIn().get(t));
							}
						}
					}
				}
			}
		}
	}

	public static void checkForCommands(MessageChannel channel) {
		for (int i = 0; i < commands.CommandList.listOfCommands.size(); i++) {
			if (message.getContentRaw().startsWith(commands.CommandList.listOfCommands.get(i).getCommand())) {

				Message commandSend = null;
				String commandMessageIdToEdit = null;
				// If the command contains a method then it sets the message to the return of
				// the method
				if (commands.CommandList.listOfCommands.get(i).getMethod() != null) {
					try {
						commandSend = (Message) commands.CommandList.listOfCommands.get(i).getMethod()
								.invoke(new Object());
						if (commands.CommandList.listOfCommands.get(i).getCommand().equals(cmdNext.getCommand())
								|| commands.CommandList.listOfCommands.get(i).getCommand()
										.equals(cmdBack.getCommand())) {
							message.delete().complete();
							commandMessageIdToEdit = Pagenator.getMessageId();
						}
						
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}

				if ((commandSend != null) && (commandMessageIdToEdit != null)) {
					channel.editMessageById(commandMessageIdToEdit, commandSend).complete();
				}
				
				if ((commandSend != null) && (commandMessageIdToEdit == null)) {
					message.editMessage(commandSend).complete();
				}
				
				if (commandSend == null) {
					message.delete().complete();
				}
				main.SystemTrayManager.executeTray(false);
				bot.settings.SettingSaver.saveSettings();
			}
		}
	}

	public static void disableChatFor(String userId, String guildId) {
		Guild guild = jda.getGuildById(guildId);
		Member member = guild.getMemberById(userId);
		List<TextChannel> textChannels = guild.getTextChannels();
		for (int q = 0; q < Statuses.affectedGuilds.size(); q++) {
			for (int i = 0; i < textChannels.size(); i++) {
				for (int d = 0; d < Statuses.affectedGuilds.get(q).getSlowModeChannels().size(); d++) {
					if (textChannels.get(i).getId()
							.equals(Statuses.affectedGuilds.get(q).getSlowModeChannels().get(d))) {
						if (!(textChannels.get(i).getMemberPermissionOverrides()
								.contains(textChannels.get(i).getPermissionOverride(member)))) {
							textChannels.get(i).createPermissionOverride(member).setDeny(Permission.MESSAGE_WRITE)
									.complete();
						} else {
							textChannels.get(i).getPermissionOverride(member).getManager()
									.deny(Permission.MESSAGE_WRITE).complete();

						}
					}
				}
			}
		}
		for (int q = 0; q < Statuses.affectedUsers.size(); q++) {
			for (int d = 0; d < Statuses.affectedUsers.get(q).getGuildsSlowModeIn().size(); d++) {
				for (int i = 0; i < textChannels.size(); i++) {
					if (guild.getId().equals(Statuses.affectedUsers.get(q).getGuildsSlowModeIn().get(d).getGuildId())) {
						if (!(textChannels.get(i).getMemberPermissionOverrides()
								.contains(textChannels.get(i).getPermissionOverride(member)))) {
							textChannels.get(i).createPermissionOverride(member).setDeny(Permission.MESSAGE_WRITE)
									.complete();
						} else {
							textChannels.get(i).getPermissionOverride(member).getManager()
									.deny(Permission.MESSAGE_WRITE).complete();
						}
					}
				}
			}
			for (int d = 0; d < Statuses.affectedUsers.get(q).getGuildsMutedIn().size(); d++) {
				for (int i = 0; i < textChannels.size(); i++) {
					if (guild.getId().equals(Statuses.affectedUsers.get(q).getGuildsMutedIn().get(d))) {
						if (!(textChannels.get(i).getMemberPermissionOverrides()
								.contains(textChannels.get(i).getPermissionOverride(member)))) {
							textChannels.get(i).createPermissionOverride(member).setDeny(Permission.MESSAGE_WRITE)
									.complete();
						} else {
							textChannels.get(i).getPermissionOverride(member).getManager()
									.deny(Permission.MESSAGE_WRITE).complete();
						}
					}
				}
			}
		}
	}

	private static void checkForMacros() {
		for (int i = 0; i < Macros.macros.size(); i++) {
			if (message.getContentRaw().startsWith(cmdSign + Macros.macros.get(i).getName())) {
				message.editMessage(Macros.macros.get(i).getAction()).complete();
			}
		}
	}

	public static void disableChatFor(String channelId) {
		Member member = guild.getMemberById(channelId);
		List<TextChannel> textChannels = guild.getTextChannels();
		for (int i = 0; i < textChannels.size(); i++) {
			if (!(textChannels.get(i).getMemberPermissionOverrides()
					.contains(textChannels.get(i).getPermissionOverride(member)))) {
				textChannels.get(i).createPermissionOverride(member).setDeny(Permission.MESSAGE_WRITE).complete();
			} else {
				textChannels.get(i).getPermissionOverride(member).getManager().deny(Permission.MESSAGE_WRITE).complete();

			}
		}
	}

	public static void enableChatFor(String userId, String guildId) {
		Guild guild = jda.getGuildById(guildId);
		Member member = guild.getMemberById(userId);
		List<TextChannel> textChannels = guild.getTextChannels();
		for (int i = 0; i < textChannels.size(); i++) {
			if (textChannels.get(i).getMemberPermissionOverrides()
					.contains(textChannels.get(i).getPermissionOverride(member))) {
				if (1 < textChannels.get(i).getPermissionOverride(member).getDenied().size()) {
					textChannels.get(i).createPermissionOverride(member).setAllow(Permission.MESSAGE_WRITE).complete();
				} else {
					textChannels.get(i).getPermissionOverride(member).delete().complete();
				}
			}
		}
	}

	public static void changeUserNick(String userId, String nicknameToLock) {
		guild.getController().setNickname(guild.getMemberById(userId), nicknameToLock).complete();
	}

	public static void changeUserNick(String userId, String nicknameToLock, String guildId) {
		Guild guild = jda.getGuildById(guildId);
		guild.getController().setNickname(guild.getMemberById(userId), nicknameToLock).complete();
	}
}
