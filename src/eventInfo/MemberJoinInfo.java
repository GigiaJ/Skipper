package eventInfo;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MemberJoinInfo extends ListenerAdapter {
	
	public static User botUser = null;
	public static Guild guild = null;
	public static Member latestMember = null;
	public static User latestUser = null;
	
	public void onGuildMemberJoin(GuildMemberJoinEvent event) throws NullPointerException {
		guild = event.getGuild();
		latestMember = event.getMember();
		latestUser = event.getUser();
		handler.MemberJoinHandler.handler();
	}
}