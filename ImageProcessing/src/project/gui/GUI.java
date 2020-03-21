package project.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import project.utilities.AdaptiveHistogramEqualisation;
import project.utilities.BilinearInterpolation;
import project.utilities.BitPlane;
import project.utilities.GlobalHistogramEqualization;
import project.utilities.HighBoost;
import project.utilities.Huffman;
import project.utilities.LZWEnDecode;
import project.utilities.SharpeningLaplacian;
import project.utilities.LinearInterpolation;
import project.utilities.Median;
import project.utilities.ZoomIn;
import project.utilities.ArithmeticMean;
import project.utilities.GeometricMean;
import project.utilities.HarmonicMean;
import project.utilities.ContraharmonicMean;
import project.utilities.Max;
import project.utilities.Min;
import project.utilities.Midpoint;
import project.utilities.AlphaTrimmed;
import project.utilities.GaussianNoise;

import project.utilities.NearestNeighbor;
import project.utilities.RLEBits;
import project.utilities.RLEGrayscale;
import project.utilities.ReadAndWriteImage;
import project.utilities.SmoothingFilter;


public class GUI extends JFrame implements ActionListener{
	JMenuBar menuBar;
	JMenu fileMenu,editMenu,sizeMenu,typeMenu,bitMenu,filterMenu,bitPlaneMenu,histogramMenu,restorationMenu,
	imageCompression;
	JMenuItem importImage,size,
	linearInterpolation,linearTypeX,linearTypeY,medainFilter,kernel,alphavalue,
	smoothingFilter,bitPlane,histogram,aHEEM,
	highBoost,lapLacian,arithmeticMean,geometricMean,
	harmonicMean,relegScale,huffman,lzwenDecode,rleBits,contraharmonicMean,max,min,midpoint,alpha,gaussiannoise,zoom,
	bitType1,bitType2,bitType3,bitType4,bitType5,bitType6,bitType7,bitType8,
	exportImage,nearestNeighbour,bilinear,grayScale,makeSmall;	
	JFileChooser fileSelection;											
	JLabel inputDisplay,outputDisplay,log;								
	File inputFile,outputFile,outputHistogramFile;				
	String output;														
	ImageFilter imageFilter;
	int width,height,maskSize,alphaval,startPlane,endPlane,hb,hex;
	String type;
	BufferedImage imgOut;
	AdaptiveHistogramEqualisation adaptiveHistogramEqualisation;
	
	public static void main(String args[]){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new GUI();
		} 
		catch (Exception e){
			System.err.println("Something went wrong with L&F");
		}		
	}

	public GUI(){	

		
		adaptiveHistogramEqualisation=new AdaptiveHistogramEqualisation();
		imageFilter=new ImageFilter();
		new BorderLayout();
		fileSelection=new JFileChooser();
		fileSelection.setAcceptAllFileFilterUsed(false);

		importImage=new JMenuItem("Import image");
		exportImage=new JMenuItem("Export image");
		importImage.addActionListener(this);
		exportImage.addActionListener(this);
		fileMenu=new JMenu("File");
		fileMenu.add(importImage);
		fileMenu.add(exportImage);
		typeMenu=new JMenu("Axis");
		bitMenu=new JMenu("Bit Value");
		arithmeticMean= new JMenuItem("Arithmetic Mean");
		geometricMean= new JMenuItem("Geometric Mean");
		harmonicMean= new JMenuItem("Harmonic Mean");
		rleBits = new JMenuItem("RLE Bits");
		relegScale = new JMenuItem("RLE Grayscale");
		huffman = new JMenuItem("Huffman");
		lzwenDecode = new JMenuItem("LZWEnDecode");
		contraharmonicMean= new JMenuItem("Contra Harmonic Mean");
		max= new JMenuItem("Max");
		min= new JMenuItem("Min");
		midpoint= new JMenuItem("Midpoint");
		alpha= new JMenuItem("Alpha-Trimmed");
		gaussiannoise= new JMenuItem("Gaussian Noise");
		medainFilter=new JMenuItem("Median");
		smoothingFilter=new JMenuItem("Smoothing");
		bitPlane = new JMenuItem("Bit plane");
		histogram = new JMenuItem("Global Histogram Equalization");
		aHEEM=new JMenuItem("Local Histogram Equalization");
		highBoost = new JMenuItem("High Boost");
		lapLacian = new JMenuItem("Sharpening Laplicican");
		medainFilter.addActionListener(this);
		smoothingFilter.addActionListener(this);
		lapLacian.addActionListener(this);
		highBoost.addActionListener(this);
		arithmeticMean.addActionListener(this);
		geometricMean.addActionListener(this);
		harmonicMean.addActionListener(this);
		relegScale.addActionListener(this);
		huffman.addActionListener(this);
		lzwenDecode.addActionListener(this);
		rleBits.addActionListener(this);
		contraharmonicMean.addActionListener(this);
		max.addActionListener(this);
		min.addActionListener(this);
		midpoint.addActionListener(this);
		alpha.addActionListener(this);
		gaussiannoise.addActionListener(this);
		filterMenu=new JMenu("Filter");
		restorationMenu=new JMenu("Restoration");
		imageCompression=new JMenu("Image Compression");
		bitPlaneMenu=new JMenu("Bit Plane");
		histogramMenu=new JMenu("Histogram");
		filterMenu.add(smoothingFilter);
		filterMenu.add(medainFilter);
		filterMenu.add(lapLacian);
		filterMenu.add(highBoost);
		imageCompression.add(relegScale);
		imageCompression.add(rleBits);
		imageCompression.add(huffman);
		imageCompression.add(lzwenDecode);
		restorationMenu.add(arithmeticMean);
		restorationMenu.add(geometricMean);
		restorationMenu.add(harmonicMean);
		restorationMenu.add(contraharmonicMean);
		restorationMenu.add(max);
		restorationMenu.add(min);
		restorationMenu.add(midpoint);
		restorationMenu.add(alpha);
		restorationMenu.add(gaussiannoise);
		bitPlane.addActionListener(this);
		histogram.addActionListener(this);
		aHEEM.addActionListener(this);
		bitPlaneMenu.add(bitPlane);
		histogramMenu.add(histogram);
		histogramMenu.add(aHEEM);
		nearestNeighbour=new JMenuItem("Nearest NeighBour");
		bilinear=new JMenuItem("Bilinear Interpolation");
		size=new JMenuItem("Enter width and height");
		kernel=new JMenuItem("Mask Size");
		alphavalue= new JMenuItem("Alpha");
		grayScale=new JMenuItem("Gray Level Resolution");
		makeSmall=new JMenuItem("Make Small");
		sizeMenu=new JMenu("Size");
		sizeMenu.add(size);
		sizeMenu.add(kernel);
		sizeMenu.add(alphavalue);
		bitType1=new JMenuItem("1");
		bitType2=new JMenuItem("2");
		bitType3=new JMenuItem("3");
		bitType4=new JMenuItem("4");
		bitType5=new JMenuItem("5");
		bitType6=new JMenuItem("6");
		bitType7=new JMenuItem("7");
		bitType8=new JMenuItem("8");
		
		bitType1.addActionListener(this);
		bitType2.addActionListener(this);
		bitType3.addActionListener(this);
		bitType4.addActionListener(this);
		bitType5.addActionListener(this);
		bitType6.addActionListener(this);
		bitType7.addActionListener(this);
		bitType8.addActionListener(this);
		
		bitMenu.add(bitType1);
		bitMenu.add(bitType2);
		bitMenu.add(bitType3);
		bitMenu.add(bitType4);
		bitMenu.add(bitType5);
		bitMenu.add(bitType6);
		bitMenu.add(bitType7);
		bitMenu.add(bitType8);
		
		linearTypeX=new JMenuItem("X");
		linearTypeY=new JMenuItem("Y");
		linearInterpolation =new JMenuItem("Linear InterPolation"); 
		zoom =new JMenuItem("Zoom"); 
		linearTypeX.addActionListener(this);
		linearTypeY.addActionListener(this);
		typeMenu.add(linearTypeX);
		typeMenu.add(linearTypeY);
		nearestNeighbour.addActionListener(this);
		bilinear.addActionListener(this);
		size.addActionListener(this);
		kernel.addActionListener(this);
		alphavalue.addActionListener(this);
		grayScale.addActionListener(this);
		makeSmall.addActionListener(this);
		linearInterpolation.addActionListener(this);
		zoom.addActionListener(this);
		editMenu=new JMenu("Edit");
		editMenu.add(makeSmall);
		editMenu.add(zoom);
		editMenu.add(nearestNeighbour);
		editMenu.add(linearInterpolation);
		editMenu.add(bilinear);
		editMenu.add(grayScale);
		
		menuBar=new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(typeMenu);
		menuBar.add(histogramMenu);
		menuBar.add(filterMenu);
		menuBar.add(restorationMenu);
		menuBar.add(imageCompression);
		//menuBar.add(sizeMenu);
		menuBar.add(bitPlaneMenu);
		menuBar.add(bitMenu);
		
		inputDisplay=new JLabel();
		inputDisplay.setPreferredSize(new Dimension(600,600));
		outputDisplay=new JLabel();
		outputDisplay.setPreferredSize(new Dimension(600,600));

		log=new JLabel(" No file chosen");
		log.setBorder(BorderFactory.createLineBorder(Color.black));

		this.add(menuBar,BorderLayout.PAGE_START);		
		this.add(inputDisplay,BorderLayout.LINE_START);	
		this.add(outputDisplay,BorderLayout.LINE_END);	
		this.add(log,BorderLayout.PAGE_END);

		this.setVisible(true);
		this.setSize(1200,700);
		this.setTitle("Image Processing by Spoorthi Basu");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void setInputImage(BufferedImage inputImageDisplay,int width,int height){
		try{

			inputDisplay.setIcon(new ImageIcon(inputImageDisplay.getScaledInstance(inputImageDisplay.getWidth(),inputImageDisplay.getHeight(),Image.SCALE_SMOOTH)));
		}
		catch(NullPointerException nPE){	
			log.setText("Invalid file: "+inputFile.getName());
			inputFile=null;
		}
	}

	public void setOutputImage(BufferedImage outputImageDisplay,int width,int height){
		float resultWidth=width,resultHeight=height;
		outputDisplay.setIcon(new ImageIcon(outputImageDisplay.getScaledInstance((int)resultWidth,(int)resultHeight,Image.SCALE_SMOOTH)));
	}

	
	
	public static int getFrameSize(){
		int frameSize=0;
		boolean valid=false;

		while(valid==false){
			try{
				frameSize=Integer.parseInt(JOptionPane.showInputDialog(null,"Enter a frame size for local histogram equalisation\n[Cannot be smaller than "+AdaptiveHistogramEqualisation.MINIMUM_FRAME_SIZE+" or greater than "+AdaptiveHistogramEqualisation.MAXIMUM_FRAME_SIZE+"]\nWarning: the larger the frame, the longer it will take\n","Enter a value",JOptionPane.QUESTION_MESSAGE));	//	Converting entered value to an int
				if((frameSize<AdaptiveHistogramEqualisation.MINIMUM_FRAME_SIZE)||(frameSize>AdaptiveHistogramEqualisation.MAXIMUM_FRAME_SIZE)){
					JOptionPane.showMessageDialog(null,"Invalid value","Value disallowed",JOptionPane.ERROR_MESSAGE);
				}
				else{
					valid=true;
				}
			}
			catch(NumberFormatException nFE){
				System.out.println("GUI: parameter cannot be null");
			}
		}		
		return frameSize;
	}
	
	public static int getThreshold(boolean adaptive){
		int threshold=100;
		boolean valid=false;
		while(valid==false){
			try{
				if(adaptive){
					threshold=Integer.parseInt(JOptionPane.showInputDialog(null,"Enter a threshold percent for contrast limited adaptive histogram equalisation\nThe value must be between 0 & 100.","Enter a threshold",JOptionPane.QUESTION_MESSAGE));	//	Converting entered value to an int
				}
				else{
					threshold=Integer.parseInt(JOptionPane.showInputDialog(null,"Enter a threshold percent for contrast limited histogram equalisation\nThe value must be between 0 & 100.","Enter a threshold",JOptionPane.QUESTION_MESSAGE));	//	Converting entered value to an int
				}
				if((threshold<0)||(threshold>100)){
					JOptionPane.showMessageDialog(null,"Invalid value","Value disallowed",JOptionPane.ERROR_MESSAGE);
				}
				else{
					valid=true;
				}
			}
			catch(NumberFormatException nFE){
				System.out.println("GUI: parameter cannot be null");
			}
		}		
		return threshold;
	}
	
	@Override
	public void actionPerformed(ActionEvent aE){
		try{
			if(aE.getSource()==importImage){		
				fileSelection.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileSelection.setAccessory(new ImagePreview(fileSelection));
				fileSelection.setFileFilter(imageFilter);
				int option=fileSelection.showOpenDialog(this);
				if(option==JFileChooser.APPROVE_OPTION){
					inputFile=fileSelection.getSelectedFile();
					BufferedImage inputBufferedImage=ImageIO.read(inputFile);
					this.setInputImage(inputBufferedImage,inputBufferedImage.getWidth(),inputBufferedImage.getHeight());					
					log.setText(" File chosen: "+inputFile.getName());
					try{
						this.setOutputImage(null,0,0);
					}
					catch(NullPointerException nPE){
						System.err.println("GUI: null setting of output image");
					}
				}
			}
			if((aE.getSource()==exportImage)){	
			    try{
			    File  f = new File("F:/impro/output.jpg");
			      ImageIO.write(imgOut, "jpg", f);
			    }catch(IOException e){
			      System.out.println(e);
			    }
			}
			
			 if(inputFile!=null){

				if(aE.getSource()==linearTypeX) {
					type="x";
				}
				if(aE.getSource()==linearTypeY) {
					type="y";
				}
				if(aE.getSource()==bitType1) {
					hex=0x01;
				}
			    if(aE.getSource()==bitType2) {
					hex=0x03;
				}
			    if(aE.getSource()==bitType3) {
					hex=0x07;
				}
			    if(aE.getSource()==bitType4) {
					hex=0x0f;
				}
			    if(aE.getSource()==bitType5) {
					hex=0x1f;
				}
			    if(aE.getSource()==bitType6) {
					hex=0x41;
				}
			    if(aE.getSource()==bitType7) {
					hex=0x7f;
				}
			    if(aE.getSource()==bitType8) {
					hex=0xff;
				}
				if(aE.getSource()==nearestNeighbour){	
					String num1,num2;					
					num1 = JOptionPane.showInputDialog("Enter Width");
					width = Integer.parseInt(num1);
					num2 = JOptionPane.showInputDialog("Enter Height");
					height = Integer.parseInt(num2);

				BufferedImage imgOut=NearestNeighbor.test(inputFile,width,height);
				this.setOutputImage	(imgOut,imgOut.getWidth(),imgOut.getHeight());
				}
				else if(aE.getSource()==arithmeticMean){
					String num3;					
					num3 = JOptionPane.showInputDialog("Enter Mask Size");
					maskSize = Integer.parseInt(num3);
					BufferedImage imgOut = ArithmeticMean.test(inputFile,maskSize);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==geometricMean){	
					String num3;					
					num3 = JOptionPane.showInputDialog("Enter Mask Size");
					maskSize = Integer.parseInt(num3);
					BufferedImage imgOut = GeometricMean.test(inputFile,maskSize);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==harmonicMean){		
					String num3;					
					num3 = JOptionPane.showInputDialog("Enter Mask Size");
					maskSize = Integer.parseInt(num3);
					BufferedImage imgOut = HarmonicMean.test(inputFile,maskSize);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				
				else if(aE.getSource()==relegScale){		
//					String num3;					
//					num3 = JOptionPane.showInputDialog("Enter Mask Size");
//					maskSize = Integer.parseInt(num3);
					Map<String,Object> map= RLEGrayscale.test(inputFile);
					BufferedImage imgOut = (BufferedImage) map.get("image");
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
					String res = (String) map.get("message");
					JOptionPane.showMessageDialog(null,res);
				}
				
				else if(aE.getSource()==huffman){		
//					String num3;					
//					num3 = JOptionPane.showInputDialog("Enter Mask Size");
//					maskSize = Integer.parseInt(num3);
					Map<String,Object> map= Huffman.test(inputFile);
					BufferedImage imgOut = (BufferedImage) map.get("image");
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
					String res = (String) map.get("message");
					JOptionPane.showMessageDialog(null,res);
				}
				

				
				else if(aE.getSource()==lzwenDecode){		
//					String num3;					

//					maskSize = Integer.parseInt(num3);
//					BufferedImage imgOut = 
					String res = LZWEnDecode.test(inputFile);
					JOptionPane.showMessageDialog(null,res);
//					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
//					this.imgOut=imgOut;
				}
				
				else if(aE.getSource()==rleBits){		
//					String num3;					

//					maskSize = Integer.parseInt(num3);
//					BufferedImage imgOut = 
					String res = RLEBits.test(inputFile);
					JOptionPane.showMessageDialog(null,res);
//					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
//					this.imgOut=imgOut;
				}
				
				else if(aE.getSource()==contraharmonicMean){
					String num3;					
					num3 = JOptionPane.showInputDialog("Enter Mask Size");
					maskSize = Integer.parseInt(num3);
					BufferedImage imgOut = ContraharmonicMean.test(inputFile, maskSize);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==max){		
					String num3;					
					num3 = JOptionPane.showInputDialog("Enter Mask Size");
					maskSize = Integer.parseInt(num3);
					BufferedImage imgOut = Max.test(inputFile, maskSize);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==min){		
					String num3;					
					num3 = JOptionPane.showInputDialog("Enter Mask Size");
					maskSize = Integer.parseInt(num3);
					BufferedImage imgOut = Min.test(inputFile, maskSize);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==midpoint){		
					String num3;					
					num3 = JOptionPane.showInputDialog("Enter Mask Size");
					maskSize = Integer.parseInt(num3);
					BufferedImage imgOut = Midpoint.test(inputFile, maskSize);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==alpha){		
					String num1,num2;					
					num1 = JOptionPane.showInputDialog("Enter Mask Size");
					maskSize = Integer.parseInt(num1);
					num2 = JOptionPane.showInputDialog("Enter Alpha Value");
					alphaval = Integer.parseInt(num2);
					BufferedImage imgOut = AlphaTrimmed.test(inputFile, maskSize,alphaval);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==gaussiannoise){		
					BufferedImage imgOut = GaussianNoise.test(inputFile);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==smoothingFilter){		
					String num3;					
					num3 = JOptionPane.showInputDialog("Enter Mask Size");
					maskSize = Integer.parseInt(num3);
					BufferedImage imgOut = SmoothingFilter.test(inputFile,maskSize);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==medainFilter){	
					String num3;					
					num3 = JOptionPane.showInputDialog("Enter Mask Size");
					maskSize = Integer.parseInt(num3);
					BufferedImage imgOut = Median.test(inputFile, maskSize);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==bitPlane){		
					String num1,num2;					
					num1 = JOptionPane.showInputDialog("Enter Start Bit Plane");
					startPlane = Integer.parseInt(num1);
					num2 = JOptionPane.showInputDialog("Enter End Bit Plane");
					endPlane = Integer.parseInt(num2);
					BufferedImage imgOut = BitPlane.test(inputFile, startPlane,endPlane);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				
				else if(aE.getSource()==histogram){		
					BufferedImage imgOut = GlobalHistogramEqualization.test(inputFile);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				
				else if(aE.getSource()==aHEEM){		//	Adaptive histogram equalisation
					log.setText(" Please wait, perfoming adaptive histogram equalisation. This may take a long time, due to massive computational compexity");
					log.setText(" Adaptive histogram equalisation complete. Time taken: "+adaptiveHistogramEqualisation.operation(inputFile,getFrameSize(),0)+" seconds");
					this.setOutputImage(adaptiveHistogramEqualisation.getBufferedImage(),adaptiveHistogramEqualisation.getWidth(),adaptiveHistogramEqualisation.getHeight());
					BufferedImage imgOut = adaptiveHistogramEqualisation.getBufferedImage();
					this.imgOut=imgOut;
				}
				
				
				else if(aE.getSource()==highBoost){
					String num1,num2;					
					num1 = JOptionPane.showInputDialog("Enter mask size");
					maskSize = Integer.parseInt(num1);
					num2 = JOptionPane.showInputDialog("Enter High Boost Value");
					hb = Integer.parseInt(num2);
					BufferedImage imgOut = HighBoost.test(inputFile,maskSize,hb);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==lapLacian){		
					String num1,num2;					
					num1 = JOptionPane.showInputDialog("Enter mask size");
					maskSize = Integer.parseInt(num1);
					BufferedImage imgOut = SharpeningLaplacian.test(inputFile, maskSize);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==bilinear){		
					String num1,num2;					
					num1 = JOptionPane.showInputDialog("Enter Width");
					width = Integer.parseInt(num1);
					num2 = JOptionPane.showInputDialog("Enter Height");
					height = Integer.parseInt(num2);
					BufferedImage imgOut = BilinearInterpolation.test(inputFile, width,height);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==grayScale){	
					BufferedImage imgOut = ReadAndWriteImage.test(inputFile,hex);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;

				}
				else if(aE.getSource()==makeSmall){
					String num1,num2;					
					num1 = JOptionPane.showInputDialog("Enter Width");
					width = Integer.parseInt(num1);
					num2 = JOptionPane.showInputDialog("Enter Height");
					height = Integer.parseInt(num2);
					BufferedImage imgOut = ReadAndWriteImage.makeSmall(inputFile, width,height);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;					
				}
				else if(aE.getSource()==linearInterpolation){
					String num1,num2;					
					num1 = JOptionPane.showInputDialog("Enter Width");
					width = Integer.parseInt(num1);
					num2 = JOptionPane.showInputDialog("Enter Height");
					height = Integer.parseInt(num2);
//      				num3 = JOptionPane.showInputDialog("Enter X-Axis or Y-Axis");
//      				type=num3.toString();
					BufferedImage imgOut = LinearInterpolation.test(inputFile, width,height,type);
					this.setOutputImage(imgOut,imgOut.getWidth(),imgOut	.getHeight());
					this.imgOut=imgOut;
				}
				else if(aE.getSource()==zoom){
					  try{
						    File  f = new File("F:/impro/output.jpg");
						      ImageIO.write(imgOut, "jpg", f);
						    }catch(IOException e){
						      System.out.println(e);
						    }
					  ZoomIn.test();
				}
			}
			else if(inputFile==null){
				log.setText(" No file chosen");
				System.out.println("GUI: no file chosen");
			}
			
		}
		catch(IOException iOE){
			System.err.println("GUI: IOException in listening method");
			iOE.printStackTrace();
		}
	}	
}