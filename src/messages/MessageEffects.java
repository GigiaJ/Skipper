package messages;

import commands.Command;
import commands.CommandList;
import commands.color.colorchat.EmojiFinder;
import eventInfo.MessageInfo;
import eventInfo.TypingInfo;
import handler.CommandHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import macro.Macro;
import macro.Macros;
import main.JNA;
import main.Main;
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
import net.dv8tion.jda.core.entities.Message.Attachment;

public class MessageEffects extends EmbedData {
    private static final Pattern URL_PATTERN = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", 2);
    private static String rawMessageContent = "";
    private static String messageContent = "";
    private static MessageChannel triggerChannel = null;

    public MessageEffects() {
    }

    public static void triggerMessageEffects(String clipboardData) throws InterruptedException {
        Message message = null;
        messageContent = clipboardData;
        rawMessageContent = clipboardData;
        embColor = CommandHandler.embedMessagingColor;
        String channelName = JNA.currentWindow().replace(" - Discord", "");
        List<?> possibleTextChannels;
        if (channelName.startsWith("@")) {
            channelName = channelName.replaceFirst("@", "");
            possibleTextChannels = MessageInfo.jda.getPrivateChannels();

            for(int i = 0; i < possibleTextChannels.size(); ++i) {
                PrivateChannel possibleChannel = MessageInfo.jda.getPrivateChannelById(((PrivateChannel)possibleTextChannels.get(i)).getId());
                if (possibleChannel.getName().equals(channelName)) {
                    triggerChannel = possibleChannel;
                }
            }
        }

        if (channelName.startsWith("#")) {
            channelName = channelName.replaceFirst("#", "");
            possibleTextChannels = Main.jda.getTextChannelsByName(channelName, false);
            if (possibleTextChannels != null) {
                for(int i = 0; i < possibleTextChannels.size(); ++i) {
                    MessageChannel possibleChannel = Main.jda.getTextChannelById(((TextChannel)possibleTextChannels.get(i)).getId());
                    if (possibleChannel.equals(TypingInfo.channel)) {
                        triggerChannel = TypingInfo.channel;
                    }
                }
            }
        }

        StringBuilder messageToSend = new StringBuilder(rawMessageContent);
        if (rawMessageContent.contains("@") && triggerChannel.getType().isGuild()) {
            Guild guild = Main.jda.getTextChannelById(triggerChannel.getId()).getGuild();
            ArrayList<String> members = new ArrayList<String>();
            ArrayList<String> memberMentions = new ArrayList<String>();
            ArrayList<String> membersInMessage = new ArrayList<String>();
            ArrayList<Member> listOfMembers = new ArrayList<Member>();
            Matcher matcher = Pattern.compile("\\@[^\\\\#]+#").matcher(messageToSend.toString());

            while(matcher.find()) {
                String nameInMessage = messageToSend.substring(matcher.start() + 1, matcher.end() - 1);
                membersInMessage.add(nameInMessage);

                for(int i = 0; i < membersInMessage.size(); ++i) {
                    ArrayList<Member> membersWithName = new ArrayList<Member>(guild.getMembersByName((String)membersInMessage.get(i), true));

                    for(int t = 0; t < membersWithName.size(); ++t) {
                        String discriminator = messageToSend.toString().substring(messageToSend.indexOf(nameInMessage) + nameInMessage.length() + 1, messageToSend.indexOf(nameInMessage) + nameInMessage.length() + 5);
                        if (((Member)membersWithName.get(t)).getUser().getDiscriminator().equals(discriminator)) {
                            listOfMembers.add((Member)membersWithName.get(t));
                        }
                    }
                }
            }

            for(int i = 0; i < listOfMembers.size(); ++i) {
                String memberName = ((Member)listOfMembers.get(i)).getUser().getName();
                members.add(memberName);
                memberMentions.add(((Member)listOfMembers.get(i)).getAsMention());
            }
            
            if (!Main.settings.getColorChatStatus()) {
                int index = 0;

                for(int i = 0; i < members.size(); ++i) {
                    if (messageToSend.toString().contains((CharSequence)members.get(i))) {
                        index = messageToSend.indexOf((String)members.get(i));
                        messageToSend.replace(index - 1, index + ((String)members.get(i)).length() + 5, (String)memberMentions.get(i));
                        index = index + ((String)memberMentions.get(i)).length();
                    }
                }
            } else {
                int index = 0;

                for(int i = 0; i < members.size(); ++i) {
                    if (messageToSend.toString().contains((CharSequence)members.get(i))) {
                        index = messageToSend.indexOf((String)members.get(i));
                        messageToSend.replace(index - 1, index + ((String)members.get(i)).length() + 5, (String)members.get(i));
                        index = index + ((String)memberMentions.get(i)).length();
                    }
                }
            }
        }

        if (!isCommandOrMacro(messageToSend.toString())) {
            if (EmojiFinder.emojiNames.isEmpty()) {
                EmojiFinder.populateEmojiList();
            }

            EmojiFinder.findEmojis(messageToSend);
            messageToSend = ReverserEffect.reverseMessageEffect(messageToSend);
            messageToSend = NoSpaceEffect.noSpaceEffect(messageToSend);
            messageToSend = SpongeBobChickenCapsEffect.spongeBobChickenCapsEffect(messageToSend);
            ColorChatEffect.colorChatEffect(new StringBuilder(messageToSend.toString()), triggerChannel, (Message)message);
            EmbedMessageEffect.embedMessageEffect(messageToSend, triggerChannel, (Message)message);
            AuthorEmbedMessageEffect.authorEmbedMessageEffect(messageToSend, triggerChannel, (Message)message);
        } else {
            triggerChannel.sendMessage(messageToSend.toString()).complete();
        }

    }

    public static synchronized void messageEffects() {
        embColor = CommandHandler.embedMessagingColor;
        rawMessageContent = MessageInfo.message.getContentRaw();
        messageContent = MessageInfo.message.getContentDisplay();
        if (!isCommandOrMacro(messageContent)) {
            StringBuilder messageToSend = new StringBuilder(checkForNicknames(rawMessageContent));
            if (!MessageInfo.message.getAttachments().isEmpty()) {
                if (!((Attachment)MessageInfo.message.getAttachments().get(0)).isImage()) {
                    return;
                }
            } else {
                if (!MessageInfo.message.isEdited()) {
                    messageToSend = ReverserEffect.reverseMessageEffect(messageToSend);
                    messageToSend = NoSpaceEffect.noSpaceEffect(messageToSend);
                    messageToSend = SpongeBobChickenCapsEffect.spongeBobChickenCapsEffect(messageToSend);
                }

                if (MessageInfo.message.getAttachments().isEmpty() && MessageInfo.message.getEmbeds().isEmpty()) {
                    ColorChatEffect.colorChatEffect(messageToSend, MessageInfo.channel, MessageInfo.message);
                }

                if (!MessageInfo.message.isEdited()) {
                    EmbedMessageEffect.embedMessageEffect(messageToSend, MessageInfo.channel, MessageInfo.message);
                    AuthorEmbedMessageEffect.authorEmbedMessageEffect(messageToSend, MessageInfo.channel, MessageInfo.message);
                }
            }
        }

    }

    private static String checkForNicknames(String checkForNicknames) {
        if (checkForNicknames.contains("@")) {
            for(int i = 0; i < MessageInfo.message.getMentionedUsers().size(); ++i) {
                String userId = IdExtracter.getIdFromMessage(checkForNicknames);
                if (userId != null) {
                    String realName = MessageInfo.guild.getMemberById(userId).getEffectiveName();
                    if (checkForNicknames.contains(userId)) {
                        checkForNicknames = checkForNicknames.replaceAll(MessageInfo.guild.getMemberById(userId).getAsMention(), realName);
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
            if (start > 0) {
                imageUrl = messageToBuild.subSequence(start, end).toString();
            }

            if (start == 0) {
                imageUrl = messageToBuild.subSequence(start, end).toString();
            }

            matcher = URL_PATTERN.matcher(messageToBuild);
            matcher.reset();
        }

    }

    public static boolean isCommandOrMacro(String stringToCheck) {
        for(int i = 0; i < CommandList.listOfCommands.size(); ++i) {
            if (stringToCheck.startsWith(((Command)CommandList.listOfCommands.get(i)).getCommand())) {
                return true;
            }
        }

        for(int i = 0; i < Macros.macros.size(); ++i) {
            if (stringToCheck.startsWith(CommandList.cmdSign.toString() + ((Macro)Macros.macros.get(i)).getName())) {
                return true;
            }
        }

        return false;
    }
}