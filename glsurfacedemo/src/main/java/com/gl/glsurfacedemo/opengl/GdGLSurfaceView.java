package com.gl.glsurfacedemo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @ClassName: GdGlSurfaceView
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/8 16:20
 * @UpdateRemark:
 * @Version:
 */
public class GdGLSurfaceView extends GLSurfaceView {

    GdRenderer mGdRenderer;

    public GdGLSurfaceView(Context context) {
        super(context);
    }

    public GdGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        setEGLContextClientVersion(2);
        mGdRenderer = new GdRenderer();
        setRenderer(mGdRenderer);

        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
