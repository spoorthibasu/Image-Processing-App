package project.utilities;


public class HuffmanNode implements Comparable<HuffmanNode> {
	private final int gray;
	private final int freq;
	private int bit = Huffman.NOT_A_GRAY_VALUE; 
	private HuffmanNode parent = null;
	private HuffmanNode left = null;
	private HuffmanNode right = null;

	public HuffmanNode(int gray, int freq) {
		this.gray = gray;
		this.freq = freq;
	}

	@Override
	public int compareTo(HuffmanNode node) {
		int freq1 = this.getFrequency();
		int freq2 = node.getFrequency();

		
		if (freq1 != freq2) {
			return this.getFrequency() - node.getFrequency();
		}
		else {
			return this.getGrayValue() - node.getGrayValue();
		}
	}

	
	public boolean isLeafNode() {
		return (this.left == null && this.right == null);
	}

	public void setBitCode(int bit) {this.bit = bit;}
	public void setParentNode(HuffmanNode parent) {this.parent = parent;}
	public void setLeftChild(HuffmanNode left) {this.left = left;}
	public void setRightChild(HuffmanNode rightChild) {this.right = rightChild;}
	public int getGrayValue() {return this.gray;}
	public int getFrequency() {return this.freq;}
	public int getBitCode() {return this.bit;}
	public HuffmanNode getParentNode() {return parent;}
	public HuffmanNode getLeftChild() {return left;}
	public HuffmanNode getRightChild() {return right;}

	@Override
	public String toString() {
		String temp = "Gray: " + this.gray + ", Bit code: " + this.bit;
		return temp;
	}
}

