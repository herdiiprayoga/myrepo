import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import static org.lwjgl.opengl.GL11.*;
import utility.EulerCamera;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Danbo {
	private static final int[] WINDOW_DIMENSIONS = {1200, 600};
        private static final float ASPECT_RATIO = (float) WINDOW_DIMENSIONS[0] / (float) WINDOW_DIMENSIONS[1];
    private static final EulerCamera camera = new EulerCamera.Builder().setPosition(0.0f, 0.0f, //edit 2
            0.0f).setRotation(0, 5, 0).setAspectRatio(ASPECT_RATIO).setFieldOfView(60).build(); //edit 2
	private static final String WINDOW_TITLE = "Lighting Test";
	private static final int FPS = 70;	//frames per second
	
	private boolean isRunning;	//variable to tell if program is running or not
	private float zTranslation = -12f;
        private static boolean flatten = false;
        private static int shaderProgram;
        private static Texture paper2;
        private static Texture paper;
        private static Texture street;
	
	//----------- Variables added for Lighting Test -----------//
	private FloatBuffer matSpecular;
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight; 
	private FloatBuffer lModelAmbient;
	//----------- END: Variables added for Lighting Test -----------//
	
	public static void main(String[] args) {
		Danbo test = new Danbo();
		test.run( );
	}

	private void run() {
		try {
			init();
			while( isRunning ) {
				getInput();	//read input
				render();                                //render graphics
                                input();
                                setUpStates();
                                setUpMatrices();
				Display.sync(FPS);	//sync to fps
				Display.update();	//update the view/screen
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void render() {
                glEnable(GL_TEXTURE_2D);
		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		glLoadIdentity(); 
                camera.applyTranslations();
                
                glTranslated(1.3f, -5.0f, -29.3f);      // Move Right And Into The Screen
                paper2.bind();
                
                //Head (Child)
		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.6f, 2.2f, -2.0f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-3.1f, 2.2f, -2.0f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-3.1f, 2.2f, 2.0f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.6f, 2.2f, 2.0f); // Bottom Right Of The Quad (Top)
                
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.6f, -1.2f, 1.5f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-3.1f, -1.2f, 1.5f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-3.1f, -1.2f, -2.0f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.6f, -1.2f, -2.0f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.6f, 2.2f, 2.0f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-3.1f, 2.2f, 2.0f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-3.1f, -1.2f, 1.5f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.6f, -1.2f, 1.5f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.6f, -1.2f, -2.5f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-3.1f, -1.2f, -2.5f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-3.1f, 2.2f, -2.0f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.6f, 2.2f, -2.0f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-3.1f, 2.2f, 2.0f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-3.1f, 2.2f, -2.0f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-3.1f, -1.2f, -2.5f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-3.1f, -1.2f, 1.5f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.6f, 2.2f, -2.0f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(2.6f, 2.2f, 2.0f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(2.6f, -1.2f, 1.5f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.6f, -1.2f, -2.5f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                // Head2 (Child)
                glTranslated(0.0f, 0.0f, 0.0f); // Move Right And Into The Screen
                paper.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
                glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.6f, 2.2f, 2.0f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-3.1f, 2.2f, 2.0f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-3.1f, -1.2f, 1.5f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.6f, -1.2f, 1.5f); // Bottom Right Of The Quad (Front)
                glEnd();
                
                
                //Body (Child)
                glTranslated(0.0f, -3.9f, -0.5f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, -1.1f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-1.8f, 2.5f, -1.1f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-1.8f, 2.5f, 1.2f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, 2.5f, 1.2f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, -1.7f, 1.2f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-1.8f, -1.7f, 1.2f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-1.8f, -1.7f, -1.1f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, -1.7f, -1.1f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, 1.2f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-1.8f, 2.5f, 1.2f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-1.8f, -1.7f, 1.2f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, -1.7f, 1.2f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, -1.7f, -1.1f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-1.8f, -1.7f, -1.1f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-1.8f, 2.5f, -1.1f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
	        glVertex3f(1.2f, 2.5f, -1.1f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-1.8f, 2.5f, 1.2f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
                glVertex3f(-1.8f, 2.5f, -1.1f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-1.8f, -1.7f, -1.1f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-1.8f, -1.7f, 1.2f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, -1.1f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, 1.2f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.2f, -1.7f, 1.2f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, -1.7f, -1.1f); // Bottom Right Of The Quad (Right)
		glEnd();
                
                
                //Left Hand (Child)
                glTranslatef(-3.1f, -1.0f, -0.5f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, 0.0f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.1f, 2.5f, 0.0f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.1f, 2.5f, 1.0f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, 2.5f, 1.0f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
	        glVertex3f(1.2f, -1.2f, 1.8f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.1f, -1.2f, 1.8f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
	        glVertex3f(0.1f, -1.2f, 0.8f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, -1.2f, 0.8f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color  Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, 1.0f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.1f, 2.5f, 1.0f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.1f, -1.2f, 1.8f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, -1.2f, 1.8f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color  Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, -1.2f, 0.8f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.1f, -1.2f, 0.8f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.1f, 2.5f, 0.0f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, 2.5f, 0.0f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color  Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(0.1f, 2.5f, 1.0f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.1f, 2.5f, 0.0f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.1f, -1.2f, 0.8f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(0.1f, -1.2f, 1.8f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color  Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, 0.0f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, 1.0f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.2f, -1.2f, 1.8f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, -1.2f, 0.8f); // Bottom Right Of The Quad (Right)
                glEnd(); // Done Drawing The Quad 
                
                // Right Hand (Child)
                glTranslatef(4.2f, 0.0f, 0.1f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, 0.0f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.1f, 2.5f, 0.0f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.1f, 2.5f, 1.0f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, 2.5f, 1.0f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, -1.2f, 1.0f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.1f, -1.2f, 1.0f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.1f, -1.2f, 0.0f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, -1.2f, 0.0f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, 1.0f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.1f, 2.5f, 1.0f); // Top Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(0.1f, -1.2f, 1.0f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, -1.2f, 1.0f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, -1.2f, 0.0f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.1f, -1.2f, 0.0f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.1f, 2.5f, 0.0f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.2f, 2.5f, 0.0f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(0.1f, 2.5f, 1.0f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.1f, 2.5f, 0.0f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.1f, -1.2f, 0.0f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
                glVertex3f(0.1f, -1.2f, 1.0f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, 0.0f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.2f, 2.5f, 1.0f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.2f, -1.2f, 1.0f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
                glVertex3f(1.2f, -1.2f, 0.0f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                // Right Foot (Child)
                glTranslatef(0.9f, 0.0f, 3.4f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.9f, 0.8f, -1.7f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 0.8f, -1.7f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 0.8f, 0.8f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.9f, 0.8f, 0.8f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.9f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.9f, -1.1f, -1.7f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.9f, 0.8f, 0.8f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 0.8f, 0.8f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.9f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.9f, -1.1f, -1.7f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 0.8f, -1.7f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.9f, 0.8f, -1.7f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-2.1f, 0.8f, 0.8f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 0.8f, -1.7f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
                glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.9f, 0.8f, -1.7f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-0.9f, 0.8f, 0.8f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-0.9f, -1.1f, 0.8f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.9f, -1.1f, -1.7f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                // Left Foot (Child)
		glTranslatef(-1.6f, 0.0f, -0.0f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.9f, 0.8f, -1.7f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 0.8f, -1.7f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 0.8f, 0.8f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.9f, 0.8f, 0.8f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.9f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
                glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.9f, -1.1f, -1.7f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.9f, 0.8f, 0.8f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 0.8f, 0.8f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.9f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.9f, -1.1f, -1.7f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 0.8f, -1.7f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.9f, 0.8f, -1.7f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-2.1f, 0.8f, 0.8f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 0.8f, -1.7f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.9f, 0.8f, -1.7f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-0.9f, 0.8f, 0.80f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-0.9f, -1.1f, 0.8f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.9f, -1.1f, -1.7f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                /*=======================================================================*/
                
                 // Head (Father)
                glLoadIdentity(); // Reset The View
                camera.applyTranslations();
		glTranslatef(6.4f, 0.6f, -34.3f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, 3.9f, -3.3f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, 3.9f, -3.3f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, 3.9f, 2.3f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, 3.9f, 2.3f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, -1.7f, 2.3f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, -1.7f, 2.3f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, 3.9f, 2.3f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, 3.9f, 2.3f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, 3.9f, -3.3f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, 3.9f, -3.3f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-4.1f, 3.9f, 2.3f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, 3.9f, -3.3f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, 3.9f, -3.3f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(4.5f, 3.9f, 2.3f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                // Head2 (Father)
                glTranslatef(0.0f, 0.0f, 0.1f); // Move Right And Into The Screen
                paper.bind();
                
                glBegin(GL_QUADS); // Start Drawing The Cube
                glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, 3.9f, 2.3f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, 3.9f, 2.3f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Right Of The Quad (Front)
                glEnd();
                
                
                 // Body (Father)
		glTranslatef(0.2f, -6.0f, -0.3f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
	        glVertex3f(2.7f, 4.2f, -2.3f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.3f, 4.2f, -2.3f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.3f, 4.2f, 1.3f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, 1.3f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, -1.7f, 1.3f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.3f, -1.7f, 1.3f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Right Of The Quad (Bottom)

	        glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, 1.3f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.3f, 4.2f, 1.3f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.3f, -1.7f, 1.3f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, 1.3f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.3f, 4.2f, -2.3f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, -2.3f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-2.3f, 4.2f, 1.3f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.3f, 4.2f, -2.3f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-2.3f, -1.7f, 1.3f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, -2.3f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
	        glVertex3f(2.7f, 4.2f, 1.3f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, 1.3f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad 
                
                
                // Left Hand (Father)
                glTranslatef(-5.1f, -1.5f, 0.0f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, 4.2f, 0.4f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.0f, -1.7f, 0.4f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.3f, -1.7f, 0.4f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.3f, -1.7f, -1.4f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, 0.4f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.3f, -1.7f, 0.4f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.0f, -1.7f, 0.4f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(0.3f, -1.7f, -1.4f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, 4.2f, -1.4f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, 0.4f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(0.3f, -1.7f, -1.4f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(0.3f, -1.7f, 0.4f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Right)
		glVertex3f(2.7f, 4.2f, 0.4f); // Top Left Of The Quad (Right)
		glVertex3f(2.0f, -1.7f, 0.4f); // Bottom Left Of The Quad (Right)
		glVertex3f(2.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                 // Right Hand (Father)
		glTranslatef(6.9f, 0.2f, 0.1f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Green
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, 4.2f, 0.4f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Set The Color To Orange
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, -1.7f, 0.4f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
                glVertex3f(1.0f, -1.7f, 0.4f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Right Of The Quad (Bottom)

                glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Red
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, 0.4f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, -1.7f, 0.4f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, 0.4f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Yellow
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, 4.2f, -1.4f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Blue
                glTexCoord2f(1.0f, 0.0f);
	        glVertex3f(1.0f, 4.2f, 0.4f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.0f, -1.7f, 0.4f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Violet
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, 0.4f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
                glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                 // Left Foot (Father)
		glTranslatef(-2.0f, -3.4f, -0.5f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
	        glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
                glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Left Of The Quad (Back)
  
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                 // Right Foot (Father)
		glTranslatef(2.8f, 0.0f, 0.0f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Bottom)

                glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
                glVertex3f(-2.1f, 2.8f, -1.3f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                /*=======================================================================*/
                
                // Head (Mother)
                glLoadIdentity(); // Reset The View
                camera.applyTranslations();
		glTranslatef(-4.0f, 0.6f, -34.3f); // Move Right And Into The Screen
                paper2.bind();

                glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, 3.9f, -3.3f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, 3.9f, -3.3f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, 3.9f, 2.3f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, 3.9f, 2.3f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, -1.7f, 2.3f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, -1.7f, 2.3f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Right Of The Quad (Bottom)

                glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
                glVertex3f(4.5f, 3.9f, 2.3f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, 3.9f, 2.3f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, 3.9f, -3.3f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
                glVertex3f(4.5f, 3.9f, -3.3f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-4.1f, 3.9f, 2.3f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, 3.9f, -3.3f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
                glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, 3.9f, -3.3f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(4.5f, 3.9f, 2.3f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
               
                // Right Hand (Mother)
		glTranslatef(1.6f, -7.6f, 0.1f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, 4.2f, 0.4f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(3.0f, -1.7f, 0.4f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.3f, -1.7f, 0.4f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
                glVertex3f(1.3f, -1.7f, -1.4f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
                glVertex3f(3.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, 0.4f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.3f, -1.7f, 0.4f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(3.0f, -1.7f, 0.4f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(3.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.3f, -1.7f, -1.4f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, 4.2f, -1.4f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, 0.4f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
	        glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.3f, -1.7f, -1.4f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.3f, -1.7f, 0.4f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(3.0f, -1.7f, 0.4f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(3.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                // Body (Mother)
		glTranslatef(-1.8f, 1.6f, -0.3f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
                glVertex3f(2.7f, 4.2f, -2.3f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.3f, 4.2f, -2.3f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.3f, 4.2f, 1.3f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, 1.3f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, -1.7f, 1.3f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.3f, -1.7f, 1.3f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, 1.3f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
                glVertex3f(-2.3f, 4.2f, 1.3f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.3f, -1.7f, 1.3f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, 1.3f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.3f, 4.2f, -2.3f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, -2.3f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-2.3f, 4.2f, 1.3f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.3f, 4.2f, -2.3f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-2.3f, -1.7f, 1.3f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, -2.3f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, 1.3f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, 1.3f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                // Right Hand (Mother)
		glTranslatef(-5.1f, -1.5f, 0.1f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Green
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
                glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
                glVertex3f(1.0f, 4.2f, 0.4f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Bottom Right Of The Quad (Top)

                glColor3f(1.0f, 1.5f, 1.0f); // Set The Color To Orange
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, -1.7f, 0.4f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, -1.7f, 0.4f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Red
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, 0.4f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, -1.7f, 0.4f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, 0.4f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Yellow
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, 4.2f, -1.4f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Blue
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, 0.4f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(1.0f, -1.7f, 0.4f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Violet
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(2.7f, 4.2f, 0.4f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, 0.4f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                // Left Foot (Mother)
		glTranslatef(5.0f, -3.1f, 0.0f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
                glVertex3f(-0.1f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
                glVertex3f(-0.1f, 2.8f, 0.8f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                
                // Right Foot (Mother)
		glTranslatef(2.6f, 0.0f, 0.0f); // Move Right And Into The Screen
                paper2.bind();

		glBegin(GL_QUADS); // Start Drawing The Cube
		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Top)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Top)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Bottom Left Of The Quad (Top)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Bottom Right Of The Quad (Top)

		glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Bottom)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Bottom)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
                glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Back)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Back)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Right Of The Quad (Back)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Left Of The Quad (Back)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, 0.8f); // Top Right Of The Quad (Left)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Left)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Left)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Right)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-0.1f, 2.8f, 0.8f); // Top Left Of The Quad (Right)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Right)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Right)
		glEnd(); // Done Drawing The Quad
                
                 // Head (Mother)
		glTranslatef(-2.3f, 10.6f, 0.2f); // Move Right And Into The Screen
                paper.bind();
                
                glBegin(GL_QUADS); // Start Drawing The Cube
                glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                glTexCoord2f(1.0f, 0.0f);
		glVertex3f(4.5f, 3.9f, 2.3f); // Top Right Of The Quad (Front)
                glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-4.1f, 3.9f, 2.3f); // Top Left Of The Quad (Front)
                glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Left Of The Quad (Front)
                glTexCoord2f(1.0f, 1.0f);
		glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Right Of The Quad (Front)
                glEnd(); // Done Drawing The Quad
                
                // Street
                glTranslated(17.5f,-3.6f,-11.0f);
                street.bind();
                glBegin(GL_QUADS);
                glColor3f(1.0f, 1.0f, 1.0f);
                glTexCoord2f(1.0f, 0.0f);
                glVertex3f(-30.0F, -8.0F, -60.0f);
                glTexCoord2f(0.0f, 0.0f);
                glVertex3f(-30.0F, -8.0F, 60.0f);
                glTexCoord2f(0.0f, 1.0f);
                glVertex3f(30.0F, -8.0F, 60.0f);
                glTexCoord2f(1.0f, 1.0f);
                glVertex3f(30.0F, -8.0F, -60.0f);
                glEnd();
                
                
                /*glTranslatef(-8.3f, -6.4f, 20.3f);
                paper.bind();
               // glTranslated(1.3f, -5.0f, -29.3f);
		
		glColor3f(1.0f, 1.0f, 1.0f);
		Sphere s = new Sphere();
		s.draw(2.0f, 70, 70);*/
            
	}
	
	
	private void getInput() {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {	// if user presses escape key
			isRunning = false;
		}
		if( Display.isCloseRequested()) {	// if user closes window
			isRunning = false;
		}
	}
	
	private void init() {
		createWindow();
		initGL();
		isRunning = true;
	}
	

	private void createWindow() {
		try {
			Display.setDisplayMode(new DisplayMode(WINDOW_DIMENSIONS[0],WINDOW_DIMENSIONS[1]));
			Display.setTitle(WINDOW_TITLE);
			Display.create();
		} catch (LWJGLException e){
			e.printStackTrace();
		}
                try {
                    // Load the wood texture from "res/images/wood.png"
                     paper = TextureLoader.getTexture("PNG", new FileInputStream(new File("C:\\Users\\Asus\\Documents\\NetBeansProjects\\tugas\\paper.png")));
                     paper2 = TextureLoader.getTexture("PNG", new FileInputStream(new File("C:\\Users\\Asus\\Documents\\NetBeansProjects\\tugas\\paper2.png")));
                     street = TextureLoader.getTexture("PNG", new FileInputStream(new File("C:\\Users\\Asus\\Documents\\NetBeansProjects\\tugas\\street.png")));
                } catch (IOException e) {
                     e.printStackTrace();
                     Display.destroy();
                     System.exit(1);
                }
	}
	
	private void initGL() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // sets background to grey
		glClearDepth(1.0f); // clear depth buffer
		glEnable(GL_DEPTH_TEST); // Enables depth testing
		glDepthFunc(GL_LEQUAL); // sets the type of test to use for depth testing
		glMatrixMode(GL_PROJECTION); // sets the matrix mode to project
		
		glMatrixMode(GL_MODELVIEW);
		
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
		
		//----------- Variables & method calls added for Lighting Test -----------//
		initLightArrays();
		glShadeModel(GL_SMOOTH);
		glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);				// sets specular material color
		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);					// sets shininess
		
		glLight(GL_LIGHT0, GL_POSITION, lightPosition);				// sets light position
		glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);				// sets specular light to white
		glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);					// sets diffuse light to white
		glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);		// global ambient light 
		
		glEnable(GL_LIGHTING);										// enables lighting
		glEnable(GL_LIGHT0);										// enables light0
		
		glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
		//----------- END: Variables & method calls added for Lighting Test -----------//
	}
	

       private static void input() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
                    flatten = !flatten;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_L) {
                    // Reload the shaders and the heightmap data.
                    glUseProgram(0);
                    glDeleteProgram(shaderProgram);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_P) {
                    // Switch between normal mode, point mode, and wire-frame mode.
                    int polygonMode = glGetInteger(GL_POLYGON_MODE);
                    if (polygonMode == GL_LINE) {
                        glPolygonMode(GL_FRONT, GL_FILL);
                    } else if (polygonMode == GL_FILL) {
                        glPolygonMode(GL_FRONT, GL_POINT);
                    } else if (polygonMode == GL_POINT) {
                        glPolygonMode(GL_FRONT, GL_LINE);
                    }
                }
            }
        }
        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        } else if (Mouse.isButtonDown(1)) {
            Mouse.setGrabbed(false);
        }
        if (Mouse.isGrabbed()) {
            camera.processMouse(1, 80, -80);
        }
        camera.processKeyboard(25, 1);
  }  
  
  private static void setUpMatrices() {
        camera.applyPerspectiveMatrix();
  }
  
  private static void setUpStates() {
        camera.applyOptimalStates();
        glPointSize(2);
        // Enable the sorting of shapes from far to near
        glEnable(GL_DEPTH_TEST);
        // Remove the back (bottom) faces of shapes for performance
        glEnable(GL_CULL_FACE);
  } 
        
	//------- Added for Lighting Test----------//
	private void initLightArrays() {
		matSpecular = BufferUtils.createFloatBuffer(4);
		matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		lightPosition = BufferUtils.createFloatBuffer(4);
                lightPosition.put(1.0f).put(1.0f).put(1.0f).put(8.0f).flip();
		//lightPosition.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
		
		whiteLight = BufferUtils.createFloatBuffer(4);
		whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		lModelAmbient = BufferUtils.createFloatBuffer(4);
		lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
	}
}
