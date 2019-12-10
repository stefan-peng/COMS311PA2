package pa2;

import java.util.ArrayList;

public class ImageProcessor {
	
	static Picture reduceWidth(int x, String inputImage) {
		if(x > 0)
		{
			Picture p = new Picture(inputImage);
			int pHeight = p.height();
			int pWidth = p.width();
			int[][] importance = new int[pWidth][pHeight];
			for(int rowIndex = 0; rowIndex < pHeight; rowIndex++) {
				for(int colIndex = 0; colIndex < pWidth; colIndex++) {
					if(0 < colIndex && colIndex < pWidth - 1) {
						importance[rowIndex][colIndex] = ImageStitch.pixelDistance(p.get(colIndex - 1, rowIndex), p.get(colIndex + 1, rowIndex));
					}
					if(colIndex == 0) {
						importance[rowIndex][colIndex] = ImageStitch.pixelDistance(p.get(colIndex, rowIndex), p.get(colIndex + 1, rowIndex));
						if(colIndex == pWidth - 1) {
							importance[rowIndex][colIndex] = ImageStitch.pixelDistance(p.get(colIndex, rowIndex), p.get(colIndex - 1, rowIndex));
						}
					}
				}
			}
			//need to do this next part x times
			ArrayList<Tuple> pixelsToRemove = new ArrayList<Tuple>();
			pixelsToRemove.addAll(MatrixCuts.widthCut(importance));
			Picture reducedPic = new Picture(pWidth - 1, pHeight);
			for(int rowIndex = 0; rowIndex < pixelsToRemove.size(); rowIndex++) {
				for(int colIndex = 0; colIndex + 1 < pWidth; colIndex++) {
					if(colIndex >= pixelsToRemove.get(rowIndex).getY())
					{
						reducedPic.set(colIndex, rowIndex, p.get(colIndex + 1, rowIndex));
					}else {
						reducedPic.set(colIndex, rowIndex, p.get(colIndex, rowIndex));
					}
				}
			}
			
			//Recursively call to do this x times removing 1 width each time
			return reduceWidth(x - 1, reducedPic);
		}else {
			Picture p = new Picture(inputImage);
			p.save("reducedPic");
			return p;
		}
		
		return null;		
	}

}
