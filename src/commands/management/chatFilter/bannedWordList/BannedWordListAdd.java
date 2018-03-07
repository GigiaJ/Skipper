package commands.management.chatFilter.bannedWordList;

import static commands.CommandList.listOfCommands;
import static commands.CommandList.cmdBWLAdd;
import static commands.CommandList.cmdBWLRemove;
import static messages.EmbedData.*;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class BannedWordListAdd extends BannedWordList {
	public static Message bwlAdd() {
		title = "Chat Manager";
		embColor = Color.GREEN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		if (rawMessageContent.startsWith(cmdBWLAdd.getCommand())) {
			String filterCommandOut = rawMessageContent.replaceAll(cmdBWLAdd.getCommand(), "");
			filterCommandOut = messages.StringDecrypter.decrypt(filterCommandOut);

			if (!(BWL.contains(filterCommandOut))) {
				if (filterCommandOut.equals("") || filterCommandOut.equalsIgnoreCase("all")) {
					embContent = "Please insert a valid phrase or word.";
					return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
				}
				boolean isCommand = false;
				for (int i = 0; i < listOfCommands.size(); i++) {
					if (filterCommandOut.contains(commands.CommandList.listOfCommands.get(i).getName())) {
						isCommand = true;
					}
				}
				if (isCommand == true) {
					embContent = "Please don't try to ban commands.";
					return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);

				} else {
					// banWordsList = new FileWriter(bwlFile, true);
					// PrintWriter printer = new PrintWriter(banWordsList);
					for (int i = 0; i < filterCommandOut.split(",").length; i++) {
						BWL.add(filterCommandOut.split(",")[i]);
					}
					embContent = filterCommandOut + " disabled to re-enable use the command "
							+ cmdBWLRemove.getCommand();
					// printer.close();
					return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
				}
			} else {
				embContent = "Word is already on the list.";
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			}
		}
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("bwl add", cmdSign, adminCmdSign, CommandType.MANAGER,
				"will add given word(s) or phrase(s) to the list of words disabled upon the server by seperating via commas you can add multiple at once.",
				"thisCommand (Words To Ban Here)").addFirstExample("thisCommand Ass, Fuck, Bitch")
						.addSecondExample("thisCommand Ass")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(BannedWordListAdd.class.getMethod("bwlAdd")).build();
	}


}
