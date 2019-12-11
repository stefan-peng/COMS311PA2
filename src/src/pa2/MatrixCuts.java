package pa2;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author Stefan Peng
 * @author Jacob Ramsey-Smith
 *
 */
public class MatrixCuts {
	/**
	 * Finds and returns the width cut of the given matrix
	 * 
	 * @param M Input matrix
	 * @return ArrayList<Tuple> of width cut
	 */
	public static ArrayList<Tuple> widthCut(int[][] M) {
		ArrayList<Tuple> minCut = new ArrayList<Tuple>();
		int rows = M.length;
		int cols = M[0].length;
		int[][] P = new int[rows][cols];

		// copy first row of M into cost array P
		P[0] = M[0];
		// populate P
		for (int i = 1; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				// cell is in first column
				if (j == 0) {
					P[i][j] = P[i - 1][j] + M[i][j];
				}
				// cell is in a middle column
				else if (j + 1 < cols) {
					P[i][j] = min(P[i - 1][j - 1], P[i - 1][j], P[i - 1][j + 1]) + M[i][j];
				}
				// cell is in last column
				else {
					P[i][j] = Math.min(P[i - 1][j - 1], P[i - 1][j]) + M[i][j];
				}
			}
		}

		// find path and populate minCut array
		// find smallest in last row
		int smallestTotal = Integer.MAX_VALUE;
		int indexOfSmallest = 0;
		for (int columnIndex = 0; columnIndex < cols; columnIndex++) {
			if (P[rows - 1][columnIndex] < smallestTotal) {
				smallestTotal = P[rows - 1][columnIndex];
				indexOfSmallest = columnIndex;
			}
		}
		Tuple tuple = new Tuple(rows - 1, indexOfSmallest);
		minCut.add(tuple);
		// now work our way up adding to minCut as we go along
		int rowMin;
		int tempIndexOfSmallest = 0;
		for (int rowIndex = rows - 2; rowIndex >= 0; rowIndex--) {
			rowMin = Integer.MAX_VALUE;
			if (indexOfSmallest + 1 < cols) {
				rowMin = P[rowIndex][indexOfSmallest + 1];
				tempIndexOfSmallest = indexOfSmallest + 1;
			}
			if (indexOfSmallest - 1 >= 0) {
				if (rowMin > P[rowIndex][indexOfSmallest - 1]) {
					rowMin = P[rowIndex][indexOfSmallest - 1];
					tempIndexOfSmallest = indexOfSmallest - 1;
				}
			}
			if (rowMin > P[rowIndex][indexOfSmallest]) {
				rowMin = P[rowIndex][indexOfSmallest];
				tempIndexOfSmallest = indexOfSmallest;
			}
			indexOfSmallest = tempIndexOfSmallest;
			Tuple tuple1 = new Tuple(rowIndex, indexOfSmallest);
			minCut.add(tuple1);
		}

		minCut.add(new Tuple(smallestTotal, -1));
		Collections.reverse(minCut);

		return minCut;
	}

	/**
	 * Finds and returns stitch cut of given matrix
	 * 
	 * @param M Input matrix
	 * @return ArrayList<Tuple> of stitch cut
	 */
	public static ArrayList<Tuple> stitchCut(int[][] M) {
		int rows = M.length;
		int cols = M[0].length;
		int[][] P = new int[rows][cols];

		// generate cost array
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				// cell is in first row
				if (i == 0) {
					P[i][j] = M[i][j];
				}
				// cell is in first column
				else if (j == 0) {
					P[i][j] = P[i - 1][j] + M[i][j];
				}
				// cell is in a later column
				else {
					P[i][j] = min(P[i - 1][j - 1], P[i - 1][j], P[i][j - 1]) + M[i][j];
				}
			}
		}

		// backtrack to find tuples
		ArrayList<Tuple> cut = new ArrayList<Tuple>();
		Tuple costtuple;

		int mincol = 0;
		int i = rows - 1;
		int mincost = P[i][0];

		// bottom row, find minimum total cost
		for (int j = 0; j < cols; j++) {
			if (P[i][j] < mincost) {
				mincost = P[i][j];
				mincol = j;
			}
		}

		costtuple = new Tuple(mincost, -1);
		cut.add(new Tuple(i, mincol));

		// find path back to row 0
		while (i > 0) {
			// left cell has lowest cost
			if (mincol > 0 && P[i][mincol - 1] < P[i - 1][mincol] && P[i][mincol - 1] < P[i - 1][mincol - 1]) {
				cut.add(new Tuple(i, mincol - 1));
				mincol--;
			}
			// mincol is 0 or top cell has lowest cost
			else if (mincol == 0
					|| i > 0 && P[i - 1][mincol] < P[i][mincol - 1] && P[i - 1][mincol] < P[i - 1][mincol - 1]) {
				cut.add(new Tuple(i - 1, mincol));
				i--;
			}
			// diagonal cell has lowest cost
			else if (i > 0 && mincol > 0) {
				cut.add(new Tuple(i - 1, mincol - 1));
				i--;
				mincol--;
			}
		}

		cut.add(costtuple);
		Collections.reverse(cut);

		return cut;

	}

	private static int min(int a, int b, int c) {
		return Math.min(a, Math.min(b, c));
	}
}
