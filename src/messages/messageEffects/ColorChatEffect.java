package messages.messageEffects;

import static bot.Bot.settings;
import static eventInfo.MessageInfo.channel;
import static eventInfo.MessageInfo.message;

import commands.color.colorchat.ColorChatImageCreator;
import messages.EmbedData;
import messages.MessageEffects;

public class ColorChatEffect extends MessageEffects {
	public static void colorChatEffect(StringBuilder messageToSend) {
		if (settings.getColorChatStatus() == true
				&& (message.getAttachments().isEmpty() && message.getEmbeds().isEmpty())) {
			int firstIndex = 0;
			int secondIndex = 0;
			int endOfEmoteName = 0;
			// Pulls out the extra fluff that you get in a raw emote
			for (int i = 0; i < message.getEmotes().size(); i++) {
				firstIndex = messageToSend.indexOf("<:");
				secondIndex = messageToSend.indexOf(">", firstIndex);
				endOfEmoteName = messageToSend.indexOf(":", firstIndex + 2);
				messageToSend.replace(firstIndex, firstIndex + 1, "");
				messageToSend.replace(secondIndex - 1, secondIndex, "");
				messageToSend.replace(endOfEmoteName, secondIndex - 1, "");
				firstIndex = secondIndex;
			}

			try {
				ColorChatImageCreator.textToImage(messageToSend.toString(), settings.getCurrentFontStyle(),
						settings.getCurrentFontSize());
			} catch (Exception e) {
				System.out.println("Exception occured while applying Color Chat effect.");
			}

			if (!settings.getAuthorEmbedStatus() && !settings.getEmbedMessageStatus() && EmbedData.imageUrl == null) {
				message.delete().complete();
				channel.sendFile(bot.Bot.imageFile).complete();
			}
		}
	}
}
