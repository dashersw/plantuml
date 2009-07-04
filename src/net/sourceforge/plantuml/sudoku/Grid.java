package net.sourceforge.plantuml.sudoku;

import java.util.Random;

// Class Grid manages the creation of puzzles
public class Grid {

	// Difficulty levels:
	public static final int LEVEL_VERY_EASY = 1;
	public static final int LEVEL_EASY = 2;
	public static final int LEVEL_NORMAL = 3;
	public static final int LEVEL_HARD = 4;
	public static final int LEVEL_VERY_HARD = 5;

	private static final boolean OPTION_SYMMETRICAL = false;
	private int difficulty;

	// The Master Grid
	private int[][] grid;

	// Size of a box (measured in squares) and the puzzle (meas. in boxes).
	private int n;
	private int nSquare; // n*n

	private boolean output = true;

	// Given numbers:
	private int[][] given;
	private int countGiven;

	// Available numbers in each box, row and column, used for contructing a
	// valid grid (in function 'create')
	private boolean[][] boxes;
	private boolean[][] rows;
	private boolean[][] cols;

	// Help figure out in which box (1..9) coordinates (i,j) are:
	private int[][] rc2box = { { 0, 0, 0, 1, 1, 1, 2, 2, 2 }, { 0, 0, 0, 1, 1, 1, 2, 2, 2 },
			{ 0, 0, 0, 1, 1, 1, 2, 2, 2 }, { 3, 3, 3, 4, 4, 4, 5, 5, 5 }, { 3, 3, 3, 4, 4, 4, 5, 5, 5 },
			{ 3, 3, 3, 4, 4, 4, 5, 5, 5 }, { 6, 6, 6, 7, 7, 7, 8, 8, 8 }, { 6, 6, 6, 7, 7, 7, 8, 8, 8 },
			{ 6, 6, 6, 7, 7, 7, 8, 8, 8 } };

	public int getGiven(int x, int y) {
		return given[x][y];
	}
	
	private final Random rnd;


	Grid(Random rnd) {
		this.rnd = rnd;
		this.n = 3;
		this.nSquare = n * n;
		this.grid = new int[nSquare][nSquare];
		this.given = new int[nSquare][nSquare];
		this.boxes = new boolean[nSquare][nSquare];
		this.rows = new boolean[nSquare][nSquare];
		this.cols = new boolean[nSquare][nSquare];
	}

	public void createSudoku() {
		// Start the timer
		final long t = System.currentTimeMillis();
		this.difficulty = 0;

		// Fill a grid with numbers:
		this.cleanGrid();
		this.create(this.grid, this.boxes, this.rows, this.cols, 0);

		// Keep track of the best grid we've found:
		final int[][] best = new int[nSquare][nSquare];
		int bestDifficulty = 0;
		int bestGiven = nSquare * nSquare + 1;
		int bestTry = 0;

		int tries = 0;
		// Generate 100 puzzles and pick the most difficult one
		while (tries < 100) {
			countGiven = 0;
			difficulty = 0;
			// Clean the given numbers, but keep the grid intact
			for (int i = 0; i < nSquare; i++) {
				for (int j = 0; j < nSquare; j++) {
					this.given[i][j] = 0;
				}
			}

			// Select 17 random numbers to show
			// 17 is an absolute minimum. Any less and the puzzle is garanteed
			// to be unsolvable.
			this.randomGiven(17);
			difficulty = this.solvable();

			// Keep adding numbers until the puzzle is solvable
			while (difficulty == 0) {
				this.addRandomGiven();
				difficulty = this.solvable();
			}
			if (countGiven <= bestGiven) {
				// If this puzzle is better then anything we've found so far,
				// store it
				bestGiven = countGiven;
				bestDifficulty = difficulty;
				bestTry = tries;
				for (int i = 0; i < nSquare; i++) {
					for (int j = 0; j < nSquare; j++) {
						best[i][j] = this.given[i][j];
					}
				}
				if (bestGiven < 35 && bestDifficulty > 960)
					break; // Good enough. Stop searching.
			}
			tries++;
		}

		// Restore the best grid:
		countGiven = bestGiven;
		difficulty = bestDifficulty;
		if (this.output) {
			System.out.println("Puzzle " + bestTry + " is best");
		}
		for (int i = 0; i < nSquare; i++) {
			for (int j = 0; j < nSquare; j++) {
				this.given[i][j] = best[i][j];
			}
		}

		if (this.output) {
			final long t2 = System.currentTimeMillis();
			System.out.println("Puzzle created in " + (t2 - t) + " ms,\n" + "with " + difficulty
					+ " difficulty points\n" + "and " + countGiven + " given numbers.");
		}
	}

	// Fill the grid with numbers, with regards to the rules of the game:
	public boolean create(int[][] g, boolean[][] b, boolean[][] r, boolean[][] c, int level) {
		boolean validFound = false;
		boolean emptySquare = false;

		// Make sure the grid is realllllyyy random!
		final int[] kList = permutateList();

		// For each row i...
		for (int i = 0; i < nSquare; i++) {
			// ... and each column j...
			for (int j = 0; j < nSquare; j++) {
				if (g[i][j] == 0) {
					emptySquare = true;
					validFound = false;

					// ... and for each value 1-9...
					for (int k = 0; k < nSquare; k++) {
						final boolean bl = b[this.rc2box[i][j]][kList[k]];
						final boolean rl = r[i][kList[k]];
						final boolean cl = c[j][kList[k]];
						// ...if k is a valid value for grid[i,j]...
						if (bl && rl && cl) {
							// ...fill it in...
							b[this.rc2box[i][j]][kList[k]] = false;
							r[i][kList[k]] = false;
							c[j][kList[k]] = false;
							g[i][j] = kList[k] + 1;

							// ...and try to fill the rest of the grid,
							// recursively
							if (create(g, b, r, c, level + 1)) {
								return true;
							}

							g[i][j] = 0;
							b[this.rc2box[i][j]][kList[k]] = bl;
							r[i][kList[k]] = rl;
							c[j][kList[k]] = cl;
						}
					}
					if (validFound == false) {
						// Puzzle is invalid. Backtrack and try again with
						// different numbers.
						return false;
					}
				}
			}
		}

		if (emptySquare == false) {
			// We're done!
			this.grid = g;
			return true;
		}
		return false;
	}

	// randomGiven(x) means: exactly x numbers are shown.
	// The lower this number, the more difficult the puzzle.
	// At least 17 numbers need to be shown for any puzzle to be solvable.
	private void randomGiven(int showHowMany) {
		// Erase all given numbers, of previous tries.
		for (int i = 0; i < this.nSquare; i++) {
			for (int j = 0; j < this.nSquare; j++) {
				this.given[i][j] = 0;
			}
		}
		while (countGiven < showHowMany) {
			this.addRandomGiven();
		}
	}


	private void addRandomGiven() {
		int i = rnd.nextInt(this.nSquare);
		int j = rnd.nextInt(this.nSquare);
		while (this.given[i][j] != 0) {
			i = rnd.nextInt(this.nSquare);
			j = rnd.nextInt(this.nSquare);
		}

		this.given[i][j] = this.grid[i][j];
		countGiven++;

		if (OPTION_SYMMETRICAL) {
			this.given[j][nSquare - i - 1] = this.grid[j][nSquare - i - 1];
			this.given[nSquare - i - 1][nSquare - j - 1] = this.grid[nSquare - i - 1][nSquare - j - 1];
			this.given[nSquare - j - 1][i] = this.grid[nSquare - j - 1][i];
			this.given[j][i] = this.grid[j][i];
			this.given[i][nSquare - j - 1] = this.grid[i][nSquare - j - 1];
			this.given[nSquare - i - 1][j] = this.grid[nSquare - i - 1][j];
			this.given[nSquare - j - 1][nSquare - i - 1] = this.grid[nSquare - j - 1][nSquare - i - 1];
			this.countGiven += 7;
		}
	}

	// Starts a Solver to check if the generated puzzle is solvable
	private int solvable() {
		// returns difficulty if puzzle is solvable, or 0 otherwise
		return new Solver().solve(this.nSquare, this.given);
	}

	// Returns the difficulty level (1...5) based on the difficulty points
	// (roughly 500...1500)
	public int getDifficultyLevel() {
		// 0... 515 = very easy
		// 515... 570 = easy
		// 570... 960 = normal
		// 960...1200 = hard
		// 1200... = very hard
		if (this.difficulty < 515) {
			return Grid.LEVEL_VERY_EASY;
		}
		if (this.difficulty < 570) {
			return Grid.LEVEL_EASY;
		}
		if (this.difficulty < 960) {
			return Grid.LEVEL_NORMAL;
		}
		if (this.difficulty < 1200) {
			return Grid.LEVEL_HARD;
		}
		return Grid.LEVEL_VERY_HARD;
	}

	public void setOutput(boolean o) {
		this.output = o;
	}

	/** * Helper functions ** */
	// Cleans all grids and prepare them for generating a new puzzle
	private void cleanGrid() {
		for (int i = 0; i < nSquare; i++) {
			for (int j = 0; j < nSquare; j++) {
				this.grid[i][j] = 0;
				this.boxes[i][j] = true;
				this.rows[i][j] = true;
				this.cols[i][j] = true;
				this.given[i][j] = 0;
			}
		}
	}

	// Creates a random permutation of {1,2,...,N}
	private int[] permutateList() {
		final int[] a = new int[nSquare];
		for (int i = 0; i < nSquare; i++) {
			a[i] = i;
		}
		for (int i = 0; i < nSquare; i++) {
			final int r = rnd.nextInt(nSquare);
			final int swap = a[r];
			a[r] = a[i];
			a[i] = swap;
		}
		return a;
	}

	/** * Debug functions ** */
	public void printGrid() {
		this.printGrid(this.grid);
	}

	private void printGrid(int[][] g) {
		if (g == null) {
			System.out.println("Grid == null!");
		}

		for (int i = 0; i < n; i++) {
			System.out.println("+-----+-----+-----+");
			for (int j = 0; j < n; j++) {
				System.out.print("|");
				for (int k = 0; k < n; k++) {
					final int r = i * n + j;
					final int c = k * n;
					System.out.print((g[r][c] != 0 ? "" + g[r][c] : ".") + " "
							+ (g[r][c + 1] != 0 ? "" + g[r][c + 1] : ".") + " "
							+ (g[r][c + 2] != 0 ? "" + g[r][c + 2] : ".") + "|");
				}
				System.out.print("\n");
			}
		}
		System.out.println("+-----+-----+-----+");
	}
}