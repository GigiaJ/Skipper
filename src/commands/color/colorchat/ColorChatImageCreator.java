package commands.color.colorchat;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import handler.CommandHandler;

public class ColorChatImageCreator {
	private static File file = null;
	private static boolean boldItalics = false;
	private static boolean italics = false;
	private static boolean bold = false;
	private static boolean strikethrough = false;
	private static boolean underline = false;
	private static int fontSize = 0;
	private final static int IMAGE_SIZE = 400;

	public static void textToImage(String text, String font, Integer size) throws Exception {
		fontSize = size;
		file = main.Main.imageFile;
		File.createTempFile("Text", "png");
		file.deleteOnExit();
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		Font setFont = new Font(text, Font.PLAIN, size);
		g2d.setFont(setFont);
		FontMetrics fm = g2d.getFontMetrics();
		int height = fm.getHeight();
		List<String> lines = wrap(text, fm, IMAGE_SIZE);

		g2d.dispose();
		int i = 0;
		height = (lines.size() * height);
		int f = lines.size();
		int l = 1;

		int heightSet = height / f - (fm.getAscent() + fm.getDescent() + fm.getLeading() - size);
		img = new BufferedImage(IMAGE_SIZE, height, BufferedImage.TYPE_INT_ARGB);
		while (!(lines.isEmpty())) {
			int linePos = (l * heightSet);
			g2d = img.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

			String unFormattedString = lines.get(i).toString();
			String formatString = unFormattedString.replaceAll("~~", "").replaceAll("__", "").replace("*", "");

			StringBuilder newLine = new StringBuilder();
			StringBuilder escapedLine = new StringBuilder();

			newLine.append(unFormattedString);

			HashMap<Integer, Integer> markingsStart = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> markingsEnd = new HashMap<Integer, Integer>();

			int d = 0;

			// Bold Italicized Count
			int bICount = 0;
			// Italicized count
			int iCount = 0;
			// Bold Count
			int bCount = 0;
			// Strikethrough count
			int sCount = 0;
			// Underlined count
			int uCount = 0;

			int beginLine = 0;
			int endLine = 0;

			int o = 1;
			int lastEnd = 0;
			boolean found = false;

			Pattern checkFor = null;
			Matcher next = null;

			o = 1;
			lastEnd = 0;
			found = false;
			beginLine = 0;
			endLine = 0;

			checkFor = Pattern.compile("\\*\\*\\*(.+?)\\*\\*\\*");
			String replace = newLine.toString().replaceAll("__", "").replace("~~", "");
			next = checkFor.matcher(replace);

			if (next.find() == true) {
				if (escapedLine.toString().isEmpty()) {
					escapedLine.append(formatString);
				}

				next.reset();
				while (next.find()) {
					if (found == true) {
						endLine = escapedLine.indexOf(next.group(o), lastEnd) + next.group(o).length();
						beginLine = escapedLine.indexOf(next.group(o), lastEnd);
						lastEnd = endLine;

					} else {
						beginLine = escapedLine.indexOf(next.group(o), endLine);
						endLine = escapedLine.indexOf(next.group(o)) + next.group(o).length();
						lastEnd = endLine;
						found = true;
					}

					markingsStart.put(d, beginLine);
					markingsEnd.put(d, endLine);
					d = d + 1;
					bICount = d;
				}
				boldItalics = true;
			}

			checkFor = Pattern.compile("\\*\\*(.+?)\\*\\*");
			replace = newLine.toString().replaceAll("__", "").replace("~~", "").replaceAll("\\*\\*\\*", "");
			next = checkFor.matcher(replace);

			if (next.find() == true) {
				if (escapedLine.toString().isEmpty()) {
					escapedLine.append(formatString);
				}

				next.reset();
				while (next.find()) {
					if (found == true) {
						endLine = escapedLine.indexOf(next.group(o), lastEnd) + next.group(o).length();
						beginLine = escapedLine.indexOf(next.group(o), lastEnd);
						lastEnd = endLine;

					} else {
						beginLine = escapedLine.indexOf(next.group(o), endLine);
						endLine = escapedLine.indexOf(next.group(o)) + next.group(o).length();
						lastEnd = endLine;
						found = true;
					}

					markingsStart.put(d, beginLine);
					markingsEnd.put(d, endLine);
					d = d + 1;
					bCount = d;
				}
				bold = true;
			}

			checkFor = Pattern.compile("\\*(.+?)\\*");
			replace = newLine.toString().replace("__", "").replace("~~", "").replaceAll("\\*\\*\\*", "")
					.replaceAll("\\*\\*", "");
			next = checkFor.matcher(replace);

			if (next.find() == true) {
				if (escapedLine.toString().isEmpty()) {
					escapedLine.append(formatString);
				}

				next.reset();
				while (next.find()) {
					if (found == true) {
						endLine = escapedLine.indexOf(next.group(o), lastEnd) + next.group(o).length();
						beginLine = escapedLine.indexOf(next.group(o), lastEnd);
						lastEnd = endLine;

					} else {
						beginLine = escapedLine.indexOf(next.group(o), endLine);
						endLine = escapedLine.indexOf(next.group(o)) + next.group(o).length();
						lastEnd = endLine;
						found = true;
					}

					markingsStart.put(d, beginLine);
					markingsEnd.put(d, endLine);
					d = d + 1;
					iCount = d;
				}
				italics = true;
			}

			o = 1;
			lastEnd = 0;
			found = false;
			beginLine = 0;
			endLine = 0;

			checkFor = Pattern.compile("~~(.+?)~~");
			replace = newLine.toString().replaceAll("__", "").replaceAll("\\*", "");
			next = checkFor.matcher(replace);

			if (next.find() == true) {
				if (escapedLine.toString().isEmpty()) {
					escapedLine.append(formatString);
				}

				next.reset();
				while (next.find()) {
					if (found == true) {
						endLine = escapedLine.indexOf(next.group(o), lastEnd) + next.group(o).length();
						beginLine = escapedLine.indexOf(next.group(o), lastEnd);
						lastEnd = endLine;

					} else {
						beginLine = escapedLine.indexOf(next.group(o), endLine);
						endLine = escapedLine.indexOf(next.group(o)) + next.group(o).length();
						lastEnd = endLine;
						found = true;
					}

					markingsStart.put(d, beginLine);
					markingsEnd.put(d, endLine);
					d = d + 1;
					sCount = d;
				}
				strikethrough = true;
			}

			o = 1;
			lastEnd = 0;
			found = false;
			beginLine = 0;
			endLine = 0;
			replace = newLine.toString().replaceAll("~~", "").replaceAll("\\*", "");

			checkFor = Pattern.compile("__(.+?)__");
			next = checkFor.matcher(replace);

			if (next.find() == true) {
				if (escapedLine.toString().isEmpty()) {
					escapedLine.append(formatString);
				}
				next.reset();
				while (next.find()) {
					if (found == true) {
						endLine = escapedLine.indexOf(next.group(o), lastEnd) + next.group(o).length();
						beginLine = escapedLine.indexOf(next.group(o), lastEnd);
						lastEnd = endLine;

					} else {
						beginLine = escapedLine.indexOf(next.group(o), endLine);
						endLine = escapedLine.indexOf(next.group(o)) + next.group(o).length();
						lastEnd = endLine;
						found = true;
					}

					markingsStart.put(d, beginLine);
					markingsEnd.put(d, endLine);
					d = d + 1;
					uCount = d;

				}
				underline = true;
			}

			// font size relative to emoji image size
			Pattern emojiPos = Pattern.compile(":(.+?):");
			int q = 0;
			int w = 0;

			int count = 0;

			String emojiCheck = "";

			if (escapedLine.toString().isEmpty()) {
				emojiCheck = newLine.toString();
			} else {
				emojiCheck = escapedLine.toString();
			}
			HashMap<Integer, Integer> emojiReplace = new HashMap<Integer, Integer>();
			ArrayList<String> urlList = new ArrayList<String>();
			HashMap<Integer, Integer> emojiLength = new HashMap<Integer, Integer>();
			Matcher match = emojiPos.matcher(emojiCheck);
			if (match.find()) {
				EmojiFinder.emojiUrl();
			}
			match.reset();
			while (match.find()) {
				emojiLength.put(w, fm.stringWidth(match.group(0)));
				// This is to create a sizable gap for the emoji
				emojiCheck = emojiCheck.replaceFirst(match.group(0), "!#&");
				urlList.add(EmojiFinder.emojiUrl.get(q));
				if (l == 1) {
					emojiReplace.put(w, (fm.stringWidth(emojiCheck.substring(0, emojiCheck.indexOf("!#&")))));
					emojiCheck = emojiCheck.replaceFirst("!#&", "    ");
				} else if (l > 1) {
					emojiReplace.put(w, (fm.stringWidth(emojiCheck.substring(0, emojiCheck.indexOf("!#&")))
							/ match.group().length()));
					emojiCheck = emojiCheck.replaceFirst("!#&", "    ");
				}
				w = w + 1;
			}

			String finalString = emojiCheck.toString().replaceAll("!#&", "    ");

			AttributedString messageAS = new AttributedString(finalString);
			messageAS.addAttribute(TextAttribute.SIZE, size);
			messageAS.addAttribute(TextAttribute.FOREGROUND, CommandHandler.chatColor);
			messageAS.addAttribute(TextAttribute.FAMILY, font);
			if (boldItalics == true) {
				while (count != bICount) {
					messageAS.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE,
							markingsStart.get(count), markingsEnd.get(count));
					messageAS.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, markingsStart.get(count),
							markingsEnd.get(count));
					count = count + 1;
				}
			}

			if (italics == true) {
				while (count != iCount) {
					messageAS.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE,
							markingsStart.get(count), markingsEnd.get(count));
					count = count + 1;
				}
			}

			if (bold == true) {
				while (count != bCount) {
					messageAS.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, markingsStart.get(count),
							markingsEnd.get(count));
					count = count + 1;
				}
			}

			if (strikethrough == true) {
				while (count != sCount) {
					messageAS.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON,
							markingsStart.get(count), markingsEnd.get(count));
					count = count + 1;
				}
			}

			if (underline == true) {
				while (count != uCount) {
					messageAS.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON,
							markingsStart.get(count), markingsEnd.get(count));
					count = count + 1;
				}
			}

			boldItalics = false;
			italics = false;
			bold = false;
			strikethrough = false;
			underline = false;

			fm = g2d.getFontMetrics();
			AttributedCharacterIterator messageIterator = messageAS.getIterator();
			g2d.drawString(messageIterator, 0, linePos);
			g2d.dispose();
			g2d = img.createGraphics();
			int m = 0;
			while (m < emojiReplace.size()) {
				String urlStr = urlList.get(0);
				URL url = new URL(urlStr);

				int imageSize = (fm.getHeight() + size) / (size) + size;

				HttpURLConnection pageRequest = (HttpURLConnection) url.openConnection();

				pageRequest.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
				pageRequest.connect();

				BufferedImage emojiImage = ImageIO.read(pageRequest.getInputStream());

				BufferedImage newImage = new BufferedImage(emojiImage.getWidth(), emojiImage.getHeight(),
						BufferedImage.TYPE_INT_ARGB);
				int emojiWidth = (imageSize - (((emojiImage.getHeight() / emojiImage.getWidth()))));
				int emojiHeight = size;
				Graphics2D g = newImage.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
						RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
				g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				g.drawImage(emojiImage, 0, 0, emojiWidth, emojiHeight, null);

				if (l == 1) {
					g2d.drawImage(newImage, emojiReplace.get(m), linePos - imageSize + 2, null);
				}

				else if (l > 1) {
					g2d.drawImage(newImage, emojiReplace.get(m) * size, linePos - imageSize + 2, null);
				}
				g.dispose();
				m = m + 1;
				pageRequest.disconnect();
			}
			g2d.dispose();

			lines.remove(i);
			f = f - 1;
			l = l + 1;

		}
		try {
			ImageIO.write(img, "png", file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static List<String> wrap(String str, FontMetrics fm, int maxWidth) {
		List<String> lines = splitIntoLines(str);
		if (lines.size() == 0)
			return lines;

		ArrayList<String> strings = new ArrayList<String>();
		for (Iterator<String> iter = lines.iterator(); iter.hasNext();)
			wrapLineInto((String) iter.next(), strings, fm, maxWidth);
		return strings;
	}

	/**
	 * Given a line of text and font metrics information, wrap the line and add the
	 * new line(s) to <var>list</var>.
	 * 
	 * @param line
	 *            a line of text
	 * @param list
	 *            an output list of strings
	 * @param fm
	 *            font metrics
	 * @param maxWidth
	 *            maximum width of the line(s)
	 */
	public static void wrapLineInto(String line, List<String> list, FontMetrics fm, int maxWidth) {
		int len = line.length();
		int width;
		while (len > 0 && (width = fm.stringWidth(line)) > maxWidth) {
			// Guess where to split the line. Look for the next space before
			// or after the guess.

			int guess = (len * maxWidth / width + 1) - (fm.getAscent() + fm.getDescent() + fm.getLeading() - fontSize);
			String before = line.substring(0, guess);

			width = fm.stringWidth(before);
			int pos;
			if (width > maxWidth) {// Too long
				pos = findBreakBefore(line, guess);
			} else { // Too short or possibly just right
				pos = findBreakAfter(line, guess);
				if (pos != -1) { // Make sure this doesn't make us too long
					before = line.substring(0, pos);
					if (fm.stringWidth(before) > maxWidth)
						pos = findBreakBefore(line, guess);
				}
			}
			if (pos == -1) {
				pos = guess; // Split in the middle of the word
			}
			list.add(line.substring(0, pos).trim());
			line = line.substring(pos).trim();
			len = line.length();
		}
		if (len > 0) {
			list.add(line);
		}
	}

	/**
	 * Returns the index of the first whitespace character or '-' in <var>line</var>
	 * that is at or before <var>start</var>. Returns -1 if no such character is
	 * found.
	 * 
	 * @param line
	 *            a string
	 * @param start
	 *            where to star looking
	 */
	public static int findBreakBefore(String line, int start) {
		for (int i = start; i >= 0; --i) {
			char c = line.charAt(i);
			if (Character.isWhitespace(c) || c == '-')
				return i;
		}
		return -1;
	}

	/**
	 * Returns the index of the first whitespace character or '-' in <var>line</var>
	 * that is at or after <var>start</var>. Returns -1 if no such character is
	 * found.
	 * 
	 * @param line
	 *            a string
	 * @param start
	 *            where to star looking
	 */
	public static int findBreakAfter(String line, int start) {
		int len = line.length();
		for (int i = start; i < len; ++i) {
			char c = line.charAt(i);
			if (Character.isWhitespace(c) || c == '-')
				return i;
		}
		return -1;
	}

	/**
	 * Returns an array of strings, one for each line in the string. Lines end with
	 * any of cr, lf, or cr lf. A line ending at the end of the string will not
	 * output a further, empty string.
	 * <p>
	 * This code assumes <var>str</var> is not <code>null</code>.
	 * 
	 * @param str
	 *            the string to split
	 * @return a non-empty list of strings
	 */
	public static List<String> splitIntoLines(String str) {
		ArrayList<String> strings = new ArrayList<String>();

		int len = str.length();
		if (len == 0) {
			strings.add("");
			return strings;
		}

		int lineStart = 0;

		for (int i = 0; i < len; ++i) {

			char c = str.charAt(i);
			if (c == '\r') {
				int newlineLength = 1;
				if ((i + 1) < len && str.charAt(i + 1) == '\n')
					newlineLength = 2;
				strings.add(str.substring(lineStart, i));
				lineStart = i + newlineLength;
				if (newlineLength == 2) // skip \n next time through loop
					++i;
			} else if (c == '\n') {
				strings.add(str.substring(lineStart, i));
				lineStart = i + 1;
			}
		}
		if (lineStart < len)
			strings.add(str.substring(lineStart));

		return strings;
	}
}