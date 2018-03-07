package eventInfo;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildMemberNickChangeInfo extends ListenerAdapter {
	public static Guild guild = null;
	public static String newNick = null;
	public static Member member = null;
	public static String previousNick = null;

	public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) throws NullPointerException {
		try {
			guild = event.getGuild();
			newNick = event.getNewNick();
			member = event.getMember();
			previousNick = event.getPrevNick();
			handler.NickNameHandler.handler();
		} catch (NullPointerException ignore) {
		}
	}
}
