package commands.identifier;

import static commands.CommandList.cmdServerRoles;
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

public class RolesIdentifier {

	public static ArrayList<String> roleList = new ArrayList<String>();
	static boolean roleEdited = false;
	static Message roleMessage = null;
	private static final int CHARACTER_LIMIT = 1980;
	public static Message serverRoles() {
		Pagenator.lastCommand();
		roleList.clear();
		PageTurner.i = 1;
		title = "Role Lister";
		embColor = Color.CYAN;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		if (rawMessageContent.startsWith(cmdServerRoles.getCommand())) {
			roleMessage = message;
			String roles = guild.getRoles().toString().replaceAll("@", "");
			StringBuilder listBuilder = new StringBuilder();
			listBuilder.append(roles);
			listBuilder = new StringBuilder(listBuilder.substring(1, listBuilder.length() - 1));

			if (listBuilder.length() > CHARACTER_LIMIT) {
				for (int i = 0; i < (listBuilder.length() / CHARACTER_LIMIT) + 1; i++) {
					if (CHARACTER_LIMIT * (i + 1) < listBuilder.length()) {
						roleList.add(listBuilder.substring((CHARACTER_LIMIT * i), (CHARACTER_LIMIT * (i + 1))));
					} else {
						roleList.add(listBuilder.substring((CHARACTER_LIMIT * i), (listBuilder.length())));
					}
				}
			} 
			else {
				roleList.add(listBuilder.toString());
			}
			embContent = roleList.get(0);
			roleEdited = true;
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("serverroles", cmdSign, "", CommandType.IDENTIFIER,
				"shows all of the roles on the server.", "thisCommand")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(RolesIdentifier.class.getMethod("serverRoles")).build();
	}
}
