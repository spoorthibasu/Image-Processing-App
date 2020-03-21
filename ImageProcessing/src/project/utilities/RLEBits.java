package project.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RLEBits {
	
	public static int getGrayscale(int rgb) {
		
		Color color = new Color(rgb);

		int red = color.getRed();
		int green = color.getGreen();
		int blue= color.getBlue();

		int grayscale = (red+ green+ blue) / 3;

		return grayscale;
	}
	
	public static byte[] imageToByte(BufferedImage img) throws IOException {
		byte[] imageByteArray = new byte[img.getWidth() * img.getHeight()];


		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
			
				int gray = getGrayscale(img.getRGB(x, y));

			
				imageByteArray[y * img.getWidth() + x] = (byte) gray;
			}
		}
		return imageByteArray;
	}
	
	public static void RLE(BufferedImage image) throws IOException {
		
		byte[] original = imageToByte(image);

		long startEncoding = System.nanoTime();
		byte[] encode= RLEEncode(original);
		long endEncoding = System.nanoTime();

		
		long startDecoding = System.nanoTime();
		byte[] decode = RLEDecode(encode);
		long EndDecoding = System.nanoTime();

		double compressionRatio = original.length / (double) encode.length;

		System.out.println("Run Length Encoding Metrics");
		System.out.println("---------------------------");
		System.out.println("Original Image Bit Size: " + original.length * 8);
		System.out.println("Encoded Image Bit Size: " + encode.length * 8);
		System.out.println("Compression Ratio: " + compressionRatio + "\n");
		System.out.println("Encoding Time: " + (endEncoding - startEncoding) / 1000000000.0 + " seconds");
		System.out.println("Decoding Time: " + (EndDecoding - startDecoding) / 1000000000.0 + " seconds\n");
	}

	public static String RLENew(BufferedImage image) throws IOException {
	
		byte[] original = imageToByte(image);


		long startEncoding = System.nanoTime();
		byte[] encode= RLEEncode(original);
		long endEncoding = System.nanoTime();

		long startDecoding = System.nanoTime();
		byte[] decode = RLEDecode(encode);
		long EndDecoding = System.nanoTime();

		double compressionRatio = original.length / (double) encode.length;

		// Printing metrics - RLE
		String res="Run Length Encoding Metrics"+"\n";
		res=res+"Original Image Bit Size: " + original.length * 8+"\n";
		res=res+"Encoded Image Bit Size: " + encode.length * 8+"\n";
		res=res+"Compression Ratio: " + compressionRatio + "\n";
		res=res+"Encoding Time: " + (endEncoding - startEncoding) / 1000000000.0 + " seconds\n";
		res=res+"Decoding Time: " + (EndDecoding - startDecoding) / 1000000000.0 + " seconds\n";
		return res;
	}
	
	public static byte[] RLEEncode(byte[] imgByteArr) {
		
		ByteArrayOutputStream runLenEncode = new ByteArrayOutputStream();

		byte lastByte = imgByteArr[0];

		
		int runCount = 1;


		for (int i = 1; i < imgByteArr.length; i++) {
	
			byte thisByte = imgByteArr[i];

			
			if (lastByte == thisByte) {
	
				runCount++;
			} else {
			
				runLenEncode.write((byte) runCount);
				runLenEncode.write((byte) lastByte);

			
				runCount = 1;
				lastByte = thisByte;
			}
		}
		
		runLenEncode.write((byte) runCount);
		runLenEncode.write((byte) lastByte);

		return runLenEncode.toByteArray();
	}

	public static byte[] RLEDecode(byte[] encode) {
		
		ByteArrayOutputStream decode = new ByteArrayOutputStream();

		for (int i = 0; i < encode.length; i += 2) {
			for (int j = 0; j < encode[i]; j++) {
			
				decode.write(encode[i + 1]);
			}
		}
		return decode.toByteArray();
	}
	public static void main(String[] args) throws IOException{
		BufferedImage originalImage = ImageIO.read(new File("F:/impro/lena512.jpg"));
		RLE(originalImage);
	}
	public static String test(File inputFile) throws IOException{
//		BufferedImage originalImage = ImageIO.read(new File("F:/impro/lena512.jpg"));
		BufferedImage originalImage = ImageIO.read(inputFile);
		return RLENew(originalImage);
		
	}

}
