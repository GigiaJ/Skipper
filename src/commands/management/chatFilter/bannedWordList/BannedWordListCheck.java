package commands.management.chatFilter.bannedWordList;

import static messages.EmbedData.*;
import static commands.CommandList.cmdBWLCheck;
import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class BannedWordListCheck extends BannedWordList {
	public static Message bwlCheck() {
		title = "Chat Manager";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		if (rawMessageContent.startsWith(cmdBWLCheck.getCommand())) {
			if (!(BWL.isEmpty())) {
				embContent = BWL.toString().substring(1, BWL.toString().length() - 1);
			} else {
				embContent = "List is empty, please add to the list first.";
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("bwl check", cmdSign, adminCmdSign, CommandType.MANAGER,
				"shows all the disabled words on the given server.", "thisCommand")
				.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
				.addMethod(BannedWordListCheck.class.getMethod("bwlCheck")).build();
	}
}
