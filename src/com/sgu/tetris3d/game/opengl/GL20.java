package com.sgu.tetris3d.game.opengl;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class GL20 extends GLES20 {

	public static int glGetShader(int shaderID, int param) {
		int[] result = new int[1];
		
		glGetShaderiv(shaderID, param, result, 0);
		return result[0];
	}

	public static void glShaderSource(int shaderID, StringBuilder shaderSource) {
		glShaderSource(shaderID, shaderSource.toString());
	}

	public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrixBuffer) {
		glUniformMatrix4fv(location, 1, transpose, matrixBuffer);
	}

}
