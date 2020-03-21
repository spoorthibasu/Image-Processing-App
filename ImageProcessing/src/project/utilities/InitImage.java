package project.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class InitImage {
	private int currBit;
	private BufferedImage buffImg;
	private int imageArray[][];
	

	public int[][] getImageArrayForm() {
		return imageArray;
	}
	
	public int getCurrentBit() {
		return currBit;
	}

	public void setCurrentBit(int currentBit) {
		this.currBit = currentBit;
	}

	public int getWidth() {
		return buffImg.getWidth();
	}

	public int getHeight() {
		return buffImg.getHeight();
	}

	public BufferedImage getBufferedImage() {
		return buffImg;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.buffImg = bufferedImage;
	}
	
	public void setInitBufferedImage(BufferedImage loadedImg) {
		int width = loadedImg.getWidth();
		int height = loadedImg.getHeight();
		this.buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.imageArray = new int[width][height];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Color c = new Color(loadedImg.getRGB(i, j));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();

				int gr = (r + g + b) / 3;
				imageArray[i][j] = gr;

				Color gColor = new Color(gr, gr, gr);						
				this.buffImg.setRGB(i, j, gColor.getRGB());
			}
		}
	}
	
	public void setImageFromArray(int arrayForm[][]) {
		int width = arrayForm.length;
		int height = arrayForm[0].length;
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.imageArray= new int[width][height];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int gr = arrayForm[i][j];
				this.imageArray[i][j] = gr;

				Color gColor = new Color(gr, gr, gr);
				temp.setRGB(i, j, gColor.getRGB());
			}
		}
		
		this.buffImg = temp;
	}

	public ImageIcon getImageIcon() {
		return new ImageIcon(this.buffImg);
	}	
}

