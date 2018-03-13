package eventInfo;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdatePermissionsEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ChannelPermissionInfo extends ListenerAdapter {
	protected static List<Member> membersWithChangedPermissions = new ArrayList<Member>();
	protected static Channel channel = null;
	protected static Guild guild = null;

	public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event) throws NullPointerException {
		try {
			membersWithChangedPermissions = event.getMembersWithPermissionChanges();
			guild = event.getGuild();
			channel = event.getChannel();
			handler.PermissionHandler.handler();
		} catch (

		NullPointerException ignore) {
			// ignore is a message that has been deleted and the library being used has no
			// method of acting upon the deleted message in order to handle this exception
			// Any attempt to handle this would constantly be invoked upon message send or
			// receive
		}
	}
}
