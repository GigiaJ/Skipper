package main;

import bot.settings.settingLoaders.SettingsLoader;
import commands.CommandUpdater;
import commands.color.ColorSetter;
import commands.color.colorchat.ChatColorSetter;
import commands.general.PrefixChange;
import commands.management.chatFilter.bannedWordList.BannedWordList;
import commands.management.manager.Statuses;
import commands.nsfw.FilterManager;
import macro.Macros;

public class FileLoader {
	protected static void loadFiles() throws Exception {
		SettingsLoader.loadSettings();
		Statuses.applySettings();
		Macros.applySettings();
		FilterManager.applySettings();
		BannedWordList.applySettings();
		PrefixChange.loadPrefixes();
		CommandUpdater.commands();
		try {
			ColorSetter.applyColor();
		} catch (Exception e) {
			System.out.println("Color failed to load");
			e.printStackTrace();
		}
		try {
			ChatColorSetter.applyColor();
		} catch (Exception e) {
			System.out.println("Color failed to load");
			e.printStackTrace();
		}
		SystemTrayManager.executeTray(true);
	}
}
