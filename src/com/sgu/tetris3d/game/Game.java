package com.sgu.tetris3d.game;

import android.content.Context;

import com.sgu.tetris3d.game.math.Vector3f;
import com.sgu.tetris3d.game.opengl.Camera;
import com.sgu.tetris3d.game.opengl.GLRenderer;

public class Game {
	private int kWidth = 768;
	private int kHeight = 1024;	
	
	private GLRenderer renderer;
	private Camera camera;
	private GameLogic logic;
	
	// private int highScore;
	
	private Context context;
	public Game(Context context) {
		this.context = context;
		
		readHighScore();
	}
	
	public void setup() {
		renderer = new GLRenderer(this.context);
		logic = new GameLogic();
		logic.currentInput = Input.None;
		
		renderer.setup();
	}
	
	public void setViewPort(int w, int h) {
		kWidth = w;
		kHeight = h;
		
		Vector3f lookPoint = new Vector3f(GameLogic.WIDTH/2.0f, GameLogic.DEPTH/2.0f, GameLogic.HEIGHT/2.0f);
		camera = new Camera((float)kWidth/(float)kHeight, lookPoint);
		
		renderer.setViewport(w, h);
	}
	
	public void render() {
		logic.tick();
		
		renderer.startRenderingWithCamera(camera);
		
		renderBlock();
		renderBoard();
		renderBB();
		
		renderer.endRendering();
	}
	
	public void saveHighScore() {
	}

	public void readHighScore() {
		//this.highScore = 0;
	}

	private void renderBlock()
	{
		Block block = logic.block;
		Element element;
		
		int block_size = block.getSize();
		int bx = block.getX();
		int by = block.getY();
		int bz = block.getZ();
		
		for (int i = 0; i < block_size; i++) {
			for (int j = 0; j < block_size; j++) {
				for (int k = 0; k < block_size; k++) {
					element = block.elementAtLocation(i, j, k);
					
					if (element != null) {
						renderer.renderElement(new Vector3f(bx + i, by + j, bz + k), element.getColor());
					}
				}
			}
		}
	}
	
	private void renderBoard()
	{
		Board board = logic.board;
		Element element;
		
		int bw = board.getWidth();
		int bd = board.getDepth();
		int bh = board.getHeight();
		for (int i = 0; i < bw; i++) {
			for (int j = 0; j < bd; j++) {
				for (int k = 0; k < bh; k++) {
					element = board.elementAtLocation(i, j, k);
					if (element != null) {
						renderer.renderElement(new Vector3f(i, j, k), element.getColor());
					}
				}
			}
		}
	}
	
	private void renderBB()
	{
		final float ALPHA = 0.1f;
		
		for (int i = 0; i <= GameLogic.WIDTH; i++) {
			Vector3f a = new Vector3f(-0.5f + i, -0.5f, -0.5f);
			Vector3f b = new Vector3f(-0.5f + i, -0.5f, GameLogic.HEIGHT -0.5f);

			Vector3f c = new Vector3f(-0.5f + i, GameLogic.DEPTH -0.5f, -0.5f);
			Vector3f d = new Vector3f(-0.5f + i, GameLogic.DEPTH -0.5f, GameLogic.HEIGHT -0.5f);
			
			renderer.renderLineWithAlpha(a, b, ALPHA);
			renderer.renderLineWithAlpha(c, d, ALPHA);
			renderer.renderLineWithAlpha(a, c, ALPHA);
		}
		
		for (int i = 0; i <= GameLogic.DEPTH; i++) {
			Vector3f a = new Vector3f(-0.5f, -0.5f + i, -0.5f);
			Vector3f b = new Vector3f(-0.5f, -0.5f + i, GameLogic.HEIGHT -0.5f);

			Vector3f c = new Vector3f(GameLogic.WIDTH -0.5f, -0.5f + i, -0.5f);
			Vector3f d = new Vector3f(GameLogic.WIDTH -0.5f, -0.5f + i, GameLogic.HEIGHT -0.5f);
			
			renderer.renderLineWithAlpha(a, b, ALPHA);
			renderer.renderLineWithAlpha(c, d, ALPHA);
			renderer.renderLineWithAlpha(a, c, ALPHA);
		}

		for (int i = 0; i <= GameLogic.HEIGHT; i++) {
			Vector3f a = new Vector3f(-0.5f, -0.5f, -0.5f + i);
			Vector3f b = new Vector3f(GameLogic.WIDTH -0.5f, -0.5f, -0.5f + i);
			Vector3f c = new Vector3f(GameLogic.WIDTH -0.5f, GameLogic.DEPTH -0.5f, -0.5f + i);
			Vector3f d = new Vector3f(-0.5f,GameLogic.DEPTH -0.5f, -0.5f + i);
			
			renderer.renderLineWithAlpha(a, b, ALPHA);
			renderer.renderLineWithAlpha(b, c, ALPHA);
			renderer.renderLineWithAlpha(c, d, ALPHA);
			renderer.renderLineWithAlpha(d, a, ALPHA);
		}
		
		Vector3f a = new Vector3f(-0.6f, -0.6f, -0.5f);
		Vector3f b = new Vector3f( 0.0f, -0.6f, -0.5f);
		Vector3f c = new Vector3f(-0.6f,  0.0f, -0.5f);
		
		renderer.renderLineWithAlpha(a, b, 1.0f);
		renderer.renderLineWithAlpha(a, c, 1.0f);
	}
	
	public void performRotateWorld(float dx, float dy, float dxy) {
		final float scale = 0.005f;
		
		camera.setYaw(camera.getYaw() + dx*scale);
		camera.setPitch(camera.getPitch() - dy*scale);
			
		final float wheelScale = 120.0f;
		camera.setDistance(camera.getDistance() + dxy/wheelScale);

	}

	public void moveLeft() {
		logic.currentInput = Input.MoveLeft;
	}
	
	public void moveRight() {
		logic.currentInput = Input.MoveRight;
	}
	
	public void moveUp() {
		logic.currentInput = Input.MoveUp;
	}
	
	public void moveDown() {
		logic.currentInput = Input.MoveDown;
	}
	
	/* private void performMove(Input inp) {
		
		 while(Keyboard.next()) {			
			if (!Keyboard.getEventKeyState()) continue;
			
			
			// Change model scale, rotation and translation values
			switch (Keyboard.getEventKey()) {
			// Move
			case Keyboard.KEY_UP:
				input = Input.MoveUp;
				break;
			case Keyboard.KEY_DOWN:
				input = Input.MoveDown;
				break;
			case Keyboard.KEY_LEFT:
				input = Input.MoveLeft;
				break;
			case Keyboard.KEY_RIGHT:
				input = Input.MoveRight;
				break;
			case Keyboard.KEY_A:
				input = Input.RotateX;
				break;
			case Keyboard.KEY_S:
				input = Input.RotateY;
				break;
			case Keyboard.KEY_D:
				input = Input.RotateZ;
				break;
			case Keyboard.KEY_SPACE:
				input = Input.Drop;
				break;
			}
		} 
		
		logic.currentInput = inp;
	} */
}
