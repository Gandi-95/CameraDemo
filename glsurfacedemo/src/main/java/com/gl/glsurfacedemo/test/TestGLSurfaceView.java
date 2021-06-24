package com.gl.glsurfacedemo.test;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.gl.glsurfacedemo.opengl.GdRenderer;

/**
 * @ClassName: TestGLSurfaceView
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/15 14:33
 * @UpdateRemark:
 * @Version:
 */
public class TestGLSurfaceView extends GLSurfaceView {


    TestGLRender mTestGLRender;

    public TestGLSurfaceView(Context context) {
        super(context);
        init();
    }

    public TestGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        mTestGLRender = new TestGLRender();
        setRenderer(mTestGLRender);

//        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float previousX;
    private float previousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - previousX;
                float dy = y - previousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mTestGLRender.setAngle(
                        mTestGLRender.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        previousX = x;
        previousY = y;
        return true;
    }
}
