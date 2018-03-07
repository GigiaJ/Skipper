package commands.general.pageChange;

import static commands.CommandList.cmdBack;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.entities.Message;

public class PreviousPage {
	public static Message previousPage() {
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		if (rawMessageContent.startsWith(cmdBack.getCommand())) {
			return Pagenator.pagenator();
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("back", cmdSign, "", CommandType.GENERAL,
				"will turn the previous page of a message if possible.", "thisCommand")
						.addMethod(PreviousPage.class.getMethod("previousPage")).build();
	}
}
