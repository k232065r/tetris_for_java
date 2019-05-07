package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Dimension;

public class Step01 extends Applet {
	public void paint(Graphics g) {
		Dimension size = getSize();
		int width = (int)size.width;
		int height = (int)size.height;
		for(int w = 0; w < width; w +=10) {
			g.drawLine(w, 0, w, height);
		}
		for(int h = 0; h < height; h +=10) {
			g.drawLine(0, h, width, h);
		}
	}
}
