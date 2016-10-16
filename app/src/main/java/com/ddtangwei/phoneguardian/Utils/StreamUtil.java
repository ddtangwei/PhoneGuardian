package com.ddtangwei.phoneguardian.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ddtangwei on 2016/10/15.
 */

public class StreamUtil {

    /**
     * 将流转换成字符串
     * @param is
     * @return
     */

    public static String StreamToString(InputStream is){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];

        int temp = -1;

        try {
            while ((temp = is.read(buf))!= -1){

                baos.write(buf,0,temp);

                return baos.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                is.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
