package project.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Midpoint {
	
	static int kernel =10;
	
	public static BufferedImage createBufferedImage(BufferedImage originalImage) {
		// return a new blank BufferedImage with replicate properties from the original image
		return new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
	}
	public static int getGrayscale(int rgb) {
		// grab the color at that position
		Color color = new Color(rgb);

		// separate out the individual colors
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();

		// simple averaging grayscale conversion
		int gray = (red + green + blue) / 3;

		return gray;
	}


	public static int max(BufferedImage image, int x, int y, int k) {
		//new gray value
		int max = 0;

		// negative->positive index for looping through kernel and image
		int offset = k / 2;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int gray = getGrayscale(image.getRGB(x + i, y + j));

				// max calculation
				if (gray > max) max = gray;
			}
		}
		return max;
	}
	public static int min(BufferedImage image, int x, int y, int k) {
		//new gray value
		int min = 255;

		// negative->positive index for looping through kernel and image
		int offset = k / 2;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int gray = getGrayscale(image.getRGB(x + i, y + j));

				// min calculation
				if (gray < min) min = gray;
			}
		}
		return min;
	}
	public static int midpoint(BufferedImage image, int x, int y, int k) {
		// grab min/max values in kernel neighborhood
		int min = min(image, x, y, k);
		int max = max(image, x, y, k);

		// return the midpoint of the two values
		return (min + max) / 2;
	}

	public static void main(String[] args) throws IOException {

		BufferedImage originalImage = ImageIO.read(new File("F:/impro/lena512.jpg"));
		BufferedImage modifiedImage = createBufferedImage(originalImage);
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				
				// our new gray value
				int modifiedGray = 0;
				modifiedGray = midpoint(originalImage, x, y, kernel);
		       // grab new color for modified gray value
				Color modifiedImageColor = new Color(modifiedGray, modifiedGray, modifiedGray);

				// set the modified Image's RGB value at that location
				modifiedImage.setRGB(x, y, modifiedImageColor.getRGB());

	}
}
		
// create modified image file and write the BufferedImage to disk
File outputFile = new File("F:/impro/Midpoint.jpg");
ImageIO.write(modifiedImage, "jpg", outputFile);
}
	public static BufferedImage test(File input, int kernel) throws IOException {
		BufferedImage originalImage = ImageIO.read(input);
		BufferedImage modifiedImage = createBufferedImage(originalImage);
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				
				// our new gray value
				int modifiedGray = 0;
				modifiedGray = midpoint(originalImage, x, y, kernel);
		       // grab new color for modified gray value
				Color modifiedImageColor = new Color(modifiedGray, modifiedGray, modifiedGray);

				// set the modified Image's RGB value at that location
				modifiedImage.setRGB(x, y, modifiedImageColor.getRGB());

	}
}
		
// create modified image file and write the BufferedImage to disk
File outputFile = new File("F:/impro/Midpoint.jpg");
ImageIO.write(modifiedImage, "jpg", outputFile);
return modifiedImage;
}
}
