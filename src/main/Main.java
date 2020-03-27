package main;

import bot.settings.Setting;
import eventInfo.ChannelPermissionInfo;
import eventInfo.GuildMemberNickChangeInfo;
import eventInfo.MemberJoinInfo;
import eventInfo.MessageInfo;
import eventInfo.TypingInfo;
import handler.UnbanHandler;
import java.io.File;
import javax.security.auth.login.LoginException;
import main.loginClient.LoginClient;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
    public static File baseFolder;
    public static File browserCookiesFolder;
    public static File settingsFile;
    public static File imageFile;
    protected static File tokenLocation;
    public static String userDirectory;
    public static Setting settings;
    private static String token = "";
    public static JDA jda;

    public Main() {
    }

    public static void main(String[] args) throws Exception {
        StartUp.startUp();
        initialize();
        beginLogin();
    }

    private static synchronized void beginLogin() {
        LoginClient login = new LoginClient();
        login.start();
    }

    private static synchronized void initialize() throws Exception {
        Initialize bot = new Initialize();
        bot.start();
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Main.token = token;
    }

    public static void createJDA() throws LoginException, InterruptedException {
        jda = (new JDABuilder(AccountType.CLIENT)).setToken(getToken()).addEventListener(new Object[]{new MessageInfo()}).addEventListener(new Object[]{new TypingInfo()}).addEventListener(new Object[]{new MemberJoinInfo()}).addEventListener(new Object[]{new GuildMemberNickChangeInfo()}).addEventListener(new Object[]{new ChannelPermissionInfo()}).addEventListener(new Object[]{new UnbanHandler()}).build().awaitReady();
    }
}
