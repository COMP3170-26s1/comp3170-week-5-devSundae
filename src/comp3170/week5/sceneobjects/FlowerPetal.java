package comp3170.week5.sceneobjects;

import static org.lwjgl.opengl.GL41.*;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.SceneObject;
import static comp3170.Math.TAU;

public class FlowerPetal extends SceneObject {

	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	public static int NSIDES = 10;

	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	private float[] colour = { 0.0f, 0.0f, 0.f }; // This is a different way to handle colour that allows you to work with java's Color library.

	public FlowerPetal() {

		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		//@formatter:off
		vertices = new Vector4f[] {
				
				new Vector4f( 0.0f, 0f, 0.0f, 1f),
				new Vector4f( 0.5f, -0.2f, 0.0f, 1.0f),
				new Vector4f( 1.0f, 0.0f, 0.0f, 1.0f),
				
				new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),
				new Vector4f(0.5f, -0.2f, 0.0f, 1.0f),
				new Vector4f(0.0f, 0.0f, 0.0f, 1f),
				
				
			};
				//@formatter:on
				vertexBuffer = GLBuffers.createBuffer(vertices);
				
			    indices = new int[] {
				    	0, 1, 2,
				    	2, 3, 0,
				};
				    
				indexBuffer = GLBuffers.createIndexBuffer(indices);

		vertices[0] = new Vector4f(0, 0, 0, 1);

		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	public void setColour(Color colour) {
		colour.getRGBColorComponents(this.colour);
	}

	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}

}