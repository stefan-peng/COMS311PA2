package pa2;

import java.util.ArrayList;

public class CutTest {

	public static void main(String[] args) {
		int[][] M = {{5, 7, 9, 4, 5}, {2, 3, 1, 1, 2}, {2, 0, 49, 46, 8}, {3, 1, 1, 1, 1}, {50, 51, 25, 26, 1}};

		ArrayList<Tuple> cut = MatrixCuts.widthCut(M);
		System.out.println(cut);
	}

}
