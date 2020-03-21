package project.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class RLEGrayscale {	
	static String res="";
	public static int[][] getImgComp(int initImg[][]) {
		long sTime = 0L;
		long eTime = 0L;
		double encTime = 0.0;
		double decTime = 0.0;
		List<Integer> encoded = new ArrayList<>();
		int w = initImg.length;
		int h = initImg[0].length;
		double orgSize = w * h * 8.0;
		double compSize = 0.0;
		int decodedImage[][] = new int[initImg.length][initImg[0].length];
		
			
			sTime = System.currentTimeMillis();
			encoded = runLngthEncGscale(initImg);
			eTime = System.currentTimeMillis();
			compSize = encoded.size() * 8.0;
			encTime = eTime - sTime;
			
			
			sTime = System.currentTimeMillis();
			decodedImage = dcdeRLEGscle(encoded);
			eTime = System.currentTimeMillis();
			decTime = eTime - sTime;			
			System.out.printf("Encoding time taken: %.0f ms (%.3f s)\n", encTime, encTime / 1000.0);
			System.out.printf("Original bytes taken: %,.0f\n", orgSize / 8.0);
			System.out.printf("Compressed bytes taken: %,.0f\n", compSize / 8.0);
			System.out.printf("Compression ratio: %.4f (%.2f%%)\n", orgSize / compSize,
					orgSize * 100.0 / compSize);
			System.out.printf("Decoding time taken: %.0f ms (%.3f s)\n", decTime, decTime / 1000.0);
			
			
			res=res+"Encoding time taken: "+encTime+"ms "+(encTime / 1000.0)+"\n" ;
			res=res+"Original bytes taken: "+(orgSize / 8.0)+"+\n";
			res=res+"Compressed bytes taken: "+(compSize / 8.0)+"\n"  ;
			res=res+"Compression ratio: "+(orgSize / compSize)+" ("+(orgSize * 100.0 / compSize)+")"+"\n";
			res=res+"Decoding time taken: "+decTime+" ms ("+(decTime / 1000.0)+")\n" ;
			
			return decodedImage;
		}
	
		public static List<Integer> runLngthEncGscale(int initImg[][]) {
			
			List<Integer> encList = new ArrayList<> ();
			int w = initImg.length;
			int h = initImg[0].length;
			encList.add(w);
			encList.add(h);
			int counter = 0; 
			int prevGValue = initImg[0][0];
			
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					int curGValue = initImg[i][j];
					
					if (curGValue != prevGValue) {
						encList.add(counter); 
						encList.add(prevGValue);  
						
						prevGValue = curGValue;
						counter = 1; 
					}
					else {
						counter++; 
					}
				}
			}
			
			return encList;
		}
		
		
		public static int[][] dcdeRLEGscle(List<Integer> encList) {
			int s = encList.size();
			int hS = s >> 1;
			int w = encList.get(0); 
			int h = encList.get(1); 
			int ii = 0; 
			int jj = 0; 
			int decImg[][] = new int[w][h];
			
			for (int i = 1; i < hS; i++) {
				int occur = encList.get(i * 2);
				int gValue = encList.get((i * 2) + 1);
				for (int j = 0; j < occur; j++) {
					decImg[ii][jj] = gValue;
					jj += 1;
					if (jj >= h) {
						jj = 0;
						ii += 1;
					}
				}
			}
			
			return decImg;
		}
		
		public static void main(String[] args) throws IOException {
	        File lenna = new File("F:/impro/lena512.jpg");
	        BufferedImage image = ImageIO.read(lenna);
	        InitImage temp =new InitImage();
	        temp.setInitBufferedImage(image);
	        InitImage tempOut =new InitImage();
	        tempOut.setImageFromArray(getImgComp(temp.getImageArrayForm()));
	        BufferedImage image2 =  tempOut.getBufferedImage();
	        File lenna2 = new File("F:/impro/RLEGrayscale.jpg");
	        ImageIO.write(image2, "jpg", lenna2);
	    }
		
		public static Map<String,Object> test(File inputFile) throws IOException {
//	        File lenna = new File("F:/impro/lena512.jpg");
	        BufferedImage image = ImageIO.read(inputFile);
//	        BufferedImage image = ImageIO.read(lenna);
	        InitImage temp =new InitImage();
	        temp.setInitBufferedImage(image);
	        InitImage tempOut =new InitImage();
	        tempOut.setImageFromArray(getImgComp(temp.getImageArrayForm()));
	        BufferedImage image2 =  tempOut.getBufferedImage();
	        File lenna2 = new File("F:/impro/RLEGrayscale.jpg");
	        ImageIO.write(image2, "jpg", lenna2);
	        Map<String,Object> map =new HashMap<String, Object>();
	        map.put("image", image2);
	        map.put("message", res);
	        return map;
	    }

}
