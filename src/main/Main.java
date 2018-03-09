package main;

import java.io.File;
import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

import bot.settings.Setting;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import eventInfo.ChannelPermissionInfo;
import eventInfo.GuildMemberNickChangeInfo;
import eventInfo.MemberJoinInfo;
import eventInfo.MessageInfo;

public class Main {
	public static File baseFolder;
	public static File settingsFile;
	public static File imageFile;
	protected static File tokenLocation;

	public static String userDirectory;
	public static Setting settings;
	protected static String token = "";
	protected static JDA jda;

	public static void main(String[] args) throws Exception {
		StartUp.startUp();
		FileLoader.loadFiles();
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
}