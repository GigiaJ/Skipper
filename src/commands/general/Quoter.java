package commands.general;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import messages.IdExtracter;
import messages.EmbedData;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import static commands.CommandList.cmdQuoter;

public class Quoter extends EmbedData {

	public static Message quoter() {
		String rawMessageContent = MessageInfo.message.getContentRaw();
		if (rawMessageContent.contains(cmdQuoter.getCommand())) {
			String filterCommandOut = rawMessageContent.replace(cmdQuoter.getCommand() + " ", "");
			String userId = IdExtracter.getIdFromMessage(filterCommandOut);
			if (userId != null) {
				JDA jda = MessageInfo.jda;
				embContent = IdExtracter.removeIdFromMessage(filterCommandOut);
				embAuthor = jda.getUserById(userId).getName();
				embAuthorIconUrl = jda.getUserById(userId).getAvatarUrl();
				title = "Said";
				embColor = Color.magenta;
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("quoter", cmdSign, "", CommandType.GENERAL,
				"will quote the given person with the supplied message.", "thisCommand (@ Desired User) (Message Here)")
						.addFirstExample("thisCommand @G#7185 Hello").addSecondExample("thisCommand @G#7185 Bye")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(Quoter.class.getMethod("quoter")).build();
	}
}