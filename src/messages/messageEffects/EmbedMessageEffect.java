package messages.messageEffects;

import static main.Main.settings;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import messages.EmbedMessage;
import messages.MessageEffects;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class EmbedMessageEffect extends MessageEffects {
	public static void embedMessageEffect(StringBuilder messageToSend, MessageChannel channel, Message message) {
			if (settings.getEmbedMessageStatus() == true) {
				// If we want to make some change later I've left it as a SB typically for a
				// filter or something
				StringBuilder messageToBuild = new StringBuilder();
				messageToBuild.append(messageToSend);
				embContent = messageToBuild.toString();
				MessageEffects.checkForImage(messageToBuild);
				Message toSend = null;
				List<MessageEmbed> embedsInMessage = null;
				if (message != null) {
					embedsInMessage = message.getEmbeds();
				}
				// Checks to see whether the embed is a rich embed or a link
				// If a rich it does nothing, if a link it replaces with your styling of embed
				if (embedsInMessage != null && !(embedsInMessage.isEmpty())) {
					if (!embedsInMessage.get(0).getUrl().isEmpty()) {
						if (message != null) {
							message.delete().complete();
						}
						toSend = EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl,
								embColor);
					}
				} else {
					if (settings.getColorChatStatus() == true && imageUrl == null) {
						if (message != null) {
							message.delete().complete();
						}
						embContent = null;
						imageUrl = "attachment://Text.png";
						toSend = EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl,
								embColor);

						try {
							InputStream file = new URL(main.Main.imageFile.toURI().toURL().toString()).openStream();
							channel.sendFile(file, "Text.png", toSend).submit();
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					} else {
						if (message != null) {
							message.delete().complete();
						}
						imageUrl = null;
						toSend = EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl,
								embColor);
					}
				}
				channel.sendMessage(toSend).submit();
			}
	}
}
