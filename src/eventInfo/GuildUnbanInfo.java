package eventInfo;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildUnbanInfo extends ListenerAdapter {
	public static User unbannedUser = null;
	public void onGuildUnban (GuildUnbanEvent e){ 
		unbannedUser = e.getUser();  
		handler.UnbanHandler.handler();
	}
}
