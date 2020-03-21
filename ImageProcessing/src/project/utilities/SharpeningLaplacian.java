package project.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SharpeningLaplacian {
	
	static int kernel =3;
	
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
	
	public static int[][] getLaplacianKernel(int k) {
		// create new kernel using the user given size
		int[][] kernel = new int[k][k];

		// offset for looping through the kernel
		int balance = k / 2;

		// loop through the kernel to assign the laplacian values
		for (int y = 0; y < k; y++) {
			for (int x = 0; x < k; x++) {
				// if the center of the kernel
				if (y == balance && x == balance) {
					// naive laplacian operation -n * n
					kernel[y][x] = -((k * k));
				} else {
					// all other kernel values are 1
					kernel[y][x] = 1;
				}
			}
		}
		return kernel;
	}
	public static int laplacianSharpening(BufferedImage image, int x, int y, int[][] kernel) {
		// iteratively added grayscale
		int totalGrayvalue = 0;

		// negative->positive index for looping through kernel and image
		int balance = kernel.length / 2;

		// kernel indices
		int xIndex = 0;
		int yIndex = 0;

		// loop through kernel and perform convolution
		for (int i = -balance; i < balance; i++) {
			for (int j = -balance; j < balance; j++) {
				// continue if pixel is outside bounds of image
				if (x + j < 0 || x + j >= image.getWidth() || y + i < 0 || y + i >= image.getHeight()) {
					xIndex++;
					continue;
				}

				// grab grayscale value
				int gray = getGrayscalevalue(image.getRGB(x + j, y + i));

				// multiple gray value by kernel
				int value =  -1 * gray * kernel[yIndex][xIndex];

				// add each color with the negated kernel value
				totalGrayvalue = totalGrayvalue + value;

				xIndex++;
			}
			xIndex = 0;
			yIndex++;
		}
		// max and min values for scaling
		double min = 255.0 * (kernel[balance][balance] + 1);
		double max = 255.0 * (-1 * kernel[balance][balance]);

		// laplaction calculation
		double scale = (((double) totalGrayvalue - min) / (max - min));
		totalGrayvalue = (int) (255 * scale);

		return totalGrayvalue;
	}

	public static void main(String[] args) throws IOException {
		
		int[][] laplacian = getLaplacianKernel(kernel);
		BufferedImage originalImage = ImageIO.read(new File("F:/impro/lena512.jpg"));
		BufferedImage newImage = createBufferedImage(originalImage);
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				
				// our new gray value
				int newGray = 0;
				newGray =laplacianSharpening(originalImage, x, y, laplacian);
		       // grab new color for modified gray value
				Color modifiedImage = new Color(newGray, newGray, newGray);

				// set the modified Image's RGB value at that location
				newImage.setRGB(x, y, modifiedImage.getRGB());

	}
}
		
// create modified image file and write the BufferedImage to disk
File outputFile = new File("F:/impro/SharpeningLaplacian.jpg");
ImageIO.write(newImage, "jpg", outputFile);
}
public static BufferedImage test(File input, int kernel) throws IOException {
		
	int[][] laplacian = getLaplacianKernel(kernel);
	BufferedImage originalImage = ImageIO.read(input);
	BufferedImage newImage = createBufferedImage(originalImage);
	for (int y = 0; y < originalImage.getHeight(); y++) {
		for (int x = 0; x < originalImage.getWidth(); x++) {
			
			// our new gray value
			int newGray = 0;
			newGray =laplacianSharpening(originalImage, x, y, laplacian);
	       // grab new color for modified gray value
			Color modifiedImage = new Color(newGray, newGray, newGray);

			// set the modified Image's RGB value at that location
			newImage.setRGB(x, y, modifiedImage.getRGB());

}
}
	
//create modified image file and write the BufferedImage to disk
File outputFile = new File("F:/impro/SharpeningLaplacian.jpg");
ImageIO.write(newImage, "jpg", outputFile);
return newImage;
}
}
