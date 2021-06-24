package com.gl.glsurfacedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gl.glsurfacedemo.test.TestGLSurfaceView;

public class TestActivity extends AppCompatActivity {


    TestGLSurfaceView mTestGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        mTestGLSurfaceView = new TestGLSurfaceView(this);
//        setContentView(mTestGLSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mTestGLSurfaceView.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();
//        mTestGLSurfaceView.onResume();
    }
}
