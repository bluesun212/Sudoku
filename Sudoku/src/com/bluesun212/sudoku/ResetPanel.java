package com.bluesun212.sudoku;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class ResetPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 4522617351436302042L;
	private Notification note;
	private GameBoard board;
	private JButton jbYes;
	private JButton jbNo;

	public ResetPanel(Notification n, GameBoard gb) {
		note = n;
		board = gb;
		
		setLayout(null);
		setSize(192, 96);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		jbYes = new CustomButton("Yes");
		jbYes.setBounds(getWidth() / 2 - 80 - 4, getHeight() - 20 - 8, 80, 20);
		jbYes.addActionListener(this);
		add(jbYes);
		
		jbNo = new CustomButton("No");
		jbNo.setBounds(getWidth() / 2 + 4, getHeight() - 20 - 8, 80, 20);
		jbNo.addActionListener(this);
		add(jbNo);
		
		JLabel label = new JLabel("Start over?");
		label.setBounds(8, 8, getWidth() - 16, 12);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label);
		
		note.bind(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		note.unbind();
		if ( arg0.getSource().equals(jbYes) ) {
			board.generateWithSeed(board.getSeed());
		}
	}
}
