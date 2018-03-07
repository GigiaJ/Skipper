package handler;

import static commands.management.manager.Statuses.affectedUsers;

import eventInfo.ChannelPermissionInfo;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

public class PermissionHandler extends ChannelPermissionInfo {

	public static void handler() {
		try {
			Member memberPermissionToRevert = null;
			for (int i = 0; i < membersWithChangedPermissions.size(); i++) {
				memberPermissionToRevert = membersWithChangedPermissions.get(i);
			}
			for (int i = 0; i < affectedUsers.size(); i++) {
				for (int t = 0; t < affectedUsers.get(i).getGuildsSlowModeIn().size(); t++) {
					if (affectedUsers.get(i).getGuildsSlowModeIn().get(t).getGuildId().equals(guild.getId()))
						channel.getPermissionOverride(memberPermissionToRevert).getManager()
								.deny(Permission.MESSAGE_WRITE).submit();
				}
				for (int t = 0; t < affectedUsers.get(i).getGuildsMutedIn().size(); t++) {
					if (affectedUsers.get(i).getGuildsMutedIn().get(t).equals(guild.getId()))
						channel.getPermissionOverride(memberPermissionToRevert).getManager()
								.deny(Permission.MESSAGE_WRITE).submit();
				}
			}
		} catch (NullPointerException ignore) {
			// ignore is a message that has been deleting and the library being used has no
			// method of acting upon the deleted message in order to handle this exception
			// Any attempt to handle this would constantly be invoked upon message send or
			// receive
		}
	}
}
