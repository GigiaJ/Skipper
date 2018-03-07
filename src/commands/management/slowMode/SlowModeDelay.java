package commands.management.slowMode;

import static commands.CommandList.cmdSlowModeDelay;
import static messages.EmbedData.*;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class SlowModeDelay extends Statuses {
	public static Message slowModeDelay() {
		title = "Slow Mode";
		embColor = Color.GREEN;
		Guild guild = MessageInfo.guild;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		if (rawMessageContent.startsWith(cmdSlowModeDelay.getCommand())) {
			String filterCommandOut = rawMessageContent.replaceAll(cmdSlowModeDelay.getCommand(), "").trim();
			for (int i = 0; i < affectedGuilds.size(); i++) {
				if (affectedGuilds.get(i).getGuildId().equals(guild.getId())) {
					affectedGuilds.get(i).setSlowModeDelay(Integer.valueOf(filterCommandOut));
				}
			}
			embContent = "The new global delay is " + filterCommandOut + ".";
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign) throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("slowmode delay", cmdSign, adminCmdSign, CommandType.MANAGER,
				"changes the delay of the slowmode within this guild. (**is global unless user has a specified delay)",
				"thisCommand (Time In Seconds)").addFirstExample("thisCommand 15").addSecondExample("thisCommand 10")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(SlowModeDelay.class.getMethod("slowModeDelay")).build();
	}
}
