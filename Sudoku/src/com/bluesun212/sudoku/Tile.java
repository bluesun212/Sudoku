package com.bluesun212.sudoku;

public class Tile {
	private boolean editable = true;
	private boolean hasNumber = false;
	private int number = 0;
	
	// Default constructor
	public Tile() {
		
	}

	// Constructor for computer generated tiles
	public Tile(int number) {
		this.number = number;
		hasNumber = true;
		editable = false;
	}
	
	public boolean isEditable() {
		return ( editable );
	}
	
	public boolean hasNumber() {
		return ( hasNumber );
	}
	
	public int getNumber() {
		return ( number );
	}
	
	public void setNumber(int n) {
		number = n;
		hasNumber = true;
	}
	
	public void resetNumber() {
		hasNumber = false;
	}
}
