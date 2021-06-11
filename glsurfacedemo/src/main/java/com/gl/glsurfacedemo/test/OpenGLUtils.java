package com.gl.glsurfacedemo.test;

import android.opengl.GLES20;

/**
 * @ClassName: OpenGLUtils
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/9 11:57
 * @UpdateRemark:
 * @Version:
 */
public class OpenGLUtils {


    /**
     *
     * @param type GLES20.GL_VERTEX_SHADER、GLES20.GL_FRAGMENT_SHADER
     * @param shaderCode
     * @return
     */
    public static int loadShader(int type,String shaderCode){
        //得到一个着色器的ID。主要是对ID进行操作
        int shaderObjectId = GLES20.glCreateShader(type);
        if (shaderObjectId!=0){
            //上传代码
            GLES20.glShaderSource(shaderObjectId,shaderCode);
            //编译代码.根据刚刚和代码绑定的ShaderObjectId进行编译
            GLES20.glCompileShader(shaderObjectId);

            int[] status = new int[1];
            //调用getShaderIv ，传入GL_COMPILE_STATUS进行查询
            GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS,status,0);
            //等于0。则表示失败
            if (status[0] == 0){
                //失败的话，需要释放资源，就是删除这个引用
                GLES20.glDeleteShader(shaderObjectId);
                return 0;
            }
        }
        return shaderObjectId;
    }

    /**
     *
     * @param vertexShader
     * @param fragmentShader
     * @return
     */
    public static int createProgram(int vertexShader, int fragmentShader){
        //创建空的 OpenGL ES 程序
        int mProgram = GLES20.glCreateProgram();
        //将顶点着色器添加到程序中
        GLES20.glAttachShader(mProgram,vertexShader);
        //将片段着色器添加到程序中
        GLES20.glAttachShader(mProgram,fragmentShader);
        //创建 OpenGL ES 程序可执行文件
        GLES20.glLinkProgram(mProgram);
        return mProgram;
    }



}


