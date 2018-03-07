package commands.management.spamFilter;

import static commands.CommandList.cmdSpamFilterChannel;
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

public class SpamFilterChannel extends Statuses {
	public static Message spamFilterChannel() {
		title = "Spam Filter";
		embColor = Color.GREEN;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		String currentChannelId = MessageInfo.channel.getId();
		if (rawMessageContent.startsWith(cmdSpamFilterChannel.getCommand())) {

			GuildStatus channelToSpamFilter = new GuildStatus(guildId);
			channelToSpamFilter = applyExistingGuildStatus(channelToSpamFilter);

			if (alreadyAffected(currentChannelId) == true) {
				affectedGuilds.remove(channelToSpamFilter);
				channelToSpamFilter = channelToSpamFilter.removeSpamFilterChannel(currentChannelId);
				affectedGuilds.add(channelToSpamFilter);
				guildStatusHasChanged();
				embContent = "Spam filter no longer active on current channel.";
			} else {
				affectedGuilds.remove(channelToSpamFilter);
				channelToSpamFilter = channelToSpamFilter.spamFilterChannel(currentChannelId);
				affectedGuilds.add(channelToSpamFilter);
				embContent = "Spam filter active on current channel.";
				enforceSpamFilter = true;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	protected static boolean alreadyAffected(String channelId) {
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (affectedGuilds.get(i).getSpamFilterChannels().contains(channelId)) {
				return true;
			}
		}
		return false;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign) throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("spamfilter channel", cmdSign, adminCmdSign, CommandType.MANAGER,
				"enables or disables the anti-spam in the current channel. (Prevents duplicate messages and spamming)",
				"thisCommand")
						.addPermissionRequirements(new String[] { Permission.MANAGE_CHANNEL.getName(),
								Permission.MESSAGE_MANAGE.getName(), Permission.MESSAGE_HISTORY.getName() })
						.addMethod(SpamFilterChannel.class.getMethod("spamFilterChannel")).build();
	}
}
