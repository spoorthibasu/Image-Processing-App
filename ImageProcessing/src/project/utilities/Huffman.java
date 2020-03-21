package project.utilities;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;

public class Huffman {
	public static final int MAX_GRAYSCALE_VALUE = 255;
	public static final int NOT_A_GRAY_VALUE = -1;
	private static HuffmanNode rootNode = null;
	static String res="";
	
	public static int[][] getImageCompression(int initImg[][]) {
		long sTime = 0L;
		long eTime = 0L;
		double encTime = 0.0;
		double decTime = 0.0;
		String huffEnc[];
		int w = initImg.length;
		int h = initImg[0].length;
		double origS = w * h * 8.0;
		double compS = 0.0;
		int decImg[][] = new int[initImg.length][initImg[0].length];
		
		sTime = System.currentTimeMillis();
		huffEnc = huffenCde(initImg);
		HuffmanNode rootNode = getRootNode();
		eTime = System.currentTimeMillis();
		compS = getTtlHuffLeng(huffEnc); 
		encTime = eTime - sTime;
		
		
		sTime = System.currentTimeMillis();
		decImg = decHuff(huffEnc, rootNode);
		eTime = System.currentTimeMillis();
		decTime = eTime - sTime;
		
		res=res+"Encoding time taken: "+encTime+" ms ("+(encTime / 1000.0)+"s)\n";
		res=res+"Original bytes taken:  "+(origS / 8.0)+"\n";
		res=res+"Compressed bytes taken: "+(compS / 8.0)+"\n";
		res=res+"Compression ratio: "+(origS / compS)+" ("+origS * 100.0 / compS+"%)\n";
		res=res+"Decoding time taken: "+decTime+" ms ("+decTime / 1000.0+"s)\n";
		
		return decImg;
	}
	
	public static String[] huffenCde(int initImg[][]) {		
		String huffCdes[] = getHuffLeng(initImg);
		
		int w = initImg.length;
		int h = initImg[0].length;
		String encArr[] = new String[w + 1]; 
		encArr[0] = String.valueOf(initImg.length) + "," + String.valueOf(initImg[0].length);
		
		for (int i = 0; i < w; i++) {
			StringBuilder temp = new StringBuilder();
			for (int j = 0; j < h; j++) {
				int curGVal = initImg[i][j];
				temp.append(huffCdes[curGVal]);
			}
			encArr[i + 1] = temp.toString();
		}
		
		return encArr;
	}
	
	public final static int getTtlHuffLeng(String encoded[]) {
		int counter = 0;
		for (String s : encoded) {
			counter += s.length();
		}
		
		return counter;
	}
	
	public static String[] getHuffLeng(int initImg[][]) {
		int freqArr[] = freqCdng(initImg);
		PriorityQueue<HuffmanNode> pQueue = new PriorityQueue<>();
		
		for (int i = 0; i < freqArr.length; i++) {
			if (freqArr[i] > 0) {
				pQueue.add(new HuffmanNode(i, freqArr[i]));
			}
		}
	
		HuffmanNode hNodes[] = new HuffmanNode[MAX_GRAYSCALE_VALUE + 1];
		for (int i = 0; i < hNodes.length; i++) {
			hNodes[i] = null;
		}
		
		// Huffman algorithm
		while (pQueue.size() > 1) {
			HuffmanNode nodeLeft = pQueue.poll();
			HuffmanNode nodeRight = pQueue.poll();
			
			nodeLeft.setBitCode(0);
			nodeRight.setBitCode(1);
			
			int tempGVal;
			if ((tempGVal = nodeLeft.getGrayValue()) != NOT_A_GRAY_VALUE) {
				hNodes[tempGVal] = nodeLeft;
			}
			if ((tempGVal = nodeRight.getGrayValue()) != NOT_A_GRAY_VALUE) {
				hNodes[tempGVal] = nodeRight;
			}
			
			int newFrequency = nodeLeft.getFrequency() + nodeRight.getFrequency();
			
			HuffmanNode newNode = new HuffmanNode(NOT_A_GRAY_VALUE, newFrequency);
			newNode.setLeftChild(nodeLeft);
			newNode.setRightChild(nodeRight);
			
			nodeLeft.setParentNode(newNode);
			nodeRight.setParentNode(newNode);
			
			pQueue.add(newNode); 
		}
		
	
		Huffman.rootNode = pQueue.poll();
			
		String bitCdeArr[] = new String[MAX_GRAYSCALE_VALUE + 1];
		for (int i = 0; i < bitCdeArr.length; i++) {
			if (hNodes[i] != null) {
				String bitCodeStr = getBitCde(hNodes[i]);
				bitCdeArr[i] = bitCodeStr;
			}
			else {bitCdeArr[i] = "";}
		}
		
		return bitCdeArr;
	}
	
	
	private static int[] freqCdng(int initImg[][]) {
		int freqArr[] = new int[MAX_GRAYSCALE_VALUE + 1];
		for (int i = 0; i < freqArr.length; i++) {
			freqArr[i] = 0;
		}
		
		int w = initImg.length;
		int h = initImg[0].length;
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int curGVal = initImg[i][j];
				freqArr[curGVal] += 1;
			}
		}
		
		return freqArr;
	}
	
	private static String getBitCde(HuffmanNode curNode) {
		StringBuilder bitCde = new StringBuilder();

		while (curNode.getParentNode() != null) {
			bitCde.append(String.valueOf(curNode.getBitCode()));
			curNode = curNode.getParentNode();
		}
		
		bitCde = bitCde.reverse();
		
		return bitCde.toString();
	}
	
	public static int[][] decHuff(String input[], HuffmanNode rootNode) {
		String widthheight = input[0];
		String temp[] = widthheight.split(",");
		int w = Integer.parseInt(temp[0]);
		int h = Integer.parseInt(temp[1]);
		int convertedImage[][] = new int[w][h];
		
		for (int i = 0; i < w; i++) {
			convertedImage[i] = decodeHuffmanHelper(input[i + 1], rootNode, h);
		}
		
		return convertedImage;
	}
	

	private static int[] decodeHuffmanHelper(String input, HuffmanNode rootNode, int totalLength) {
		int output[] = new int[totalLength];
		int outputIndex = 0;
		HuffmanNode curNode = rootNode;
		
		for (int i = 0; i < input.length(); i++) {
			char curChar = input.charAt(i);
			
			if (curNode.isLeafNode()) {
				int grayValue = curNode.getGrayValue();
				output[outputIndex++] = grayValue;
				
				
				curNode = rootNode;
			}
			
			if (curChar == '0') {
				curNode = curNode.getLeftChild();
			}
			else {
				curNode = curNode.getRightChild();
			}
		}
		
		return output;
	}
	
	
	public static HuffmanNode getRootNode() {return Huffman.rootNode;}
	
	public static void main(String[] args) throws IOException {
        File lenna = new File("F:/impro/lena512.jpg");
        BufferedImage image = ImageIO.read(lenna);
        InitImage temp =new InitImage();
        temp.setInitBufferedImage(image);
        InitImage tempOut =new InitImage();
        tempOut.setImageFromArray(getImageCompression(temp.getImageArrayForm()));
        BufferedImage image2 =  tempOut.getBufferedImage();
        File lenna2 = new File("F:/impro/HUffman.jpg");
        ImageIO.write(image2, "jpg", lenna2);
    }
	
	public static Map<String,Object> test(File inputFile) throws IOException {
//        File lenna = new File("F:/impro/lena512.jpg");
//        BufferedImage image = ImageIO.read(lenna);
		BufferedImage image = ImageIO.read(inputFile);
        InitImage temp =new InitImage();
        temp.setInitBufferedImage(image);
        InitImage tempOut =new InitImage();
        tempOut.setImageFromArray(getImageCompression(temp.getImageArrayForm()));
        BufferedImage image2 =  tempOut.getBufferedImage();
        File lenna2 = new File("F:/impro/HUffman.jpg");
        ImageIO.write(image2, "jpg", lenna2);
        Map<String,Object> map =new HashMap<String, Object>();
        map.put("image", image2);
        map.put("message", res);
        return map;
    }
}
