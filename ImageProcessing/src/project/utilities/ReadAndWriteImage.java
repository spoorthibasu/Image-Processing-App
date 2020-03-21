package project.utilities;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
 
public class ReadAndWriteImage
{   
  
	public static void main(String args[])throws IOException{
    BufferedImage img = null;
    File f = null;

    //read image
    try{
      f = new File("F:/impro/lena512.jpg");
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
        
        System.out.println(a);
      }
    }
    
 //write image
    try{
      f = new File("F:/impro/output6.jpg");
      ImageIO.write(img, "jpg", f);
//      ImageIO.write(imgNew, "jpg", f);
    }catch(IOException e){
      System.out.println(e);
    }
  }//main() ends here

	public static BufferedImage test(File input,int hex)throws IOException{
	    BufferedImage img = null;
	    File f = null;
	    

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
	        int a = (p>>24)&hex;
	        int r = (p>>16)&hex;
	        int g = (p>>8)&hex;
	        int b = p&hex;

	        //calculate average
	        int avg = (r+g+b)/3;

	        //replace RGB value with avg
	        p = (a<<24) | (avg<<16) | (avg<<8) | avg;
	        img.setRGB(x, y, p);
	      }
	    }
	    
	    return img;
	  }//main() ends here

public static BufferedImage makeSmall(File input, int finalWidth, int finalHeight) {

    BufferedImage img = null;
    File f = null;
    

    //read image
    try{
    	f=input;
      img = ImageIO.read(f);
    }catch(IOException e){
      System.out.println(e);
    }
	
    int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    BufferedImage small = img;
    BufferedImage scratchedImage = null;
    Graphics2D g2 = null;

    int width = img.getWidth();
    int height = img.getHeight();
    int prevW = width;
    int prevH = height;

    do {
        if (width > finalWidth) {
            width /= 2;
            width = (width < finalWidth) ? finalWidth : width;
        }

        if (height > finalHeight) {
            height /= 2;
            height = (height < finalHeight) ? finalHeight : height;
        }

        if (scratchedImage == null) {
        	scratchedImage = new BufferedImage(width, height, type);
            g2 = scratchedImage.createGraphics();
        }

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(small, 0, 0, width, height, 0, 0, prevW, prevH, null);

        prevW = width;
        prevH = height;
        small = scratchedImage;
    } while (width != finalWidth || height != finalHeight);

    if (g2 != null) {
        g2.dispose();
    }

    if (finalWidth != small.getWidth() || finalHeight != small.getHeight()) {
    	scratchedImage = new BufferedImage(finalWidth, finalHeight, type);
        g2 = scratchedImage.createGraphics();
        g2.drawImage(small, 0, 0, null);
        g2.dispose();
        small = scratchedImage;
    }

    return small;

}
}//class ends here