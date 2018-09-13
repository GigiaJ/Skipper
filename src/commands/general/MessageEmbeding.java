package commands.general;

import static main.Main.settings;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class MessageEmbeding {
	public static Message embedMessage() {
		settings = settings.debuild().setAuthorEmbedStatus(false).build();
		settings = settings.debuild().setEmbedMessageStatus(!settings.getEmbedMessageStatus()).build();
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("embmsg", cmdSign, "", CommandType.GENERAL, "will toggle embing messages on or off.",
				"thisCommand").addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(MessageEmbeding.class.getMethod("embedMessage")).build();
	}
}
