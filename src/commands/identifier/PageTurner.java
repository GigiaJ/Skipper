package commands.identifier;

import eventInfo.MessageInfo;
import messages.EmbedMessage;
import net.dv8tion.jda.core.entities.Message;
import static messages.EmbedData.*;

import java.awt.Color;

import static commands.CommandList.cmdNext;
import static commands.CommandList.cmdBack;

public class PageTurner {
	static int i = 1;
	static boolean exists = false;

	public static Message pageChange() {
		embColor = Color.CYAN;
		exists = false;
		if (MembersIdentifier.memberEdited == true) {
			Message message = MessageInfo.message;
			String rawMessageContent = message.getContentRaw();
			if (rawMessageContent.startsWith(cmdNext.getCommand())) {
				if (i < MembersIdentifier.memberList.size() - 1) {
					i = i + 1;
					exists = true;
				}
				String nextPage = MembersIdentifier.memberList.get(i);
				embContent = nextPage;
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			}
			if (rawMessageContent.startsWith(cmdBack.getCommand())) {
				if (i > 0) {
					i = i - 1;
					exists = true;
				}
				String previousPage = MembersIdentifier.memberList.get(i);
				embContent = previousPage;
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			}
		}
		if (RolesIdentifier.roleEdited == true) {
			Message message = MessageInfo.message;
			String rawMessageContent = message.getContentRaw();
			if (rawMessageContent.startsWith(cmdNext.getCommand())) {
				if (i < RolesIdentifier.roleList.size() - 1) {
					i = i + 1;
					exists = true;
				}
				String nextPage = RolesIdentifier.roleList.get(i);
				embContent = nextPage;
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			}
			if (rawMessageContent.startsWith(cmdBack.getCommand())) {
				if (i > 0) {
					i = i - 1;
					exists = true;
				}
				String previousPage = RolesIdentifier.roleList.get(i);
				embContent = previousPage;
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			}
		}
		return null;
	}
	
	public static String messageToEdit() {
		if (RolesIdentifier.roleEdited == true) {
			return RolesIdentifier.roleMessage.getId();
		}
		if (MembersIdentifier.memberEdited == true) {
			return MembersIdentifier.memberMessage.getId();
		}
		return null;
	}
}
