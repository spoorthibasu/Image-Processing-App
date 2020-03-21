package project.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class AlphaTrimmed {
	
	static int kernel =10;
	static int alpha = 3;// alpha value smaller than kernel for trimming
	
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

	public static int alphaTrimmed(BufferedImage image, int x, int y, int kernel, int alpha) {
		//gray values in kernel neighborhood
		ArrayList<Integer> gray = new ArrayList<Integer>();

		int count = 0;

		// negative->positive index for looping through kernel and image
		int balance = kernel / 2;

		// loop through kernel and perform convolution
		for (int i = -balance; i < balance; i++) {
			for (int j = -balance; j < balance; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int grayscale = getGrayscalevalue(image.getRGB(x + i, y + j));

				// store this gray value for later use
				gray.add(grayscale);

				count++;
			}
		}
		// can't compute if alpha is larger than total array
		if (count-alpha <= 0) return 0;

		// new gray
		int newGray = 0;

		// sort the array to find the median
		Collections.sort(gray);

		// determine edges to chop off
		int half = alpha/2;

		// remove edges
		for (int i = 0 + half; i < count-half; i++) {
        		newGray += gray.get(i);
		}

		// alpha-trimmed calculation
		return flatten(newGray / (count-alpha));
	}
	public static void main(String[] args) throws IOException {

		BufferedImage originalImage = ImageIO.read(new File("F:/impro/lena512.jpg"));
		BufferedImage newImage = createBufferedImage(originalImage);
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				
				// our new gray value
				int newGray = 0;
				newGray = alphaTrimmed(originalImage, x, y, kernel, alpha);
		       // grab new color for modified gray value
				Color modifiedImageColor = new Color(newGray, newGray, newGray);

				// set the modified Image's RGB value at that location
				newImage.setRGB(x, y, modifiedImageColor.getRGB());

	}
}
		
// create modified image file and write the BufferedImage to disk
File output = new File("F:/impro/AlphaTrimmed.jpg");
ImageIO.write(newImage, "jpg", output);
}
	public static BufferedImage test(File input, int kernel, int alpha) throws IOException {
		BufferedImage originalImage = ImageIO.read(input);
		BufferedImage newImage = createBufferedImage(originalImage);
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				
				// our new gray value
				int newGray = 0;
				newGray = alphaTrimmed(originalImage, x, y, kernel, alpha);
		       // grab new color for modified gray value
				Color modifiedImageColor = new Color(newGray, newGray, newGray);

				// set the modified Image's RGB value at that location
				newImage.setRGB(x, y, modifiedImageColor.getRGB());

	}
}
		
// create modified image file and write the BufferedImage to disk
File output = new File("F:/impro/AlphaTrimmed.jpg");
ImageIO.write(newImage, "jpg", output);
return newImage;
}
}
