package com.gl.glsurfacedemo.render.filter;

import android.content.Context;
import android.opengl.GLES11Ext;
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

    private static final String TAG = "CameraFilter";

    private Context mContext;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;

    private int mProgram;

    private int vPosition;
    private int vCoord;
    private int vMatrix;
    private int vTexture;

    protected int mOutputHeight;
    protected int mOutputWidth;
    protected int y;
    protected int x;

    protected int[] mFrameBuffers;
    protected int[] mFBOTextures;

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

        /**
         * 着色器中 uniform 全局变量 使用glGetUniformLocation
         * 着色器中 attribute 局部变量 glGetAttribLocation
         */
        //获取顶点着色器的句柄
        vPosition = GLES20.glGetAttribLocation(mProgram,"vPosition");
        vCoord = GLES20.glGetAttribLocation(mProgram,"vCoord");
        vMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        vTexture = GLES20.glGetUniformLocation(mProgram,"vTexture");
    }

    public void prepare(int width, int height, int x, int y) {
        mOutputWidth = width;
        mOutputHeight = height;
        this.x = x;
        this.y = y;
        loadFOB();
    }

    private void loadFOB() {

        if (mFrameBuffers != null) {
            destroyFrameBuffers();
        }
        //创建FrameBuffer
        mFrameBuffers = new int[1];
        GLES20.glGenFramebuffers(mFrameBuffers.length, mFrameBuffers, 0);
        //穿件FBO中的纹理
        mFBOTextures = new int[1];
        OpenGLUtils.glGenTextures(mFBOTextures);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFBOTextures[0]);
        //指定FBO纹理的输出图像的格式 RGBA
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mOutputWidth, mOutputHeight,
                0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);

        //将fbo绑定到2d的纹理上
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, mFBOTextures[0], 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

    }

    public int onDrawFrame(int textureId,float[] matrix) {

        //锁定绘制的区域  绘制是从左下角开始的
        GLES20.glViewport(0, 0, mOutputWidth, mOutputHeight);
        //绑定FBO，在FBO上操作
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);

        //将程序添加到 OpenGL ES 环境
        GLES20.glUseProgram(mProgram);

        //赋值vPosition
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2,GLES20.GL_FLOAT, false,0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);
        //赋值vCoord
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2,GLES20.GL_FLOAT, false,0, textureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);
        //赋值vMatrix
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, matrix, 0);


        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //SurfaceTexture 对应 GL_TEXTURE_EXTERNAL_OES 类型 , 绑定纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        //赋值vTexture
        GLES20.glUniform1i(vTexture, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);

        return mFBOTextures[0];
    }


    public void destroyFrameBuffers() {
        //删除fbo的纹理
        if (mFBOTextures != null) {
            GLES20.glDeleteTextures(1, mFBOTextures, 0);
            mFBOTextures = null;
        }
        //删除fbo
        if (mFrameBuffers != null) {
            GLES20.glDeleteFramebuffers(1, mFrameBuffers, 0);
            mFrameBuffers = null;
        }
    }

    public void release() {
        GLES20.glDeleteProgram(mProgram);
    }
}
