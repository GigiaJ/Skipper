package commands.management.manager;

import static bot.Bot.settings;

import java.time.Instant;
import java.util.ArrayList;

import commands.management.slowMode.SlowModeThreading;

public class Statuses {
	public static boolean enforceMute = false;
	public static boolean enforceSlowMode = false;
	public static boolean enforceNameLock = false;
	public static boolean enforceSpamFilter = false;
	public static boolean enforceLengthFilter = false;
	public static boolean enforceChatFilter = false;
	public static boolean enforceNameFilter = false;

	public static ArrayList<MemberStatus> affectedUsers = new ArrayList<MemberStatus>();
	public static ArrayList<GuildStatus> affectedGuilds = new ArrayList<GuildStatus>();

	public static void applySettings() {
		if (settings.getAffectedUsers() != null) {
			affectedUsers = settings.getAffectedUsers();
			updateSlowModes();
			memberStatusHasChanged();
		}
		if (settings.getAffectedGuilds() != null) {
			affectedGuilds = settings.getAffectedGuilds();
			guildStatusHasChanged();
		}
	}

	public static MemberStatus applyExistingMemberStatus(MemberStatus member) {
		boolean userHasStatus = false;
		for (int i = 0; i < affectedUsers.size(); i++) {
			if (affectedUsers.get(i).getUserId().equals(member.getUserId())) {
				member = affectedUsers.get(i);
				userHasStatus = true;
				break;
			}
		}
		if (userHasStatus == false) {
			affectedUsers.add(member);
		}
		return member;
	}

	public static GuildStatus applyExistingGuildStatus(GuildStatus guild) {
		boolean guildHasStatus = false;
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (affectedGuilds.get(i).getGuildId().equals(guild.getGuildId())) {
				guild = affectedGuilds.get(i);
				guildHasStatus = true;
				break;
			}
		}
		if (guildHasStatus == false) {
			affectedGuilds.add(guild);
		}
		return guild;
	}

	public static void guildStatusHasChanged() {
		boolean chatFilter = false;
		boolean spamFilter = false;
		boolean lengthFilter = false;
		boolean slowMode = false;
		boolean nameFilter = false;
		for (int i = 0; i < affectedGuilds.size(); i++) {
			if (!affectedGuilds.get(i).getSlowModeChannels().isEmpty()) {

				slowMode = true;
				enforceSlowMode = true;
			} else {
				if (affectedUsers.isEmpty()) {
					enforceSlowMode = false;
				} else {
					for (int t = 0; t < affectedUsers.size(); t++) {
						if (affectedUsers.get(t).getGuildsSlowModeIn().isEmpty()) {
							enforceSlowMode = false;
						}
					}
				}
			}
			if (!affectedGuilds.get(i).getLengthFilterChannels().isEmpty()) {
				lengthFilter = true;
				enforceLengthFilter = true;
			} else {
				enforceLengthFilter = false;
			}
			if (!affectedGuilds.get(i).getChatFilterChannels().isEmpty()) {
				chatFilter = true;
				enforceChatFilter = true;
			} else {
				enforceChatFilter = false;
			}
			if (!affectedGuilds.get(i).getSpamFilterChannels().isEmpty()) {
				spamFilter = true;
				enforceSpamFilter = true;
			} else {
				enforceSpamFilter = false;
			}
			if (affectedGuilds.get(i).getNameFilter() == false) {
				nameFilter = true;
				enforceNameFilter = false;
			}
			if (spamFilter == true && chatFilter == true && slowMode == true && lengthFilter == true
					&& nameFilter == true) {
				affectedGuilds.remove(i);
				i = 0;
			}
		}
	}

	public static void memberStatusHasChanged() {
		boolean mutes = false;
		boolean slowmodes = false;
		boolean namelocks = false;
		for (int i = 0; i < affectedUsers.size(); i++) {
			if (!affectedUsers.get(i).getGuildsMutedIn().isEmpty()) {
				mutes = true;
				enforceMute = true;
			} else {
				enforceMute = false;
			}
			if (!affectedUsers.get(i).getGuildsNickNameLockIn().isEmpty()) {
				namelocks = true;
				enforceNameLock = true;
			} else {
				enforceNameLock = false;
			}
			if (!affectedUsers.get(i).getGuildsSlowModeIn().isEmpty()) {
				slowmodes = true;
				enforceSlowMode = true;
			} else {
				if (affectedGuilds.isEmpty()) {
					enforceSlowMode = false;
				} else {
					for (int t = 0; t < affectedGuilds.size(); t++) {
						if (!affectedGuilds.get(t).getSlowModeChannels().isEmpty()) {
							enforceSlowMode = true;
							break;
						}
					}
				}
			}

			if (slowmodes == false && namelocks == false && mutes == false) {
				affectedUsers.remove(i);
				i = 0;
			}
		}
	}

	private static void updateSlowModes() {
		for (int i = 0; i < affectedUsers.size(); i++) {
			for (int t = 0; t < affectedUsers.get(i).getGuildsSlowModeIn().size(); t++) {
				long checkDuration = (affectedUsers.get(i).getGuildsSlowModeIn().get(t).getStart()
						+ affectedUsers.get(i).getGuildsSlowModeIn().get(t).getDuration());
				if (Instant.now().getEpochSecond() < checkDuration) {
					MemberStatus memberToSlowMode = affectedUsers.get(i);
					affectedUsers.remove(i);
					String memberGuildId = memberToSlowMode.getGuildsSlowModeIn().get(t).getGuildId();
					int memberDelay = memberToSlowMode.getGuildsSlowModeIn().get(t).getDelay();
					int timeLeft = (int) ((memberToSlowMode.getGuildsSlowModeIn().get(t).getDuration())
							- (Instant.now().getEpochSecond()
									- memberToSlowMode.getGuildsSlowModeIn().get(t).getStart()));
					memberToSlowMode = memberToSlowMode.removeSlowMode(memberToSlowMode.getGuildsSlowModeIn().get(t));
					memberToSlowMode = memberToSlowMode.slowMode(memberGuildId, memberDelay, timeLeft);
					affectedUsers.add(memberToSlowMode);
					SlowModeThreading slowModeThread = new SlowModeThreading(memberToSlowMode,
							new SlowModeData(memberGuildId, memberDelay, timeLeft));
					Thread thread = new Thread(slowModeThread);
					thread.start();
				} else {
					if (!(affectedUsers.get(i).getGuildsSlowModeIn().get(t).getDuration() < 0))
						affectedUsers.get(i).getGuildsSlowModeIn().remove(t);
					t = 0;
				}
			}
		}
	}
}
