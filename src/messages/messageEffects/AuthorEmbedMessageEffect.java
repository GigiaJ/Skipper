package messages.messageEffects;

import static bot.Bot.settings;
import static eventInfo.MessageInfo.channel;
import static eventInfo.MessageInfo.message;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import eventInfo.MessageInfo;
import messages.EmbedMessage;
import messages.MessageEffects;
import net.dv8tion.jda.core.entities.Message;

public class AuthorEmbedMessageEffect extends MessageEffects {
	public static void authorEmbedMessageEffect(StringBuilder messageToSend) {
		if (!(message.isEdited()) && (settings.getAuthorEmbedStatus() == true)) {
			embAuthor = MessageInfo.authorName;
			embAuthorIconUrl = MessageInfo.userIcon;
			StringBuilder messageToBuild = new StringBuilder();
			messageToBuild.append(messageToSend);
			embContent = messageToBuild.toString();
			MessageEffects.checkForImage(messageToBuild);

			Message toSend = null;
			// Checks to see whether the embed is a rich embed or a link
			// If a rich it does nothing, if a link it replaces with your styling of embed

			if (!(message.getEmbeds().isEmpty())) {
				if (!message.getEmbeds().get(0).getUrl().isEmpty()) {
					message.delete().complete();
					toSend = EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
				}
			} else {
				if (settings.getColorChatStatus() == true && imageUrl == null) {
					message.delete().complete();
					embContent = null;
					imageUrl = "attachment://Text.png";
					toSend = EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);

					try {
						InputStream file = new URL(bot.Bot.imageFile.toURI().toURL().toString()).openStream();
						channel.sendFile(file, "Text.png", toSend).submit();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				} else {
					message.delete().complete();
					imageUrl = null;
					toSend = EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
				}
			}
			channel.sendMessage(toSend).submit();
		}
	}
}
