package com.bluesun212.sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NewPanel extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 4522617351436302042L;
	private Notification note;
	private GameBoard board;
	private JSlider slider;
	private JButton jbStart;
	private JButton jbCancel;
	
	public NewPanel(Notification n, GameBoard gb, boolean hasCancel) {
		note = n;
		board = gb;
		
		setLayout(null);
		setSize(192, 96);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		jbCancel = new CustomButton("Cancel");
		jbCancel.setBounds(getWidth() / 2 + 2, getHeight() - 20 - 8, 80, 20);
		jbCancel.addActionListener(this);
		
		int offset = 40;
		if ( hasCancel ) {
			add(jbCancel);
			offset = 82;
		}
		
		jbStart = new CustomButton("Start!");
		jbStart.setBounds(getWidth() / 2 - offset, getHeight() - 20 - 8, 80, 20);
		jbStart.addActionListener(this);
		add(jbStart);
		
		slider = new JSlider(JSlider.HORIZONTAL);
		slider.setMaximum(100);
		slider.setValue(0);
		slider.setBounds(8, 24, getWidth() - 16, 20);
		slider.addChangeListener(this);
		add(slider);
		
		Hashtable<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
		map.put(TextAttribute.TRACKING, 0.2);
		setFont(getFont().deriveFont(map).deriveFont(Font.BOLD, 14f));
		
		note.bind(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if ( arg0.getSource().equals(jbStart) ) {
			board.generateWithDifficulty((int)((float)slider.getValue() / 10));
		}
		
		note.unbind();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g2) {
		super.paintComponent(g2);
		
		String text = "Difficulty: ";
		if ( slider.getValue() == 100 ) {
			text += "Death";
		} else if ( slider.getValue() >= 70 ) {
			text += "Hard";
		} else if ( slider.getValue() >= 35 ) {
			text += "Medium";
		} else {
			text += "Easy";
		}
		
		int g = Math.min((int) (((float) (70 - slider.getValue()) / 35) * 255), 255);
		int r = Math.min((int) (((float) slider.getValue() / 35) * 255), 255);
		int a = 255;
		if ( slider.getValue() >= 70 ) {
			r = (int) (((float) (100 - slider.getValue()) / 30) * 255);
			a = r;
			g = 0;
		}
		
		// Draw text
		g2.setColor(new Color(0, 0, 0, a));
		g2.setFont(getFont());
		Rectangle2D r2d = g2.getFontMetrics().getStringBounds(text, g2);
		g2.drawString(text, getWidth() / 2 - (int)r2d.getCenterX() + 1, 20);
		g2.drawString(text, getWidth() / 2 - (int)r2d.getCenterX() - 1, 20);
		g2.drawString(text, getWidth() / 2 - (int)r2d.getCenterX(), 21);
		g2.drawString(text, getWidth() / 2 - (int)r2d.getCenterX(), 19);
		
		g2.setColor(new Color(r, g, 0));
		g2.drawString(text, getWidth() / 2 - (int)r2d.getCenterX(), 20);
	}
}
