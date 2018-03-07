package commands.identifier;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import eventInfo.MessageInfo;
import messages.EmbedMessage;
import messages.EmbedData;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class GuildIdentifier extends EmbedData {
	static boolean roleEdited = false;
	static boolean memberEdited = false;
	static boolean exists = false;
	static Message roleMessage = null;
	Message originalMsg = null;
	static Message memberMessage = null;
	static int i = 0;
	
	static HashMap<Integer, String> roleList = new HashMap<Integer, String>();
	static HashMap<Integer, String> memberList = new HashMap<Integer, String>();

	public static Message guildInfo() {
		embColor = Color.CYAN;
		if ((roleEdited == true || memberEdited == false) && roleMessage != null) {
			roleEdited = false;
			roleList.clear();
			memberEdited = false;
			memberList.clear();
		} 
		Message message = MessageInfo.message;
		String rawMessageContent = message.getContentRaw();
		Guild guild = MessageInfo.guild;
		String getInfo = commands.CommandList.cmdSign + "server", getInfoServer = getInfo + "info", getInfoRoles = getInfo + "roles",
				getInfoMembers = getInfo + "members";

		if (rawMessageContent.equals(getInfoServer)) {
			title = "Server Info";
			String formattedMessage = "**" + guild.getName() + "**\n" + "**Guild Creation Date**: "
					+ guild.getCreationTime().format(DateTimeFormatter.ISO_DATE) + "\n" + "**Region**: "
					+ guild.getRegion().getName() + "\n**" + "Users**: " + guild.getMembers().size() + "\n"
					+ "**Voice Channels**: " + guild.getVoiceChannels().size() + "\n" + "**Text Channels**: "
					+ guild.getTextChannels().size() + "\n" + "**Roles**: " + guild.getRoles().size() + "\n"
					+ "**Owner**" + guild.getOwner().getNickname() + "\n" + "**Server ID**: " + guild.getId();
			embContent = formattedMessage;
			return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
		}

		if (rawMessageContent.startsWith(getInfoRoles)) {
			if (rawMessageContent.startsWith(getInfoRoles)) {
				title = "Role Lister";
				roleMessage = message;
				i = 0;
				String roles = guild.getRoles().toString().replaceAll("@", "");
				if ((roles.length() > 1980)) {
					// .replaceAll("[", "").replaceAll("]", "")
					StringBuilder listManager = new StringBuilder();
					listManager.append(roles).deleteCharAt(roles.length() - 1).deleteCharAt(0);

					while (listManager.toString().contains("[") || listManager.toString().contains("]")) {
						listManager.deleteCharAt(listManager.indexOf("["));
						listManager.deleteCharAt(listManager.indexOf("]"));
					}

					int count = 1980;
					int count2 = 1980;
					roleList.put(i, listManager.substring(0, count));
					i = i + 1;
					double maxSize = listManager.length();
					double afterCount = maxSize - count;
					double maxSizeRatio = (maxSize - count2) / count2;
					int maxInt = (int) Math.floor(maxSizeRatio);
					double decimal = maxSizeRatio - maxInt;
					int finalAmount = 0;
					if (count < maxSize - count) {
						while (maxInt > 0) {
							maxSizeRatio = maxSizeRatio - 1;
							if (maxSizeRatio < 1) {
								finalAmount = (int) (decimal * count2);
							}

							if (count > afterCount) {
								roleList.put(i, listManager.substring(count, count + finalAmount));
							} else {
								roleList.put(i, listManager.substring(count, count + count2));
							}
							count = count + 1980;
							i = i + 1;
							maxInt = maxInt - 1;
						}
					}

					embContent = roleList.get(0);
					roleEdited = true;
					i = 0;
					return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
				} else {
					embContent = roles;
					roleEdited = true;
					return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
				}
			}

		}

		if (rawMessageContent.startsWith(getInfoMembers)) {
			title = "Member Lister";
			memberMessage = message;
			i = 0;
			String members = guild.getMembers().toString().replaceAll("@", "");
			if ((members.length() > 1980)) {
				// .replaceAll("[", "").replaceAll("]", "")
				StringBuilder listManager = new StringBuilder();
				listManager.append(members).deleteCharAt(members.length() - 1).deleteCharAt(0);

				while (listManager.toString().contains("[") || listManager.toString().contains("]")) {
					listManager.deleteCharAt(listManager.indexOf("["));
					listManager.deleteCharAt(listManager.indexOf("]"));
				}

				int count = 1980;
				int count2 = 1980;
				memberList.put(i, listManager.substring(0, count));
				i = i + 1;
				double maxSize = listManager.length();
				double afterCount = maxSize - count;
				double maxSizeRatio = (maxSize - count2) / count2;
				int maxInt = (int) Math.floor(maxSizeRatio);
				double decimal = maxSizeRatio - maxInt;
				int finalAmount = 0;
				if (count < maxSize - count) {
					while (maxInt > 0) {
						maxSizeRatio = maxSizeRatio - 1;
						if (maxSizeRatio < 1) {
							finalAmount = (int) (decimal * count2);
						}

						if (count > afterCount) {
							memberList.put(i, listManager.substring(count, count + finalAmount));
						} else {
							memberList.put(i, listManager.substring(count, count + count2));
						}
						count = count + 1980;
						i = i + 1;
						maxInt = maxInt - 1;
					}
				}
				embContent = memberList.get(0);
				memberEdited = true;
				i = 0;
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			} else {
				embContent = members;
				memberEdited = true;
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			}
		}
		return null;
	}

	public static String messageToEdit() {
		if (roleEdited == true) {
			return roleMessage.getId();
		}
		if (memberEdited == true) {
			return memberMessage.getId();
		}
		return null;
	}

	public static boolean pageTurnSuccess() {
		if (exists == true) {
			return true;
		}
		return false;

	}

	public static Message pageChange() {
		exists = false;
		if (memberEdited == true) {
			Message message = MessageInfo.message;
			String rawMessageContent = message.getContentRaw();
			if ((rawMessageContent.startsWith("/next"))) {
				if (i < memberList.size() - 1) {
					i = i + 1;
					exists = true;
				}
				String nextPage = memberList.get(i);
				embContent = nextPage;
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			}
			if ((rawMessageContent.startsWith("/back"))) {
				if (i > 0) {
					i = i - 1;
					exists = true;
				}
				String prevPage = memberList.get(i);
				embContent = prevPage;
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			}
		}
		if (roleEdited == true) {
			Message message = MessageInfo.message;
			String rawMessageContent = message.getContentRaw();
			if ((rawMessageContent.startsWith("/next"))) {
				if (i < roleList.size() - 1) {
					i = i + 1;
					exists = true;
				}
				String nextPage = roleList.get(i);
				embContent = nextPage;
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			}
			if ((rawMessageContent.startsWith("/back"))) {
				if (i > 0) {
					i = i - 1;
					exists = true;
				}
				String prevPage = roleList.get(i);
				embContent = prevPage;
				return EmbedMessage.embMsg(title, embContent, imageUrl, embAuthor, embAuthorIconUrl, embColor);
			}
		}
		return null;
	}
}
