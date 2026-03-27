package comp3170.week5.sceneobjects;

import static org.lwjgl.opengl.GL41.*;

import java.awt.Color;


import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.SceneObject;
import static comp3170.Math.TAU;


public class FlowerHead extends SceneObject {
	
	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;

	private Vector3f petalColour = new Vector3f(1.0f,1.0f,1.0f);
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private int colorBuffer;
	
	private float innerRadius = 0.45f;
	private float outerRadius = 0.7f;

	public FlowerHead(int nPetals, Vector3f colour) {
		
		// TODO: Create the flower head. (TASK 1)
		// Consider the best way to draw the mesh with the nPetals input. 
		// Note that this may involve moving some code OUT of this class!
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		Matrix4f outerScaling = new Matrix4f().scaling(outerRadius);
		Matrix4f innerScaling = new Matrix4f().scaling(innerRadius);
		
		vertices = new Vector4f[nPetals + 1];

		vertices[0] = new Vector4f(0, 0, 0, 1); // centre

		Matrix4f rotate = new Matrix4f();
		
		Vector3f[] vertexColors = new Vector3f[nPetals + 1];
	    vertexColors[0] = new Vector3f(1.0f, 1.0f, 1.0f);
		
		for (int i = 0; i < nPetals; i++) {
			float angle = i * TAU / nPetals;
			float radius = (i % 2 == 0) ? innerRadius : outerRadius;

			rotate.rotationZ(angle); // R = R(angle)
			vertices[i + 1] = new Vector4f(radius, 0, 0, 1);
			//vertices[i + 1] = new Vector4f(0.5f, 0, 0, 1); // v = (1,0,0)
			vertices[i + 1].mul(rotate); // v = R v
		}

		vertexBuffer = GLBuffers.createBuffer(vertices);

		indices = new int[nPetals * 3]; // each side creates 1 triangle with 3 vertices

		int k = 0;
		for (int i = 1; i <= nPetals; i++) {
			indices[k++] = 0;
			indices[k++] = i;
			indices[k++] = (i % nPetals) + 1; // wrap around when i = NSIDES
		}

		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		java.util.Random rand = new java.util.Random();
	    
	    for (int i = 0; i < nPetals; i++) {
	        float r = rand.nextFloat(); // 0.0 to 1.0
	        float g = rand.nextFloat(); // 0.0 to 1.0
	        float b = rand.nextFloat(); // 0.0 to 1.0
	        
	        vertexColors[i + 1] = new Vector3f(r, g, b);
	    }
		
		//petalColour = colour;
		colorBuffer = GLBuffers.createBuffer(vertexColors);
	}

	public void update(float dt) {
		// TODO: Make the flower head rotate. (TASK 5)
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		
		shader.setAttribute("a_colour", colorBuffer);
		
		//shader.setUniform("u_colour", petalColour);
		

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
