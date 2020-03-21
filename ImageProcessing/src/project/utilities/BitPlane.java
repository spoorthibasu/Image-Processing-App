package project.utilities;

import java.awt.image.*;

import javax.imageio.*;

import java.io.*;


public class BitPlane{

	public static int[][] bitPlaneRemoval(int inputArray[][], int startPlane, int endPlane) {
		int imgheight = inputArray.length;
		int imgwidth = inputArray[0].length;
		int newImage[][] = new int[imgheight][imgwidth];
		
		for (int i = 0; i < imgheight; i++) {
			for (int j = 0; j < imgwidth; j++) {
				String binval = String.format("%8s", Integer.toBinaryString(inputArray[i][j]));
				binval = binval.replace(' ', '0');
								
				String replacement  = "";
				for (int start = startPlane; start <= endPlane; start++) {
					replacement += "0";
				}
				binval = binval.substring(0, startPlane) + replacement + binval.substring(endPlane + 1);
				newImage[i][j] = Integer.parseInt(binval, 2);				
			}
		}
		
		return newImage;
	}
	
public static void main(String[] args) throws IOException {
    File lenna = new File("F:/impro/lena512.jpg");
    BufferedImage image = ImageIO.read(lenna);
    InitImage temp =new InitImage();
    temp.setInitBufferedImage(image);
    InitImage tempOut =new InitImage();
    tempOut.setImageFromArray(bitPlaneRemoval(temp.getImageArrayForm(),2,7));
    BufferedImage image2 =  tempOut.getBufferedImage();
    File lenna2 = new File("F:/impro/bitplane.jpg");
    ImageIO.write(image2, "jpg", lenna2);
}


public static BufferedImage test(File input,int start,int end) throws IOException {
	
  BufferedImage image = ImageIO.read(input);
  InitImage temp =new InitImage();
  temp.setInitBufferedImage(image);
  InitImage tempOut =new InitImage();
  tempOut.setImageFromArray(bitPlaneRemoval(temp.getImageArrayForm(),start,end));
  BufferedImage image2 =  tempOut.getBufferedImage();
  File lenna2 = new File("F:/impro/bitplane.jpg");
  ImageIO.write(image2, "jpg", lenna2);
  return image2;
}

}


