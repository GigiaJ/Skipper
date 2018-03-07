package messages;

public class IdExtracter {
	public static String getIdFromMessage(String input) {
		try {
			if (!(input.contains("<") && input.contains(">"))) {
				EmbedData.embContent = "Invalid user please try again.";
				input = null;
			} else {
				input = input.substring(input.indexOf("<"), input.indexOf(">") + 1).replaceAll("@", "")
						.replaceAll("<", "").replaceAll(">", "").replaceAll("!", "");
			}
		} catch (StringIndexOutOfBoundsException e) {
			EmbedData.embContent = "Invalid user please try again.";
			input = null;
		}
		return input;
	}

	public static String removeIdFromMessage(String input) {
		return input.replace(input.substring(input.indexOf("<"), input.indexOf(">") + 1), "");
	}
}
