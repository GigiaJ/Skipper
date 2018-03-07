package handler;

import static bot.Bot.settings;
import static commands.CommandList.cmdNext;
import static commands.CommandList.cmdBack;
import static commands.CommandList.cmdSign;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import commands.color.ColorSetter;
import commands.color.colorchat.ChatColorSetter;
import commands.general.pageChange.Pagenator;
import eventInfo.MessageInfo;
import macro.Macros;
import messages.EmbedData;
import messages.MessageEffects;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class CommandHandler extends MessageInfo {
	public static Color embedMessagingColor;
	public static Color chatColor;

	public static void handler() {
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

			EmbedData.resetEmbedData();
			checkForCommands();
			checkForMacros();
			// Checks for any effects applied to the message and then resets the embed
			// settings so they can be used properly for the next message
			MessageEffects.messageEffects();
		}
	}

	public static void activeCommands() {
		
		if (!author.equals(botUser)) {
			if (commands.management.spamFilter.EnforceSpamFilter.activeCommand() == true) {
				message.delete().submit();
			}

			if (commands.management.muter.EnforceMutes.activeCommand() == true) {
				message.delete().submit();
			}

			if (commands.management.lengthFilter.EnforceLengthFilter.activeCommand() == true) {
				message.delete().submit();
			}

			if (commands.management.slowMode.EnforceSlowModes.activeCommand() == true) {
				message.delete().submit();
			}

		}
	}

	private static void checkForCommands() {
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
							message.delete().submit();
						}
						commandMessageIdToEdit = Pagenator.getMessageId();
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				
				if ((commandSend != null) && (commandMessageIdToEdit != null)) {
					channel.editMessageById(commandMessageIdToEdit, commandSend).submit();
				}
				if ((commandSend != null) && (commandMessageIdToEdit == null)) {
					message.editMessage(commandSend).submit();
				}
				if (commandSend == null) {
					message.delete().submit();
				}
				bot.SystemTrayHandler.executeTray(false);
				bot.settings.SettingSaver.saveSettings();
			}
		}
	}
	
	public static void disableChatFor(String userId, String guildId) {
		Guild guild = jda.getGuildById(guildId);
		Member member = guild.getMemberById(userId);
		List<TextChannel> textChannels = guild.getTextChannels();
		for (int i = 0; i < textChannels.size(); i++) {
			if (!(textChannels.get(i).getMemberPermissionOverrides()
					.contains(textChannels.get(i).getPermissionOverride(member)))) {
				textChannels.get(i).createPermissionOverride(member).setDeny(Permission.MESSAGE_WRITE).complete();
			} else {
				textChannels.get(i).getPermissionOverride(member).getManager().deny(Permission.MESSAGE_WRITE)
						.complete();

			}
		}
	}
	
	private static void checkForMacros() {
		for (int i = 0; i < Macros.macros.size(); i++) {
			if (message.getContentRaw().startsWith(cmdSign + Macros.macros.get(i).getName())) {
				message.editMessage(Macros.macros.get(i).getAction()).submit();
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
				textChannels.get(i).getPermissionOverride(member).getManager().deny(Permission.MESSAGE_WRITE)
						.complete();

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
		guild.getController().setNickname(guild.getMemberById(userId), nicknameToLock).submit();
	}
}
