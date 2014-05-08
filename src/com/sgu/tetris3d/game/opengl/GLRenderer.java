package com.sgu.tetris3d.game.opengl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.sgu.tetris3d.game.Color;
import com.sgu.tetris3d.game.math.Matrix4f;
import com.sgu.tetris3d.game.math.Vector3f;

public class GLRenderer {
	// Quad variables
	private int quadVertexVBO = 0;
	private int quadIndicesVBO = 0;
	private int quadIndicesCount = 0;
	
	private int bbIndicesVBO = 0;
	private int bbIndicesCount = 0;
	
	private VertexData[] vertices = null;
	private ByteBuffer verticesByteBuffer = null;
	
	// Shader variables
	private int quadVertexShader = 0;
	private int quadFragmentShader = 0;
	private int quadProgram = 0;

	
	// Attributes Binding constants
	
	private final int kPosAttrib = 0;
	private final int kNormAttrib = 1;
	
	// Hooks to shaders' variables
	private int viewProjectionMatrixLocation = 0;
	private int viewMatrixLocation = 0;
	private int modelMatrixLocation = 0;
	private int colorVectorLocation = 0;
	private int lightFactorLocation = 0;

	private FloatBuffer matrix44Buffer = null;
	
	public void setup()
	{
		this.setupOpenGL();
		
		this.setupQuad();
		this.setupShaders();
		this.setupMatrices();
	}

	public void startRenderingWithCamera(Camera camera)
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL20.glUseProgram(quadProgram);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVertexVBO);
		GL20.glVertexAttribPointer(kPosAttrib, VertexData.positionElementCount, GL11.GL_FLOAT, 
				false, VertexData.stride, VertexData.positionByteOffset);
		GL20.glVertexAttribPointer(kNormAttrib, VertexData.normalElementCount, GL11.GL_FLOAT, 
				false, VertexData.stride, VertexData.normalByteOffset);
		
		GL20.glEnableVertexAttribArray(kPosAttrib);
		GL20.glEnableVertexAttribArray(kNormAttrib);	
		

		camera.getViewProjectionMatrix().store(matrix44Buffer);
		matrix44Buffer.flip();
		GL20.glUniformMatrix4(viewProjectionMatrixLocation, false, matrix44Buffer);

		camera.getViewMatrix().store(matrix44Buffer);
		matrix44Buffer.flip();
		GL20.glUniformMatrix4(viewMatrixLocation, false, matrix44Buffer);
	}
	
	public void endRendering()
	{
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(kPosAttrib);
		GL20.glDisableVertexAttribArray(kNormAttrib);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL20.glUseProgram(0);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void renderElement(Vector3f offset, Color color)
	{		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, quadIndicesVBO);

		Vector3f colorVec = colorToVector(color);
		GL20.glUniform4f(colorVectorLocation, colorVec.x, colorVec.y, colorVec.z, 1.0f);
		GL20.glUniform1f(lightFactorLocation, 0.7f);
		renderWallsAtOffset(offset);
	}
	
	public void renderLineWithAlpha(Vector3f from, Vector3f to, float alpha)
	{
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, bbIndicesVBO);
		GL20.glUniform4f(colorVectorLocation, 1.0f, 1.0f, 1.0f, alpha);
		GL20.glUniform1f(lightFactorLocation, 0.0f);

		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		Vector3f base = new Vector3f(0,1,0);
		
		Vector3f dir = new Vector3f();
		Vector3f.sub(to, from, dir);
		
		float angle = Vector3f.angle(dir, base);
		Vector3f axis = new Vector3f();
		Vector3f.cross(dir, base, axis);
		
		float length = axis.length();
		if (length != 0.0)
		{
			axis.scale(1.0f/length);
		}
		float scale = dir.length(); 
		
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(from, modelMatrix, modelMatrix);
		Matrix4f.rotate(-angle, axis, modelMatrix, modelMatrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);		

		modelMatrix.store(matrix44Buffer);
		matrix44Buffer.flip();
		GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);
		GL11.glDrawElements(GL11.GL_LINES, bbIndicesCount, GL11.GL_UNSIGNED_BYTE, 0);	
	}
	
	private void setupMatrices() {
		matrix44Buffer = BufferUtils.createFloatBuffer(16);
	}

	private void setupOpenGL() {
		GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		this.exitOnGLError("setupOpenGL");
	}
	
	public void setViewport(int w, int h) {
		GL11.glViewport(0, 0, w, h);
	}
	
	private void setupQuad() {
		vertices = CubeMesh.VERTICES;
		
		verticesByteBuffer = BufferUtils.createByteBuffer(vertices.length * VertexData.stride);				
		FloatBuffer verticesFloatBuffer = verticesByteBuffer.asFloatBuffer();
		for (int i = 0; i < vertices.length; i++) {
			verticesFloatBuffer.put(vertices[i].getElements());
		}
		verticesFloatBuffer.flip();
		
		byte []indices = CubeMesh.FACES;
		
		quadIndicesCount = indices.length;
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(quadIndicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
	
		// Create a new VBO
		quadVertexVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVertexVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices.length * VertexData.stride, verticesFloatBuffer, GL15.GL_STREAM_DRAW);
		
		// Create a new IBO
		quadIndicesVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, quadIndicesVBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, quadIndicesCount, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		// Bounding box
		byte[] lineIndices = {
				64, 65,
		};
		bbIndicesCount = lineIndices.length;
		indicesBuffer = BufferUtils.createByteBuffer(bbIndicesCount);
		indicesBuffer.put(lineIndices);
		indicesBuffer.flip();
		
		bbIndicesVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, bbIndicesVBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, bbIndicesCount, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		this.exitOnGLError("setupQuad");
	}
	
	private void setupShaders() {		
		quadVertexShader = this.loadShader("vertexShader.glsl", GL20.GL_VERTEX_SHADER);
		quadFragmentShader = this.loadShader("fragmentShader.glsl", GL20.GL_FRAGMENT_SHADER);
		
		// Create a new shader program that links both shaders
		quadProgram = GL20.glCreateProgram();
		GL20.glAttachShader(quadProgram, quadVertexShader);
		GL20.glAttachShader(quadProgram, quadFragmentShader);
		GL20.glLinkProgram(quadProgram);

		GL20.glBindAttribLocation(quadProgram, kPosAttrib, "in_Position");
		GL20.glBindAttribLocation(quadProgram, kNormAttrib, "in_Normal");

		viewProjectionMatrixLocation = GL20.glGetUniformLocation(quadProgram, "viewProjectionMatrix");
		viewMatrixLocation = GL20.glGetUniformLocation(quadProgram, "viewMatrix");
		modelMatrixLocation = GL20.glGetUniformLocation(quadProgram, "modelMatrix");
		colorVectorLocation = GL20.glGetUniformLocation(quadProgram, "colorVector");
		lightFactorLocation = GL20.glGetUniformLocation(quadProgram, "lightFactor");
		
		GL20.glValidateProgram(quadProgram);
		
		this.exitOnGLError("setupShaders");
	}
	
	private void renderWallsAtOffset(Vector3f offset)
	{
		Vector3f[] rotVectors = {
				new Vector3f(0, 1, 0),
				new Vector3f(0, 1, 0),
				new Vector3f(0, 1, 0),
				new Vector3f(0, 1, 0),
				new Vector3f(0, 0, 1),
				new Vector3f(0, 0, 1),
		};
		
		float[] angles = {
			0.0f,
			(float) (Math.PI/2.0),
			(float) (Math.PI),
			(float) (Math.PI*3.0/2.0),
			(float) (Math.PI/2.0),
			(float) (-Math.PI/2.0),
		};

		for (int i = 0; i < 6; i++) {
			
			Matrix4f modelMatrix = new Matrix4f();
			Matrix4f.translate(offset, modelMatrix, modelMatrix);
			Matrix4f.rotate(angles[i], rotVectors[i], modelMatrix, modelMatrix);
			
			
			modelMatrix.store(matrix44Buffer);
			matrix44Buffer.flip();
			GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);
			GL11.glDrawElements(GL11.GL_TRIANGLES, quadIndicesCount, GL11.GL_UNSIGNED_BYTE, 0);	
		}	
	}
	
	private Vector3f colorToVector(Color color)
	{
		switch (color) {
		case Red:
			return new Vector3f(188.0f/255.0f, 45.0f/255.0f, 30.0f/255.0f);
		case Green:
			return new Vector3f(106.0f/255.0f, 176.0f/255.0f, 47.0f/255.0f);
		case Blue:
			return new Vector3f(55.0f/255.0f, 162.0f/255.0f, 181.0f/255.0f);
		case Yellow:
			return new Vector3f(233.0f/255.0f, 208.0f/255.0f, 2.0f/255.0f);
		}
		
		return new Vector3f(0.0f, 0.0f, 0.0f);
	}
	
	private int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
					 
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			Log.e("loadShader", "Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			String err = GLES20.glGetShaderInfoLog(shaderID);
			Log.e("loadShader", "Could not compile shader: " + err);
			System.exit(-1);
		}
		
		this.exitOnGLError("loadShader");
		return shaderID;
	}

	private void exitOnGLError(String errorMessage) {
		int errorValue = GL11.glGetError();
		
		if (errorValue != GL11.GL_NO_ERROR) {
			Log.e("exitOnGLError", "OpenGL error happend: " + errorValue);
			System.exit(-1);
		}
	}
	
	private Context context;
	public GLRenderer(Context context) {
		this.context = context;
	}

}