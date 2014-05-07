package com.sgu.tetris3d.game.opengl;

import android.opengl.GLES20;

public class GL15 extends GLES20 {

	public static int glGenBuffers() {
		int[] result = new int[1];
		
		glGenBuffers(1, result, 0);
		return result[0];
	}

	public static void glDeleteBuffers(int bufferId) {
		int[] buff = new int[1];
		buff[0] = bufferId;
		
		glDeleteBuffers(1, buff, 0);
	}

}
