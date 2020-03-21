package project.utilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;



public class GlobalHistogramEqualization {
	
	public static int[] CalculateHistogram(BufferedImage image) {
	    int pixel[];
	    //array represents the intensity values of the pixels
	    int lev[] = new int[256];
	    for (int i = 0; i < image.getWidth(); i++) {
	        for (int j = 0; j < image.getHeight(); j++) {
	        pixel = image.getRaster().getPixel(i, j, new int[3]);
	        //increase if same pixel appears
	        lev[pixel[0]]++;
	        }
	     }
	     //return the histogram array
	     return lev;
	}
	public static BufferedImage histogramEqualize(BufferedImage image) {
	    //call CalculateHist method to get the histogram
	    int[] h = CalculateHistogram(image);
	    //calculate total number of pixel
	    int mass = image.getWidth() * image.getHeight();
	    int k = 0;
	    long sum = 0;
	    int pixel[];
	    //calculate the scale factor
	    float scale = (float) 255.0 / mass;
	    //calculte cdf
	    for (int x = 0; x < h.length; x++) {
	        sum += h[x];
	        int value = (int) (scale * sum);
	        if (value > 255) {
	           value = 255;
	        }
	        h[x] = value;
	    }
	    for (int i = 0; i < image.getWidth(); i++) {
	        for (int j = 0; j < image.getHeight(); j++) {
	        pixel = image.getRaster().getPixel(i, j, new int[3]);
	        //set the new value
	        k = h[pixel[0]];
	        Color color = new Color(k, k, k);
	        int rgb = color.getRGB();
	        image.setRGB(i, j, rgb);
	        
	    }
	  }
	    return image;
	}
	public static void main(String[] args) throws IOException {
		File lenna = new File("F:/impro/lena512.jpg");
        BufferedImage image = ImageIO.read(lenna);
        BufferedImage image2 = histogramEqualize(image);
        File lenna2 = new File("F:/impro/HistogramOutput.jpg");
        ImageIO.write(image2, "jpg", lenna2);
    }
	
	public static BufferedImage test(File input) throws IOException {
//		File lenna = new File("F:/impro/lena512.jpg");
		File lenna = input;
        BufferedImage image = ImageIO.read(lenna);
        BufferedImage image2 = histogramEqualize(image);
        File lenna2 = new File("F:/impro/HistogramOutput.jpg");
        ImageIO.write(image2, "jpg", lenna2);
		return image2;
    }
}
