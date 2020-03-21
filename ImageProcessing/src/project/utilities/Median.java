package project.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class Median {
	
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

	public static int median(BufferedImage image, int x, int y, int k) {
		//gray values in kernel neighborhood
		ArrayList<Integer> grayvalue = new ArrayList<Integer>();

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

				// store this gray value for later use
				grayvalue.add(grayscale);
			}
		}
		// sort the array to find the median
		Collections.sort(grayvalue);

		// find middle index
		int middle = grayvalue.size() / 2;

		if (grayvalue.size() % 2 == 1) {
			// return median value
			return grayvalue.get(middle);
		} else {
			// return average of two median values (even number of items)
			return (int) ((grayvalue.get(middle-1) + grayvalue.get(middle)) / 2.0);
		}
	}

	public static void main(String[] args) throws IOException {

		BufferedImage originalImage = ImageIO.read(new File("F:/impro/lena512.jpg"));
		BufferedImage newImage = createBufferedImage(originalImage);
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				
				// our new gray value
				int newGray = 0;
				newGray = median(originalImage, x, y, kernel);
		       // grab new color for modified gray value
				Color modifiedImageColor = new Color(newGray, newGray, newGray);

				// set the modified Image's RGB value at that location
				newImage.setRGB(x, y, modifiedImageColor.getRGB());

	}
}
		
// create modified image file and write the BufferedImage to disk
File output = new File("F:/impro/Median.jpg");
ImageIO.write(newImage, "jpg", output);
}
public static BufferedImage test(File input, int kernel) throws IOException {
		BufferedImage originalImage = ImageIO.read(input);
		BufferedImage newImage = createBufferedImage(originalImage);
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				
				// our new gray value
				int newGray = 0;
				newGray = median(originalImage, x, y, kernel);
		       // grab new color for modified gray value
				Color modifiedImageColor = new Color(newGray, newGray, newGray);

				// set the modified Image's RGB value at that location
				newImage.setRGB(x, y, modifiedImageColor.getRGB());

	}
}
		
// create modified image file and write the BufferedImage to disk
File output = new File("F:/impro/Median.jpg");
ImageIO.write(newImage, "jpg", output);
return newImage;
}
}
