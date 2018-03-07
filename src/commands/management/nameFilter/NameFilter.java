package commands.management.nameFilter;

import static commands.CommandList.cmdNameFilter;
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

public class NameFilter extends Statuses {
	public static Message nameFilter() {
		title = "Name Filter";
		embColor = Color.GREEN;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		GuildStatus guildToNameFilter = new GuildStatus(guildId);
		guildToNameFilter = applyExistingGuildStatus(guildToNameFilter);
		if (rawMessageContent.startsWith(cmdNameFilter.getCommand())) {
			affectedGuilds.remove(guildToNameFilter);
			guildToNameFilter.setNameFilter(!guildToNameFilter.getNameFilter());
			affectedGuilds.add(guildToNameFilter);
			embContent = "Name filter active on current guild.";
			enforceNameFilter = true;
			guildStatusHasChanged();
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign) throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("namefilter", cmdSign, adminCmdSign, CommandType.MANAGER,
				"toggles the name filter in the guild. (Name Manager prevents users from setting names on the banned words list.)",
				"thisCommand").addPermissionRequirements(
						new String[] { Permission.MESSAGE_EMBED_LINKS.getName(), Permission.NICKNAME_MANAGE.getName() })
						.addMethod(NameFilter.class.getMethod("nameFilter")).build();
	}
}
