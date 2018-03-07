package commands.management.purger;

import static commands.CommandList.cmdAdminPurgeAll;

import java.util.List;
import java.util.concurrent.ExecutionException;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class AdminPurgeAll {
	public static void purgeAll() throws InterruptedException, ExecutionException {
		int messagePosition = 0;
		Message message = MessageInfo.message;
		MessageChannel channel = MessageInfo.channel;
		List<Message> messageHistory = null;
		message.getChannelType();
		if (message.isFromType(ChannelType.TEXT)) {
			messageHistory = channel.getHistory().retrievePast(100).submit().get();
		}

		String rawMessageContent = message.getContentRaw();

		String filterCommandOut = "";
		filterCommandOut = rawMessageContent.replace(cmdAdminPurgeAll.getCommand(), "").trim();
		int purgeNumber = Integer.valueOf(filterCommandOut);

		messagePosition = 1;
		for (int o = 0; o < purgeNumber; messagePosition++) {
			String messageId = messageHistory.get(messagePosition).getId();
			messageHistory.get(messagePosition).delete().submit();
			messageHistory = channel.getHistoryAround(messageId, 100).complete().getRetrievedHistory();
			o++;
			messagePosition = 0;
		}
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("purge all", cmdSign, adminCmdSign, CommandType.ADVANCED,
				"deletes the given number of messages from chat.", "thisCommand (Number Of Messages)")
						.addFirstExample("thisCommand 15").addSecondExample("thisCommand 5")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_HISTORY.getName(),
								Permission.MESSAGE_MANAGE.getName() })
						.addMethod(AdminPurgeAll.class.getMethod("purgeAll")).build();
	}
}
