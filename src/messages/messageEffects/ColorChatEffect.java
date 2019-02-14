package messages.messageEffects;

import static main.Main.settings;

import commands.color.colorchat.ColorChatImageCreator;
import messages.EmbedData;
import messages.MessageEffects;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class ColorChatEffect extends MessageEffects {
	public static void colorChatEffect(StringBuilder messageToSend, MessageChannel channel, Message message) {
		if (settings.getColorChatStatus() == true) {
			int firstIndex = 0;
			int secondIndex = 0;
			int endOfEmoteName = 0;
			// Pulls out the extra fluff that you get in a raw emote
			if (message != null) {
				for (int i = 0; i < message.getEmotes().size(); i++) {
					firstIndex = messageToSend.indexOf("<:");
					secondIndex = messageToSend.indexOf(">", firstIndex);
					endOfEmoteName = messageToSend.indexOf(":", firstIndex + 2);
					messageToSend.replace(firstIndex, firstIndex + 1, "");
					messageToSend.replace(secondIndex - 1, secondIndex, "");
					messageToSend.replace(endOfEmoteName, secondIndex - 1, "");
					firstIndex = secondIndex;
				}
			}

			try {
				ColorChatImageCreator.textToImage(messageToSend.toString(), settings.getCurrentFontStyle(),
						settings.getCurrentFontSize());
			} catch (Exception e) {
		        e.printStackTrace();
			}

			if (!settings.getAuthorEmbedStatus() && !settings.getEmbedMessageStatus() && EmbedData.imageUrl == null) {
				if (message != null) {
					message.delete().complete();
				}
				channel.sendFile(main.Main.imageFile).complete();
			}
		}
	}
}
