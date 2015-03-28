package com.bluesun212.sudoku;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

public class CustomButton extends JButton {
	private static final long serialVersionUID = -7580432937773655979L;

	public CustomButton(String string) {
		super(string);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// Draw gradient
		for (int y = 1; y < getHeight(); y++) {
			int c = (int)(((float) y / getHeight()) * 55) + 200;
			g.setColor(new Color(c, c, c));
			g.drawLine(0, y, getWidth(), y);
		}
		
		// Draw text
		g.setColor(Color.black);
		g.setFont(getFont());
		Rectangle2D r2d = g.getFontMetrics().getStringBounds(getText(), g);
		g.drawString(getText(), getWidth() / 2 - (int)r2d.getCenterX(), getHeight() / 2 - (int)r2d.getCenterY());
	}
}
