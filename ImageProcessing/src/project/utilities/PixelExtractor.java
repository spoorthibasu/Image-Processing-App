package project.utilities;

import java.awt.Image;
import java.awt.image.PixelGrabber;

public class PixelExtractor {

	/**
	 * Hide the constructor
	 */
	private PixelExtractor() {
	}

	/**
	 * Extracts pixels from an Image object and turns them into a pixel array
	 * int[ROW][COL][COLOR_INDEX]
	 * 
	 * @param image
	 *            The image object to convert to a pixel array
	 * 
	 * @return int[ROW][COL][COLOR_INDEX] pixel array
	 */
	public static int[][][] getPixelData(Image image) {

		// get Image pixels in 1D int array
		int width = image.getWidth(null);
		int height = image.getHeight(null);

		int[] imgDataOneD = imageToPixels(image);

		// convert to row, col, color array
		return toThreeDImg(imgDataOneD, width, height);
	}

	/**
	 * Convert Image object into a 1d pixel array
	 * 
	 * @param image
	 *            Image object to conver
	 * 
	 * @return A 1d pixel array
	 */
	private static int[] imageToPixels(Image image) {

		int height = image.getHeight(null);
		int width = image.getWidth(null);

		int pixels[] = new int[width * height];

		PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height,
				pixels, 0, width);

		try {
			grabber.grabPixels();
		} catch (InterruptedException e) {
		}

		return pixels;
	}

	/**
	 * The purpose of this method is to convert the data in the 3D array of ints
	 * back into the 1d array of type int. This is the reverse of the method
	 * named convertToThreeDim.
	 * 
	 * @param imgData
	 * 
	 * @return
	 */
	// 
	public static int[] toOneDImg(int[][][] imgData) {

		int numRows = imgData.length;
		int numCols = imgData[0].length;

		// Create the 1D array of type int to be
		// populated with pixel data, one int value
		// per pixel, with four color and alpha bytes
		// per int value.
		int[] oneDPix = new int[numCols * numRows * 4];

		// Move the data into the 1D array. Note the
		// use of the bitwise OR operator and the
		// bitwise left-shift operators to put the
		// four 8-bit bytes into each int.
		for (int row = 0, cnt = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				oneDPix[cnt] = ((imgData[row][col][0] << 24) & 0xFF000000)
						| ((imgData[row][col][1] << 16) & 0x00FF0000)
						| ((imgData[row][col][2] << 8) & 0x0000FF00)
						| ((imgData[row][col][3]) & 0x000000FF);
				cnt++;
			}// end for loop on col

		}// end for loop on row

		return oneDPix;
	}// end convertToOneDim
	
	/**
	 * The purpose of this method is to convert the data in the 3D array of ints
	 * back into the 1d array of type int. This is the reverse of the method
	 * named convertToThreeDim.
	 * 
	 * @param imgData
	 * 
	 * @return
	 */
	// 
	public static int[] toOneDImg(double[][][] imgData) {

		int numRows = imgData.length;
		int numCols = imgData[0].length;

		// Create the 1D array of type int to be
		// populated with pixel data, one int value
		// per pixel, with four color and alpha bytes
		// per int value.
		int[] oneDPix = new int[numCols * numRows * 4];

		// Move the data into the 1D array. Note the
		// use of the bitwise OR operator and the
		// bitwise left-shift operators to put the
		// four 8-bit bytes into each int.
		for (int row = 0, cnt = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				oneDPix[cnt] = ((((int)imgData[row][col][0] << 24)) & 0xFF000000)
						| (((int)imgData[row][col][1] << 16) & 0x00FF0000)
						| (((int)imgData[row][col][2] << 8) & 0x0000FF00)
						| (((int)imgData[row][col][3]) & 0x000000FF);
				cnt++;
			}// end for loop on col

		}// end for loop on row

		return oneDPix;
	}// end convertToOneDim

	/**
	 * Converts a 1d pixel array to a 3d pixel array reference
	 * http://www.developer.com/java/other/article.php/3403921
	 * 
	 */
	private static int[][][] toThreeDImg(int[] oneDPix, int imgCols,
			int imgRows) {
		// Create the new 3D array to be populated
		// with color data.
		int[][][] data = new int[imgRows][imgCols][4];

		for (int row = 0; row < imgRows; row++) {
			// Extract a row of pixel data into a
			// temporary array of ints
			int[] aRow = new int[imgCols];
			for (int col = 0; col < imgCols; col++) {
				int element = row * imgCols + col;
				aRow[col] = oneDPix[element];
			}// end for loop on col

			// Move the data into the 3D array. Note
			// the use of bitwise AND and bitwise right
			// shift operations to mask all but the
			// correct set of eight bits.
			for (int col = 0; col < imgCols; col++) {
				// Alpha data
				data[row][col][0] = (aRow[col] >> 24) & 0xFF;
				// Red data
				data[row][col][1] = (aRow[col] >> 16) & 0xFF;
				// Green data
				data[row][col][2] = (aRow[col] >> 8) & 0xFF;
				// Blue data
				data[row][col][3] = (aRow[col]) & 0xFF;
			}// end for loop on col
		}// end for loop on row
		return data;
	}// end convertToThreeDim
	// -------------------------------------------//

}
