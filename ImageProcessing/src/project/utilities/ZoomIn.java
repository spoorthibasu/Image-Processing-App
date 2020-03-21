package project.utilities;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

public class ZoomIn extends JPanel {
BufferedImage image;
double scale = 1.0;

public ZoomIn(BufferedImage image) {
this.image = image;
}

protected void paintComponent(Graphics g) {
super.paintComponent(g);
Graphics2D g2 = (Graphics2D)g;
g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
RenderingHints.VALUE_INTERPOLATION_BICUBIC);
double x = (getWidth() - scale*image.getWidth())/2;
double y = (getHeight() - scale*image.getHeight())/2;
AffineTransform at = AffineTransform.getTranslateInstance(x,y);
at.scale(scale, scale);
g2.drawRenderedImage(image, at);
}

public Dimension getPreferredSize() {
int w = (int)(scale*image.getWidth());
int h = (int)(scale*image.getHeight());
return new Dimension(w, h);
}

private JSlider getSlider() {
int min = 1, max = 36, inc = 5;
final JSlider slider = new JSlider(min, max, 16);
slider.setMajorTickSpacing(5);
slider.setMinorTickSpacing(1);
slider.setPaintTicks(true);
slider.setSnapToTicks(true);
slider.setLabelTable(getLabelTable(min, max, inc));
slider.setPaintLabels(true);
slider.addChangeListener(new ChangeListener() {
public void stateChanged(ChangeEvent e) {
int value = slider.getValue();
scale = (value+4)/20.0;
revalidate();
repaint();
}
});
return slider;
}

private Hashtable getLabelTable(int min, int max, int inc) {
Hashtable table = new Hashtable();
for(int j = min; j <= max; j += inc)
{ 
	String s = String.format("%.2f", (j+4)/20.0); 
	table.put(Integer.valueOf(j), new JLabel(s));
} 
return table;
} 
public static void main(String[] args) throws IOException 
{
	String path = "F:/impro/output.jpg";
	BufferedImage image = ImageIO.read(new File(path)); 
	ZoomIn test = new ZoomIn(image); 
	JFrame f = new JFrame(); 
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	f.getContentPane().add(new JScrollPane(test)); 
	f.getContentPane().add(test.getSlider(), "Last"); 
	f.setSize(400,400); f.setLocation(200,200);
	f.setVisible(true);
	}

public static void test() throws IOException 
{
	String path = "F:/impro/output.jpg";
	BufferedImage image = ImageIO.read(new File(path)); 
	ZoomIn test = new ZoomIn(image); 
	JFrame f = new JFrame(); 
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	f.getContentPane().add(new JScrollPane(test)); 
	f.getContentPane().add(test.getSlider(), "Last"); 
	f.setSize(400,400); f.setLocation(200,200);
	f.setVisible(true);
	}


}
