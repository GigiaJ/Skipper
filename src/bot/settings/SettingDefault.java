package bot.settings;

import static bot.Bot.settings;

import java.io.IOException;
import bot.VersionEnum;

public class SettingDefault {
	public static void setSettingDefault() throws IOException {
		SettingBuilder builder = new SettingBuilder();
		builder.setCurrentVersion(VersionEnum.VERSION.getVersion());
		builder.setAutoColorStatus(false);
		builder.setColorChatStatus(false);
		builder.setEmbedMessageStatus(false);
		builder.setAuthorEmbedStatus(false);		
		builder.setColorChatColor("#ff0000");
		builder.setEmbedColor("#ff0000");
		builder.setCurrentFontStyle("Whitney");
		builder.setCurrentFontSize(14);
		builder.setNsfwFilters(new boolean[] {true, false, false});
		builder.setCommandSign("/");
		builder.setAdminCommandSign("/");
		builder.setAffectedGuilds(null);
		builder.setAffectedUsers(null);
		builder.setMacros(null);
		builder.setBannedWordList(null);
		settings = builder.build();
		bot.Bot.deleteDirectory(bot.Bot.baseFolder);
		bot.Bot.checkForFiles();
		SettingSaver.saveSettings();
	}
}
