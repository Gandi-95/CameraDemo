package com.gl.glsurfacedemo.render.filter;

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

    private FloatBuffer vertexBuffer;

    public CameraFilter() {
        vertexBuffer = ByteBuffer.allocateDirect(4*2*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }
}
