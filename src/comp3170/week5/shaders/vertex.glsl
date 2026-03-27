#version 410

in vec4 a_position;		// MODEL
uniform mat4 u_mvpMatrix;	// MODEL->WORLD
in vec3 a_colour;

out vec3 v_colour;

void main() {
	
	v_colour = a_colour;
	gl_Position = u_mvpMatrix * a_position;
}

