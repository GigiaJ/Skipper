package commands.identifier;

import static commands.CommandList.cmdServerInfo;
import static messages.EmbedData.*;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class ServerIdentifier {
	public static Message serverInfo() {
		embColor = Color.CYAN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		if (rawMessageContent.startsWith(cmdServerInfo.getCommand())) {
			title = "Server Info";
			embContent += "**Name**:" + guild.getName() + "\n";
			embContent += "**Guild Creation Date**: " + guild.getCreationTime().format(DateTimeFormatter.ISO_DATE)
					+ "\n";
			embContent += "**Region**: " + guild.getRegion().getName() + "\n";
			embContent += "**Users**: " + guild.getMembers().size() + "\n";
			embContent += "**Voice Channels**: " + guild.getVoiceChannels().size() + "\n";
			embContent += "**Text Channels**: " + guild.getTextChannels().size() + "\n";
			embContent += "**Roles**: " + guild.getRoles().size() + "\n";
			embContent += "**Owner**" + guild.getOwner().getNickname() + "\n";
			embContent += "**Server ID**: " + guild.getId();
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}
	
	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("serverinfo", cmdSign, "", CommandType.IDENTIFIER,
				"shows information about the server.", "thisCommand")
				.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
				.addMethod(ServerIdentifier.class.getMethod("serverInfo")).build();
	}
}
