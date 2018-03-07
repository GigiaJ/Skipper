package commands.management.manager;

import java.util.ArrayList;

public class GuildStatus {
	
	protected String guildId;
	protected ArrayList<String> lengthFilterChannels;
	protected ArrayList<String> spamFilterChannels;
	protected ArrayList<String> chatFilterChannels;
	protected ArrayList<String> slowModeChannels;
	protected boolean nameFilter;
	protected int slowModeDelay;
	protected int antiSpamDelay;
	protected final int ANTI_SPAM_DEFAULT_DELAY = 5;
	protected final int SLOW_MODE_DEFAULT_DELAY = 5;
	
	public GuildStatus(String guildId) {
		this.guildId = guildId;
		this.lengthFilterChannels = new ArrayList<String>();
		this.spamFilterChannels = new ArrayList<String>();
		this.chatFilterChannels = new ArrayList<String>();
		this.slowModeChannels = new ArrayList<String>();
		this.nameFilter = false;
		this.slowModeDelay = SLOW_MODE_DEFAULT_DELAY;
		this.antiSpamDelay = ANTI_SPAM_DEFAULT_DELAY;
		
	}
	
	public boolean getNameFilter() {
		return nameFilter;
	}

	public String getGuildId() {
		return guildId;
	}
	
	public ArrayList<String> getLengthFilterChannels() {
		return lengthFilterChannels;
	}
	
	public ArrayList<String> getSpamFilterChannels() {
		return spamFilterChannels;
	}
	
	public ArrayList<String> getChatFilterChannels() {
		return chatFilterChannels;
	}
	
	public ArrayList<String> getSlowModeChannels() {
		return slowModeChannels;
	}
	
	public int getSlowModeDelay() {
		return slowModeDelay;
	}
	
	public int getAntiSpamDelay() {
		return antiSpamDelay;
	}

	public GuildStatus setAntiSpamDelay(int antiSpamDelay) {
		this.antiSpamDelay = antiSpamDelay;
		return this;
	}

	public GuildStatus setSlowModeDelay(int delay) {
		this.slowModeDelay = delay;
		return this;
	}
	
	public GuildStatus setNameFilter(boolean nameFilter) {
		this.nameFilter = nameFilter;
		return this;
	}
	
	public GuildStatus slowModeChannel(String channelId) {
		slowModeChannels.add(channelId);
		return this;
	}

	public GuildStatus lengthFilterChannel(String channelId) {
		lengthFilterChannels.add(channelId);
		return this;
	}
	
	public GuildStatus spamFilterChannel(String channelId) {
		spamFilterChannels.add(channelId); 
		return this;
	}
	
	public GuildStatus chatFilterChannel(String channelId) {
		chatFilterChannels.add(channelId); 
		return this;
	}
	
	public GuildStatus removeSlowModeChannel(String channelId) {
		slowModeChannels.remove(channelId);
		return this;
	}

	public GuildStatus removeSpamFilterChannel(String channelId) {		
		spamFilterChannels.remove(channelId);
		return this;
	}
	
	public GuildStatus removeChatFilterChannel(String channelId) {		
		chatFilterChannels.remove(channelId);
		return this;
	}
	
	public GuildStatus removeLengthFilterChannel(String channelId) {		
		lengthFilterChannels.remove(channelId);
		return this;
	}

	public GuildStatus removeAllSlowModeChannels() {
		slowModeChannels.clear();
		return this;
	}
	
	public GuildStatus removeAllSpamFilterChannels() {
	spamFilterChannels.clear();
	return this;
	}
	
	public GuildStatus removeAllChatFilterChannels() {
	chatFilterChannels.clear();
	return this;
	}
	
	public GuildStatus removeAllLengthFilterChannels() {
	lengthFilterChannels.clear();
	return this;
	}
	


}
