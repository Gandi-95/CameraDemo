package com.gl.glsurfacedemo.test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @ClassName: Triangle
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/9 11:20
 * @UpdateRemark:
 * @Version:
 */
public class Triangle {

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer vertexBuffer;

    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f,  0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    public Triangle() {
        //初始化形状坐标的顶点字节缓冲区 (坐标值的数量 * 每个浮点数 4 个字节)
        ByteBuffer bb = ByteBuffer.allocate(triangleCoords.length*4);
        //使用设备硬件的本机字节顺序
        bb.order(ByteOrder.nativeOrder());
        //从 ByteBuffer 创建一个浮点缓冲区
        vertexBuffer = bb.asFloatBuffer();
        //将坐标添加到 FloatBuffer
        vertexBuffer.put(triangleCoords);
        //设置缓冲区读取第一个坐标
        vertexBuffer.position(0);
    }
}
