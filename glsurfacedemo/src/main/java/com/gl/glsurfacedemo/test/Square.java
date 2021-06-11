package com.gl.glsurfacedemo.test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * @ClassName: Square
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/9 11:33
 * @UpdateRemark:
 * @Version:
 */
public class Square {

    FloatBuffer vertexBuffer;
    ShortBuffer drawListBuffer;


    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // 绘制顶点的顺序


    public Square() {
        vertexBuffer = ByteBuffer.allocate(squareCoords.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        drawListBuffer = ByteBuffer.allocate(drawOrder.length*2).order(ByteOrder.nativeOrder()).asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }
}
