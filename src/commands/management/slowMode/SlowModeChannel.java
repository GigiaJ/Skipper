package commands.management.slowMode;

import static commands.CommandList.cmdSlowModeChannel;
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

public class SlowModeChannel extends Statuses {

	public static Message slowModeChannel() {
		title = "Slow Mode";
		embColor = Color.GREEN;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		String currentChannelId = MessageInfo.channel.getId();
		if (rawMessageContent.startsWith(cmdSlowModeChannel.getCommand())) {

			GuildStatus guildToSlowMode = new GuildStatus(guildId);
			guildToSlowMode = applyExistingGuildStatus(guildToSlowMode);

			if (alreadyAffected(currentChannelId) == true) {
				affectedGuilds.remove(guildToSlowMode);
				guildToSlowMode = guildToSlowMode.removeSlowModeChannel(currentChannelId);
				affectedGuilds.add(guildToSlowMode);
				guildStatusHasChanged();
				embContent = "Slowmode no longer active on current channel.";
			} else {
				affectedGuilds.remove(guildToSlowMode);
				guildToSlowMode = guildToSlowMode.slowModeChannel(currentChannelId);
				affectedGuilds.add(guildToSlowMode);
				embContent = "Slowmode active on current channel.";
				enforceSlowMode = true;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	protected static boolean alreadyAffected(String channelId) {
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (affectedGuilds.get(i).getSlowModeChannels().contains(channelId)) {
				return true;
			}
		}
		return false;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("slowmode channel", cmdSign, adminCmdSign, CommandType.MANAGER,
				"slows down chat for a channel.", "thisCommand")
						.addFirstExample("thisCommand")
						.addPermissionRequirements(new String[] { Permission.MANAGE_CHANNEL.getName(),
								Permission.MESSAGE_MANAGE.getName(), Permission.MESSAGE_HISTORY.getName() })
						.addMethod(commands.management.slowMode.SlowModeChannel.class.getMethod("slowModeChannel"))
						.build();
	}

}
