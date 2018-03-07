package commands.management.chatFilter;

import static commands.CommandList.cmdChatFilterGuild;
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

public class ChatFilterGuild  extends Statuses {
	public static Message chatFilterGuild() {
		title = "Chat Filter";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();

		if (rawMessageContent.contains(cmdChatFilterGuild.getCommand())) {
			GuildStatus guildToChatFilter = new GuildStatus(guildId);
			guildToChatFilter = applyExistingGuildStatus(guildToChatFilter);

			if (alreadyAffected(guild) == true) {
				guildToChatFilter = guildToChatFilter.removeAllChatFilterChannels();
				affectedGuilds.add(guildToChatFilter);
				guildStatusHasChanged();
				embContent = "Chat filter no longer active on current guild.";
			} else {
				affectedGuilds.remove(guildToChatFilter);
				for (int i = 0; i < guild.getTextChannels().size(); i++) {
					if (!guildToChatFilter.getChatFilterChannels().contains(guild.getTextChannels().get(i).getId())) {
						guildToChatFilter = guildToChatFilter.chatFilterChannel(guild.getTextChannels().get(i).getId());
					}
				}
				affectedGuilds.add(guildToChatFilter);
				embContent = "Chat filter active on current guild.";
				enforceChatFilter = true;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	protected static boolean alreadyAffected(Guild guild) {
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (affectedGuilds.get(i).getGuildId().equals(guild.getId())) {
				if (affectedGuilds.get(i).getChatFilterChannels().size() == guild.getTextChannels().size()) {
					return true;
				}
			}
		}
		return false;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("chatfilter guild", cmdSign, adminCmdSign, CommandType.MANAGER,
				"enables or disables the chat filter on or off in the current channel. (Prevents words on banned word list from being used in chat by deleting the message containing.)", "thisCommand")
				.addPermissionRequirements(new String[] { Permission.MANAGE_CHANNEL.getName(),
						Permission.MESSAGE_MANAGE.getName(), Permission.MESSAGE_HISTORY.getName() })
				.addMethod(ChatFilterGuild.class.getMethod("chatFilterGuild")).build();
	}
}