package commands.color.colorchat;

import static main.Main.settings;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class ColorChat {
	public static Message colorChat() {
		settings = settings.debuild().setColorChatStatus(!settings.getColorChatStatus()).build();
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("colorchat", cmdSign, "", CommandType.COLOR, "toggles color chat.",
				"thisCommand").addPermissionRequirements(new String[] { Permission.MESSAGE_ATTACH_FILES.getName() }).addMethod(ColorChat.class.getMethod("colorChat"))
				.build();
	}
}
