package com.bluesun212.sudoku;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Notification extends JPanel implements Runnable {
	private static final long serialVersionUID = -3261929622901115969L;
	private JPanel child;
	private Sudoku parent;
	
	private int alpha = 0;
	private final int MAX = 50;
	private int sign = 1;
	
	public Notification(Sudoku parent) {
		this.parent = parent;
	}
	
	// Start rolling out menu
	public void bind(JPanel child) {
		this.child = child;
		sign = 1;
		new Thread(this).start();
	}
	
	// Start rolling back menu
	public void unbind() {
		sign = -1;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		if ( sign == 1 ) { // Moving towards center of screen
			parent.stopInput();
			add(child);
			child.setLocation(getWidth() / 2 - child.getWidth() / 2, -child.getHeight());
			
			for (int i = 0; i <= MAX; i++) {
				double d = (double)i / MAX;
				alpha = (int) (192 * d);
				
				if ( (d *= 2) < 1 ) {
					d = 0.5 * d * d * d * d;
				} else {
					d = -0.5 * ((d -= 2) * d * d * d - 2);
				}
				
				int diff = getHeight() / 2 + child.getHeight() / 2;
				child.setLocation(child.getX(), (int)(diff * d) - child.getHeight());
				parent.repaint();
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else if ( sign == -1 ) { // Moving to edge of screen
			for (int i = 0; i <= MAX; i++) {
				double d = (double)i / MAX;
				alpha = 192 - (int) (192 * d);
				
				if ( (d *= 2) < 1 ) {
					d = 0.5 * d * d * d * d;
				} else {
					d = -0.5 * ((d -= 2) * d * d * d - 2);
				}
				
				int diff = getWidth() / 2 + child.getWidth() / 2 + 4;
				child.setLocation(getWidth() / 2 - child.getWidth() / 2 - (int)(diff * d), child.getY());
				parent.repaint();
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			remove(child);
			child = null;
			parent.startInput();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		if ( child != null ) {
			g.setColor(new Color(255, 255, 255, alpha));
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
}
