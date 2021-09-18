package com.gl.glsurfacedemo.test;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @ClassName: OpenGLUtils
 * @Description: TODO
 * @Author: gandi
 * @CreateDate: 2021/6/9 11:57
 * @UpdateRemark:
 * @Version:
 */
public class OpenGLUtils {

    private static final String TAG = "OpenGLUtils";


    public static String readRawShaderFile(Context context, int shareId) {
        InputStream is = context.getResources().openRawResource(shareId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        StringBuffer sb = new StringBuffer();
        try {

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

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
            Log.i(TAG, "loadShader: status:"+status[0]);
            //等于0。则表示失败
            if (status[0] == 0){
                //失败的话，需要释放资源，就是删除这个引用
                GLES20.glDeleteShader(shaderObjectId);
                return 0;
            }
        }
        Log.i(TAG, "loadShader: shaderObjectId:"+shaderObjectId);
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


    public static void glGenTextures(int[] textures) {
        GLES20.glGenTextures(textures.length, textures, 0);


        for (int i = 0; i < textures.length; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);


            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);


            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        }
    }

}


