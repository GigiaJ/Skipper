package commands.management.spamFilter;

import static commands.CommandList.cmdSpamFilterGuild;
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

public class SpamFilterGuild extends Statuses {
	public static Message spamFilterGuild() {
		title = "Chat Manager";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();

		if (rawMessageContent.contains(cmdSpamFilterGuild.getCommand())) {
			GuildStatus guildToSpamFilter = new GuildStatus(guildId);
			guildToSpamFilter = applyExistingGuildStatus(guildToSpamFilter);

			if (alreadyAffected(guild) == true) {
				guildToSpamFilter = guildToSpamFilter.removeAllSpamFilterChannels();
				affectedGuilds.add(guildToSpamFilter);
				guildStatusHasChanged();
				embContent = "Spam filter no longer active on current guild.";
			} else {
				affectedGuilds.remove(guildToSpamFilter);
				for (int i = 0; i < guild.getTextChannels().size(); i++) {
					if (!guildToSpamFilter.getSpamFilterChannels().contains(guild.getTextChannels().get(i).getId())) {
						guildToSpamFilter = guildToSpamFilter.spamFilterChannel(guild.getTextChannels().get(i).getId());
					}
				}
				affectedGuilds.add(guildToSpamFilter);
				embContent = "Spam filter active on current guild.";
				enforceSpamFilter = true;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	protected static boolean alreadyAffected(Guild guild) {
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (affectedGuilds.get(i).getGuildId().equals(guild.getId())) {
				if (affectedGuilds.get(i).getSpamFilterChannels().size() == guild.getTextChannels().size()) {
					return true;
				}
			}
		}
		return false;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("spamfilter guild", cmdSign, adminCmdSign, CommandType.MANAGER,
				"enables or disables the anti-spam in the current guild. (Prevents duplicate messages and spamming)",
				"thisCommand")
						.addPermissionRequirements(new String[] { Permission.MANAGE_CHANNEL.getName(),
								Permission.MESSAGE_MANAGE.getName(), Permission.MESSAGE_HISTORY.getName() })
						.addMethod(SpamFilterGuild.class.getMethod("spamFilterGuild")).build();
	}

}
