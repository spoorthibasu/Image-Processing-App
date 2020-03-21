
package project.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class LZWEnDecode {
	
	public static int getGrayscale(int rgb) {
		
		Color color = new Color(rgb);

		int red = color.getRed();
		int green = color.getGreen();
		int blue= color.getBlue();

	
		int grayscale = (red+ green+ blue) / 3;

		return grayscale;
	}
	public static int[] imageToIntrArr(BufferedImage img) throws IOException {
	
		int[] imageIntArr = new int[img.getWidth() * img.getHeight()];

	
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				
				int gray = getGrayscale(img.getRGB(x, y));
				imageIntArr[y * img.getWidth() + x] = gray;
			}
		}
		return imageIntArr;
	}
	
	public static int convert(BitSet bits) {
		int signedvalue = 0;
		
		for (int i = 0; i < bits.length(); ++i) {
			signedvalue += bits.get(i) ? (1 << i) : 0;
		}
		return signedvalue;
	}
	public static BitSet convert(int value) {
		BitSet bits = new BitSet();
		int index = 0;

		while (value != 0) {
			if (value % 2 != 0) {
				bits.set(index);
			}
			++index;
			value = value >>> 1;
		}
		return bits;
	}
	
	public static void LZW(BufferedImage img) throws IOException {
		
		int[] imgByte = imageToIntrArr(img);

		
		StringBuilder inputTemp = new StringBuilder();
		for (int i = 0; i < imgByte.length; i++) {
			inputTemp.append((char) imgByte[i]);
		}
		String input = new String(inputTemp);

		
		long startEncoding = System.nanoTime();
		ArrayList<BitSet> encode = LZWEncode(input);
		long endEncoding = System.nanoTime();

		int totalBits = 0;
		for (int i = 0; i < encode.size(); i++) {
			BitSet temp = encode.get(i);
			totalBits += temp.length();
		}
		long startDecoding = System.nanoTime();
		String decode = LZWDecode(encode);
		long EndDecoding = System.nanoTime();

		double compressionRatio = (imgByte.length * 8) / (double) totalBits;

		// Print metrics - LZW
		System.out.println("LZW Encoding/Decoding Metrics");
		System.out.println("-----------------------------");
		System.out.println("Original Image Bit Size: " + imgByte.length * 8);
		System.out.println("Encoded Image Bit Size: " + totalBits);
		System.out.println("Compression Ratio: " + compressionRatio + "\n");
		System.out.println("Encoding Time: " + (endEncoding - startEncoding) / 1000000000.0 + " seconds");
		System.out.println("Decoding Time: " + (EndDecoding - startDecoding) / 1000000000.0 + " seconds\n");
	}

	
	public static String LZWNew(BufferedImage img) throws IOException {
	
		int[] imgByte = imageToIntrArr(img);

		
		StringBuilder inputTemp = new StringBuilder();
		for (int i = 0; i < imgByte.length; i++) {
			inputTemp.append((char) imgByte[i]);
		}
		String input = new String(inputTemp);

		long startEncoding = System.nanoTime();
		ArrayList<BitSet> encode = LZWEncode(input);
		long endEncoding = System.nanoTime();

		
		int totalBits = 0;
		for (int i = 0; i < encode.size(); i++) {
			BitSet temp = encode.get(i);
			totalBits += temp.length();
		}

		
		long startDecoding = System.nanoTime();
		String decode = LZWDecode(encode);
		long EndDecoding = System.nanoTime();

		
		double compressionRatio = (imgByte.length * 8) / (double) totalBits;

		// Print metrics - LZW
		String res="LZW Encoding/Decoding Metrics"+"\n";
		res=res+"-----------------------------"+"\n";
		res=res+"Original Image Bit Size: " + imgByte.length * 8+"\n";
		res=res+"Encoded Image Bit Size: " + totalBits+"\n";
		res=res+"Compression Ratio: " + compressionRatio + "\n";
		res=res+"Encoding Time: " + (endEncoding - startEncoding) / 1000000000.0 + " seconds"+"\n";
		res=res+"Decoding Time: " + (EndDecoding - startDecoding) / 1000000000.0 + " seconds\n";
		return res;
	}
	
	public static ArrayList<BitSet> LZWEncode(String input) {
	
		int sym = (int) Math.pow(2, 8);


		HashMap<String, BitSet> tablevalues = new HashMap<String, BitSet>();

		for (int i = 0; i < sym; i++) {
			BitSet values = convert(i);
			tablevalues.put("" + (char) i, values);
		}

		
		String str = "";

		ArrayList<BitSet> res = new ArrayList<BitSet>();

		for (char a : input.toCharArray()) {
			
			String stra = str + a;

			if (tablevalues.containsKey(stra)) {
				str = stra;
			} else {
				
				res.add(tablevalues.get(str));

			
				tablevalues.put(stra, convert(sym++));

				
				str = "" + a;
			}
		}

		if (!str.equals("")) {
			
			res.add(tablevalues.get(str));
		}
		return res;
	}

	public static String LZWDecode(ArrayList<BitSet> encode) {
		
		int sym = (int) Math.pow(2, 8);

	
		HashMap<BitSet, String> tablevalues = new HashMap<BitSet, String>();

		
		for (int i = 0; i < sym; i++) {
			tablevalues.put(convert(i), "" + (char) i);
		}

		String str = "" + (char) convert(encode.remove(0));
		StringBuffer res = new StringBuffer(str);

		
		for (BitSet b : encode) {
			String inp = null;

			if (tablevalues.containsKey(b)) {
				inp = tablevalues.get(b);
			} else if (convert(b) == sym) {
				
				inp = str + str.charAt(0);
			}
			
			res.append(inp);

			
			tablevalues.put(convert(sym++), str + inp.charAt(0));

			str = inp;
		}
		return res.toString();
	}
	public static void main(String[] args) throws IOException{
		BufferedImage originalImage = ImageIO.read(new File("F:/impro/lena512.jpg"));
		LZW(originalImage);
		
	}
	
	public static String test(File inputFile) throws IOException{
//		BufferedImage originalImage = ImageIO.read(new File("F:/impro/lena512.jpg"));
		BufferedImage originalImage = ImageIO.read(inputFile);
		return LZWNew(originalImage);
		
	}
}
