package messages;

import java.awt.Color;

public class EmbedData {
	public static String title = null;
	public static String imageUrl = null;
	public static String embContent = null;
	public static String embAuthor = null;
	public static String embAuthorIconUrl = null;
	public static Color embColor = null;

	public static void resetEmbedData() {
		title = null;
		imageUrl = null;
		embContent = "";
		embAuthor = null;
		embAuthorIconUrl = null;
		embColor = null;
	}
}
