package project.utilities;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class GaussianNoise{
	
	private static final double SIGMA = 20;
	
	public static Image transformImage(Image image){
		
		int[][][] imageData = PixelExtractor.getPixelData(image);
		int numofRows = imageData.length;
		int numofCols = imageData[0].length;
		
		double noisyImage[][][] = new double[numofRows][numofCols][4];
		
		Random randomval = new Random();
		
		double x = 2.0 * SIGMA;
		
		double mean = (3.0 * x) / 2.0;
		
		for(int row = 0; row < numofRows; row++){
			for(int col =0; col < numofCols; col++){
				
				double random1 = randomval.nextDouble() * x;
				double random2 = randomval.nextDouble() * x;
				double random3 = randomval.nextDouble() * x;

				noisyImage[row][col][0] = 255;
				
				for(int colorIndexValue = 1; colorIndexValue < 4; colorIndexValue++){
					
					// make 0 mean
					double noise = random1 + random2 + random3 - mean;
					
					if (noise + imageData[row][col][colorIndexValue] > 255) {
						noisyImage[row][col][colorIndexValue] = 255;
					} 
					else if (noise + imageData[row][col][colorIndexValue] < 0) {
						noisyImage[row][col][colorIndexValue] = 0;
					} 
					else {
						noisyImage[row][col][colorIndexValue] = noise
								+ imageData[row][col][colorIndexValue];
					}
				}
				
			}
		}
		
		int[] imageDataOne = PixelExtractor.toOneDImg(noisyImage);
		
		image = new Container().createImage(new MemoryImageSource(numofCols, numofRows, imageDataOne, 0, numofCols));
		return image;
		
		
	}
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }
	    // Create a buffered image with transparency
	    BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
	    // Draw the image on to the buffered image
	    Graphics2D bimg = image.createGraphics();
	    bimg.drawImage(img, 0, 0, null);
	    bimg.dispose();

	    // Return the buffered image
	    return image;
	}
  public static void main(String[] args) throws IOException {
        File lenna = new File("F:/impro/lena512.jpg");
        Image image = ImageIO.read(lenna);
        Image image1 = transformImage(image);
        BufferedImage image2=toBufferedImage(image1);
        File lenna2 = new File("F:/impro/Gaussiannoise.jpg");
        ImageIO.write(image2, "jpg", lenna2);
    }
  public static BufferedImage test(File input) throws IOException {
		BufferedImage originalImage = ImageIO.read(input);
		Image image1 = transformImage(originalImage);
        BufferedImage image2=toBufferedImage(image1);
return image2;
}
}	
