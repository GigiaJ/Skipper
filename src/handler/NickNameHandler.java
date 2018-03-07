package handler;

import commands.management.chatFilter.bannedWordList.BannedWordList;
import commands.management.nameFilter.EnforceNameFilter;
import commands.management.nicknameLock.EnforceNicknameLock;
import eventInfo.GuildMemberNickChangeInfo;

public class NickNameHandler extends GuildMemberNickChangeInfo {

	public static void handler() {
		if (EnforceNameFilter.activeCommand() == true) {
			if (BannedWordList.BWL.contains(messages.StringDecrypter.decrypt(newNick)))
				guild.getController().setNickname(member, previousNick).submit();
		}
		if (EnforceNicknameLock.activeCommand() == true) {
			guild.getController().setNickname(member, EnforceNicknameLock.setNickname()).submit();
		}
	}
}