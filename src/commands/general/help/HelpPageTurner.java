package commands.general.help;

import java.awt.Color;

import eventInfo.MessageInfo;

import static commands.CommandList.cmdNext;
import static commands.CommandList.cmdBack;

import messages.EmbedMessage;
import messages.EmbedData;
import net.dv8tion.jda.core.entities.Message;

public class HelpPageTurner extends EmbedData implements HelpInterface  {

	static int i = 0;
	public static Message pageChange() {
		title = "";
		embColor = Color.CYAN;
		boolean helpActive = HelpList.helpActive;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		if (helpActive == true) {
			if ((rawMessageContent.startsWith(cmdNext.getCommand()))) {
				if (i < helpPages.size() - 2) {
					i = i + 1;
					title = helpTitle + " Page " + (i + 1);
					for (int t = 0; t < COMMANDS_TO_DISPLAY; t++) {
						embContent += helpPages.get(i).get(t).getEntry();
					}
					return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
				}
			}
			if ((rawMessageContent.startsWith(cmdBack.getCommand()))) {
				if (i > 0) {
					i = i - 1;
					title = helpTitle + " Page " + (i + 1);
					for (int t = 0; t < COMMANDS_TO_DISPLAY; t++) {
						embContent += helpPages.get(i).get(t).getEntry();
					}
					return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
				}
			}
		}
		return null;
	}
	
	static void resetPages() {
		i = 0;
	}
	
	public static String messageToEdit() {
		return HelpList.helpPageID;
	}
}
