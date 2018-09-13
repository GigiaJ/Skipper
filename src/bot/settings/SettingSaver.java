package bot.settings;

import static main.Main.settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

import commands.management.chatFilter.bannedWordList.BannedWordList;
import commands.management.manager.Statuses;
import macro.Macros;

public class SettingSaver implements ISetting {

	public static void saveSettings() {
		File settingsFile = main.Main.settingsFile;
		try {
			main.Main.settingsFile.delete();
			main.Main.settingsFile.createNewFile();
			FileWriter fileWriter = new FileWriter(settingsFile, true);
			PrintWriter printer = new PrintWriter(fileWriter);
			printer.print(SETTINGS + SPACE + CURLY_BRACKET_START);
			printer.print(settings.getCurrentVersion() + COMMA);
			printer.print(Boolean.toString(settings.getAutoColorStatus()) + COMMA);
			printer.print(Boolean.toString(settings.getColorChatStatus()) + COMMA);
			printer.print(Boolean.toString(settings.getEmbedMessageStatus()) + COMMA);
			printer.print(Boolean.toString(settings.getAuthorEmbedStatus()) + COMMA);
			printer.print(settings.getColorChatColor() + COMMA);
			printer.print(settings.getEmbedColor() + COMMA);
			printer.print(settings.getCurrentFontStyle() + COMMA);
			printer.print(settings.getCurrentFontSize() + COMMA);
			printer.print(BRACKET_START);
			for (int i = 0; i < settings.getNsfwFilters().length; i++) {
				printer.print(Boolean.valueOf(settings.getNsfwFilters()[i]));
				if (i < settings.getNsfwFilters().length - 1) {
					printer.print(COMMA);
				}
			}
			printer.print(BRACKET_END);
			printer.print(COMMA);
			printer.print(settings.getCommandSign() + COMMA);
			printer.print(settings.getAdminCommandSign() + CURLY_BRACKET_END);
			printer.println();
			printer.println();
			printer.print(GUILDS + SPACE + CURLY_BRACKET_START);
			for (int i = 0; i < Statuses.affectedGuilds.size(); i++) {
				printer.print(GUILD + SPACE + PARENTHESES_START + Statuses.affectedGuilds.get(i).getGuildId() + COMMA);
				printer.print(Statuses.affectedGuilds.get(i).getSlowModeDelay() + COMMA);
				printer.print(Statuses.affectedGuilds.get(i).getAntiSpamDelay() + COMMA);
				printer.print(Boolean.valueOf(Statuses.affectedGuilds.get(i).getNameFilter()) + COMMA);
				printer.print(SLOWMODE_CHANNELS + Statuses.affectedGuilds.get(i).getSlowModeChannels() + COMMA);
				printer.print(SPAM_FILTER_CHHANELS + Statuses.affectedGuilds.get(i).getSpamFilterChannels() + COMMA);
				printer.print(CHAT_FILTER_CHANNELS + Statuses.affectedGuilds.get(i).getChatFilterChannels() + COMMA);
				printer.print(LENGTH_FILTER_CHANNELS + Statuses.affectedGuilds.get(i).getLengthFilterChannels()
						+ PARENTHESES_END + SPACE);
			}
			printer.print(CURLY_BRACKET_END);
			printer.println();
			printer.println();
			printer.print(USERS + SPACE + CURLY_BRACKET_START);
			for (int i = 0; i < Statuses.affectedUsers.size(); i++) {
				printer.print(USER + SPACE + PARENTHESES_START + Statuses.affectedUsers.get(i).getUserId() + COMMA
						+ MUTED_GUILDS + Statuses.affectedUsers.get(i).getGuildsMutedIn() + COMMA + SLOWMODE_GUILDS
						+ BRACKET_START);
				for (int t = 0; t < Statuses.affectedUsers.get(i).getGuildsSlowModeIn().size(); t++) {
					long timeLeft = (Statuses.affectedUsers.get(i).getGuildsSlowModeIn().get(t).getDuration())
							- (Instant.now().getEpochSecond()
									- Statuses.affectedUsers.get(i).getGuildsSlowModeIn().get(t).getStart());
					printer.print(PARENTHESES_START
							+ Statuses.affectedUsers.get(i).getGuildsSlowModeIn().get(t).getGuildId() + COMMA
							+ Statuses.affectedUsers.get(i).getGuildsSlowModeIn().get(t).getDelay() + COMMA
							+ timeLeft + COMMA
							+ Statuses.affectedUsers.get(i).getGuildsSlowModeIn().get(t).getStart() + PARENTHESES_END);
					if (t < (Statuses.affectedUsers.get(i).getGuildsSlowModeIn().size() - 1))
						printer.print(COMMA);

				}
				printer.print(BRACKET_END + COMMA);
				printer.print(USERLOCK_GUILDS + Statuses.affectedUsers.get(i).getGuildsNickNameLockIn() + COMMA);
				printer.print(NICKNAMES + Statuses.affectedUsers.get(i).getNickNames() + PARENTHESES_END + SPACE);
			}
			printer.print(CURLY_BRACKET_END);
			printer.println();
			printer.println();
			printer.print(BWL + SPACE + CURLY_BRACKET_START);
			if (!BannedWordList.BWL.isEmpty())
				printer.print(BannedWordList.BWL);
			printer.print(CURLY_BRACKET_END);
			printer.println();
			printer.println();
			printer.print(MACROS + SPACE + CURLY_BRACKET_START);
			for (int i = 0; i < Macros.macros.size(); i++) {
				printer.print(MACRO);
				printer.print(PARENTHESES_START);
				printer.print(Macros.macros.get(i).getName() + COMMA);
				printer.print(Macros.macros.get(i).getAction() + COMMA);
				printer.print(Macros.macros.get(i).getType());
				printer.print(PARENTHESES_END);
				if (i < (Macros.macros.size() - 1))
					printer.print(COMMA);
			}
			printer.print(CURLY_BRACKET_END);

			printer.println();
			printer.close();
			fileWriter.close();
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}
}
