package com.ten.hanabi.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.ImageIcon;

import com.ten.hanabi.core.Card;
import com.ten.hanabi.core.CardKnowlege;
import com.ten.hanabi.core.Color;
import com.ten.hanabi.ui.play.PlayerPanel;

public class Utils {

	private static final String resourcePackage = "/com/ten/hanabi/ui/img/";

	public static double RESCALE = 1;

	public static Image rescaleBasic(Image srcImg) {
		if(RESCALE == 1)
			return srcImg;
		int w = srcImg.getWidth(null);
		int h = srcImg.getHeight(null);
		return getScaledImage(srcImg, w, h, 0, 0, w, h);
	}

	public static Image getScaledImage(Image srcImg, int w, int h, int fromw, int fromh, int tow, int toh) {
		// Rescaling for small screens
		w = (int) (RESCALE * w);
		h = (int) (RESCALE * h);

		int origW = srcImg.getWidth(null);
		int origH = srcImg.getHeight(null);
		if(origW < 0 || origH < 0)
			throw new RuntimeException("Couldn't get image size");
		if(tow < 0)
			tow = origW;
		if(toh < 0)
			toh = origH;
		double wFactor = ((double) w) / ((double) (tow - fromw));
		double hFactor = ((double) h) / ((double) (toh - fromh));

		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, ((int) (-fromw * wFactor)), ((int) (-fromh * hFactor)), (int) (origW * wFactor),
				(int) (origH * hFactor), null);
		g2.dispose();

		return resizedImg;
	}

	private static HashMap<String, Image> imageDP = new HashMap<>();

	public static Image getImageForFile(String filename) {
		if(!imageDP.containsKey(filename)) {
			ImageIcon ii = new ImageIcon(PlayerPanel.class.getResource(filename));
			imageDP.put(filename, ii.getImage());
		}
		return imageDP.get(filename);

	}

	public static Image getTokenImage(Color c) {
		String name = c == Color.MULTI ? "Multicolor" : c.smallName() + c.name().toLowerCase().substring(1);
		String filename = resourcePackage + "Token" + name + ".png";
		return getImageForFile(filename);
	}

	public static Image getTokenImage(int n) {
		return getImageForFile(resourcePackage + "Token" + n + ".png");
	}

	public static void drawCardKnowlege(Image img, CardKnowlege ck) {
		if(ck == null)
			return;
		int W = img.getWidth(null);
		int H = img.getHeight(null);

		int h = H / 4;
		int w = h;

		Graphics2D g2 = ((BufferedImage) img).createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		Color c = ck.getColorFromClues();
		if(c != null) {
			Image cImg = getTokenImage(c);
			g2.drawImage(cImg, W / 10, (H - h) / 2, h, w, null);
		}

		int n = ck.getNumberFromClues();
		if(n > 0) {
			Image nImg = getTokenImage(n);
			g2.drawImage(nImg, W - W / 10 - w, (H - h) / 2, h, w, null);
		}

		g2.dispose();

		return;
	}

	public static Image getCardImage(Card c) {
		return getCardImage(c, null);
	}

	public static Image getCardImage(Card c, CardKnowlege ck) {
		return getCardImage(c, ck, 98, 150);
	}

	public static Image getCardImage(Card c, CardKnowlege ck, int w, int h) {
		if(c == null)
			return getCardBackImage(ck, w, h);
		Image cardsImage = getImageForFile(resourcePackage + "cards.png");
		int x = 65 * (c.getNumber() - 1);
		int y = 100 * (c.getColor().ordinal());
		Image image = getScaledImage(cardsImage, w, h, x, y, x + 65, y + 100);
		if(ck != null) {
			drawCardKnowlege(image, ck);
		}
		return image;
	}

	public static Image getCardBackImage() {
		return getCardImage(null, null);
	}

	public static Image getCardBackImage(CardKnowlege ck) {
		return getCardImage(null, ck);
	}

	public static Image getCardBackImage(CardKnowlege ck, int w, int h) {
		Image image = getScaledImage(getImageForFile(resourcePackage + "cardBack.png"), w, h, 0, 0, 81, 125);
		if(ck != null) {
			drawCardKnowlege(image, ck);
		}
		return image;
	}

	public static Image getCardSmallImage(Card c) {
		return getCardSmallImage(c, 50, 50);
	}

	public static Image getCardSmallImage(Card c, int w, int h) {
		Image cardsImage = getImageForFile(resourcePackage + "cardsSmall.png");
		int x, y;
		if(c != null) {
			x = 50 * (c.getNumber());
			y = 50 * (c.getColor().ordinal());
		} else {
			x = 0;
			y = 50 * 5;
		}
		return getScaledImage(cardsImage, w, h, x, y, x + 50, y + 50);
	}
}
