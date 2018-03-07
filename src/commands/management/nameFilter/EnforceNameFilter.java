package commands.management.nameFilter;

import commands.management.manager.Statuses;

public class EnforceNameFilter extends Statuses {
	public static boolean activeCommand() {
		if (enforceNameFilter == true) {
			return true;
		}
		return false;
	}
}
