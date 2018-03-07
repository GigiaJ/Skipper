package commands.identifier;

import static commands.CommandList.cmdServerMembers;
import static messages.EmbedData.*;

import java.awt.Color;
import java.util.ArrayList;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import commands.general.pageChange.Pagenator;
import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class MembersIdentifier {
	public static ArrayList<String> memberList = new ArrayList<String>();
	static boolean memberEdited = false;
	static Message memberMessage = null;
	private static final int CHARACTER_LIMIT = 1980;

	public static Message serverMembers() {
		Pagenator.lastCommand();
		memberList.clear();
		PageTurner.i = 1;
		title = "Member Lister";
		embColor = Color.CYAN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		if (rawMessageContent.startsWith(cmdServerMembers.getCommand())) {
			memberMessage = message;
			StringBuilder listBuilder = new StringBuilder();
			String members = guild.getMembers().toString().replaceAll("@", "");
			listBuilder.append(members);
			listBuilder = new StringBuilder(listBuilder.substring(1, listBuilder.length() - 1));

			if (listBuilder.length() > CHARACTER_LIMIT) {
				for (int i = 0; i < (listBuilder.length() / CHARACTER_LIMIT) + 1; i++) {
					if (CHARACTER_LIMIT * (i + 1) < listBuilder.length()) {
						memberList.add(listBuilder.substring((CHARACTER_LIMIT * i), (CHARACTER_LIMIT * (i + 1))));
					} else {
						memberList.add(listBuilder.substring((CHARACTER_LIMIT * i), (listBuilder.length())));
					}
				}
			} else {
				memberList.add(listBuilder.toString());
			}

			embContent = memberList.get(0);
			memberEdited = true;
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("servermembers", cmdSign, "", CommandType.IDENTIFIER,
				"shows all of the members on the server.", "thisCommand")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(MembersIdentifier.class.getMethod("serverMembers")).build();
	}
}
