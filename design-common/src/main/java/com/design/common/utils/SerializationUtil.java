package com.design.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtil {
    /**
     * 序列化
     * @param <T>
     * 
     * @param object
     * @return
     */
    public static <T> byte[] serialize(T object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
        	e.printStackTrace();
        }finally{
        	closeStream(baos);
        	closeStream(oos);
        }
        return null;
    }
    public static void closeStream(Closeable stream){
    	if(stream==null){
    		return ;
    	}
    	try {
    		stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * 反序列化
     * 
     * @param bytes
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
        	e.printStackTrace();
        }finally{
        	closeStream(ois);
        	closeStream(bais);
        }
        return null;
    }
}
