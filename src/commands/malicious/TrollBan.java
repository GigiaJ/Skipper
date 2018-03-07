package commands.malicious;

import static commands.CommandList.cmdTrollBan;
import static messages.EmbedData.*;

import java.util.List;
import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import handler.MemberJoinHandler;
import messages.EmbedMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.Message;

public class TrollBan {
	public static Message trollBan() {
		Guild guild = MessageInfo.guild;
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();

		if (rawMessageContent.startsWith(cmdTrollBan.getCommand())) {
			String filterCommandOut = rawMessageContent.replaceAll(cmdTrollBan.getCommand() + " ", "");
			String userId = messages.IdExtracter.getIdFromMessage(filterCommandOut);
			if (userId != null) {
				List<Invite> listOfInvites = guild.getInvites().complete();
				String invite = "";
				if (!listOfInvites.isEmpty())
					invite = listOfInvites.get(0).getURL();
				guild.getMemberById(userId).getRoles();
				if (!invite.isEmpty()) {
					TrollBanUserData data = new TrollBanUserData(userId, guild.getMemberById(userId).getRoles(),
							guild.getMemberById(userId).getNickname());
					MemberJoinHandler.addToListOfUserToRestore(data);
					guild.getMemberById(userId).getUser().openPrivateChannel().complete().sendMessage(invite)
							.complete();
					guild.getController().kick(userId).complete();
					embContent = "User has been banned.";
				} else {
					embContent = "No invites currently exist, please create one and try again.";
				}
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("tban", cmdSign, adminCmdSign, CommandType.MALICIOUS,
				"sends a user an invite and then kicks them from the server and when they rejoins it restores their nickname and roles. (Requires an invite to already exist on the list of invites.)",
				"thisCommand (@ Desired User)").addPermissionRequirements(
						new String[] { Permission.MESSAGE_EMBED_LINKS.getName(), Permission.NICKNAME_MANAGE.getName() })
						.addMethod(TrollBan.class.getMethod("trollBan")).build();

	}
}
