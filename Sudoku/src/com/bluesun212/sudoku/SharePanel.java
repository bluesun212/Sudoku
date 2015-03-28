package com.bluesun212.sudoku;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;

public class SharePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 4522617351436302042L;
	private Notification note;

	public SharePanel(Notification n, GameBoard gb) {
		note = n;
		
		setLayout(null);
		setSize(192, 96);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		JButton jbOK = new CustomButton("OK");
		jbOK.setBounds(getWidth() / 2 - 96 / 2, getHeight() - 20 - 8, 96, 20);
		jbOK.addActionListener(this);
		add(jbOK);
		
		JTextField jtf = new JTextField();
		jtf.setBounds(8, 8, getWidth() - 16, 20);
		jtf.setDocument(new FrozenDocument("http://bluesun212.com/sudoku?seed=" + gb.getSeed()));
		add(jtf);
		
		note.bind(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		note.unbind();
	}
	
	private class FrozenDocument extends PlainDocument {
		private static final long serialVersionUID = -7829229022515064459L;

		public FrozenDocument(String text) {
			try {
				this.insertString(0, text, null);
			} catch (BadLocationException e) {}
			
			setDocumentFilter(new DocumentFilter() {
		        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {}
		        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {}
		        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {}
			});
		}
	}
}
