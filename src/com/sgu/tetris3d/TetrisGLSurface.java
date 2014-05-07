package com.sgu.tetris3d;

import com.sgu.tetris3d.game.Game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TetrisGLSurface extends GLSurfaceView {
	private TetrisRenderer renderer;
	private Game game;
	private Context context;
	
	public TetrisGLSurface(Context context, AttributeSet attrs)
	{
	  super(context, attrs);
	  
	  this.context = context;
	  initialize();
	}
	
	public TetrisGLSurface(Context context) {
		super(context);
		
		this.context = context;
		initialize();
	}
	
	private void initialize() {
		game = new Game(context);
		renderer = new TetrisRenderer(game);
		setEGLContextClientVersion(2);
		setRenderer(renderer);
	}
	
	// private int movePointerId;
	private float prevWorldRotX = 0;
	private float prevWorldRotY = 0;
	private float prevWorldZoom = 0;
	
	private float prevMoveX = 0;
	private float prevMoveY = 0;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		if (event.getPointerCount() == 2) {
			float x, y;
			
			float x1, y1, x2, y2;
			float dx0, dy0, dxy;
			
			x1 = event.getX(0);
			y1 = event.getY(0);
			x2 = event.getX(1);
			y2 = event.getY(1);
			
			dx0 = x1 - x2;
			dy0 = y1 - y2;
			dxy = (float) Math.sqrt(dx0 * dx0 + dy0 * dy0);
			
			int maskedEvent = event.getActionMasked();
			int pointerIndex = event.getActionIndex();
			
			x = event.getX(pointerIndex);
			y = event.getY(pointerIndex);
			
			switch(maskedEvent) {
			case MotionEvent.ACTION_MOVE:
				float dx = x - prevWorldRotX;
                float dy = prevWorldRotY - y;
                
                game.performRotateWorld(dx, dy, prevWorldZoom- dxy);
				break;
			}
			
			prevWorldRotX = x;
			prevWorldRotY = y;
			prevWorldZoom = dxy;
			
		} else if(event.getPointerCount() == 1) {
			float x = event.getX();
			float y = event.getY();
			
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				prevMoveX = x;
				prevMoveY = y;
				break;
				
			case MotionEvent.ACTION_MOVE:
				float dx = x - prevMoveX;
				float dy = y - prevMoveY;
				
				if (Math.abs(dx) > 50) {
					if (dx < 0) {
						game.moveLeft();
					} else {
						game.moveRight();
					}
					
					prevMoveX = x;
				}
				
				if (Math.abs(dy) > 50) {
					if (dy < 0) {
						game.moveUp();
					} else {
						game.moveDown();
					}
					
					prevMoveY = y;
				}
				
				break;
			}
		}
		
		invalidate();
		return true;
	}
}
