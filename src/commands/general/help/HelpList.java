package commands.general.help;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.general.pageChange.Pagenator;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import messages.EmbedData;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class HelpList extends EmbedData implements HelpInterface {
	static Message helpPage = null;
	static String helpPageID = "";
	static int i = 0;
	public static boolean helpActive = false;

	public static Message help() {
		embColor = Color.CYAN;
		HelpInterface.createDiscordHelpPages();
		HelpPageTurner.resetPages();
		i = 1;
		for (int i = 0; i < COMMANDS_TO_DISPLAY; i++) {
			embContent += helpPages.get(0).get(i).getEntry();
		}
		title = helpTitle + " Page " + i;
		helpPageID = MessageInfo.message.getId();
		helpActive = true;
		Pagenator.lastCommand();
		return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("help", cmdSign, "", CommandType.GENERAL, "opens the regular help.", "thisCommand")
				.addMethod(HelpList.class.getMethod("help"))
				.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() }).build();
	}
}
