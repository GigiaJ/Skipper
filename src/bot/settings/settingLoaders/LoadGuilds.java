package bot.settings.settingLoaders;

import java.util.ArrayList;
import java.util.Arrays;

import commands.management.manager.GuildStatus;

public class LoadGuilds extends SettingsLoader {
	protected static void checkForGuilds(StringBuilder sb) {
		ArrayList<GuildStatus> listOfGuilds = new ArrayList<GuildStatus>();
		if (sb.toString().startsWith(GUILDS + SPACE + CURLY_BRACKET_START)) {
			sb.replace(START, sb.indexOf(CURLY_BRACKET_START), EMPTY);
			while (sb.substring(START, sb.indexOf(CURLY_BRACKET_END)).contains(COMMA)) {
				while (sb.substring(START, sb.indexOf(CURLY_BRACKET_END)).contains(PARENTHESES_START)) {
					sb.replace(START, sb.indexOf(PARENTHESES_START) + 1, EMPTY);
					// Loads the ID of the guild
					GuildStatus guildToAdd = new GuildStatus(sb.substring(START, sb.indexOf(COMMA)));
					sb.replace(START, sb.indexOf(COMMA) + 2, EMPTY);
					// Loads SlowModeDelay
					guildToAdd.setSlowModeDelay((Integer.valueOf(sb.substring(START, sb.indexOf(COMMA)))));
					sb.replace(START, sb.indexOf(COMMA) + 2, EMPTY);
					// Loads AntiSpamDelay
					guildToAdd.setAntiSpamDelay((Integer.valueOf(sb.substring(START, sb.indexOf(COMMA)))));
					sb.replace(START, sb.indexOf(COMMA) + 2, EMPTY); 
					//
					// Loads NameFilter Status
					guildToAdd.setNameFilter((Boolean.valueOf(sb.substring(START, sb.indexOf(COMMA)))));
					sb.replace(START, sb.indexOf(COMMA) + 2, EMPTY);
					//
					while (sb.substring(START, sb.indexOf(PARENTHESES_END)).contains(COMMA)) {
						// Loads the SlowModeChannels
						if (!sb.substring((sb.indexOf(SLOWMODE_CHANNELS + BRACKET_START) + (SLOWMODE_CHANNELS + BRACKET_START).length()),
								sb.indexOf(BRACKET_END)).isEmpty()) {
							guildToAdd.getSlowModeChannels().addAll((Arrays
									.asList(sb.substring(sb.indexOf(BRACKET_START) + 1, sb.indexOf(BRACKET_END)).split(COMMA))));
						}
						sb.replace(START, sb.indexOf(BRACKET_END) + 2, EMPTY); // Clears the previous entry

						// Loads the SpamFilterChannels
						if (!sb.substring((sb.indexOf(SPAM_FILTER_CHHANELS + BRACKET_START) + (SPAM_FILTER_CHHANELS + BRACKET_START).length()),
								sb.indexOf(BRACKET_END)).isEmpty()) {
							guildToAdd.getSpamFilterChannels().addAll((Arrays
									.asList(sb.substring(sb.indexOf(BRACKET_START) + 1, sb.indexOf(BRACKET_END)).split(COMMA))));

						}
						sb.replace(START, sb.indexOf(BRACKET_END) + 2, EMPTY); // Clears the previous entry

						// Loads the ChatFilterChannels
						if (!sb.substring((sb.indexOf(CHAT_FILTER_CHANNELS + BRACKET_START) + (CHAT_FILTER_CHANNELS + BRACKET_START).length()),
								sb.indexOf(BRACKET_END)).isEmpty()) {
							// For Later Guild Effect
						}
						sb.replace(START, sb.indexOf(BRACKET_END) + 2, EMPTY); // Clears the previous entry

						// Loads the LengthFilterChannels
						if (!sb.substring(
								(sb.indexOf(LENGTH_FILTER_CHANNELS + BRACKET_START) + (LENGTH_FILTER_CHANNELS + BRACKET_START).length()),
								sb.indexOf(BRACKET_END)).isEmpty()) {
							guildToAdd.getLengthFilterChannels().addAll((Arrays
									.asList(sb.substring(sb.indexOf(BRACKET_START) + 1, sb.indexOf(BRACKET_END)).split(COMMA))));
						}
						sb.replace(START, sb.indexOf(BRACKET_END) + 1, EMPTY); // Clears the previous entry
						listOfGuilds.add(guildToAdd);
					}
				}
			}
			settingsToLoad.add(listOfGuilds);
		}
	}
}
