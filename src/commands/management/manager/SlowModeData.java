package commands.management.manager;

import java.time.Instant;

public class SlowModeData {
	protected String guildId;
	protected int delay;
	protected int duration;
	protected long start;

	public SlowModeData(String guildId, int delay, int duration) {
		this.guildId = guildId;
		this.delay = delay;
		this.duration = duration;
		this.start = Instant.now().getEpochSecond();
	}

	public SlowModeData(String[] split) {
		if (split.length == 4) {
			this.guildId = split[0];
			this.delay = Integer.valueOf(split[1]);
			this.duration = Integer.valueOf(split[2]);
			this.start = Long.valueOf(split[3]);
		}
		else {
			this.guildId = split[0];
			this.delay = Integer.valueOf(split[1]);
			this.duration = Integer.valueOf(split[2]);
		}
	}

	public String getGuildId() {
		return guildId;
	}

	public int getDelay() {
		return delay;
	}

	public int getDuration() {
		return duration;
	}

	public long getStart() {
		return start;
	}
}
