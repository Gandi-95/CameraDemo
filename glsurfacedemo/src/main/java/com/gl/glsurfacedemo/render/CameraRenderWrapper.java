package com.gl.glsurfacedemo.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.gl.glsurfacedemo.camera.Camera2;
import com.gl.glsurfacedemo.render.filter.CameraFilter;
import com.gl.glsurfacedemo.render.filter.ScreenFilter;

import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @ClassName: CameraRenderWrapper
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/24 20:09
 * @UpdateRemark:
 * @Version:
 */
public class CameraRenderWrapper implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = "CameraRenderWrapper";

    GLSurfaceView mGLSurfaceView;
    Context mContext;
    Camera2 mCamera;
    int[] mTextures;
    SurfaceTexture mSurfaceTexture;
    CameraFilter cameraFilter;
    ScreenFilter screenFilter ;

    float[] matrix = new float[16];
    private int screenSurfaceWid;
    private int screenSurfaceHeight;
    private int screenX;
    private int screenY;

    public CameraRenderWrapper(GLSurfaceView glSurfaceView) {
        mGLSurfaceView = glSurfaceView;
        mContext = glSurfaceView.getContext();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated: ");
        mCamera = new Camera2(mContext);
        mTextures = new int[1];
        //创建一个纹理
        GLES20.glGenTextures(mTextures.length, mTextures, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextures[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);


        //将纹理和离屏buffer绑定
        mSurfaceTexture = new SurfaceTexture(mTextures[0]);
        mSurfaceTexture.setOnFrameAvailableListener(this);

        //使用fbo 将samplerExternalOES 输入到sampler2D中
        cameraFilter = new CameraFilter(mContext);
        //负责将图像绘制到屏幕上
        screenFilter = new ScreenFilter(mContext);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i(TAG, "onSurfaceChanged: ");
        mCamera.setCameraId(2);
        mCamera.setSurfaceTexture(mSurfaceTexture);
        mCamera.openCamera();

        int mPreviewHeight = mCamera.getPreviewSize().getHeight();
        int mPreviewWdith = mCamera.getPreviewSize().getWidth();

        float scaleX = (float) mPreviewHeight / (float) width;
        float scaleY = (float) mPreviewWdith / (float) height;

        float max = Math.max(scaleX, scaleY);

        screenSurfaceWid = (int) (mPreviewHeight / max);
        screenSurfaceHeight = (int) (mPreviewWdith / max);
        screenX = width - (int) (mPreviewHeight / max);
        screenY = height - (int) (mPreviewWdith / max);

        Log.i(TAG, "onSurfaceChanged: width="+width+" height="+height+" scaleX="+scaleX+" scaleY="+scaleY
                +" screenSurfaceWid="+screenSurfaceWid+" screenSurfaceHeight="+screenSurfaceHeight
                +" screenX="+screenX+" screenY="+screenY);
//        width=1260 height=656 scaleX=0.5714286 scaleY=1.9512196 screenSurfaceWid=369 screenSurfaceHeight=656 screenX=891 screenY=0

        //prepare 传如 绘制到屏幕上的宽 高 起始点的X坐标 起使点的Y坐标
//        cameraFilter.prepare(screenSurfaceWid, screenSurfaceHeight, screenX, screenY);
//        screenFilter.prepare(screenSurfaceWid, screenSurfaceHeight, screenX, screenY);

        cameraFilter.prepare(1280, 720, 0, 0);
        screenFilter.prepare(1280, 720, 0, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        Log.i(TAG, "onDrawFrame: ");
        // 配置屏幕
        //清理屏幕 :告诉opengl 需要把屏幕清理成什么颜色
        GLES20.glClearColor(0, 0, 0, 0);
        //执行上一个：glClearColor配置的屏幕颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //更新获取一张图
        mSurfaceTexture.updateTexImage();

        mSurfaceTexture.getTransformMatrix(matrix);
        Log.i(TAG, "onDrawFrame: matrix = "+ Arrays.toString(matrix));

        int textureId = cameraFilter.onDrawFrame(mTextures[0],matrix);
        Log.i(TAG, "onDrawFrame: textureId = "+textureId);
        screenFilter.onDrawFrame(textureId);
    }



    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.i(TAG, "onFrameAvailable: ");
        mGLSurfaceView.requestRender();
    }
}
