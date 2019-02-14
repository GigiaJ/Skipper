package messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import commands.color.colorchat.EmojiFinder;
import eventInfo.TypingInfo;
import handler.CommandHandler;
import macro.Macros;
import main.JNA;
import messages.messageEffects.AuthorEmbedMessageEffect;
import messages.messageEffects.ColorChatEffect;
import messages.messageEffects.EmbedMessageEffect;
import messages.messageEffects.NoSpaceEffect;
import messages.messageEffects.ReverserEffect;
import messages.messageEffects.SpongeBobChickenCapsEffect;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;

import static eventInfo.MessageInfo.message;
import static handler.CommandHandler.*;
import static main.Main.settings;

public class MessageEffects extends EmbedData {
	private final static Pattern URL_PATTERN = Pattern.compile(
			"\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", Pattern.CASE_INSENSITIVE);

	private static String rawMessageContent = "";
	private static String messageContent = "";

	private static MessageChannel triggerChannel = null;

	public static void triggerMessageEffects(String clipboardData) throws InterruptedException {
		Message message = null;
		messageContent = clipboardData;
		rawMessageContent = clipboardData;
		// Discord seems to have a weird issue where when you first send a message the
		// data takes a few milliseconds to adjust/ work with

		embColor = embedMessagingColor;

		String channelName = JNA.currentWindow().replace(" - Discord", "");
		if (channelName.startsWith("@")) {
			channelName = channelName.replaceFirst("@", "");
			List<PrivateChannel> possiblePrivateChannels = jda.getPrivateChannels();
			for (int i = 0; i < possiblePrivateChannels.size(); i++) {

				PrivateChannel possibleChannel = jda.getPrivateChannelById(possiblePrivateChannels.get(i).getId());
				if (possibleChannel.getName().equals(channelName)) {

					triggerChannel = possibleChannel;
				}
			}

		}
		if (channelName.startsWith("#")) {
			channelName = channelName.replaceFirst("#", "");
			List<TextChannel> possibleTextChannels = jda.getTextChannelsByName(channelName, false);
			if (possibleTextChannels != null) {
				for (int i = 0; i < possibleTextChannels.size(); i++) {
					MessageChannel possibleChannel = jda.getTextChannelById(possibleTextChannels.get(i).getId());
					if (possibleChannel.equals(TypingInfo.channel)) {
						triggerChannel = TypingInfo.channel;
					}
				}
			}
		}

		StringBuilder messageToSend = new StringBuilder(rawMessageContent);

		if (rawMessageContent.contains("@")) {
			if (triggerChannel.getType().isGuild()) {
				Guild guild = jda.getTextChannelById(triggerChannel.getId()).getGuild();

				ArrayList<String> members = new ArrayList<String>();
				ArrayList<String> memberMentions = new ArrayList<String>();

				ArrayList<String> membersInMessage = new ArrayList<String>();
				ArrayList<Member> listOfMembers = new ArrayList<Member>();

				Matcher matcher = Pattern.compile("\\@[^\\\\#]+#").matcher(messageToSend.toString());

				while (matcher.find()) {
					String nameInMessage = messageToSend.substring(matcher.start() + 1, matcher.end() - 1);
					membersInMessage.add(nameInMessage);
					for (int i = 0; i < membersInMessage.size(); i++) {
						ArrayList<Member> membersWithName = new ArrayList<Member>(
								guild.getMembersByName(membersInMessage.get(i), true));
						for (int t = 0; t < membersWithName.size(); t++) {
							String discriminator = messageToSend.toString().substring(
									messageToSend.indexOf(nameInMessage) + nameInMessage.length() + 1,
									messageToSend.indexOf(nameInMessage) + nameInMessage.length() + 5);
							if (membersWithName.get(t).getUser().getDiscriminator().equals(discriminator)) {
								listOfMembers.add(membersWithName.get(t));
							}
						}
					}
				}

				for (int i = 0; i < listOfMembers.size(); i++) {
					String memberName = listOfMembers.get(i).getUser().getName();
					members.add(memberName);
					memberMentions.add(listOfMembers.get(i).getAsMention());
				}

				if (settings.getColorChatStatus() == false) {
					int index = 0;
					for (int i = 0; i < members.size(); i++) {
						if (messageToSend.toString().contains(members.get(i))) {
							index = messageToSend.indexOf(members.get(i));
							messageToSend.replace(index - 1, index + members.get(i).length() + 5,
									memberMentions.get(i));
							index = index + memberMentions.get(i).length();
						}
					}
				} else {
					int index = 0;
					for (int i = 0; i < members.size(); i++) {
						if (messageToSend.toString().contains(members.get(i))) {
							index = messageToSend.indexOf(members.get(i));
							messageToSend.replace(index - 1, index + members.get(i).length() + 5, members.get(i));
							index = index + memberMentions.get(i).length();
						}
					}
				}

			}
		}

		if (!isCommandOrMacro(messageToSend.toString())) {

			// Non-string affecting effects

			ArrayList<String> emojis = new ArrayList<String>();
			ArrayList<String> emojisOnClipboard = new ArrayList<String>();
			ArrayList<String> emojiMentions = new ArrayList<String>();
			ArrayList<String> colorChatEmojis = new ArrayList<String>();
			EmojiFinder.emojiUrl(null);
			for (int i = 0; i < EmojiFinder.emojiNames.size(); i++) {
				StringBuilder emojiInput = new StringBuilder(EmojiFinder.emojiNames.get(i).getAsMention());
				emojiInput = emojiInput.replace(emojiInput.lastIndexOf(">"), emojiInput.lastIndexOf(">") + 1, "");
				emojiInput = emojiInput.replace(emojiInput.indexOf("<"), emojiInput.indexOf("<") + 1, "");

				emojiInput = emojiInput.replace(emojiInput.lastIndexOf(":") + 1, emojiInput.length(), "");

				emojis.add(emojiInput.toString());
				emojiMentions.add(EmojiFinder.emojiNames.get(i).getAsMention());
				if (settings.getColorChatStatus() == true) {
					emojiInput = emojiInput.replace(0, 1, "");
					emojiInput = emojiInput.replace(emojiInput.length() - 1, emojiInput.length(), "");
					emojisOnClipboard.add(emojiInput.toString());
				}
			}
			int index = 0;

			for (int i = 0; i < emojis.size(); i++) {
				if (messageToSend.toString().contains(emojis.get(i))) {
					colorChatEmojis.add(emojisOnClipboard.get(i));
					index = messageToSend.indexOf(emojis.get(i));
					if (settings.getColorChatStatus() == false) {
						messageToSend.replace(index, index + emojis.get(i).length(), emojiMentions.get(i));
					} else {
						messageToSend.replace(index, index + emojis.get(i).length(), emojis.get(i));
					}
					index = index + emojiMentions.get(i).length();
				}
			}

			if (settings.getColorChatStatus() == true)
				EmojiFinder.emojiUrl(colorChatEmojis.toArray(new String[colorChatEmojis.size()]));

			// Perform in this order to prevent future bugs IE combining future effects
			messageToSend = ReverserEffect.reverseMessageEffect(messageToSend);
			messageToSend = NoSpaceEffect.noSpaceEffect(messageToSend);
			messageToSend = SpongeBobChickenCapsEffect.spongeBobChickenCapsEffect(messageToSend);
			// messageToSend = ActiveTranslateEffect.translater(messageToSend);

			ColorChatEffect.colorChatEffect(new StringBuilder(messageToSend.toString()), triggerChannel, message);
			EmbedMessageEffect.embedMessageEffect(messageToSend, triggerChannel, message);
			AuthorEmbedMessageEffect.authorEmbedMessageEffect(messageToSend, triggerChannel, message);
		} else {
			triggerChannel.sendMessage(messageToSend.toString()).complete();
		}
	}

	public synchronized static void messageEffects() {

		embColor = embedMessagingColor;

		rawMessageContent = message.getContentRaw();
		messageContent = message.getContentDisplay();

		// Discord seems to have a weird issue where when you first send a message the
		// data takes a few milliseconds to adjust/ work with

		if (!isCommandOrMacro(messageContent)) {
			StringBuilder messageToSend = new StringBuilder(checkForNicknames(rawMessageContent));

			if (!message.getAttachments().isEmpty()) {
				if (!message.getAttachments().get(0).isImage()) {
					return;
				}
			} else {
				// Perform in this order to prevent future bugs IE combining future effects
				if (!(message.isEdited())) {
					messageToSend = ReverserEffect.reverseMessageEffect(messageToSend);
					messageToSend = NoSpaceEffect.noSpaceEffect(messageToSend);
					messageToSend = SpongeBobChickenCapsEffect.spongeBobChickenCapsEffect(messageToSend);
				}
				// messageToSend = ActiveTranslateEffect.translater(messageToSend);

				// Non-string affecting effects
				if (message.getAttachments().isEmpty() && message.getEmbeds().isEmpty()) {
					ColorChatEffect.colorChatEffect(messageToSend, channel, message);
				}
				if (!(message.isEdited())) {
					EmbedMessageEffect.embedMessageEffect(messageToSend, channel, message);
					AuthorEmbedMessageEffect.authorEmbedMessageEffect(messageToSend, channel, message);
				}
			}
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

	public static boolean isCommandOrMacro(String stringToCheck) {
		for (int i = 0; i < commands.CommandList.listOfCommands.size(); i++) {
			if (stringToCheck.startsWith(commands.CommandList.listOfCommands.get(i).getCommand())) {
				return true;
			}
		}

		for (int i = 0; i < Macros.macros.size(); i++) {
			if (stringToCheck.startsWith(commands.CommandList.cmdSign.toString() + Macros.macros.get(i).getName())) {
				return true;
			}
		}
		return false;
	}

}
