package com.sgu.tetris3d.game;


import java.util.Random;

public final class BlockFactory {

	public static Block randomBlockAtPosition(int x, int y, int z)
	{
		char[][][] template = blocks[random.nextInt(blocks.length)];
		Color[] colorValues = Color.values();
		Color color = colorValues[random.nextInt(colorValues.length)];
		
		Element[][][] elements = new Element[template.length][template.length][template.length];
		
		for (int i = 0; i < template.length; i++) {
			for (int j = 0; j < template[i].length; j++) {
				
				assert(template[i].length == template.length); // should be square
				
				for (int k = 0; k < template[i][j].length; k++)
				{
					assert(template[i].length == template[i][j].length); // should be square
					
					if (template[i][j][k] == 0) continue;
					
					Element element = new Element(color);
					elements[i][j][k] = element;
				}
			}
		}
		
		return new Block(elements, x, y, z);
	}
	
	private static Random random = new Random();
	private static char blocks[][][][] = {
		{ // cube shape
			{
				{1,},
			},
		},
		{ // T shape
			{
				{0, 0, 0},
				{0, 0, 0},
				{0, 0, 0}
			},
			{
				{0, 1, 0},
				{1, 1, 1},
				{0, 0, 0}
			},
			{
				{0, 0, 0},
				{0, 0, 0},
				{0, 0, 0}
			}
		}, 
		{ // Z Shape
			{
				{0, 0, 0},
				{0, 0, 0},
				{0, 0, 0}
			},
			{
				{0, 1, 0},
				{1, 1, 0},
				{1, 0, 0}
			},
			{
				{0, 0, 0},
				{0, 0, 0},
				{0, 0, 0}
			}
		},
		{ // I Shape
			{
				{0, 0, 0},
				{0, 1, 0},
				{0, 0, 0}
			},
			{
				{0, 0, 0},
				{0, 1, 0},
				{0, 0, 0}
			},
			{
				{0, 0, 0},
				{0, 1, 0},
				{0, 0, 0}
			}
		},
		{ // L shape
			{
				{0, 0, 0},
				{0, 0, 0},
				{0, 0, 0}
			},
			{
				{0, 1, 0},
				{0, 1, 0},
				{0, 1, 1}
			},
			{
				{0, 0, 0},
				{0, 0, 0},
				{0, 0, 0}
			}
		},
		{ 
			{
				{0, 0, 0},
				{0, 0, 0},
				{0, 0, 0},
			},
			{
				{1, 1, 0},
				{0, 1, 0},
				{0, 1, 1},
			},
			{
				{0, 0, 0},
				{0, 0, 0},
				{0, 0, 0},
			}
		},
		{
			{
				{0, 1},
				{1, 1},
			},
			{
				{0, 0},
				{0, 1},
			},
		},
		
		{ // fancy shape
			{
				{0, 1, 0},
				{0, 0, 0},
				{0, 0, 0},
			},
			{
				{0, 1, 0},
				{0, 1, 0},
				{0, 0, 0},
			},
			{
				{0, 0, 0},
				{1, 1, 0},
				{0, 0, 0},
			}
		}
	};
}
