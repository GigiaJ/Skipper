package handler;

import java.util.ArrayList;

import commands.malicious.TrollBanUserData;
import eventInfo.MemberJoinInfo;


public class MemberJoinHandler extends MemberJoinInfo {
	public static ArrayList<TrollBanUserData> removedUsers = new ArrayList<TrollBanUserData>();
	public static void handler() {
		restoreUserSettings();
	}

	public static void restoreUserSettings() {
		for (int i = 0; i < removedUsers.size(); i++) {
			if (latestMember.getUser().getId().equals(removedUsers.get(i).getUserId())) {
				guild.getController().addRolesToMember(latestMember, removedUsers.get(i).getRoles()).submit();
				guild.getController().setNickname(latestMember, removedUsers.get(i).getNickname()).submit();
			}
		}
	}
	
	public static void addToListOfUserToRestore(TrollBanUserData data) {
		removedUsers.add(data);
	}
}
