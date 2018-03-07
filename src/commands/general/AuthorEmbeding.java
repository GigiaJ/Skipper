package commands.general;

import static bot.Bot.settings;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class AuthorEmbeding {
	public static Message authorEmbedMessage() {
		settings = settings.debuild().setEmbedMessageStatus(false).build();
		settings = settings.debuild().setAuthorEmbedStatus(!settings.getAuthorEmbedStatus()).build();
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("authoremb", cmdSign, "", CommandType.GENERAL,
				"will toggle embing messages on or off with your icon and name at the top.", "thisCommand")
				.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(AuthorEmbeding.class.getMethod("authorEmbedMessage")).build();
	}
}
