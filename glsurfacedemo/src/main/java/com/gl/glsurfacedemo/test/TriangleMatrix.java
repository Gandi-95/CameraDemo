package com.gl.glsurfacedemo.test;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @ClassName: TriangleMatrix
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/15 19:49
 * @UpdateRemark:
 * @Version:
 */
public class TriangleMatrix {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer vertexBuffer;

    // 此数组中每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f,  0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    //使用红色、绿色、蓝色和 alpha（不透明度）值设置颜色
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    private int mProgram;
    private int positionHandle;
    private int colorHandle;
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private int vPMatrixHandle;

    public TriangleMatrix() {
        //初始化形状坐标的顶点字节缓冲区 (坐标值的数量 * 每个浮点数 4 个字节)
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length*4);
        //使用设备硬件的本机字节顺序
        bb.order(ByteOrder.nativeOrder());
        //从 ByteBuffer 创建一个浮点缓冲区
        vertexBuffer = bb.asFloatBuffer();
        //将坐标添加到 FloatBuffer
        vertexBuffer.put(triangleCoords);
        //设置缓冲区读取第一个坐标
        vertexBuffer.position(0);


        int vertexShader = OpenGLUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = OpenGLUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = OpenGLUtils.createProgram(vertexShader, fragmentShader);

    }

    public void draw(float[] mvpMatrix){
        //将程序添加到 OpenGL ES 环境
        GLES20.glUseProgram(mProgram);
        //获取顶点着色器的 vPosition 成员的句柄
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(positionHandle);
        //准备三角坐标数据
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,GLES20.GL_FLOAT, false,vertexStride, vertexBuffer);
        //获取片段着色器的 vColor 成员的句柄
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        /***************
         **新增代码******
         *************/
        //获取形状变换矩阵的句柄
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //将投影和视图转换传递给着色器
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        /***************
         **新增代码******
         *************/


        //画三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁用顶点数组
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
