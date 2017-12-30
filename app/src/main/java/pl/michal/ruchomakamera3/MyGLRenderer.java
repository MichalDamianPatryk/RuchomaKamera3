package pl.michal.ruchomakamera3;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


/**
 *  OpenGL Custom renderer used with GLSurfaceView
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Context context;   // Application's context

    private float pi;

    private float xpos;
    private float ypos;
    private float zpos;
    private int filter = 0;

    public float getXrot() {
        return xrot;
    }

    public void addToXrot(float dx) {
        this.xrot += dx;
    }

    public float getYrot() {
        return yrot;
    }

    public void addToYrot(float dy) {
        this.yrot += dy;
    }

    private float xrot;
    private float yrot;

    public void goForward(){
        float xrotrad, yrotrad;
        yrotrad = (yrot / 180 * pi);
        xrotrad = (xrot / 180 * pi);
        xpos += sin(yrotrad);
        zpos -= cos(yrotrad);
        ypos -= sin(xrotrad);
    }

    public void goBack(){
        float xrotrad, yrotrad;
        yrotrad = (yrot / 180 * pi);
        xrotrad = (xrot / 180 * pi);
        xpos -= sin(yrotrad);
        zpos += cos(yrotrad);
        ypos += sin(xrotrad);
    }

    public void strafeLeft() {
        float yrotrad;
        yrotrad = (yrot / 180 * pi);
        xpos -= cos(yrotrad) * 0.1f;
        zpos -= sin(yrotrad) * 0.1f;
    }

    public void strafeRight() {
        float yrotrad;
        yrotrad = (yrot / 180 * pi);
        xpos += cos(yrotrad) * 0.1f;
        zpos += sin(yrotrad) * 0.1f;
    }


    private Kostka kostka;

    public MyGLRenderer(Context context) {


        this.context = context;

        pi = 3.141592654f;
        kostka = new Kostka();

        xpos = 0;
        ypos = 0;
        zpos = 5;
        xrot = 0;
        yrot = 0;


    }


    // Call back when the surface is first created or re-created
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glEnable(GL10.GL_BLEND);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance
        kostka.loadGLTexture(gl,this.context);
        gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture
//
    }

    // Call back after onSurfaceCreated() or whenever the window's size changes
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix

        GLU.gluPerspective(gl, 60, aspect, 0.1f, 100.f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();// Reset

        // You OpenGL|ES display re-sizing code here
        // ......
    }

    // Call back to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear color and depth buffers using clear-value set earlier
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // You OpenGL|ES rendering code here
        gl.glLoadIdentity();    //Reset model-view matrix
        gl.glRotatef(xrot, 1.0f, 0, 0);
        gl.glRotatef(yrot, 0, 1.0f, 0);
        gl.glTranslatef(-xpos-3,-ypos, -zpos); // Translate left and into the screen


        // Translate right, relative to the previous translation
        gl.glLoadIdentity();                 // Reset the mode-view matrix
        if(zpos < 1.1f) {  //blocks the camera to go through the cube

            zpos = 1.1f;
        }
        gl.glRotatef(xrot, 1.0f, 0, 0);
        gl.glRotatef(yrot, 0, 1.0f, 0);
        gl.glTranslatef(-xpos,-ypos, -zpos);

        Log.d("GL", "onDrawFrame: xrot:" + String.valueOf(xrot) + "yrot:" + String.valueOf(yrot)  + "xpos:" + String.valueOf(xpos) + "ypos:" + String.valueOf(ypos) + "zpos" + String.valueOf(zpos));

        kostka.draw(gl, filter);
    }
}
