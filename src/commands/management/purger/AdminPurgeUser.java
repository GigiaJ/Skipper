package commands.management.purger;

import static commands.CommandList.cmdAdminPurgeUser;
import static messages.EmbedData.embAuthor;
import static messages.EmbedData.embAuthorIconUrl;
import static messages.EmbedData.embColor;
import static messages.EmbedData.embContent;
import static messages.EmbedData.imageUrl;
import static messages.EmbedData.title;

import java.util.List;
import java.util.concurrent.ExecutionException;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import messages.IdExtracter;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class AdminPurgeUser {

	public static Message purgeUser() throws InterruptedException, ExecutionException {
		int messagePosition = 0;
		Message message = MessageInfo.message;
		MessageChannel channel = MessageInfo.channel;
		Guild guild = MessageInfo.guild;
		List<Message> messageHistory = null;
		message.getChannelType();
		if (message.isFromType(ChannelType.TEXT)) {
			messageHistory = channel.getHistory().retrievePast(100).submit().get();
		}

		String rawMessageContent = message.getContentRaw();
		String filterCommandOut = rawMessageContent.replace(cmdAdminPurgeUser.getCommand(), "").trim();
		String userId = IdExtracter.getIdFromMessage(filterCommandOut);
		if (userId != null) {
			filterCommandOut = IdExtracter.removeIdFromMessage(filterCommandOut).trim();
			int purgeNumber = Integer.valueOf(filterCommandOut);

			for (int o = 0; o < purgeNumber; messagePosition++) {
				if (messageHistory.get(messagePosition).getAuthor().equals(guild.getMemberById(userId).getUser())) {
					String messageId = messageHistory.get(messagePosition).getId();
					messageHistory.get(messagePosition).delete().submit();
					messageHistory = channel.getHistoryAround(messageId, 100).complete().getRetrievedHistory();
					o++;
					messagePosition = 0;
				}
			}
		} else {
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("purge user", cmdSign, adminCmdSign, CommandType.ADVANCED,
				"deletes the given number of messages of a user from chat.",
				"thisCommand (@ Desired User) (Number Of Messages)").addFirstExample("thisCommand @G#7185 15")
						.addSecondExample("thisCommand @G#7185 30")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_HISTORY.getName(),
								Permission.MESSAGE_MANAGE.getName() })
						.addMethod(AdminPurgeUser.class.getMethod("purgeUser")).build();
	}
}
