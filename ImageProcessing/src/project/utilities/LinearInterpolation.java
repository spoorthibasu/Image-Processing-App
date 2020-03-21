package project.utilities;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
public class LinearInterpolation {
    private static int get(int x, int n) {
        return (x >> (n * 8)) & 0xFF;
    }
 
    private static float lerp(float s, float e, float t) {
        return s + (e - s) * t;
    }
 
    private static BufferedImage scale(BufferedImage img, float scaleX, float scaleY,String a) {
        int newWidth = (int) (img.getWidth() * scaleX);
        int newHeight = (int) (img.getHeight() * scaleY);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, img.getType());
        for (int x = 0; x < newWidth; ++x) {
            for (int y = 0; y < newHeight; ++y) {
                float gx = ((float) x) / newWidth * (img.getWidth() - 1);
                float gy = ((float) y) / newHeight * (img.getHeight() - 1);
                int gxi = (int) gx;
                int gyi = (int) gy;
                int rgb = 0;
                int a00 = img.getRGB(gxi, gyi);
                int a10 = img.getRGB(gxi + 1, gyi);
                int a01 = img.getRGB(gxi, gyi + 1);
                int a11 = img.getRGB(gxi + 1, gyi + 1);
                for (int i = 0; i <= 2; ++i) {
                    float b00 = get(a00, i);
                    float b10 = get(a10, i);
                    float b01 = get(a01, i);
                    float b11 = get(a11, i);
                    int lex = ((int) lerp(b00, b10,gx - gxi) ) << (8 * i);
                    int ley = ((int) lerp(b01, b11,gy - gyi) ) << (8 * i);
                    
                    if(a=="x")
                    {
                        rgb = rgb | lex ;
                    }
                    if(a=="y")
                    {
                    rgb = rgb | ley ;
                    }
                }
                newImage.setRGB(x, y, rgb);
            }
        }
        return newImage;
    }
 
    public static void main(String[] args) throws IOException {
        File lenna = new File("F:/impro/small.jpg");
        BufferedImage image = ImageIO.read(lenna);
        BufferedImage image2 = scale(image, 3.125f, 3.125f,"x");
        File lenna2 = new File("F:/impro/outputlinearXXX.jpg");
        ImageIO.write(image2, "jpg", lenna2);
    }
    
    
    public static BufferedImage test(File inputFile,int wNew,int lNew,String type) throws IOException {
//        File lenna = new File("F:/impro/small.jpg");
    	File lenna =inputFile;
        BufferedImage image = ImageIO.read(lenna);
        BufferedImage image2 = scale(image, wNew/image.getWidth(),lNew/image.getHeight() ,type);
//        File lenna2 = new File("F:/impro/outputlinearXXX.jpg");
//        ImageIO.write(image2, "jpg", lenna2);
        return image2;
    }
}

