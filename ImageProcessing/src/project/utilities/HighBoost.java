package project.utilities;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;


public class HighBoost
{
	
	public static int[][] rgbImage(int inputArray[][], int minimum, int maximum) {
		int adjustedMax = maximum - minimum;
		int newImage[][] = new int[inputArray.length][inputArray[0].length];
		
		// Scale the image then apply the mask
		for (int row = 0; row < inputArray.length; row++) {
			for (int col = 0; col < inputArray[row].length; col++) {
				double temp = ((double) inputArray[row][col]) - minimum;
				temp = temp / adjustedMax;
				int result = SmoothingFilter.customRoundValue(temp * 255);
				if (result > 255)
					result = 255;
				newImage[row][col] = result;			
			}
		}
		
		return newImage;
	}
	
	private static int[][] highBoost(int inputArray[][], int maskSize, int hboost) {
		if (hboost < 0) return null;
				
		int height = inputArray.length;
		int width = inputArray[0].length;
		int newImage[][] = new int[height][width];
		int gmaskvalue[][] = new int[height][width];
		
		 // always smooth the image by a mask of 3
		int blurImage[][] = SmoothingFilter.smoothing(inputArray, 3);
				
		int minimum = 1073741824;
		int maximum = -(1073741824);
		
		// Get the unsharp mask
		for (int row = 0; row < inputArray.length; row++) {
			for (int col = 0; col < inputArray[row].length; col++) {
				int resultval = (inputArray[row][col]) - blurImage[row][col];
				if (resultval < minimum) minimum = resultval;
				if (resultval > maximum) maximum = resultval;
				gmaskvalue[row][col] = resultval;
			}
		}
					
		// Scale the image now
		minimum = 1073741824;
		maximum = -(1073741824);
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				int result = (inputArray[row][col]) + (gmaskvalue[row][col] * hboost);
				if (result < minimum) minimum = result;
				if (result > maximum) maximum = result;
				newImage[row][col] = result;
			}
		}
				
		newImage = rgbImage(newImage, minimum, maximum);
		return newImage;
	}

public static BufferedImage test(File input,int kernel,int hb) throws IOException {
BufferedImage image = ImageIO.read(input);
InitImage temp =new InitImage();
temp.setInitBufferedImage(image);
InitImage tempOut =new InitImage();
tempOut.setImageFromArray(highBoost(temp.getImageArrayForm(),kernel,hb));
BufferedImage image2 =  tempOut.getBufferedImage();
File lenna2 = new File("F:/impro/highboost.jpg");
ImageIO.write(image2, "jpg", lenna2);
return image2;
  }
 
  
  public static void main(String[] args) throws IOException {
      File lenna = new File("F:/impro/lena512.jpg");
      BufferedImage image = ImageIO.read(lenna);
      InitImage temp =new InitImage();
      temp.setInitBufferedImage(image);
      InitImage tempOut =new InitImage();
      tempOut.setImageFromArray(highBoost(temp.getImageArrayForm(),10,5));
      BufferedImage image2 =  tempOut.getBufferedImage();
      File lenna2 = new File("F:/impro/highboost22.jpg");
      ImageIO.write(image2, "jpg", lenna2);
  }
  
  }
