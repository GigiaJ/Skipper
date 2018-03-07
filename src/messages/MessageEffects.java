package messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import macro.Macros;
import messages.messageEffects.AuthorEmbedMessageEffect;
import messages.messageEffects.ColorChatEffect;
import messages.messageEffects.EmbedMessageEffect;

import static handler.CommandHandler.*;

public class MessageEffects extends EmbedData {
	private final static Pattern URL_PATTERN = Pattern.compile(
			"\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", Pattern.CASE_INSENSITIVE);

	private static boolean isCommand = false;
	private static boolean isMacro = false;

	private static String rawMessageContent = "";
	private static String messageContent = "";

	public synchronized static void messageEffects() {
		isCommand = false;
		isMacro = false;

		embColor = embedMessagingColor;

		rawMessageContent = message.getContentRaw();
		messageContent = message.getContentDisplay();

		// Discord seems to have a weird issue where when you first send a message the
		// data takes a few milliseconds to adjust/ work with

		// Checks for a command at the start of the message
		for (int i = 0; i < commands.CommandList.listOfCommands.size(); i++) {
			if (messageContent.startsWith(commands.CommandList.listOfCommands.get(i).getCommand())) {
				isCommand = true;
			}
		}

		for (int i = 0; i < Macros.macros.size(); i++) {
			if (messageContent.startsWith(commands.CommandList.cmdSign.toString() + Macros.macros.get(i).getName())) {
				isMacro = true;
			}
		}
		if ((isCommand == false && isMacro == false)) {
			StringBuilder messageToSend = new StringBuilder(checkForNicknames(rawMessageContent));
			//Perform in this order to prevent future bugs IE combining future effects
			//messageToSend = ReverserEffect.reverseMessageEffect(messageToSend);
			//messageToSend = ActiveTranslateEffect.translater(messageToSend);
			
			//Non-string affecting effects
			ColorChatEffect.colorChatEffect(messageToSend);
			EmbedMessageEffect.embedMessageEffect(messageToSend);
			AuthorEmbedMessageEffect.authorEmbedMessageEffect(messageToSend);
		}
	}
	
	private static String checkForNicknames(String checkForNicknames) {
		if (checkForNicknames.contains("@")) {
			for (int i = 0; i < message.getMentionedUsers().size(); i++) {
				String userId = IdExtracter.getIdFromMessage(checkForNicknames);
				if (userId != null) {
				String realName = guild.getMemberById(userId).getEffectiveName();
				if (checkForNicknames.contains(userId)) {
					checkForNicknames = checkForNicknames.replaceAll(guild.getMemberById(userId).getAsMention(),
							realName);
				}
				}
			}
		}
		return checkForNicknames;
	}
	
	public static void checkForImage(StringBuilder messageToBuild) {
		if (URL_PATTERN.matcher(rawMessageContent).find()) {
			Matcher matcher = URL_PATTERN.matcher(messageToBuild);
			matcher.find();
			int start = matcher.start();
			int end = matcher.end();
			// if image is not at the start of the message
			if (start > 0) {
				imageUrl = messageToBuild.subSequence(start, end).toString();
			}
			// if it is
			if (start == 0) {
				imageUrl = messageToBuild.subSequence(start, end).toString();
			}

			matcher = URL_PATTERN.matcher(messageToBuild);
			matcher.reset();
		}
	}

}
