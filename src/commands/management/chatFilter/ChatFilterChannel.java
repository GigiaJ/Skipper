package commands.management.chatFilter;

import static commands.CommandList.cmdChatFilterChannel;
import static messages.EmbedData.*;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.management.manager.GuildStatus;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class ChatFilterChannel extends Statuses {
	public static Message chatFilterChannel() {
		title = "Chat Filter";
		embColor = Color.GREEN;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		String currentChannelId = MessageInfo.channel.getId();
		if (rawMessageContent.startsWith(cmdChatFilterChannel.getCommand())) {

			GuildStatus channelToChatFilter = new GuildStatus(guildId);
			channelToChatFilter = applyExistingGuildStatus(channelToChatFilter);

			if (alreadyAffected(currentChannelId) == true) {
				affectedGuilds.remove(channelToChatFilter);
				channelToChatFilter = channelToChatFilter.removeChatFilterChannel(currentChannelId);
				affectedGuilds.add(channelToChatFilter);
				guildStatusHasChanged();
				embContent = "Chat filter no longer active on current channel.";
			} else {
				affectedGuilds.remove(channelToChatFilter);
				channelToChatFilter = channelToChatFilter.chatFilterChannel(currentChannelId);
				affectedGuilds.add(channelToChatFilter);
				embContent = "Chat filter active on current channel.";
				enforceChatFilter = true;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	protected static boolean alreadyAffected(String channelId) {
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (affectedGuilds.get(i).getChatFilterChannels().contains(channelId)) {
				return true;
			}
		}
		return false;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign) throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("chatfilter channel", cmdSign, adminCmdSign, CommandType.MANAGER,
				"enables or disables the chat filter on or off in the current channel. (Prevents words on banned word list from being used in chat by deleting the message containing.)", "thisCommand")
				.addPermissionRequirements(new String[] { Permission.MANAGE_CHANNEL.getName(),
						Permission.MESSAGE_MANAGE.getName(), Permission.MESSAGE_HISTORY.getName() })
				.addMethod(ChatFilterChannel.class.getMethod("chatFilterChannel")).build();
	}
}