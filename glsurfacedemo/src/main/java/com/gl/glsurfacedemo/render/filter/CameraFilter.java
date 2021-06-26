package com.gl.glsurfacedemo.render.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.gl.glsurfacedemo.R;
import com.gl.glsurfacedemo.test.OpenGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @ClassName: CameraFilter
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/24 20:24
 * @UpdateRemark:
 * @Version:
 */
public class CameraFilter {


    private Context mContext;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;

    private int mProgram;

    private int vPosition;
    private int vCoord;
    private int vMatrix;

    float[] VERTEX = {
            -1.0f, 1.0f,
            1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f
    };

    float[] TEXTURE = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
    };


    public CameraFilter(Context context) {
        mContext = context;

        vertexBuffer = ByteBuffer.allocateDirect(VERTEX.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(VERTEX);

        textureBuffer = ByteBuffer.allocateDirect(TEXTURE.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureBuffer.put(TEXTURE);


        initlize();

    }

    private void initlize(){
        String vertexShaderCode = OpenGLUtils.readRawShaderFile(mContext, R.raw.camera_vertex);
        String fragmentShaderCode = OpenGLUtils.readRawShaderFile(mContext, R.raw.camera_frag);

        int vertexShader = OpenGLUtils.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = OpenGLUtils.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        mProgram = OpenGLUtils.createProgram(vertexShader,fragmentShader);

        //获取顶点着色器的句柄
        vPosition = GLES20.glGetAttribLocation(mProgram,"vPosition");
        vCoord = GLES20.glGetAttribLocation(mProgram,"vCoord");
        vCoord = GLES20.glGetAttribLocation(mProgram,"vMatrix");
    }


    public int onDrawFrame(int textureId) {

        //将程序添加到 OpenGL ES 环境
        GLES20.glUseProgram(mProgram);

        //赋值vPosition
        vertexBuffer.position(0);
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(vPosition);
        //准备三角坐标数据
        GLES20.glVertexAttribPointer(vPosition, 2,GLES20.GL_FLOAT, false,0, vertexBuffer);


        return 0;
    }
}
