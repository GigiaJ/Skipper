package bot.settings;

import java.util.ArrayList;

import commands.management.manager.GuildStatus;
import commands.management.manager.MemberStatus;
import macro.Macro;

public class Setting {
	protected final String currentVersion;
	protected final String currentCommandSign;
	protected final String currentAdminCommandSign;
	protected final boolean colorChatStatus;
	protected final boolean autoColorStatus;
	protected final boolean embedMessageStatus;
	protected final boolean authorEmbedStatus;
	protected final String colorChatColor;
	protected final String embedColor;
	protected final String currentFontStyle;
	protected final int currentFontSize;
	protected final boolean[] nsfwFilters;
	protected final ArrayList<MemberStatus> affectedUsers;// = new ArrayList<MemberStatus>();
	protected final ArrayList<GuildStatus> affectedGuilds;// = new ArrayList<GuildStatus>();
	protected final ArrayList<String> bannedWordList;
	protected final ArrayList<Macro> macros;

	public Setting(SettingBuilder builder) {
		currentVersion = builder.currentVersion;
		currentCommandSign = builder.currentCommandSign;
		currentAdminCommandSign = builder.currentAdminCommandSign;
		colorChatStatus = builder.colorChatStatus;
		autoColorStatus = builder.autoColorStatus;
		embedMessageStatus = builder.embedMessageStatus;
		authorEmbedStatus = builder.authorEmbedStatus;
		colorChatColor = builder.colorChatColor;
		embedColor = builder.embedColor;
		currentFontStyle = builder.currentFontStyle;
		currentFontSize = builder.currentFontSize;
		nsfwFilters = builder.nsfwFilters;
		affectedUsers = builder.affectedUsers;
		affectedGuilds = builder.affectedGuilds;
		bannedWordList = builder.bannedWordList;
		macros = builder.macros;
	}
	
	public String getCurrentVersion() {
		return currentVersion;
	}

	public String getCommandSign() {
		return currentCommandSign;
	}

	public String getAdminCommandSign() {
		return currentAdminCommandSign;
	}

	public boolean getColorChatStatus() {
		return colorChatStatus;
	}

	public boolean getAutoColorStatus() {
		return autoColorStatus;
	}

	public boolean getEmbedMessageStatus() {
		return embedMessageStatus;
	}

	public boolean getAuthorEmbedStatus() {
		return authorEmbedStatus;
	}

	public String getColorChatColor() {
		return colorChatColor;
	}

	public String getEmbedColor() {
		return embedColor;
	}

	public String getCurrentFontStyle() {
		return currentFontStyle;
	}

	public int getCurrentFontSize() {
		return currentFontSize;
	}

	public boolean[] getNsfwFilters() {
		return nsfwFilters;
	}

	public ArrayList<MemberStatus> getAffectedUsers() {
		return affectedUsers;
	}

	public ArrayList<GuildStatus> getAffectedGuilds() {
		return affectedGuilds;
	}

	public ArrayList<String> getBannedWordList() {
		return bannedWordList;
	}

	public ArrayList<Macro> getMacros() {
		return macros;
	}

	public GuildStatus getGuildById(String guildId) {
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (affectedGuilds.get(i).getGuildId().equals(guildId)) {
				return affectedGuilds.get(i);
			}
		}
		return null;
	}

	public MemberStatus getUserById(String userId) {
		for (int i = 0; i < affectedUsers.size(); i++) {
			if (affectedUsers.get(i).getUserId().equals(userId)) {
				return affectedUsers.get(i);
			}
		}
		return null;
	}
	
	public SettingBuilder debuild() {
		return new SettingBuilder(this);
	}
}
