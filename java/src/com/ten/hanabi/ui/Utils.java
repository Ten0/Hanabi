package com.ten.hanabi.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.ten.hanabi.core.Card;
import com.ten.hanabi.ui.play.PlayerPanel;

public class Utils {

	public static Image getScaledImage(Image srcImg, int w, int h, int fromw, int fromh, int tow, int toh){
		int origW = srcImg.getWidth(null);
		int origH = srcImg.getHeight(null);
		if(origW < 0 || origH <0) throw new RuntimeException("Couldn't get image size");
		if(tow < 0) tow = origW;
		if(toh < 0) toh = origH;
		double wFactor = ((double)w)/((double)(tow-fromw));
		double hFactor = ((double)h)/((double)(toh-fromh));
		
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, ((int)(-fromw*wFactor)), ((int)(-fromh*hFactor)), (int)(origW*wFactor), (int)(origH*hFactor), null);
		g2.dispose();

		return resizedImg;
	}
	
	public static Image getCardImage(Card c) { return getCardImage(c, 85, 130); }
	public static Image getCardImage(Card c, int w, int h) {
		if(c == null) return getCardBackImage(w, h);
		ImageIcon ii = new ImageIcon(PlayerPanel.class.getResource("/com/ten/hanabi/ui/img/cards.png"));
		int x = 65 * (c.getNumber()-1);
		int y = 100 * (c.getColor().ordinal());
		return getScaledImage(ii.getImage(), w, h, x, y, x+65, y+100);
	}
	
	public static Image getCardBackImage() { return getCardBackImage(85, 130); }
	public static Image getCardBackImage(int w, int h) {
		ImageIcon ii = new ImageIcon(PlayerPanel.class.getResource("/com/ten/hanabi/ui/img/cardBack.png"));
		return getScaledImage(ii.getImage(), w, h, 0, 0, 81, 125);
	}
}
