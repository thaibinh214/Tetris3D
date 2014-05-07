package com.sgu.tetris3d.game;

public class Board {

	private int width;
	private int depth;
	private int height;
	
	private Element[][][] elements;
	
	public Board(int width, int depth, int height)
	{
		this.width = width;
		this.depth = depth;
		this.height = height;
		this.elements = new Element[width][depth][height];
	}
	
	public boolean canBlockBePlacedLegally(Block block)
	{		
		for (int i = 0; i < block.getSize(); i++) {
			for (int j = 0; j < block.getSize(); j++) {
				for (int k = 0; k < block.getSize(); k++) {
					if (block.elementAtLocation(i, j, k) == null) {
						continue;
					}
					int x = i + block.getX();
					int y = j + block.getY();
					int z = k + block.getZ();
										
					if (x < 0 || x >= width || y < 0 || y >= depth || z < 0)
						return false; // out of range
					
					if (z >= height)
						continue;
					
					if (this.elements[x][y][z] != null)
						return false; // already taken
				}
			}
		}
		return true;
	}
	
	public boolean isBlockInContact(Block block)
	{				
		for (int i = 0; i < block.getSize(); i++) {
			for (int j = 0; j < block.getSize(); j++) {
						
				int x = i + block.getX();
				int y = j + block.getY();
				
				for (int k = 0; k < block.getSize(); k++) {
					if (block.elementAtLocation(i, j, k) != null) {
						int z = k + block.getZ();
						
						assert(z >= 0);
						
						if (z == 0) {
							return true; // block is touching ground
						}
						
						if (z <= height && elements[x][y][z - 1] != null) {
							return true; // there is a piece underneath checked block
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public Boolean addBlock(Block block)
	{
		for (int i = 0; i < block.getSize(); i++) {
			for (int j = 0; j < block.getSize(); j++) {
				for (int k = 0; k < block.getSize(); k++) {
					Element element;
					if ((element = block.elementAtLocation(i, j, k)) == null) {
						continue;
					}
					int x = i + block.getX();
					int y = j + block.getY();
					int z = k + block.getZ();
					
					assert(x >= 0 && x < width && y >= 0 && y < depth);
					assert(this.elements[x][y][z] == null);
					
					if (z >= height) {
						return false; // added block is over the top edge
					}
					
					this.elements[x][y][z] = element;
				}
			}
		}
		
		return true;
	}
	
	public int reduceLevels()
	{
		int reducedLevels = 0;
		Element[][][] newElements = new Element[width][depth][height];

		for( int z = 0; z < height; z++ )
		{
			boolean isLevelFull = true;

			for( int x = 0; x < width; x++ )
			{
				for (int y = 0; y < depth; y++) 
				{
					if (elements[x][y][z] == null)
					{
						isLevelFull = false;
						break; // no need to check other elements, level is not full
					}
				}
			}
			
			if (isLevelFull) {
				reducedLevels++;
				continue;
			} 
			
			for( int x = 0; x < width; x++ )
			{
				for (int y = 0; y < depth; y++) 
				{
					newElements[x][y][z - reducedLevels] = elements[x][y][z]; // copy contents of level at proper place
				}
			}
		}
		
		elements = newElements;

		return reducedLevels;
	}
	
	public Element elementAtLocation(int i, int j, int k)
	{
		return elements[i][j][k];
	}

	public int getWidth() {
		return width;
	}

	public int getDepth() {
		return depth;
	}

	public int getHeight() {
		return height;
	}

}
