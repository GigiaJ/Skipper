package commands.general;

import java.awt.Color;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import messages.EmbedData;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class SelfBotLink extends EmbedData {

	public static Message botLink() {
		title = "**Bot Link**";
		embAuthorIconUrl = MessageInfo.jda.getGuildById("301054293778104320").getIconUrl();
		embColor = Color.GREEN;
		embContent = "https://discord.gg/zb7GghU";
		return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("link", cmdSign, "", CommandType.GENERAL,
				"provides a link to allow others to download the bot.", "thisCommand")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(SelfBotLink.class.getMethod("botLink")).build();
	}
}
