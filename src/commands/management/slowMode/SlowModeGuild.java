package commands.management.slowMode;

import static commands.CommandList.cmdSlowModeGuild;
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

public class SlowModeGuild extends Statuses {
	public static Message slowModeGuild() {
		title = "Slow Mode";
		embColor = Color.GREEN;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		if (rawMessageContent.startsWith(cmdSlowModeGuild.getCommand())) {

			GuildStatus guildToSlowMode = new GuildStatus(guildId);
			guildToSlowMode = applyExistingGuildStatus(guildToSlowMode);

			if (alreadyAffected(guild) == true) {
				affectedGuilds.remove(guildToSlowMode);
				guildToSlowMode = guildToSlowMode.removeAllSlowModeChannels();
				affectedGuilds.add(guildToSlowMode);
				guildStatusHasChanged();
				embContent = "Slowmode no longer active on current guild.";
			} else {
				affectedGuilds.remove(guildToSlowMode);
				for (int i = 0; i < guild.getTextChannels().size(); i++) {
					if (!guildToSlowMode.getSlowModeChannels().contains(guild.getTextChannels().get(i).getId())) {
					guildToSlowMode = guildToSlowMode.slowModeChannel(guild.getTextChannels().get(i).getId());
					}
				}
				affectedGuilds.add(guildToSlowMode);
				embContent = "Slowmode active on current guild.";
				enforceSlowMode = true;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	protected static boolean alreadyAffected(Guild guild) {
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (affectedGuilds.get(i).getGuildId().equals(guild.getId())) {
				if (affectedGuilds.get(i).getSlowModeChannels().size() == guild.getTextChannels().size()) {
					return true;
				}
			}
		}
		return false;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign) throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("slowmode guild", cmdSign, adminCmdSign, CommandType.MANAGER,
				"slows down chat for a guild.", "thisCommand guild").addFirstExample("thisCommand guild")
						.addPermissionRequirements(new String[] { Permission.MANAGE_CHANNEL.getName(),
								Permission.MESSAGE_MANAGE.getName(), Permission.MESSAGE_HISTORY.getName() })
						.addMethod(SlowModeGuild.class.getMethod("slowModeGuild")).build();
	}
}
