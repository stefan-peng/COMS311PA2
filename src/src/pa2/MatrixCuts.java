package pa2;

import java.util.ArrayList;

public class MatrixCuts {
	public static ArrayList<Tuple> widthCut(int[][] M){
		ArrayList<Tuple> minCut = new ArrayList<Tuple>();
		int rows = M.length;
		int cols = M[0].length;
		int[][] countArray = new int[rows][cols];
		//TODO test this and make sure it works just putting the
		//first row of M into countArray
		countArray[0] = M[0];
		//populate countArray
		for(int rowIndex = 1; rowIndex < rows; rowIndex++) {
			for(int columnIndex = 0; columnIndex < cols; columnIndex++) {
				int lowest = Integer.MAX_VALUE;
				if(columnIndex > 0)
					lowest = M[rowIndex - 1][columnIndex -1] + M[rowIndex][columnIndex];
				if(M[rowIndex-1][columnIndex] + M[rowIndex][columnIndex] < lowest)
					lowest = M[rowIndex-1][columnIndex];
				if(columnIndex + 1 < cols) {
					if(M[rowIndex - 1][columnIndex + 1] < lowest)
						lowest = M[rowIndex-1][columnIndex];
				}			
			}
		}
		//find path and populate minCut array
		//find smallest in last row
		int cost = 0;
		int smallestTotal = Integer.MAX_VALUE;
		int indexOfSmallest = 0;
		for(int columnIndex = 0; columnIndex < cols; columnIndex++) {
			if(countArray[rows - 1][columnIndex] < smallestTotal) {
				smallestTotal = countArray[rows - 1][columnIndex];
				indexOfSmallest = columnIndex;
				cost = smallestTotal;
			}
		}
		Tuple tuple = new Tuple(rows - 1,indexOfSmallest);
		minCut.add(tuple);		
		//now work our way up adding to minCut as we go along
		int rowMin;
		int tempIndexOfSmallest = 0;
		for(int rowIndex = rows - 2; rowIndex >= 0; rowIndex--) {
			rowMin = Integer.MAX_VALUE;
			if(indexOfSmallest + 1 < cols) {
				rowMin = countArray[rowIndex][indexOfSmallest + 1];
				tempIndexOfSmallest = indexOfSmallest + 1;
			}
			if(indexOfSmallest - 1 >= 0) {
				if(rowMin > countArray[rowIndex][indexOfSmallest - 1]) {
					rowMin = countArray[rowIndex][indexOfSmallest - 1];
					tempIndexOfSmallest = indexOfSmallest - 1;
				}
			}
			if(rowMin > countArray[rowIndex][indexOfSmallest]) {
				rowMin = countArray[rowIndex][indexOfSmallest];
				tempIndexOfSmallest = indexOfSmallest;
			}
			indexOfSmallest = tempIndexOfSmallest;
			Tuple tuple1 = new Tuple(rowIndex, indexOfSmallest);
			minCut.add(tuple1);			
		}
		
		//minCut is currently backwards. Fill in another array by iterating backwards through minCut
		ArrayList<Tuple> reverse = new ArrayList<Tuple>();
		for(int i = minCut.size() - 1; i >= 0; i--) {
			reverse.add(minCut.get(i));
		}		
		return reverse;
		
	}
}
