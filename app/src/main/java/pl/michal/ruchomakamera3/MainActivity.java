package pl.michal.ruchomakamera3;

import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

        private GLSurfaceView glView;   // Use GLSurfaceView
        private MyGLRenderer glRenderer;
        private float previousX;
        private float previousY;

        // Call back when the activity is started, to initialize the view
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            glRenderer = new MyGLRenderer(this);
            glView = new GLSurfaceView(this);           // Allocate a GLSurfaceView
            glView.setRenderer(glRenderer); // Use a custom renderer
            this.setContentView(glView);                // This activity sets to GLSurfaceView
            this.previousX = -1;
            this.previousY = -1;
        }

        // Call back when the activity is going into the background
        @Override
        protected void onPause() {
            super.onPause();
            glView.onPause();
        }

        // Call back after onPause()
        @Override
        protected void onResume() {
            super.onResume();
            glView.onResume();
        }

        @Override
        public boolean onTouchEvent(MotionEvent e)
        {
            float x = e.getX();
            float y = e.getY();

            if(previousX == -1){
                previousX = x;
            }
            if(previousY == -1){
                previousY = y;
            }
            float dx = previousX - x;
            float dy = previousY - y;
            float TOUCH_SCALE = 0.2f;

//*******************************************************************************************************
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (y < glView.getHeight() / 4) {

                    if (x < glView.getWidth() / 2) {
                        glRenderer.goBack();
                    } else if (x > glView.getWidth() / 2) {
                        glRenderer.goForward();
                    }
                }
                    if (y > glView.getHeight() / 4 && y < glView.getHeight() / 2 ) {
                    if (x < glView.getWidth() / 2) {
                        glRenderer.strafeRight();
                    } else if (x > glView.getWidth() / 2) {
                        glRenderer.strafeLeft();
                    }
                } else if (y > glView.getHeight() / 2) {
                        glRenderer.addToYrot(dx*TOUCH_SCALE);
                        glRenderer.addToXrot(dy*TOUCH_SCALE);
                }
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (x < glView.getWidth() / 4) {

                    if (y < glView.getHeight() / 2) {
                        glRenderer.goBack();
                    } else if (y > glView.getHeight() / 2) {
                        glRenderer.goForward();
                    }
                }
                if (x > glView.getWidth() / 4 && x < glView.getWidth() / 2 ) {
                    if (y < glView.getHeight() / 2) {
                        glRenderer.strafeRight();
                    } else if (y > glView.getHeight() / 2) {
                        glRenderer.strafeLeft();
                    }
                } else if (x > glView.getWidth() / 2) {
                    glRenderer.addToYrot(dx*TOUCH_SCALE);
                    glRenderer.addToXrot(dy*TOUCH_SCALE);
                }
            }
//*******************************************************************************************************
            previousY = y;
            previousX = x;
            return true;
        }
    }