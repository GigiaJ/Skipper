package commands.management.chatFilter.bannedWordList;

import static messages.EmbedData.*;
import static commands.CommandList.cmdBWLRemoveAll;
import static commands.CommandList.cmdBWLRemove;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class BannedWordListRemove extends BannedWordList {
	public static Message bwlRemove() {
		title = "Chat Manager";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		if (rawMessageContent.startsWith(cmdBWLRemove.getCommand())
				&& (!(rawMessageContent.startsWith(cmdBWLRemoveAll.getCommand())))) {
			String filterCommandOut = rawMessageContent.replaceAll(cmdBWLRemove.getCommand() + " ", "");

			if (BWL.contains(filterCommandOut)) {
				BWL.remove(filterCommandOut);
				embContent = filterCommandOut + " has been removed from the list.";

			} else {
				embContent = filterCommandOut + " is not on the list, please provide a valid entry.";
			}

			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		if (rawMessageContent.startsWith(cmdBWLRemoveAll.getCommand())) {
			return BannedWordListRemoveAll.bwlRemoveAll();
		}
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("bwl remove", cmdSign, adminCmdSign, CommandType.MANAGER,
				"will remove a given word or phrase from the list of words disabled upon the server.",
				"thisCommand (Word To Unban Here)").addFirstExample("thisCommand Ass")
						.addSecondExample("thisCommand Ass, Fuck, Bitch")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(BannedWordListRemove.class.getMethod("bwlRemove")).build();
	}

}
