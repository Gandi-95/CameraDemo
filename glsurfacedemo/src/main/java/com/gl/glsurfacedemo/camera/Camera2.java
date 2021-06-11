package com.gl.glsurfacedemo.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Camera2
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/7 18:05
 * @UpdateRemark:
 * @Version:
 */
public class Camera2 {

    Context mContext;
    CameraManager mCameraManager;
    Size mPreviewSize;
    int mCameraId;
    TextureView mTextureView;

    private Handler mHandler;
    private HandlerThread mThreadHandler;

    CameraDevice mCameraDevice;
    CaptureRequest.Builder mPreviewBuilder;
    CameraCaptureSession mCameraCaptureSession;

    public Camera2(Context context, int cameraId, TextureView textureView) {
        mContext = context;

        mPreviewSize = new Size(1280,720);
        mCameraId = cameraId;
        mTextureView = textureView;


        mThreadHandler = new HandlerThread("CAMERA2");
        mThreadHandler.start();
        mHandler = new Handler(mThreadHandler.getLooper());


    }


    @SuppressLint("MissingPermission")
    public void openCamera(){
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);

        try {
            mCameraManager.openCamera(mCameraId+"",mCameraDeviceCallback,mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    CameraDevice.StateCallback mCameraDeviceCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };

    private void startPreview(){
        try {
            List<Surface> surfaces = new ArrayList<Surface>();

            SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(),mPreviewSize.getHeight());
            Surface surface = new Surface(surfaceTexture);

            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE,0);
            mPreviewBuilder.addTarget(surface);

            surfaces.add(surface);

            mCameraDevice.createCaptureSession(surfaces,mCameraCaptureSessionCallback,mHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    CameraCaptureSession.StateCallback mCameraCaptureSessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            mCameraCaptureSession = session;
            updatePreview();
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {

        }
    };

    private void updatePreview(){

        try {
            mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_MODE_OFF);
            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH);
            mCameraCaptureSession.setRepeatingRequest(mPreviewBuilder.build(),null,mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    public void closeCamera(){
        closePreviewSession();
        if (mCameraDevice!=null){
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    private void closePreviewSession() {
        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }

    }
}
