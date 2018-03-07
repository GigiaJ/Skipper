package commands.management.slowMode;

import static commands.CommandList.cmdSlowModeCheck;
import static messages.EmbedData.*;

import java.awt.Color;
import java.time.Instant;

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

public class SlowModeCheck extends Statuses {

	public static Message slowModeCheck() {
		title = "Slow Mode";
		embColor = Color.GREEN;

		String rawMessageContent = MessageInfo.message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String guildId = guild.getId();
		JDA jda = MessageInfo.jda;
		GuildStatus currentGuild = null;
		if (rawMessageContent.startsWith(cmdSlowModeCheck.getCommand())) {

			embContent += "**Guilds:** ";
			for (int i = 0; i < affectedGuilds.size(); i++) {
				if (!affectedGuilds.get(i).getSlowModeChannels().isEmpty()) {
					embContent += jda.getGuildById(affectedGuilds.get(i).getGuildId()).getName() + ":"
							+ affectedGuilds.get(i).getSlowModeDelay() + "s, ";

				}
				if (affectedGuilds.get(i).getGuildId().equals(guildId)) {
					currentGuild = affectedGuilds.get(i);
				}

			}

			if (embContent.lastIndexOf(":**") + 4 == embContent.length()) {
				embContent += " No Guilds Slowed";
			} else {
				// Removes the comma from the end in a simpler method
				embContent = embContent.substring(0, embContent.length() - 2);
			}
			embContent += "\n";

			embContent += "**Channels in this Guild:** ";
			if (currentGuild != null) {
				for (int i = 0; i < currentGuild.getSlowModeChannels().size(); i++) {
					if (!currentGuild.getSlowModeChannels().isEmpty()) {
						if (embContent.contains("  ")) {
							embContent = embContent.replaceFirst("  ", ", ");
						}
						embContent += jda.getTextChannelById(currentGuild.getSlowModeChannels().get(i)).getName()
								+ "  ";
					}
				}
			}
			if (embContent.lastIndexOf(":**") + 4 == embContent.length()) {
				embContent += " No channels in this guild slowed";
			}
			embContent += "\n";

			embContent += "**Users:** ";
			for (int i = 0; i < affectedUsers.size(); i++) {
				for (int t = 0; t < affectedUsers.get(i).getGuildsSlowModeIn().size(); t++) {
					if (affectedUsers.get(i).getGuildsSlowModeIn().get(t).getGuildId().equals(guildId)) {
						if (embContent.contains("  ")) {
							embContent = embContent.replaceFirst("  ", ", ");
						}
						long timeLeft = (affectedUsers.get(i).getGuildsSlowModeIn().get(t).getDuration()) - (Instant.now().getEpochSecond() - affectedUsers.get(i).getGuildsSlowModeIn().get(t).getStart());
						embContent += guild.getMemberById(affectedUsers.get(i).getUserId()).getAsMention() + ":"
								+ affectedUsers.get(i).getGuildsSlowModeIn().get(t).getDelay() + "s:"
								+ timeLeft + "s" + "  ";
					}
				}
			}
			if (embContent.lastIndexOf(":**") + 4 == embContent.length()) {
				embContent += " No Users Slowed";
			}

			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("slowmode check", cmdSign, adminCmdSign, CommandType.MANAGER,
				"check displays the slowmode settings.", "thisCommand")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(commands.management.slowMode.SlowModeCheck.class.getMethod("slowModeCheck")).build();

	}
}
