package project.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ContraharmonicMean {
	
	static int kernel =10;
	
	public static BufferedImage createBufferedImage(BufferedImage originalImage) {
		// return a new blank BufferedImage with replicate properties from the original image
		return new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
	}
	public static int getGrayscalevalue(int rgb) {
		// grab the color at that position
		Color color = new Color(rgb);

		// separate out the individual colors
		int r = color.getRed();
		int g = color.getGreen();
		int b= color.getBlue();

		// simple averaging grayscale conversion
		int grayscale = (r+ g+ b) / 3;

		return grayscale;
	}
	public static int flatten(double gray) {
		// mathematical calculation to squash a value between 0 and 255
		return Math.min(Math.max((int) gray, 0), 255);
	}

	public static int contraHarmonicMean(BufferedImage image, int x, int y, int k) {
		// averaging amount
		int count = 0;

		// iteratively added grayscale numerator and denominator
		double grayNum = 0.0;
		double grayDenom = 0.0;

		// negative->positive index for looping through kernel and image
		int balance = k / 2;

		// loop through kernel and perform convolution
		for (int i = -balance; i < balance; i++) {
			for (int j = -balance; j < balance; j++) {
				
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int grayscale = getGrayscalevalue(image.getRGB(x + i, y + j));

				// add each color squared to numerator
				// add each color to denominator
				grayNum += Math.pow(grayscale, 2);
				grayDenom += grayscale;

				// increment total value used for averaging
				count++;
			}
		}
		// can't divide by zero so return 0
		if (count == 0) return 0;

		// contra harmonic mean calculation
		// squash color values between 0 - 255
		return flatten(grayNum / grayDenom);
	}
	public static void main(String[] args) throws IOException {

		BufferedImage originalImage = ImageIO.read(new File("F:/impro/lena512.jpg"));
		BufferedImage newImage = createBufferedImage(originalImage);
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				
				// our new gray value
				int newGray = 0;
				newGray =contraHarmonicMean(originalImage, x, y, kernel);
		       // grab new color for modified gray value
				Color modifiedImage = new Color(newGray, newGray, newGray);

				// set the modified Image's RGB value at that location
				newImage.setRGB(x, y, modifiedImage.getRGB());

	}
}
		
// create modified image file and write the BufferedImage to disk
File output = new File("F:/impro/ContraHarmonicMean.jpg");
ImageIO.write(newImage, "jpg", output);
}
public static BufferedImage test(File input, int kernel) throws IOException {
	BufferedImage originalImage = ImageIO.read(input);
	BufferedImage newImage = createBufferedImage(originalImage);
	for (int y = 0; y < originalImage.getHeight(); y++) {
		for (int x = 0; x < originalImage.getWidth(); x++) {
			
			// our new gray value
			int newGray = 0;
			newGray =contraHarmonicMean(originalImage, x, y, kernel);
	       // grab new color for modified gray value
			Color modifiedImage = new Color(newGray, newGray, newGray);

			// set the modified Image's RGB value at that location
			newImage.setRGB(x, y, modifiedImage.getRGB());

}
}
	
//create modified image file and write the BufferedImage to disk
File output = new File("F:/impro/ContraHarmonicMean.jpg");
ImageIO.write(newImage, "jpg", output);
return newImage;
}
}
