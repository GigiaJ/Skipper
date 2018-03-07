package commands.management.spamFilter;

import java.util.List;

import commands.management.manager.Statuses;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class EnforceSpamFilter extends Statuses {
	private final static int ONE_BILLION = 1000000000;
	private final static int THIRD_MESSAGE = 2;

	public static boolean activeCommand() {
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		MessageChannel channel = MessageInfo.channel;
		if (enforceSpamFilter == true) {
			for (int i = 0; i < affectedGuilds.size(); i++) {
				if (affectedGuilds.get(i).getSpamFilterChannels().contains(channel.getId())) {
					List<Message> messageHistory = channel.getHistory().retrievePast(50).complete();
					if (messageHistory.get(THIRD_MESSAGE).getContentDisplay().equals(rawMessageContent)) {
						if (message.getAttachments().isEmpty()) {
							if (!(message.isEdited())) {
								return true;
							}
							if (!message.getEmbeds().isEmpty()) {
								if (!messageHistory.get(THIRD_MESSAGE).getEmbeds().isEmpty()) {
									if (message.getEmbeds().get(0).getDescription().equals(
											messageHistory.get(THIRD_MESSAGE).getEmbeds().get(0).getDescription())) {
										return true;
									}
								}
							}
						}
					}
					long lastMessageTime = messageHistory.get(THIRD_MESSAGE).getCreationTime().toEpochSecond();
					long currentMessageTime = message.getCreationTime().toEpochSecond();
					double currentNano = message.getCreationTime().getNano() / ONE_BILLION;
					// Provides a decimal when divided by one billion
					double lastNano = messageHistory.get(THIRD_MESSAGE).getCreationTime().getNano() / ONE_BILLION;
					User lastMessageSender = messageHistory.get(THIRD_MESSAGE).getAuthor();
					if (lastMessageSender.equals(MessageInfo.author) && ((currentMessageTime + currentNano)
							- (lastMessageTime + lastNano) <= affectedGuilds.get(i).getAntiSpamDelay())) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
