package com.gl.glsurfacedemo.test;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @ClassName: TestGLRender
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/9 11:23
 * @UpdateRemark:
 * @Version:
 */
public class TestGLRender implements GLSurfaceView.Renderer {

    Triangle mTriangle;



    TriangleMatrix mTriangleMatrix;
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private float[] rotationMatrix = new float[16];


    public volatile float mAngle;

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0,0,0,0);
//        mTriangle = new Triangle();
        mTriangleMatrix = new TriangleMatrix();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        float ratio = (float) width / height;
        if (mTriangleMatrix!=null){
            //填充了一个投影矩阵 mProjectionMatrix，之后可以将其与 onDrawFrame() 方法中的相机视图转换合并
            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        if (mTriangle!=null){
            mTriangle.draw();
        }

        if (mTriangleMatrix!=null) {
            // 设置相机位置（视图矩阵
            Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            // 计算投影和视图变换
            Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

            //旋转
//            long time = SystemClock.uptimeMillis() % 4000L;
//            float angle = 0.090f * ((int) time);

            float[] scratch = new float[16];
            //为三角形创建旋转变换
            Matrix.setRotateM(rotationMatrix, 0, mAngle, 0, 0, -1.0f);
            //将旋转矩阵与投影和相机视图结合起来
            // 注意 vPMatrix 因子*必须是第一个*
            // 为了矩阵乘积是正确的。
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);
//            mTriangleMatrix.draw(vPMatrix);
            mTriangleMatrix.draw(scratch);
        }
    }
}
