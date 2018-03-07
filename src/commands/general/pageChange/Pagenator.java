package commands.general.pageChange;

import static commands.CommandList.cmdHelp;
import static commands.CommandList.cmdServerMembers;
import static commands.CommandList.cmdServerRoles;

import commands.general.help.HelpPageTurner;
import commands.identifier.PageTurner;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.entities.Message;

public class Pagenator {
	static String previousCommand = "";
	static boolean pageTurnForHelp = false;
	static boolean pageTurnForAdminHelp = false;
	static boolean pageTurnForMembers = false;
	static boolean pageTurnForRoles = false;

	public static Message pagenator() {
		if (pageTurnForMembers == true) {
			return PageTurner.pageChange();
		}
		if (pageTurnForRoles == true) {
			return PageTurner.pageChange();
		}
		if (pageTurnForHelp == true) {
			return HelpPageTurner.pageChange();
		}

		return null;
	}

	public static String getMessageId() {
		if (pageTurnForRoles == true) {
			return PageTurner.messageToEdit();
		}
		if (pageTurnForMembers == true) {
			return PageTurner.messageToEdit();
		}
		if (pageTurnForHelp == true) {
			return HelpPageTurner.messageToEdit();
		}
		return null;
	}

	public static void lastCommand() {
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		
		if (rawMessageContent.equals(cmdHelp.getCommand())) {
			previousCommand = cmdHelp.getCommand();
		}

		if (rawMessageContent.equals(cmdServerMembers.getCommand())) {
			previousCommand = cmdServerMembers.getCommand();
		}

		if (rawMessageContent.equals(cmdServerRoles.getCommand())) {
			previousCommand = cmdServerRoles.getCommand();
		}

		if (!(org.apache.commons.lang3.StringUtils.isEmpty(previousCommand))) {
			if (previousCommand.equals(cmdHelp.getCommand())) {
				pageTurnForHelp = true;
			}

			if (previousCommand.equals(cmdServerMembers.getCommand())) {
				pageTurnForMembers = true;
			}

			if (previousCommand.equals(cmdServerRoles.getCommand())) {
				pageTurnForRoles = true;
			}
		}

	}
}
