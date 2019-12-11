package pa2;

import java.util.ArrayList;

/**
 * 
 * @author Stefan Peng
 * @author Jacob Ramsey-Smith
 *
 */
public class ImageProcessor {

	/**
	 * Reduces input image by given number of pixels
	 * 
	 * @param x          Number of pixels to remove
	 * @param inputImage
	 * @return Reduced-width image
	 */
	public static Picture reduceWidth(int x, String inputImage) {
		Picture p = new Picture(inputImage);
		for (int i = 0; i < x; i++) {
			int[][] importance = importanceArray(p);

			ArrayList<Tuple> pixelsToRemove = new ArrayList<Tuple>();
			pixelsToRemove.addAll(MatrixCuts.widthCut(importance));
			pixelsToRemove.remove(0);
			Picture reducedPic = new Picture(p.width() - 1, p.height());

			for (int rowIndex = 0; rowIndex < pixelsToRemove.size(); rowIndex++) {
				for (int colIndex = 0; colIndex + 1 < p.width(); colIndex++) {
					// pixel after cut; shift to left
					if (colIndex >= pixelsToRemove.get(rowIndex).getY()) {
						reducedPic.set(colIndex, rowIndex, p.get(colIndex + 1, rowIndex));
					}
					// pixel before cut; don't modify
					else {
						reducedPic.set(colIndex, rowIndex, p.get(colIndex, rowIndex));
					}
				}
			}
			p = reducedPic;
		}
		return p;
	}

	/**
	 * Generate importance array for given picture
	 * 
	 * @param p
	 * @return
	 */
	private static int[][] importanceArray(Picture p) {
		int pHeight = p.height();
		int pWidth = p.width();
		int[][] importance = new int[pHeight][pWidth];
		for (int rowIndex = 0; rowIndex < pHeight; rowIndex++) {
			for (int colIndex = 0; colIndex < pWidth; colIndex++) {
				if (0 < colIndex && colIndex < pWidth - 1) {
					importance[rowIndex][colIndex] = ImageStitch.pixelDistance(p.get(colIndex - 1, rowIndex),
							p.get(colIndex + 1, rowIndex));
				} else if (colIndex == 0) {
					importance[rowIndex][colIndex] = ImageStitch.pixelDistance(p.get(colIndex, rowIndex),
							p.get(colIndex + 1, rowIndex));
				} else if (colIndex == pWidth - 1) {
					importance[rowIndex][colIndex] = ImageStitch.pixelDistance(p.get(colIndex, rowIndex),
							p.get(colIndex - 1, rowIndex));
				}
			}
		}
		return importance;
	}

}
