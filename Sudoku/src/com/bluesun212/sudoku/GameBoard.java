package com.bluesun212.sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.swing.JComponent;


public class GameBoard extends JComponent implements MouseListener, MouseMotionListener, KeyListener {
	private static final long serialVersionUID = -6921485017421829567L;
	
	private boolean input = true;
	private boolean done = false;
	private Font arial = new Font("arial", Font.PLAIN, 24);
	private Font arialBold = new Font("arial", Font.BOLD, 24);
	private boolean selectVisible = false;
	private boolean tileVisible = false;
	private int selectX = 0;
	private int selectY = 0;
	private int tileX = 0;
	private int tileY = 0;
	
	private Tile[][] tiles = new Tile[9][9];
	private Random r = new Random();
	private int seed = 0;
	
	public GameBoard(Sudoku parent) {
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		clear();
	}
	
	public void setInputEnabled(boolean b) {
		input = b && !done;
	}
	
	public int getSeed() {
		return ( seed );
	}
	
	public void clear() {
		done = false;
		selectVisible = false;
		
		// Fill sudoku board with tiles
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				tiles[i][j] = new Tile();
			}
		}
	}
	
	public void generateWithDifficulty(int difficulty) {
		difficulty = 20 - difficulty * 2;
		Random seedGen = new Random();
		while (true) {
			int seed = seedGen.nextInt();
			r.setSeed(seed);
			
			if ( r.nextInt(21) == difficulty ) {
				generateWithSeed(seed);
				break;
			}
		}
	}
	
	public void generateWithSeed(int seed) {
		this.seed = seed;
		r.setSeed(seed);
		int num = r.nextInt(21) + 17;
		
		BoardGen.setRandom(r); 
		int[][] solution = BoardGen.generateBoard();
		clear();
		
		// Generate tiles
		for (int i = 0; i < num; i++) {
			int x = 0;
			int y = 0;
			do {
				x = r.nextInt(9);
				y = r.nextInt(9);
			} while ( !tiles[x][y].isEditable() );
			
			tiles[x][y] = new Tile(solution[x][y]);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// Draw white background
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		
		if ( tileVisible ) { // Draw the mouse selector
			g.setColor(Color.red);
			g.drawRect(tileX * 36 + 1, tileY * 36 + 1, 34, 34);
		}
		
		// Draw the light gray lines in the back
		g.setColor(Color.lightGray);
		for (int x = 0; x < getWidth(); x += 36) {
			g.drawLine(x, 0, x, getHeight());
			g.drawLine(0, x, getWidth(), x);
		}
		
		// Draw the selected box
		if ( selectVisible ) {
			g.fillRect(selectX * 36 + 1, selectY * 36 + 1, 36, 36);
		}
		
		// Draw the black lines in the back
		g.setColor(done ? Color.green : Color.black);
		for (int x = 0; x < getWidth(); x += 108) {
			g.drawLine(x, 0, x, getHeight());
			g.drawLine(0, x, getWidth(), x);
		}
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // Draw the black rectangle around
		
		// Draw the text
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				Tile t = tiles[x][y];
				if ( t.hasNumber() ) {
					if ( t.isEditable() ) {
						g.setColor(Color.gray);
						g.setFont(arial);
					} else {
						g.setColor(Color.black);
						g.setFont(arialBold);
					}
					
					String s = "" + t.getNumber();
					int w = g.getFontMetrics().stringWidth(s);
					int h = g.getFontMetrics().getAscent();
					g.drawString(s, x * 36 - w / 2 + 18, y * 36 + h / 2 + 18);
				}
			}
		}
	}
	
	private void moveSelect(int x, int y) {
		int i = selectX;
		int j = selectY;
		boolean found = true;
		
		do {
			i += x;
			j += y;
			
			if ( i < 0 || j < 0 || i > 8 || j > 8 ) {
				found = false;
				break;
			}
		} while ( !tiles[i][j].isEditable() );
		
		if ( found ) {
			selectX = i;
			selectY = j;
		}
	}
	
	// Input methods
	@Override
	public void keyPressed(KeyEvent arg0) {
		if ( !input ) {
			return;
		}
		
		if ( selectVisible ) {
			int num = arg0.getKeyCode();
			if ( num == KeyEvent.VK_LEFT ) {
				moveSelect(-1, 0);
			} else if ( num == KeyEvent.VK_RIGHT ) {
				moveSelect(1, 0);
			} else if ( num == KeyEvent.VK_UP ) {
				moveSelect(0, -1);
			} else if ( num == KeyEvent.VK_DOWN ) {
				moveSelect(0, 1);
			} else if ( tiles[selectX][selectY].isEditable() ) {
				if ( num > 48 && num < 58 ) {
					tiles[selectX][selectY].setNumber(num - 48);
				} else if ( num == KeyEvent.VK_DELETE || num == KeyEvent.VK_BACK_SPACE ) {
					tiles[selectX][selectY].resetNumber();
				}
			}
			
			repaint();
		}
		
		if ( arg0.getKeyCode() == KeyEvent.VK_SPACE ) {
			// Check to see if all tiles are filled
			int result = 2;
			
			for (int i = 0; i < 81; i++) {
				if ( !tiles[i % 9][i / 9].hasNumber() ) {
					result = 0;
				}
			}
			
			// Check to see if the board is completed
			if ( result != 0 ) {
				for (int i = 0; i < 9; i++) {
					// Horizontal groups
					int h = 0;
					for (int j = 0; j < 9; j++) {
						h |= 1 << tiles[i][j].getNumber() - 1;
					}
					
					if ( h != 511 ) {
						result = 1;
						break;
					}
					
					// Vertical groups
					int v = 0;
					for (int j = 0; j < 9; j++) {
						v |= 1 << tiles[j][i].getNumber() - 1;
					}
					
					if ( v != 511 ) {
						result = 1;
						break;
					}
					
					// Diagonals
					int d = 0;
					for (int j = 0; j < 9; j++) {
						d |= 1 << tiles[(i % 3) * 3 + j % 3][(i / 3) * 3 + j / 3].getNumber() - 1;
					}
					
					if ( d != 511 ) {
						System.out.println("c");
						result = 1;
						break;
					}
				}
			}
			
			if ( result == 2 ) {
				done = true;
				selectVisible = false;
			}
		} 
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		if ( !input || done ) {
			return;
		}
		
		if ( selectVisible && selectX == tileX && selectY == tileY ) { // Clicking already-selected tile
			selectVisible = false;
		} else if ( tiles[tileX][tileY].isEditable() ) { // Clicked new tile
			selectVisible = true;
			selectX = tileX;
			selectY = tileY;
		}
		
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		mouseMoved(arg0);
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// Move the cursor around
		if ( !input ) {
			return;
		}
		
		tileVisible = true;
		tileX = arg0.getX() / 36;
		tileY = arg0.getY() / 36;
		repaint();
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		// Mouse moved off the sudoku box
		if ( !input ) {
			return;
		}
		
		tileVisible = false;
		repaint();
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Mouse moved into the sudoku box
		if ( !input ) {
			return;
		}
		
		tileVisible = true;
		repaint();
	}
	
	// Methods that I don't care about
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}
