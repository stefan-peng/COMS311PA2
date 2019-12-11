package pa2;

import java.util.ArrayList;
import java.util.Collections;

public class MatrixCuts {
	public static ArrayList<Tuple> widthCut(int[][] M) {
		ArrayList<Tuple> minCut = new ArrayList<Tuple>();
		int rows = M.length;
		int cols = M[0].length;
		int[][] countArray = new int[rows][cols];
		// TODO test this and make sure it works just putting the
		// first row of M into countArray
		countArray[0] = M[0];
		// populate countArray
		for (int rowIndex = 1; rowIndex < rows; rowIndex++) {
			for (int columnIndex = 0; columnIndex < cols; columnIndex++) {
				int lowest = Integer.MAX_VALUE;
				if (columnIndex > 0)
					lowest = M[rowIndex - 1][columnIndex - 1] + M[rowIndex][columnIndex];
				if (M[rowIndex - 1][columnIndex] + M[rowIndex][columnIndex] < lowest)
					lowest = M[rowIndex - 1][columnIndex];
				if (columnIndex + 1 < cols) {
					if (M[rowIndex - 1][columnIndex + 1] < lowest)
						lowest = M[rowIndex - 1][columnIndex];
				}
			}
		}
		// find path and populate minCut array
		// find smallest in last row
		int cost = 0;
		int smallestTotal = Integer.MAX_VALUE;
		int indexOfSmallest = 0;
		for (int columnIndex = 0; columnIndex < cols; columnIndex++) {
			if (countArray[rows - 1][columnIndex] < smallestTotal) {
				smallestTotal = countArray[rows - 1][columnIndex];
				indexOfSmallest = columnIndex;
				cost = smallestTotal;
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
				rowMin = countArray[rowIndex][indexOfSmallest + 1];
				tempIndexOfSmallest = indexOfSmallest + 1;
			}
			if (indexOfSmallest - 1 >= 0) {
				if (rowMin > countArray[rowIndex][indexOfSmallest - 1]) {
					rowMin = countArray[rowIndex][indexOfSmallest - 1];
					tempIndexOfSmallest = indexOfSmallest - 1;
				}
			}
			if (rowMin > countArray[rowIndex][indexOfSmallest]) {
				rowMin = countArray[rowIndex][indexOfSmallest];
				tempIndexOfSmallest = indexOfSmallest;
			}
			indexOfSmallest = tempIndexOfSmallest;
			Tuple tuple1 = new Tuple(rowIndex, indexOfSmallest);
			minCut.add(tuple1);
		}

		// minCut is currently backwards. Fill in another array by iterating backwards
		// through minCut
		ArrayList<Tuple> reverse = new ArrayList<Tuple>();
		for (int i = minCut.size() - 1; i >= 0; i--) {
			reverse.add(minCut.get(i));
		}
		return reverse;
	}

	static ArrayList<Tuple> stitchCut(int[][] M) {
		int[][] P = new int[M.length][M[0].length];
		// generate cost array
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
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

		int rows = P.length;
		int cols = P[0].length;

		int mincol = 0;
		int i = rows - 1;
		int mincost = P[i][0];

		// testing
		//		for (int k = 0; k < rows; k++) {
		//			for (int j = 0; j < cols; j++) {
		//				System.out.printf("%2d ", P[k][j]);
		//			}
		//			System.out.println("\n");
		//		}

		// bottom row, find minimum total cost
		for (int j = 0; j < cols; j++) {
			if (P[i][j] < mincost) {
				mincost = P[i][j];
				mincol = j;
			}
		}

		costtuple = new Tuple(mincost, -1);
		cut.add(new Tuple(i, mincol));

		//		 find path back to row 0
		while (i > 0) {
			// find minimum of left, top, diagonal

			////			int left = P[i][mincol-1];
			////			int diagonal =  P[i-1][mincol-1];
			////			int top = P[i-1][mincol];

			// left cell has lowest cost
			if (mincol > 0 && P[i][mincol-1] < P[i-1][mincol] && P[i][mincol-1] < P[i-1][mincol-1]) {
				cut.add(new Tuple(i,mincol-1));
				mincol--;
			}
			// mincol is 0 or top cell has lowest cost
			else if (mincol == 0 || i > 0 && P[i-1][mincol] < P[i][mincol-1] && P[i-1][mincol] < P[i-1][mincol-1]) {
				cut.add(new Tuple(i-1,mincol));
				i--;
			}
			// diagonal cell has lowest cost
			else if (i > 0 && mincol > 0) {
				cut.add(new Tuple(i-1,mincol-1));
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
