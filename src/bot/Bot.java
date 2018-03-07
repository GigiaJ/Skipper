package bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

import bot.settings.Setting;
import bot.settings.settingLoaders.SettingsLoader;
import commands.color.ColorSetter;
import commands.color.colorchat.ChatColorSetter;
import commands.CommandUpdater;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import commands.general.PrefixChange;
import commands.management.chatFilter.bannedWordList.BannedWordList;
import commands.management.manager.Statuses;
import commands.nsfw.FilterManager;
import eventInfo.ChannelPermissionInfo;
import eventInfo.GuildMemberNickChangeInfo;
import eventInfo.MemberJoinInfo;
import eventInfo.MessageInfo;
import macro.Macros;

public class Bot {
	public static File baseFolder;
	public static File settingsFile;
	public static File imageFile;
	private static File tokenLocation;

	public static String userDirectory;
	public static Setting settings;
	private static String token = "";
	protected static JDA jda;

	public static void main(String[] args) throws Exception {
		startUp();
		loadFiles();
		try {
			jda = new JDABuilder(AccountType.CLIENT).setToken(token).addEventListener(new MessageInfo())
					.addEventListener(new MemberJoinInfo()).addEventListener(new GuildMemberNickChangeInfo())
					.addEventListener(new ChannelPermissionInfo()).buildAsync();
		} catch (LoginException e) {
			 JOptionPane.showMessageDialog(null, e.toString(), "Error",
                     JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	private static void startUp() throws IOException {
		String finalPath = "";
		if (OSCheck.isWindows()) {
			Path thisFile = Paths.get("this").toAbsolutePath();
			String userPath = thisFile.toString();
			String directory = userPath.substring(9);
			String userName = directory.substring(0, directory.indexOf("\\"));
			int userNameLength = userName.length();
			userDirectory = userPath.substring(0, 9 + userNameLength);
			finalPath = userDirectory
					+ "\\AppData\\Roaming\\discord\\Local Storage\\https_discordapp.com_0.localstorage";
			tokenLocation = Paths.get(finalPath).toFile();
			baseFolder = new File(userDirectory + "\\AppData\\Local\\Skipper");
			settingsFile = new File(Bot.userDirectory + "\\AppData\\Local\\Skipper\\Settings.txt");
			imageFile = new File(Bot.userDirectory + "\\AppData\\Local\\Skipper\\Text.png");
			checkForFiles();
			getToken();
		} else if (OSCheck.isMac()) {
			Path thisFile = Paths.get("this").toAbsolutePath();
			String userPath = thisFile.toString();
			String directory = userPath.substring(9);
			String userName = directory.substring(0, directory.indexOf("/"));
			int userNameLength = userName.length();
			userDirectory = userPath.substring(0, 9 + userNameLength);
			finalPath = userDirectory
					+ "/Library/Application Support/discord/Local Storage/https_discordapp.com_0.localstorage";
			tokenLocation = Paths.get(finalPath).toFile();

			File dir = new File(userDirectory + "/Library/Application Support/Skipper");
			dir.mkdir();
			settingsFile = new File(Bot.userDirectory + "/Library/Application Support/Skipper/Settings.txt");
			imageFile = new File(Bot.userDirectory + "/Library/Application Support/Skipper/Text.png");
			checkForFiles();
			getToken();

		} else

		{
			System.out.println("Your OS is not support!!");
		}
	}

	private static void loadFiles() throws Exception {
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
		SystemTrayHandler.executeTray(true);
	}

	public static void checkForFiles() throws IOException {
		if (!(baseFolder.exists())) {
			baseFolder.mkdir();
		}
		if (!(settingsFile.exists())) {
			settingsFile.createNewFile();
		}
	}

	private static void getToken() {
		String currentLine = "";
		StringBuilder tokenGetter = new StringBuilder();
		try (InputStream fisToken = new FileInputStream(tokenLocation);
				InputStreamReader isrToken = new InputStreamReader(fisToken, Charset.forName("UTF-8"));
				BufferedReader brToken = new BufferedReader(isrToken);

		) {
			while ((currentLine = brToken.readLine()) != null) {
				tokenGetter.append(currentLine);
			}
			tokenGetter = new StringBuilder(tokenGetter.toString().replaceAll("[^a-zA-Z0-9\\-\\.\\_\\\"]", ""));
			if (tokenGetter.toString().contains("token\"M")) {
				tokenGetter = new StringBuilder(tokenGetter.substring(tokenGetter.indexOf("token\"M") + 6));
				token = tokenGetter.substring(0, tokenGetter.indexOf("\""));
			} else if (tokenGetter.toString().contains("token\"m")) {
				tokenGetter = new StringBuilder(tokenGetter.substring(tokenGetter.indexOf("token\"m") + 6));
				token = tokenGetter.substring(0, tokenGetter.indexOf("\""));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(children[i]);
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
}