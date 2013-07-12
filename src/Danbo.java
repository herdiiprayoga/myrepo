import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import utility.EulerCamera;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;
import org.lwjgl.util.glu.Sphere;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Herdi
 */
public class Danbo {
    
    String windowTitle = "REPO DANBO";
	public boolean closeRequested = false;
        private static final int WIDTH = 1200, HEIGHT = 600;
        private static final DisplayMode DISPLAY_MODE = new DisplayMode(WIDTH, HEIGHT);
        private static final float ASPECT_RATIO = (float) WIDTH / (float) HEIGHT; //edit 1
        private static final EulerCamera camera = new EulerCamera.Builder().setPosition(0.0f, 0.0f, //edit 2
            0.0f).setRotation(0, 5, 0).setAspectRatio(ASPECT_RATIO).setFieldOfView(60).build(); //edit 2

	long lastFrameTime; // used to calculate delta
	
	float quadAngle; // Angle of rotation for the quads
        
        private static Texture paper;
        private static Texture paper2;
        private static boolean flatten = false;
        private static int shaderProgram;
    
    private void run() { // edit 4
            while(!Display.isCloseRequested()) {
            if(Display.isVisible()) {
                renderGL();
                input();
                setUpStates();
                setUpMatrices();           
            }
            else {
                if(Display.isDirty()) {
                    renderGL();
                }
                try {
                    Thread.sleep(100);
                }
                catch(InterruptedException ex) {
                }
            }
            Display.update();
            Display.sync(60); //fps --> 60
        }
	} //
    
    private void initGL() {

		/* OpenGL */
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();

		GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
		GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
		GL11.glLoadIdentity(); // Reset The Projection Matrix
		GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
		GL11.glLoadIdentity(); // Reset The Modelview Matrix

		GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations
	}
    
    private void renderGL() {
                
                GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                
                GL11.glLoadIdentity(); // Reset The View
                camera.applyTranslations();
		GL11.glTranslated(1.3f, -5.0f, -29.3f); // Move Right And Into The Screen
                paper2.bind();
                
                //Head (Child)
		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.6f, 2.2f, -2.0f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-3.1f, 2.2f, -2.0f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-3.1f, 2.2f, 2.0f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.6f, 2.2f, 2.0f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.6f, -1.2f, 1.5f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-3.1f, -1.2f, 1.5f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-3.1f, -1.2f, -2.0f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.6f, -1.2f, -2.0f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.6f, 2.2f, 2.0f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-3.1f, 2.2f, 2.0f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-3.1f, -1.2f, 1.5f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.6f, -1.2f, 1.5f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.6f, -1.2f, -2.5f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-3.1f, -1.2f, -2.5f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-3.1f, 2.2f, -2.0f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.6f, 2.2f, -2.0f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-3.1f, 2.2f, 2.0f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-3.1f, 2.2f, -2.0f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-3.1f, -1.2f, -2.5f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-3.1f, -1.2f, 1.5f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.6f, 2.2f, -2.0f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(2.6f, 2.2f, 2.0f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(2.6f, -1.2f, 1.5f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.6f, -1.2f, -2.5f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                // Head2 (Child)
                GL11.glTranslated(0.0f, 0.0f, 0.1f); // Move Right And Into The Screen
                paper.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
                GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.6f, 2.2f, 2.0f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-3.1f, 2.2f, 2.0f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-3.1f, -1.2f, 1.5f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.6f, -1.2f, 1.5f); // Bottom Right Of The Quad (Front)
                GL11.glEnd();
                
                //Body (Child)
                GL11.glTranslated(0.0f, -3.9f, -0.5f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, -1.1f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1.8f, 2.5f, -1.1f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1.8f, 2.5f, 1.2f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, 2.5f, 1.2f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, -1.7f, 1.2f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1.8f, -1.7f, 1.2f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1.8f, -1.7f, -1.1f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.7f, -1.1f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, 1.2f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1.8f, 2.5f, 1.2f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1.8f, -1.7f, 1.2f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.7f, 1.2f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, -1.7f, -1.1f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1.8f, -1.7f, -1.1f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1.8f, 2.5f, -1.1f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, 2.5f, -1.1f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-1.8f, 2.5f, 1.2f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1.8f, 2.5f, -1.1f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1.8f, -1.7f, -1.1f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-1.8f, -1.7f, 1.2f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, -1.1f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, 1.2f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.7f, 1.2f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.7f, -1.1f); // Bottom Right Of The Quad (Right)
		GL11.glEnd();
                
                
                //Left Hand (Child)
                GL11.glTranslatef(-3.1f, -1.0f, -0.5f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, 0.0f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.1f, 2.5f, 0.0f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.1f, 2.5f, 1.0f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, 2.5f, 1.0f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, -1.2f, 1.0f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.1f, -1.2f, 1.0f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.1f, -1.2f, 0.8f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.2f, 0.8f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color  Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, 1.0f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.1f, 2.5f, 1.0f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.1f, -1.2f, 1.8f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.2f, 1.8f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color  Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, -1.2f, 0.8f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.1f, -1.2f, 0.8f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.1f, 2.5f, 0.0f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, 2.5f, 0.0f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color  Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(0.1f, 2.5f, 1.0f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.1f, 2.5f, 0.0f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.1f, -1.2f, 0.8f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(0.1f, -1.2f, 1.8f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color  Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, 0.0f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, 1.0f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.2f, 1.8f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.2f, 0.8f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad 
                
                // Right Hand (Child)
                GL11.glTranslatef(4.2f, 0.0f, 0.1f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, 0.0f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.1f, 2.5f, 0.0f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.1f, 2.5f, 1.0f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, 2.5f, 1.0f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, -1.2f, 1.0f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.1f, -1.2f, 1.0f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.1f, -1.2f, 0.0f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.2f, 0.0f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, 1.0f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.1f, 2.5f, 1.0f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(0.1f, -1.2f, 1.0f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.2f, 1.0f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, -1.2f, 0.0f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.1f, -1.2f, 0.0f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.1f, 2.5f, 0.0f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, 2.5f, 0.0f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(0.1f, 2.5f, 1.0f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.1f, 2.5f, 0.0f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.1f, -1.2f, 0.0f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(0.1f, -1.2f, 1.0f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, 0.0f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.2f, 2.5f, 1.0f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.2f, 1.0f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.2f, -1.2f, 0.0f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                // Right Foot (Child)
		GL11.glTranslatef(0.9f, 0.0f, 3.4f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.9f, 0.8f, -1.7f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 0.8f, -1.7f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 0.8f, 0.8f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.9f, 0.8f, 0.8f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.9f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.9f, -1.1f, -.7f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.9f, 0.8f, 0.8f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 0.8f, 0.8f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.9f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.9f, -1.1f, -1.7f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 0.8f, -1.7f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.9f, 0.8f, -1.7f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 0.8f, 0.8f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 0.8f, -1.7f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.9f, 0.8f, -1.7f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-0.9f, 0.8f, 1.0f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-0.9f, -1.1f, 1.0f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.9f, -1.1f, -1.7f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                // Left Foot (Child)
		GL11.glTranslatef(-1.6f, 0.0f, -0.0f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.9f, 0.8f, -1.7f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 0.8f, -1.7f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 0.8f, 0.8f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.9f, 0.8f, 0.8f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.9f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.9f, -1.1f, -.7f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.9f, 0.8f, 0.8f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 0.8f, 0.8f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.9f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.9f, -1.1f, -1.7f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 0.8f, -1.7f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.9f, 0.8f, -1.7f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 0.8f, 0.8f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 0.8f, -1.7f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.7f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.9f, 0.8f, -1.7f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-0.9f, 0.8f, 1.0f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-0.9f, -1.1f, 1.0f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.9f, -1.1f, -1.7f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                /*=======================================================================*/
                
                 // Head (Father)
                GL11.glLoadIdentity(); // Reset The View
                camera.applyTranslations();
		GL11.glTranslatef(6.4f, 0.6f, -34.3f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, 3.9f, -3.3f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, 3.9f, -3.3f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, 3.9f, 2.3f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, 3.9f, 2.3f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, -1.7f, 2.3f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, -1.7f, 2.3f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, 3.9f, 2.3f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, 3.9f, 2.3f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, 3.9f, -3.3f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, 3.9f, -3.3f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-4.1f, 3.9f, 2.3f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, 3.9f, -3.3f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, 3.9f, -3.3f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(4.5f, 3.9f, 2.3f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                // Head2 (Father)
                GL11.glTranslatef(0.0f, 0.0f, 0.1f); // Move Right And Into The Screen
                paper.bind();
                
                GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
                GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, 3.9f, 2.3f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, 3.9f, 2.3f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Right Of The Quad (Front)
                GL11.glEnd();
                
                
                 // Body (Father)
		GL11.glTranslatef(0.2f, -6.0f, -0.3f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -2.3f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.3f, 4.2f, -2.3f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.3f, 4.2f, 1.3f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, 1.3f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, -1.7f, 1.3f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.3f, -1.7f, 1.3f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 1.3f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.3f, 4.2f, 1.3f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.3f, -1.7f, 1.3f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, 1.3f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.3f, 4.2f, -2.3f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, -2.3f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-2.3f, 4.2f, 1.3f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.3f, 4.2f, -2.3f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-2.3f, -1.7f, 1.3f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -2.3f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 1.3f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, 1.3f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad 
                
                
                // Left Hand (Father)
                GL11.glTranslatef(-5.1f, -1.5f, 0.0f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.0f, -1.7f, 0.4f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.3f, -1.7f, 0.4f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.3f, -1.7f, -1.4f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.3f, -1.7f, 0.4f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.0f, -1.7f, 0.4f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(0.3f, -1.7f, -1.4f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(0.3f, -1.7f, -1.4f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(0.3f, -1.7f, 0.4f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Right)
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Top Left Of The Quad (Right)
		GL11.glVertex3f(2.0f, -1.7f, 0.4f); // Bottom Left Of The Quad (Right)
		GL11.glVertex3f(2.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                 // Right Hand (Father)
		GL11.glTranslatef(6.9f, 0.2f, 0.1f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Green
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Set The Color To Orange
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, -1.7f, 0.4f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, -1.7f, 0.4f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Red
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, -1.7f, 0.4f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, 0.4f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Yellow
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Blue
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.0f, -1.7f, 0.4f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Violet
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, 0.4f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                 // Left Foot (Father)
		GL11.glTranslatef(-2.0f, -3.4f, -0.5f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                 // Right Foot (Father)
		GL11.glTranslatef(2.8f, 0.0f, 0.0f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                /*=======================================================================*/
                
                 // Head (Mother)
                GL11.glLoadIdentity(); // Reset The View
                camera.applyTranslations();
		GL11.glTranslatef(-4.0f, 0.6f, -34.3f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, 3.9f, -3.3f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, 3.9f, -3.3f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, 3.9f, 2.3f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, 3.9f, 2.3f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, -1.7f, 2.3f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, -1.7f, 2.3f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, 3.9f, 2.3f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, 3.9f, 2.3f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, 3.9f, -3.3f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, 3.9f, -3.3f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-4.1f, 3.9f, 2.3f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, 3.9f, -3.3f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, -1.7f, -3.3f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, 3.9f, -3.3f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(4.5f, 3.9f, 2.3f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, -1.7f, -3.3f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
               
                // Right Hand (Mother)
		GL11.glTranslatef(1.6f, -7.6f, 0.1f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(3.0f, -1.7f, 0.4f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.3f, -1.7f, 0.4f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.3f, -1.7f, -1.4f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(3.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.3f, -1.7f, 0.4f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(3.0f, -1.7f, 0.4f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(3.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.3f, -1.7f, -1.4f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.3f, -1.7f, -1.4f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.3f, -1.7f, 0.4f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(3.0f, -1.7f, 0.4f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(3.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                // Body (Mother)
		GL11.glTranslatef(-1.8f, 1.6f, -0.3f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -2.3f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.3f, 4.2f, -2.3f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.3f, 4.2f, 1.3f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, 1.3f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, -1.7f, 1.3f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.3f, -1.7f, 1.3f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 1.3f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.3f, 4.2f, 1.3f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.3f, -1.7f, 1.3f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, 1.3f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.3f, 4.2f, -2.3f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, -2.3f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-2.3f, 4.2f, 1.3f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.3f, 4.2f, -2.3f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.3f, -1.7f, -2.3f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-2.3f, -1.7f, 1.3f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -2.3f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 1.3f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, 1.3f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, -2.3f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                // Right Hand (Mother)
		GL11.glTranslatef(-5.1f, -1.5f, 0.1f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Green
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Set The Color To Orange
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, -1.7f, 0.4f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, -1.7f, 0.4f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Red
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, -1.7f, 0.4f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, 0.4f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Yellow
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Blue
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, 0.4f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1.0f, 4.2f, -1.4f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1.0f, -1.7f, -1.4f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1.0f, -1.7f, 0.4f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Set The Color To Violet
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, -1.4f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(2.7f, 4.2f, 0.4f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, 0.4f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(2.7f, -1.7f, -1.4f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                // Left Foot (Mother)
		GL11.glTranslatef(5.0f, -3.1f, 0.0f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                
                // Right Foot (Mother)
		GL11.glTranslatef(2.6f, 0.0f, 0.0f); // Move Right And Into The Screen
                paper2.bind();

		GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Top
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Top)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Bottom Left Of The Quad (Top)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Bottom Right Of The Quad (Top)

		GL11.glColor3f(1.0f, 1.5f, 1.0f); // Default Color Bottom
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Top Right Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Top Left Of The Quad (Bottom)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Bottom)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Bottom)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Front)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Back
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Back)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Right Of The Quad (Back)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Left Of The Quad (Back)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Left
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, 0.8f); // Top Right Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-2.1f, 2.8f, -1.3f); // Top Left Of The Quad (Left)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, -1.3f); // Bottom Left Of The Quad (Left)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-2.1f, -1.1f, 0.8f); // Bottom Right Of The Quad (Left)

		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Right
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, -1.3f); // Top Right Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-0.1f, 2.8f, 0.8f); // Top Left Of The Quad (Right)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, 0.8f); // Bottom Left Of The Quad (Right)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-0.1f, -1.1f, -1.3f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
                
                 // Head (Mother)
		GL11.glTranslatef(-2.3f, 10.6f, 0.2f); // Move Right And Into The Screen
                paper.bind();
                
                GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
                GL11.glColor3f(1.0f, 1.0f, 1.0f); // Default Color Front
                GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(4.5f, 3.9f, 2.3f); // Top Right Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-4.1f, 3.9f, 2.3f); // Top Left Of The Quad (Front)
                GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-4.1f, -1.7f, 2.3f); // Bottom Left Of The Quad (Front)
                GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(4.5f, -1.7f, 2.3f); // Bottom Right Of The Quad (Front)
                GL11.glEnd(); // Done Drawing The Quad
    }
    
    private static void input() { // edit 7
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
                    int polygonMode = GL11.glGetInteger(GL11.GL_POLYGON_MODE);
                    if (polygonMode == GL11.GL_LINE) {
                        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
                    } else if (polygonMode == GL11.GL_FILL) {
                        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_POINT);
                    } else if (polygonMode == GL11.GL_POINT) {
                        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
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
        camera.processKeyboard(50, 1);
    } // 
  
  private static void setUpMatrices() { // edit 7
        camera.applyPerspectiveMatrix();
    } //
	
  private static void setUpStates() { // edit 8
        camera.applyOptimalStates();
        GL11.glPointSize(2);
        // Enable the sorting of shapes from far to near
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        // Remove the back (bottom) faces of shapes for performance
        GL11.glEnable(GL11.GL_CULL_FACE);
    } //
  
  private void createWindow() {
		try {
			Display.setDisplayMode(DISPLAY_MODE);
			Display.setVSyncEnabled(true);
			Display.setTitle(windowTitle);
			Display.create();
		} catch (LWJGLException e) {
			Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
			System.exit(0);
		}
               try {
                    // Load the wood texture from "res/images/wood.png"
                     paper = TextureLoader.getTexture("PNG", new FileInputStream(new File("E:/res/paper.png")));
                     paper2 = TextureLoader.getTexture("PNG", new FileInputStream(new File("E:/res/paper2.png")));
                } catch (IOException e) {
                     e.printStackTrace();
                     Display.destroy();
                     System.exit(1);
                }
	}
  
  private void cleanup() {
		Display.destroy();
	}
	
	public static void main(String[] args) {
		Danbo main = null;
        try {
            main = new Danbo();
            main.createWindow();
            main.run();
        }catch(Exception e){}
        
        if(main != null) {
            main.cleanup();	
	}
        }   
}
