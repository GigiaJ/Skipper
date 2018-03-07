package bot.settings.settingLoaders;

import java.util.ArrayList;
import java.util.Arrays;

import commands.management.manager.MemberStatus;
import commands.management.manager.SlowModeData;

public class LoadUsers extends SettingsLoader {
	protected static void checkForUsers(StringBuilder sb) {	
		if (sb.toString().startsWith(USERS + SPACE + CURLY_BRACKET_START)) {
			ArrayList<MemberStatus> listOfUsers = new ArrayList<MemberStatus>();
			sb.replace(START, sb.indexOf(CURLY_BRACKET_START), EMPTY);
			while (sb.substring(START, sb.indexOf(CURLY_BRACKET_END)).contains(PARENTHESES_START)) {

				sb.replace(START, sb.indexOf(PARENTHESES_START) + 1, EMPTY);
				// Loads the ID of the guild
				MemberStatus userToAdd = new MemberStatus(sb.substring(START, sb.indexOf(COMMA)));
				sb.replace(START, sb.indexOf(COMMA) + 1, EMPTY);
				while (sb.substring(START, sb.indexOf(PARENTHESES_END)).contains(COMMA)) {

					// Loads the SlowModeChannels
					if (!sb.substring(
							(sb.indexOf(MUTED_GUILDS + BRACKET_START) + (MUTED_GUILDS + BRACKET_START).length()),
							sb.indexOf(BRACKET_END)).isEmpty()) {
						userToAdd.getGuildsMutedIn().addAll((Arrays.asList(
								sb.substring(sb.indexOf(BRACKET_START) + 1, sb.indexOf(BRACKET_END)).split(COMMA))));
					}
					sb.replace(START, sb.indexOf(BRACKET_END) + 2, EMPTY); // Clears the previous entry

					// Loads the SlowModeGuilds
					if (!sb.substring(
							(sb.indexOf(SLOWMODE_GUILDS + BRACKET_START) + (SLOWMODE_GUILDS + BRACKET_START).length()),
							sb.indexOf(BRACKET_END)).isEmpty()) {

						for (int i = START; i < sb
								.substring((sb.indexOf(SLOWMODE_GUILDS + BRACKET_START)
										+ (SLOWMODE_GUILDS + BRACKET_START).length()), sb.indexOf(BRACKET_END))
								.split(REGEX).length; i++) {

							userToAdd.getGuildsSlowModeIn()
									.add(new SlowModeData(
											sb.substring(
													(sb.indexOf(SLOWMODE_GUILDS + BRACKET_START)
															+ (SLOWMODE_GUILDS + BRACKET_START).length()),
													sb.indexOf(BRACKET_END)).split(REGEX)[i]
															.substring(1, sb
																	.substring(
																			(sb.indexOf(SLOWMODE_GUILDS + BRACKET_START)
																					+ (SLOWMODE_GUILDS + BRACKET_START)
																							.length()),
																			sb.indexOf(BRACKET_END))
																	.split(REGEX)[i].length() - 1)
															.split(COMMA)));
						}

					}
					sb.replace(START, sb.indexOf(BRACKET_END) + 2, EMPTY); // Clears the previous entry

					// Loads the UserLockGuilds
					if (!sb.substring(
							(sb.indexOf(USERLOCK_GUILDS + BRACKET_START) + (USERLOCK_GUILDS + BRACKET_START).length()),
							sb.indexOf(BRACKET_END)).isEmpty()) {
						userToAdd.getGuildsNickNameLockIn().addAll((Arrays.asList(
								sb.substring(sb.indexOf(BRACKET_START) + 1, sb.indexOf(BRACKET_END)).split(COMMA))));
					}
					sb.replace(START, sb.indexOf(BRACKET_END) + 2, EMPTY); // Clears the previous entry

					// Loads the Nicknames for the previous
					if (!sb.substring((sb.indexOf(NICKNAMES + BRACKET_START) + (NICKNAMES + BRACKET_START).length()),
							sb.indexOf(BRACKET_END)).isEmpty()) {
						userToAdd.getNickNames().addAll((Arrays.asList(
								sb.substring(sb.indexOf(BRACKET_START) + 1, sb.indexOf(BRACKET_END)).split(COMMA))));
					}
					sb.replace(START, sb.indexOf(BRACKET_END) + 1, EMPTY); // Clears the previous entry

					listOfUsers.add(userToAdd);
				}
			}
			settingsToLoad.add(listOfUsers);
		}
	}
}
