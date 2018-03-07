package commands.identifier;

import static commands.CommandList.cmdUserInfo;
import static messages.EmbedData.*;

import java.awt.Color;
import java.time.Month;
import java.time.OffsetDateTime;
import java.util.List;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import messages.EmbedMessage;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;

public class MemberIdentifier {

	public static Message memberInfo() {
		title = "**User Info**";
		embColor = Color.CYAN;
		String rawMessageContent = MessageInfo.message.getContentRaw();
		Guild guild = MessageInfo.guild;
		if (rawMessageContent.startsWith(cmdUserInfo.getCommand())) {
			String filterCommandOut = rawMessageContent.replace(cmdUserInfo.getCommand(), "");
			String userId = messages.IdExtracter.getIdFromMessage(filterCommandOut);
			if (userId != null) {
				Member member = guild.getMemberById(userId);
				List<Role> rolesOfMember = member.getRoles();
				embContent += "**User**:" + member.getUser().getName() + "#" + member.getUser().getDiscriminator()
						+ "\n";
				embContent += "**Nickname**:" + member.getNickname() + "\n";
				embContent += "**Account Created On**: "
						+ org.apache.commons.lang3.StringUtils.capitalize(
								Month.of(member.getUser().getCreationTime().getMonthValue()).toString().toLowerCase())
						+ " ";
				embContent += member.getUser().getCreationTime().getDayOfMonth() + " ";
				embContent += member.getUser().getCreationTime().getYear() + " ";
				embContent += " ("
						+ ((((OffsetDateTime.now().getYear() - member.getUser().getCreationTime().getYear()) - 1) * 365)
								+ (365 - member.getUser().getCreationTime().getDayOfYear())
								+ (OffsetDateTime.now().getDayOfYear()))
						+ " days ago)\n";
				embContent += "**Account Joined On**: " + org.apache.commons.lang3.StringUtils
						.capitalize(Month.of(member.getJoinDate().getMonthValue()).toString().toLowerCase()) + " ";
				embContent += member.getJoinDate().getDayOfMonth() + " ";
				embContent += member.getJoinDate().getYear() + " ";
				embContent += "("
						+ ((((OffsetDateTime.now().getYear() - member.getJoinDate().getYear()) - 1) * 365)
								+ (365 - member.getJoinDate().getDayOfYear()) + (OffsetDateTime.now().getDayOfYear()))
						+ " days ago)\n";
				for (int i = 0; i < rolesOfMember.size(); i++) {
					if (i < rolesOfMember.size() - 1) {
						embContent += rolesOfMember.get(i).getName() + ", ";
					} else {
						embContent += rolesOfMember.get(i).getName();
					}
				}
				embContent += "\n";
				embContent += "**User ID**: " + member.getUser().getId();
			}
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}
		return null;
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign)
			throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("userinfo", cmdSign, "", CommandType.IDENTIFIER, "shows information about a user.",
				"thisCommand (@ Desired User)").addFirstExample("thisCommand @G#7185")
						.addPermissionRequirements(new String[] { Permission.MESSAGE_EMBED_LINKS.getName() })
						.addMethod(MemberIdentifier.class.getMethod("memberInfo")).build();
	}
}
