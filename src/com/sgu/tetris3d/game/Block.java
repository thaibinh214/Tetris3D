package com.sgu.tetris3d.game;

public class Block {
	private int x;
	private int y;
	private int z;
		
	private int size;
	
	private Element[][][] elements;
	
	public Block(Element[][][] elements)
	{
		size = elements.length;
		this.elements = elements;
	}

	public Block(Element[][][] elements, int x, int y, int z)
	{
		size = elements.length;
		this.elements = elements;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Block translatedBlock(int x, int y, int z)
	{
		Block newBlock = new Block(elements.clone());
		newBlock.x += this.x + x;
		newBlock.y += this.y + y;
		newBlock.z += this.z + z;
		
		return newBlock;
	}
	
	public Block xRotatedBlock()
	{
		Element[][][] newElements = new Element[size][size][size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) {
					newElements[i][j][k] = elements[i][size - k - 1][j];
				}
			}
		}
		
		return new Block(newElements, x, y, z);
	}

	public Block yRotatedBlock()
	{
		Element[][][] newElements = new Element[size][size][size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) {
					newElements[i][j][k] = elements[k][j][size - i - 1];
				}
			}
		}
		
		return new Block(newElements, x, y, z);
	}
	
	public Block zRotatedBlock()
	{
		Element[][][] newElements = new Element[size][size][size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) {
					newElements[i][j][k] = elements[size - j - 1][i][k];
				}
			}
		}
		
		return new Block(newElements, x, y, z);
	}
	
	public Element elementAtLocation(int x, int y, int z)
	{
		return elements[x][y][z];
	}


	public int getSize() {
		return size;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}	
}
