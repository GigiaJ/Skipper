package commands.management.ban;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class TemporaryBan {
	public static Message temporaryBan() {
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("tempban", cmdSign, adminCmdSign, CommandType.MANAGER,
				"Temporarily bans a user and disallows a user from being unbanned by any other user for a set amount of time and will reban them immediately if another unbans them.",
				"thisCommand (@ Desired User) (duration)").addPermissionRequirements(
						new String[] { Permission.BAN_MEMBERS.getName(), Permission.MANAGE_SERVER.getName() })
						.addMethod(TemporaryBan.class.getMethod("temporaryBan")).build();
	}
}
