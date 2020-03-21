package project.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
 
public class NearestNeighbor
{   
	public static int[] convertToPixels(Image img) {
	    int width = img.getWidth(null);
	    int height = img.getHeight(null);
	    int[] pixel = new int[width * height];

	    PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixel, 0, width);
	    try {
	      pg.grabPixels();
	    } catch (InterruptedException e) {
	      throw new IllegalStateException("Error: Interrupted Waiting for Pixels");
	    }
	    if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
	      throw new IllegalStateException("Error: Image Fetch Aborted");
	    }
	    return pixel;
	  }

	public static int[] resizePixels(int[] pixels,int originalwidth,int originalheight,int finalwidth,int finalheight) {
	    int[] temp = new int[finalwidth*finalheight] ;
	    double x_ratio = originalwidth/(double)finalwidth ;
	    double y_ratio = originalheight/(double)finalheight ;
	    double px, py ; 
	    for (int i=0;i<finalheight;i++) {
	        for (int j=0;j<finalwidth;j++) {
	            px = Math.floor(j*x_ratio) ;
	            py = Math.floor(i*y_ratio) ;
	            temp[(i*finalwidth)+j] = pixels[(int)((py*originalwidth)+px)] ;
	        }
	    }
	    return temp ;
	}
	
	public static Image getImageFromArray(int[] pixels, int width, int height) throws IOException {
	    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    WritableRaster raster = image.getRaster();
	    raster.setDataElements(0, 0, width, height, pixels); 
	    System.out.print("written");
	    return image;
	}	
	
	public static BufferedImage test(File input,int finalwidth,int finalheight) {
		BufferedImage img = null;
		   
	    File f = null;
	    ArrayList<Integer> myList = new ArrayList<Integer>();
	    

	    //read image
	    try{
	    	f=input;
	      img = ImageIO.read(f);
	    }catch(IOException e){
	      System.out.println(e);
	    }
	    
	  //get image width and height
	    int width = img.getWidth();
	    int height = img.getHeight();
	    System.out.println("height "+height+"width "+width);
	    //convert to grayscale
	    
	    for(int y = 0; y < height; y++){
	      for(int x = 0; x < width; x++){
	        int p = img.getRGB(x,y);

	        int a = (p>>24)&0xff;
	        int r = (p>>16)&0xff;
	        int g = (p>>8)&0xff;
	        int b = p&0xff;

	        //calculate average
	        int avg = (r+g+b)/3;

	        //replace RGB value with avg
	        p = (a<<24) | (avg<<16) | (avg<<8) | avg;
	        myList.add(p);         
	        img.setRGB(x, y, p);
	      }
	    }
	  
		int[] pixels =convertToPixels(img);
		int[] newPixels= resizePixels(pixels,width,height,finalwidth,finalheight);
		try {
			img=  (BufferedImage) getImageFromArray(newPixels,finalwidth,finalheight);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	
	
	public static void main(String args[])throws IOException{
    BufferedImage img = null;  
    File f = null;
 
    //read image
    try{
      f = new File("F:/impro/outputsmall.jpg");
      img = ImageIO.read(f);
    }catch(IOException e){
      System.out.println(e);
    }

  //get image width and height
    int width = img.getWidth();
    int height = img.getHeight();
    System.out.println("height "+height+"width "+width);
    //convert to grayscale
    
    for(int y = 0; y < height; y++){
      for(int x = 0; x < width; x++){
        int p = img.getRGB(x,y);

        int a = (p>>24)&0xff;
        int r = (p>>16)&0xff;
        int g = (p>>8)&0xff;
        int b = p&0xff;

        //calculate average
        int avg = (r+g+b)/3;

        //replace RGB value with avg
        p = (a<<24) | (avg<<16) | (avg<<8) | avg;        
        img.setRGB(x, y, p);
      }
    }
  
	int[] pixels =convertToPixels(img);
	int[] newPixels= resizePixels(pixels,width,height,100,100);
	img=  (BufferedImage) getImageFromArray(newPixels,100,100);
	
 //write image
    try{
      f = new File("F:/impro/outputlargeneighbor.jpg");
      ImageIO.write(img, "jpg", f);
    }catch(IOException e){
      System.out.println(e);
    }
  }
	
}