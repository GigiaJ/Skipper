package commands.management.chatFilter.bannedWordList;

import static messages.EmbedData.*;
import static commands.CommandList.cmdBWLRemoveAll;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class BannedWordListRemoveAll extends BannedWordList {
	public static Message bwlRemoveAll() {
		title = "Chat Manager";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		if (rawMessageContent.startsWith(cmdBWLRemoveAll.getCommand())) {
			BWL.clear();
			embContent = "Banned word list has been cleared.";
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("bwl remove all", cmdSign, adminCmdSign, CommandType.MANAGER,
				"will remove all of the words or phrases from the list of disabled words.", "thisCommand")
				.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
				.addMethod(BannedWordListRemoveAll.class.getMethod("bwlRemoveAll")).build();
	}

}
