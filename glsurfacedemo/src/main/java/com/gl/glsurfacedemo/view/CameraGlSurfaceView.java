package com.gl.glsurfacedemo.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.gl.glsurfacedemo.opengl.GdRenderer;
import com.gl.glsurfacedemo.render.CameraRenderWrapper;

/**
 * @ClassName: CameraGlSurfaceView
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/24 20:05
 * @UpdateRemark:
 * @Version:
 */
public class CameraGlSurfaceView extends GLSurfaceView {

    CameraRenderWrapper mRender;

    public CameraGlSurfaceView(Context context) {
        super(context);
        init();
    }

    public CameraGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        mRender = new CameraRenderWrapper();
        setRenderer(mRender);

        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
