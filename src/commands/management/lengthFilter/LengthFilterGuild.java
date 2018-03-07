package commands.management.lengthFilter;

import static commands.CommandList.cmdLengthFilterGuild;
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

public class LengthFilterGuild  extends Statuses {
	public static Message lengthFilterGuild() {
		title = "Length Filter";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();

		if (rawMessageContent.contains(cmdLengthFilterGuild.getCommand())) {
			GuildStatus guildToLengthFilter = new GuildStatus(guildId);
			guildToLengthFilter = applyExistingGuildStatus(guildToLengthFilter);

			if (alreadyAffected(guild) == true) {
				guildToLengthFilter = guildToLengthFilter.removeAllLengthFilterChannels();
				affectedGuilds.add(guildToLengthFilter);
				guildStatusHasChanged();
				embContent = "Length filter no longer active on current guild.";
			} else {
				affectedGuilds.remove(guildToLengthFilter);
				for (int i = 0; i < guild.getTextChannels().size(); i++) {
					if (!guildToLengthFilter.getLengthFilterChannels().contains(guild.getTextChannels().get(i).getId())) {
						guildToLengthFilter = guildToLengthFilter.lengthFilterChannel(guild.getTextChannels().get(i).getId());
					}
				}
				affectedGuilds.add(guildToLengthFilter);
				embContent = "Length filter active on current guild.";
				enforceLengthFilter = true;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	protected static boolean alreadyAffected(Guild guild) {
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (affectedGuilds.get(i).getGuildId().equals(guild.getId())) {
				if (affectedGuilds.get(i).getLengthFilterChannels().size() == guild.getTextChannels().size()) {
					return true;
				}
			}
		}
		return false;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("lengthfilter guild", cmdSign, adminCmdSign, CommandType.MANAGER,
				"enables or disables the text limiter in the current guild. (One character minimum)", "thisCommand")
				.addPermissionRequirements(new String[] { Permission.MANAGE_CHANNEL.getName(),
						Permission.MESSAGE_MANAGE.getName(), Permission.MESSAGE_HISTORY.getName() })
				.addMethod(LengthFilterGuild.class.getMethod("lengthFilterGuild")).build();
	}

}
