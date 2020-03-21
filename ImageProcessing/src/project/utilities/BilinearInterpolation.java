package project.utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
public class BilinearInterpolation {

    private static int get(int x, int n) {
        return (x >> (n * 8)) & 0xFF;
    }
 
    private static float lerp(float s, float e, float t) {
        return s + (e - s) * t;
    }
 
    private static float blerp(final Float a00, float a10, float a01, float a11, float tx, float ty) {
        return lerp(lerp(a00, a10, tx), lerp(a01, a11, tx), ty);
    }
 
    private static BufferedImage scale(BufferedImage img, float scaleX, float scaleY) {
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
                    int ble = ((int) blerp(b00, b10, b01, b11, gx - gxi, gy - gyi)) << (8 * i);
                    rgb = rgb | ble;
                }
                newImage.setRGB(x, y, rgb);
            }
        }
        return newImage;
    }
 
    public static void main(String[] args) throws IOException {
        File lenna = new File("F:/impro/outputsmall.jpg");
        BufferedImage image = ImageIO.read(lenna);
        BufferedImage image2 = scale(image, 3.125f, 3.125f);
        File lenna2 = new File("F:/impro/outputbilinear.jpg");
        ImageIO.write(image2, "jpg", lenna2);
    }
    
    public static BufferedImage test(File input,int width,int height) throws IOException {
        BufferedImage image = ImageIO.read(input);
        BufferedImage image2 = scale(image, width/image.getWidth(), height/image.getHeight());
        return image2;
    }
}