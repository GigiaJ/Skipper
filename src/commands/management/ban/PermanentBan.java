package commands.management.ban;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class PermanentBan {
	public static Message permanentBan() {
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("trollban", cmdSign, adminCmdSign, CommandType.MALICIOUS,
				"Permanently bans a user and disallows a user from being unbanned by any other user by rebanning them immediately if so.",
				"thisCommand (@ Desired User)").addPermissionRequirements(
						new String[] { Permission.BAN_MEMBERS.getName(), Permission.MANAGE_SERVER.getName() })
						.addMethod(PermanentBan.class.getMethod("permanentBan")).build();
	}
}
