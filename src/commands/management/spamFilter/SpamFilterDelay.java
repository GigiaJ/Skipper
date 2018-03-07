package commands.management.spamFilter;

import static commands.CommandList.cmdSpamFilterDelay;
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

public class SpamFilterDelay extends Statuses {
	public static Message spamFilterDelay() {
		title = "Spam Filter";
		embColor = Color.GREEN;
		Guild guild = MessageInfo.guild;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		if (rawMessageContent.startsWith(cmdSpamFilterDelay.getCommand())) {
			String filterCommandOut = rawMessageContent.replaceAll(cmdSpamFilterDelay.getCommand(), "").trim();
			for (int i = 0; i < affectedGuilds.size(); i++) {
				if (affectedGuilds.get(i).getGuildId().equals(guild.getId())) {
					affectedGuilds.get(i).setAntiSpamDelay(Integer.valueOf(filterCommandOut));
				}
			}
			embContent = "The new global delay is " + filterCommandOut + ".";
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign) throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("spamfilter delay", cmdSign, adminCmdSign, CommandType.MANAGER,
				"sets the delay on the anti-spam trigger. (Default is one second)", "thisCommand (New Delay Here)")
				.addFirstExample("thisCommand 5").addSecondExample("thisCommand 15")
				.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
				.addMethod(SpamFilterDelay.class.getMethod("spamFilterDelay")).build();
	}
}
