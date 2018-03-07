package commands.management.purger;

import static commands.CommandList.cmdPurge;

import java.util.List;
import java.util.concurrent.ExecutionException;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class UserMessagePurger extends MessageInfo  {
	public static void purge() {
		try {
			int messagePosition = 1;
			List<Message> messageHistory = channel.getHistory().retrievePast(100).submit().get();
			String rawMessageContent = message.getContentRaw();
			if (rawMessageContent.startsWith(cmdPurge.getCommand())) {
				message.delete().submit();
				String filterCommandOut = "";
				filterCommandOut = rawMessageContent.replace(cmdPurge.getCommand() + " ", "");
				int purgeNumber = Integer.valueOf(filterCommandOut);
				for (int o = 0; o < purgeNumber; messagePosition++) {
					if (messageHistory.get(messagePosition).getAuthor().equals(botUser)) {
						String messageId = messageHistory.get(messagePosition).getId();
						messageHistory.get(messagePosition).delete().submit();
						messageHistory = channel.getHistoryAround(messageId, 100).complete().getRetrievedHistory();
						o++;
						messagePosition = 0;
					}
				}
			}
		} catch (NullPointerException | InterruptedException | ExecutionException ignore) {
			ignore.printStackTrace();
		}
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("purge mine", cmdSign, "", CommandType.ADVANCED,
				"deletes the given number of messages from you.", "thisCommand mine (Number Of Messages)")
				.addFirstExample("thisCommand mine 15").addSecondExample("thisCommand mine 5")
				.addPermissionRequirements(new String[] { Permission.MESSAGE_HISTORY.getName() }).addMethod(UserMessagePurger.class.getMethod("purge")).build();
	}
}