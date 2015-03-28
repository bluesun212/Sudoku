package com.bluesun212.sudoku;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class BoardGen {
	private static Random rng = new Random();
	public static void setRandom(Random r) {
		rng = r;
	}
	
	@SuppressWarnings("unchecked")
	public static int[][] generateBoard() {
		int[] board = new int[81];
		Arrays.fill(board, -1);
		
		LinkedList<Integer[]> guesses = deduce(board);
		LinkedList<Object[]> remembered = new LinkedList<Object[]>();
		remembered.add(new Object[]{guesses, 0, board});
		while ( !remembered.isEmpty() ) {
			Object[] objs = remembered.removeLast();
			guesses = (LinkedList<Integer[]>) objs[0];
			int c = (Integer) objs[1];
			board = (int[]) objs[2];
			if ( c >= guesses.size() ) {
				continue;
			}
			
			remembered.add(new Object[]{guesses, c + 1, copy(board)});
			Integer[] ints = guesses.get(c);
			board[ints[0]] = ints[1];
			guesses = deduce(board);
			if ( guesses == null ) {
				break;
			}
			
			remembered.add(new Object[]{guesses, 0, copy(board)});
		}
		
		int[][] ret = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				ret[i][j] = board[getPositionFor(i, j, 0)] + 1;
			}
		}
		
		return ( ret );
	}
	
	@SuppressWarnings("unchecked")
	private static LinkedList<Integer[]> deduce(int[] board) {
		while ( true ) {
			boolean stuck = true;
			LinkedList<Integer[]> guess = null;
			int count = 0;
			Object[] objs = figureBits(board);
			int[] allowed = (int[]) objs[0];
			LinkedList<Integer> needed = (LinkedList<Integer>) objs[1];
			for (int pos = 0; pos < 81; pos++) {
				if ( board[pos] == -1 ) {
					LinkedList<Integer> bits = listBits(allowed[pos]);
					if ( bits.isEmpty() ) {
						return ( new LinkedList<Integer[]>() );
					} else if ( bits.size() == 1 ) {
						board[pos] = bits.get(0);
						stuck = false;
					} else if ( stuck ) {
						LinkedList<Integer[]> t = new LinkedList<Integer[]>();
						for (int n : bits ) {
							t.add(new Integer[]{pos, n});
						}
						
						Object[] objs2 = pickBetter(guess, count, t);
						guess = (LinkedList<Integer[]>) objs2[0];
						count = (Integer) objs2[1];
					}
				}
			}
			
			if ( !stuck ) {
				objs = figureBits(board);
				allowed = (int[]) objs[0];
				needed = (LinkedList<Integer>) objs[1];
			}
			
			for (int axis = 0; axis < 3; axis++) {
				for (int x = 0; x < 9; x++) {
					LinkedList<Integer> bits = listBits(needed.get(axis * 9 + x));
					for ( int n : bits ) {
						int bit = 1 << n;
						LinkedList<Integer> spots = new LinkedList<Integer>();
						for (int y = 0; y < 9; y++) {
							int pos = getPositionFor(x, y, axis);
							if ( (allowed[pos] & bit) != 0 ) {
								spots.add(pos);
							}
						}
						
						if ( spots.isEmpty() ) {
							return ( new LinkedList<Integer[]>() );
						} else if ( spots.size() == 1 ) {
							board[spots.get(0)] = n;
							stuck = false;
						} else if ( stuck ) {
							LinkedList<Integer[]> t = new LinkedList<Integer[]>();
							for ( int pos : spots ) {
								t.add(new Integer[]{pos, n});
							}
							
							Object[] objs2 = pickBetter(guess, count, t);
							guess = (LinkedList<Integer[]>) objs2[0];
							count = (Integer) objs2[1];
						}
					}
				}
			}
			
			if ( stuck ) {
				if ( guess != null ) {
					guess = shuffle(guess);
				}
				return ( guess );
			}
		}
	}

	private static Object[] figureBits(int[] board) {
		int[] allowed = new int[board.length];
		for (int i = 0; i < board.length; i++) {
			if ( board[i] == -1 ) {
				allowed[i] = 511;
			}
		}
		
		LinkedList<Integer> needed = new LinkedList<Integer>();
		for (int axis = 0; axis < 3; axis++) {
			for (int x = 0; x < 9; x++) {
				int bits = 0;
				for (int y = 0; y < 9; y++) {
					int e = board[getPositionFor(x, y, axis)];
					if ( e > -1 ) {
						bits |= 1 << e;
					}
				}
				
				bits = 511 ^ bits;
				needed.add(bits);
				for (int y = 0; y < 9; y++) {
					allowed[getPositionFor(x, y, axis)] &= bits;
				}
			}
		}
		
		return ( new Object[]{allowed, needed} );
	}
	
	private static Object[] pickBetter(LinkedList<Integer[]> b, int c, LinkedList<Integer[]> t) {
		if ( b == null || t.size() < b.size() ) {
			return ( new Object[]{t, 1} );
		}
		
		if ( t.size() > b.size() ) {
			return ( new Object[]{b, c} );
		}
		
		if ( rng.nextInt(c) == 0 ) {
			return ( new Object[]{t, c + 1} );
		} else {
			return ( new Object[]{b, c + 1} );
		}
	}
	
	private static LinkedList<Integer> listBits(int num) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < 9; i++) {
			if ( (num & (1 << i)) != 0 ) {
				list.add(new Integer(i));
			}
		}
		
		return ( list );
	}
	
	private static final int[] X_AXIS = new int[] {0,3,6,27,30,33,54,57,60};
	private static final int[] Y_AXIS = new int[] {0,1,2,9,10,11,18,19,20};
	private static int getPositionFor(int x, int y, int axis) {
		if ( axis == 0 ) {
			return ( x * 9 + y );
		} else if ( axis == 1 ) {
			return ( y * 9 + x );
		} else {
			return ( X_AXIS[x] + Y_AXIS[y] );
		}
	}
	
	// Utility methods
	private static LinkedList<Integer[]> shuffle(LinkedList<Integer[]> guess) {
		LinkedList<Integer[]> t = new LinkedList<Integer[]>();
		
		while ( !guess.isEmpty() ) {
			t.add(guess.remove(rng.nextInt(guess.size())));
		}
		
		return ( t );
	}
	
	private static int[] copy(int[] x) {
		int[] y = new int[x.length];
		for (int i = 0; i < x.length; i++) {
			y[i] = x[i];
		}
		
		return ( y );
	}
}
