package commands.management.manager;

import java.util.ArrayList;

public class MemberStatus {
	protected String userId;
	protected ArrayList<String> guildsMutedIn;
	protected ArrayList<String> guildsNicknameLockedIn;
	protected ArrayList<String> correspondingNicks;
	protected ArrayList<SlowModeData> guildsSlowModeIn;

	public MemberStatus(String userId) {
		this.userId = userId;
		this.guildsMutedIn = new ArrayList<String>();
		this.guildsNicknameLockedIn = new ArrayList<String>();
		this.correspondingNicks = new ArrayList<String>();
		this.guildsSlowModeIn = new ArrayList<SlowModeData>();
	}

	public String getUserId() {
		return userId;
	}

	public MemberStatus mute(String guildId) {
		guildsMutedIn.add(guildId);
		return this;
	}

	public MemberStatus nicknameLock(String guildId, String nickname) {
		guildsNicknameLockedIn.add(guildId);
		correspondingNicks.add(nickname);
		return this;
	}

	public MemberStatus slowMode(String guildId, int delay, int duration) {
		guildsSlowModeIn.add(new SlowModeData(guildId, delay, duration));
		return this;
	}

	public MemberStatus unmute(String guildId) {
		guildsMutedIn.remove(guildId);
		return this;
	}

	public MemberStatus removeNicknameLock(String guildId) {
		correspondingNicks.remove(guildsNicknameLockedIn.indexOf(guildId));
		guildsNicknameLockedIn.remove(guildId);
		return this;
	}

	public MemberStatus removeSlowMode(SlowModeData data) {
		if (Statuses.affectedUsers.size() == 0) {
			guildsSlowModeIn.clear();
		}
		else {
			guildsSlowModeIn.remove(data);
		}
		return this;

	}

	public ArrayList<String> getGuildsMutedIn() {
		return guildsMutedIn;
	}

	public ArrayList<String> getGuildsNickNameLockIn() {
		return guildsNicknameLockedIn;
	}

	public ArrayList<String> getNickNames() {
		return correspondingNicks;
	}
	
	public ArrayList<SlowModeData> getGuildsSlowModeIn() {
		return guildsSlowModeIn;
	}
	
} 
