package com.gl.glsurfacedemo;



import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import com.gl.glsurfacedemo.camera.Camera2;
import com.gl.glsurfacedemo.view.CameraGlSurfaceView;

public class MainActivity extends AppCompatActivity {

    Camera2 camera2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CameraGlSurfaceView mTextureView = findViewById(R.id.tv_camera);
//        camera2 = new Camera2(this,2,mTextureView.getSurfaceTexture());
//        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//            @Override
//            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//                camera2.openCamera();
//            }
//
//            @Override
//            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//            }
//
//            @Override
//            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//                return false;
//            }
//
//            @Override
//            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//            }
//        });
    }
}
