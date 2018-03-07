package commands.management.chatFilter;

import static commands.CommandList.cmdChatFilterCheck;
import static messages.EmbedData.*;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.management.manager.GuildStatus;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class ChatFilterCheck extends Statuses {

	public static Message chatFilterCheck() {
		title = "Chat Filter";
		embColor = Color.GREEN;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		JDA jda = MessageInfo.jda;
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		GuildStatus currentGuild = null;
		if (rawMessageContent.startsWith(cmdChatFilterCheck.getCommand())) {
			embContent += "**Guilds:** ";
			for (int i = 0; i < affectedGuilds.size(); i++) {
				if (!affectedGuilds.get(i).getChatFilterChannels().isEmpty()) {
					embContent += jda.getGuildById((affectedGuilds.get(i).getGuildId())).getName() + ", ";
				}
				if (affectedGuilds.get(i).getGuildId().equals(guildId)) {
					currentGuild = affectedGuilds.get(i);
				}
			}

			if (embContent.lastIndexOf(":**") + 4 == embContent.length()) {
				embContent += "No Guilds Being Filtered";
			} else {
				embContent = embContent.substring(0, embContent.length() - 2);
			}
			embContent += "\n";
			if (currentGuild != null) {
				if (!currentGuild.getChatFilterChannels().isEmpty()) {
					embContent += "**Channels in this Guild:** ";
					for (int i = 0; i < currentGuild.getChatFilterChannels().size(); i++) {
						embContent = embContent.replaceFirst("  ", ", ");
						embContent += guild.getTextChannelById(currentGuild.getChatFilterChannels().get(i)).getName()
								+ "  ";
					}
				}
			}
			if (embContent.lastIndexOf(":**") + 4 == embContent.length()) {
				embContent += "No channels in this guild being filtered";
			} else {
				if (embContent.contains(", "))
				embContent = embContent.substring(0, embContent.length() - 2);
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("chatfilter check", cmdSign, adminCmdSign, CommandType.MANAGER,
				"displays the guilds which is being filtered and the channels on this guild if applicable.", "thisCommand")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(ChatFilterCheck.class.getMethod("chatFilterCheck")).build();
	}
}