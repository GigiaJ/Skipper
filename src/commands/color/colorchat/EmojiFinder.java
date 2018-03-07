package commands.color.colorchat;

import java.util.ArrayList;
import java.util.List;

import eventInfo.MessageInfo;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;

public class EmojiFinder {

	public static ArrayList<String> emojiUrl = new ArrayList<String>();
	public static ArrayList<String> emojiUrlList = new ArrayList<String>();
	public static ArrayList<Emote> emojiNames = new ArrayList<Emote>();

	public static void emojiUrl() {
		// Issue seems to be around the connection and it remembers the
		// last emoji on the list so if you dont reset it'll work every
		// time after the initial
		JDA jda = MessageInfo.jda;
		Message message = MessageInfo.message;
		if (emojiUrlList.isEmpty()) {
			for (int i = 0; i < jda.getEmotes().size(); i++) {
				// Get a list that can get an object by the object
				// itself or by the integer IE By name or by subscript
				emojiNames.add(jda.getEmotes().get(i));
				emojiUrlList.add(jda.getEmotes().get(i).getImageUrl());
			}
		}
		emojiUrl.clear();
		List<Emote> emotes = message.getEmotes();
		if (!(emotes.isEmpty())) {
			for (int i = 0; emojiUrl.isEmpty(); i++) {
				if (emotes.contains(emojiNames.get(i))) {
					emojiUrl.add(emojiUrlList.get(i));
				}
			}
		}
	}
}
