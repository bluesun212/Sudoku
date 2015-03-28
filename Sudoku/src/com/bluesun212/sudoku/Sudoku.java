package com.bluesun212.sudoku;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.SpringLayout;

public class Sudoku extends JApplet implements ActionListener {
	private static final long serialVersionUID = 2378049184086681452L;
	private GameBoard board;
	private Notification note;
	private JButton resetGame;
	private JButton newGame;
	private JButton shareGame;
	
	@Override
	public void start() {
		// Create the layout for the content pane
		SpringLayout layout = new SpringLayout();
		getContentPane().setLayout(layout);
		
		// Create components
		board = new GameBoard(this);
		note = new Notification(this);		
		resetGame = new CustomButton("Reset Game");
		newGame = new CustomButton("New Game");
		shareGame = new CustomButton("Share Game");
		
		// Set component sizes and listeners
		board.setPreferredSize(new Dimension(324, 324));
		note.setPreferredSize(new Dimension(getWidth(), getHeight() - 1));
		resetGame.addActionListener(this);
		newGame.addActionListener(this);
		shareGame.addActionListener(this);
		
		// Add components to content pane
		getContentPane().add(board);
		getContentPane().add(resetGame);
		getContentPane().add(newGame);
		getContentPane().add(shareGame);
		getContentPane().add(note);
		getContentPane().setComponentZOrder(note, 0);
		
		// Set layout contstraints for each component
		layout.putConstraint(SpringLayout.NORTH, board, 16, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.WEST, board, 16, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, resetGame, 8, SpringLayout.SOUTH, board);
		layout.putConstraint(SpringLayout.NORTH, newGame, 8, SpringLayout.SOUTH, board);
		layout.putConstraint(SpringLayout.NORTH, shareGame, 8, SpringLayout.SOUTH, board);
		layout.putConstraint(SpringLayout.SOUTH, resetGame, -16, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, newGame, -16, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, shareGame, -16, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.WEST, resetGame, 0, SpringLayout.WEST, board);
		layout.putConstraint(SpringLayout.WEST, newGame, 8, SpringLayout.EAST, resetGame);
		layout.putConstraint(SpringLayout.WEST, shareGame, 8, SpringLayout.EAST, newGame);
		layout.putConstraint(SpringLayout.EAST, shareGame, 0, SpringLayout.EAST, board);
		
		// Start game
		String str = getParameter("seed");
		if ( str == null ) {
			new NewPanel(note, board, false);
		} else {
			try {
				int seed = Integer.parseInt(str);
				board.generateWithSeed(seed);
			} catch ( NumberFormatException e ) {
				new NewPanel(note, board, false);
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paint(g);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if ( arg0.getSource().equals(shareGame) ) {
			new SharePanel(note, board);
		} else if ( arg0.getSource().equals(resetGame) ) {
			new ResetPanel(note, board);
		} else if ( arg0.getSource().equals(newGame) ) {
			new NewPanel(note, board, true);
		} 
	}
	
	
	// Methods called by Notification
	public void stopInput() {
		resetGame.setEnabled(false);
		newGame.setEnabled(false);
		shareGame.setEnabled(false);
		board.setInputEnabled(false);
	}
	
	public void startInput() {
		resetGame.setEnabled(true);
		newGame.setEnabled(true);
		shareGame.setEnabled(true);
		board.setInputEnabled(true);
	}
}
