package bot.settings;

public enum SettingEnum {
	VERSION(0), AUTOCOLORSTATUS(1), COLORCHATSTATUS(2), EMBEDMESSAGE(3), AUTHOREMBED(4), CHAT_COLOR(5), EMBED_COLOR(
			6), FONTSTYLE(7), FONTSIZE(8), NSFWFILTER(
					9), COMMANDSIGN(10), ADMINCOMMANDSIGN(11), AFFECTED_GUILDS(12), AFFECTED_USERS(13), BANNED_WORD_LIST(14), MACROS(15);

	private int value;

	SettingEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
