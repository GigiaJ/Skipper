package messages.messageEffects;

public class ReverserEffect {
	public static StringBuilder reverseMessageEffect(StringBuilder messageToSend) {
			return new StringBuilder(org.apache.commons.lang3.StringUtils.reverse(messageToSend.toString()));
		}
}
