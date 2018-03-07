package commands.management.chatFilter.bannedWordList;

import java.util.ArrayList;

public class BannedWordList {
	public static ArrayList<String> BWL = new ArrayList<String>();
	
	public static void applySettings() {
		if (bot.Bot.settings.getBannedWordList() != null) {
			BWL = bot.Bot.settings.getBannedWordList();
		}
	}
}
