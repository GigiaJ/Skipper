package eventInfo;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MemberLeaveInfo extends ListenerAdapter {
	
	public static User botUser = null;
	public static Guild guild = null;
	public static Member latestMember = null;
	public static User latestUser = null;
	
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) throws NullPointerException {
		guild = event.getGuild();
		latestMember = event.getMember();
		latestUser = event.getUser();
	}
}