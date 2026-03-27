package comp3170.week5.sceneobjects;

import static org.lwjgl.opengl.GL41.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Flower extends SceneObject {
	
	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;
	
	private final float HEIGHT = 1.0f;
	private final float WIDTH = 0.1f;
	private Vector3f stemColour = new Vector3f(0f, 0.5f, 0f); // Dark Green
	private Vector3f petalColour = new Vector3f(1f,1f,0f); // Yellow

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private int colorBuffer;
	
	private Vector4f position = new Vector4f();

	public Flower(int nPetals) {
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		Vector3f[] vertexColors = new Vector3f[4]; // The stem has 4 vertices
		for (int i = 0; i < vertexColors.length; i++) {
		    vertexColors[i] = stemColour; // Use stem color for all stem vertices
		}
	
		// make the stem of the flower

		// vertices for a wxh square with origin at the end
		// 
		//  (-w/2, h)     (w/2, h)		
		//       2-----------3
		//       | \         |
		//       |   \       |
		//       |     \     |
		//       |       \   |
		//       |         \ |
		//       0-----*-----1
		//  (-w/2, 0)     (w/2, 0)	
		
		//@formatter:off
		vertices = new Vector4f[] {
			new Vector4f(-WIDTH / 2,           0, 0, 1),
			new Vector4f( WIDTH / 2,           0, 0, 1),
			new Vector4f(-WIDTH / 2, HEIGHT, 0, 1),
			new Vector4f( WIDTH / 2, HEIGHT, 0, 1),
		};
		//@formatter:on
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
	    indices = new int[] {
		    	0, 1, 2,
		    	3, 2, 1,
		};
	    
	    createFlowerHead(position, nPetals);
		    
		indexBuffer = GLBuffers.createIndexBuffer(indices);
		colorBuffer = GLBuffers.createBuffer(vertexColors);
	}
	
	public void createFlowerHead(Vector4f position, int nPetals) {
		FlowerHead flowerHead = new FlowerHead(nPetals, petalColour);
		flowerHead.setParent(this);	
		flowerHead.getMatrix().translate(position.x,position.y+1,0.0f);
	}
	
	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setAttribute("a_colour", colorBuffer);    
	    
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);		
	}
	
	public void update(float dt) {
		// TODO: make the flower sway. (TASK 5)
	}
}
