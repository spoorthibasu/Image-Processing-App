package project.utilities;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SmoothingFilter {
    
	public static int[][] smoothing(int[][] inputArray, int maskSize) {
    	
		int newImage[][] = new int[inputArray.length][inputArray[0].length];
		int paddednewImage[][] = paddednewImage(inputArray, maskSize);
		
		int smoothingMask[][] = generateMask(maskSize, true);
		double maskSizeValue = MaskTotal(smoothingMask);
	
		// Obtain scaled values from mask
		for (int row = 0; row < newImage.length; row++) {
			for (int col = 0; col < newImage[row].length; col++) {
				int localAreaVal[][] = LocalArea(paddednewImage, row,
						col, maskSize);
				int total = LocalAreaSum(localAreaVal, smoothingMask);
				int newGrValue = customRoundValue((double) total / maskSizeValue);
				if (newGrValue > 255)
					newGrValue = 255; // to prevent RGB values going over 255
				
				newImage[row][col] = newGrValue;
			}
		}
		
		return newImage;
	}
    
    public static final int[][] paddednewImage(final int inputArray[][], int maskSize) {
		int oneDirection = maskSize >> 1; // how far to go in one direction
		int Width = inputArray.length + maskSize - 1;
		int Height = inputArray[0].length + maskSize - 1;
		int paddednewImage[][] = new int[Width][Height];
		
		for (int i = 0; i < Width; i++) {
			for (int j = 0; j <Height; j++) {
				// Pad the top and bottom rows and left and right columns with 0s
				if (i < oneDirection || i >= (Width - oneDirection) ||
					j < oneDirection || j >= (Height - oneDirection)) {
					paddednewImage[i][j] = 0;
				}
				// Fill in the rest
				else {
					paddednewImage[i][j] = inputArray[i-oneDirection][j-oneDirection];
				}
			}
		}
		
		return paddednewImage;
	}
    
    public static final int customRoundValue(Double input) {
		return (int) Math.floor(input + 0.5);
	}
    
	public static final int[][] LocalArea(int paddedImage[][],
			int centeri, int centerj, int maskSize) {
		int localAreaVal[][] = new int[maskSize][maskSize];
		
		for (int i = 0; i < maskSize; i++) {
			int RowIndex = centeri + i;
			for (int j = 0; j < maskSize; j++) {
				int ColumnIndex = centerj + j;
				localAreaVal[i][j] = paddedImage[RowIndex][ColumnIndex];
			}
		}
		
		return localAreaVal;
	}
	public static int LocalAreaSum(int localAreaval[][], int maskSize[][]) {
		int total = 0;
		
		for (int row = 0; row < localAreaval.length; row++) {
			for (int col = 0; col < localAreaval[row].length; col++) {
				total += (localAreaval[row][col] * maskSize[row][col]);
			}
		}
		
		return total;
	}
	private static double MaskTotal(int mask[][]) {
		double maskSizeval = 0;
		int maskSize = mask.length;
		for (int row = 0; row < maskSize; row++) {
			for (int col = 0; col < maskSize; col++) {
				maskSizeval += mask[row][col];
			}
		}
		
		return maskSizeval;
	}
    
	public  static int[][] generateMask(int maskSize, boolean isPosit) {
		// Create a mask of all 1s
		int maskval[][] = new int[maskSize][maskSize];
		for (int row = 0; row < maskSize; row++) {
			for (int col = 0; col < maskSize; col++) {
				if (isPosit)
					maskval[row][col] = 1;
				else
					maskval[row][col] = -1;
			}
		}
		return maskval;
	}

    public static void main(String[] args) throws IOException {
        File lenna = new File("F:/impro/lena512.jpg");
        BufferedImage image = ImageIO.read(lenna);
        InitImage temp =new InitImage();
        temp.setInitBufferedImage(image);
        InitImage tempOut =new InitImage();
        tempOut.setImageFromArray(smoothing(temp.getImageArrayForm(),10));
        BufferedImage image2 =  tempOut.getBufferedImage();
        File lenna2 = new File("F:/impro/Smoothing.jpg");
        ImageIO.write(image2, "jpg", lenna2);
    }
    
    public static BufferedImage test(File input,int kernel) throws IOException {
    	 BufferedImage image = ImageIO.read(input);
         InitImage temp =new InitImage();
         temp.setInitBufferedImage(image);
         InitImage tempOut =new InitImage();
         tempOut.setImageFromArray(smoothing(temp.getImageArrayForm(),kernel));
         BufferedImage image2 =  tempOut.getBufferedImage();
         File lenna2 = new File("F:/impro/outputblur.jpg");
         ImageIO.write(image2, "jpg", lenna2);
		return image2;
    }

}
