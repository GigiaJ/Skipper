package commands.general.pageChange;

import static commands.CommandList.cmdNext;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.entities.Message;

public class NextPage {
	public static Message nextPage() {
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		if (rawMessageContent.startsWith(cmdNext.getCommand())) {
			return Pagenator.pagenator();
		}
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("next", cmdSign, "", CommandType.GENERAL,
				"will turn the next page of a message if possible.", "thisCommand")
						.addMethod(NextPage.class.getMethod("nextPage")).build();
	}
}
