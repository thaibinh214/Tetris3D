package com.sgu.tetris3d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.sgu.tetris3d.game.Game;

import android.opengl.GLSurfaceView;

public class TetrisRenderer implements GLSurfaceView.Renderer {
	private Game game;
	
	public TetrisRenderer(Game g) {
		game = g;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {	
		game.render();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		game.setViewPort(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		game.setup();
	}

	/* public void performRotateWorld(float dx, float dy, float dxy) {
		game.performRotateWorld(dx, dy);
	} */
}
