package commands.management.lengthFilter;

import static commands.CommandList.cmdLengthFilterChannel;
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

public class LengthFilterChannel extends Statuses {
	public static Message lengthFilterChannel() {
		title = "Length Filter";
		embColor = Color.GREEN;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		String currentChannelId = MessageInfo.channel.getId();
		if (rawMessageContent.startsWith(cmdLengthFilterChannel.getCommand())) {

			GuildStatus channelToLengthFilter = new GuildStatus(guildId);
			channelToLengthFilter = applyExistingGuildStatus(channelToLengthFilter);

			if (alreadyAffected(currentChannelId) == true) {
				affectedGuilds.remove(channelToLengthFilter);
				channelToLengthFilter = channelToLengthFilter.removeLengthFilterChannel(currentChannelId);
				affectedGuilds.add(channelToLengthFilter);
				guildStatusHasChanged();
				embContent = "Length filter no longer active on current channel.";
			} else {
				affectedGuilds.remove(channelToLengthFilter);
				channelToLengthFilter = channelToLengthFilter.lengthFilterChannel(currentChannelId);
				affectedGuilds.add(channelToLengthFilter);
				embContent = "Length filter active on current channel.";
				enforceLengthFilter = true;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	protected static boolean alreadyAffected(String channelId) {
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (affectedGuilds.get(i).getLengthFilterChannels().contains(channelId)) {
				return true;
			}
		}
		return false;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign) throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("lengthfilter channel", cmdSign, adminCmdSign, CommandType.MANAGER,
				"enables or disables the text limiter in the current channel. (One character minimum)", "thisCommand")
				.addPermissionRequirements(new String[] { Permission.MANAGE_CHANNEL.getName(),
						Permission.MESSAGE_MANAGE.getName(), Permission.MESSAGE_HISTORY.getName() })
				.addMethod(LengthFilterChannel.class.getMethod("lengthFilterChannel")).build();
	}
}