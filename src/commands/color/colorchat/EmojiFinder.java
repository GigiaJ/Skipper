package commands.color.colorchat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import main.Main;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;

public class EmojiFinder {
    public static ArrayList<String> emojiUrlList = new ArrayList();
    public static ArrayList<Emote> emojiNames = new ArrayList();
    static ArrayList<String> emojiMentions = new ArrayList();
    static ArrayList<String> emojis = new ArrayList();
    public static ArrayList<ColorChatEmoji> colorChatEmojis = new ArrayList();

    public static void findEmojis(StringBuilder sb) {
        colorChatEmojis.clear();

        for(int i = 0; i < emojis.size(); ++i) {
            if (sb.toString().contains((CharSequence)emojis.get(i))) {
                Matcher matcher = null;
                matcher = Pattern.compile((String)emojis.get(i)).matcher(sb.toString());

                while(matcher.find()) {
                    colorChatEmojis.add(new ColorChatEmoji(matcher.start(), (String)emojiUrlList.get(i), (String)emojis.get(i)));
                }
            }
        }

        Collections.sort(colorChatEmojis, (emoji1, emoji2) -> {
            return emoji1.getStartPos() - emoji2.getStartPos();
        });
    }

    public static void populateEmojiList() {
        JDA jda = Main.jda;
        int i;
        if (emojiNames.isEmpty()) {
            for(i = 0; i < jda.getEmotes().size(); ++i) {
                emojiNames.add((Emote)jda.getEmotes().get(i));
                emojiUrlList.add(((Emote)jda.getEmotes().get(i)).getImageUrl());
            }
        }

        for(i = 0; i < emojiNames.size(); ++i) {
            StringBuilder emojiInput = new StringBuilder(((Emote)emojiNames.get(i)).getAsMention());
            emojiInput = emojiInput.replace(emojiInput.lastIndexOf(">"), emojiInput.lastIndexOf(">") + 1, "");
            emojiInput = emojiInput.replace(emojiInput.indexOf("<"), emojiInput.indexOf("<") + 1, "");
            emojiInput = emojiInput.replace(emojiInput.lastIndexOf(":") + 1, emojiInput.length(), "");
            emojis.add(emojiInput.toString());
            emojiMentions.add(((Emote)emojiNames.get(i)).getAsMention());
        }

    }
}
