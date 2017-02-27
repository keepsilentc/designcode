package com.design.common.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.design.common.assist.DesignException;
import com.google.common.base.Throwables;

public class ImageUtil {
	
	private static Logger log = LoggerFactory.getLogger(ImageUtil.class);
	
	 public static BufferedImage getImage(BufferedImage image){  
	     int width=image.getWidth();  
	     int height=image.getHeight();
	     BufferedImage toImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB); 
	     Graphics2D g2D = (Graphics2D) toImage.getGraphics();
	     g2D.drawImage(image, 0, 0, null);
	     return getConvertedImage(toImage);  
	 }
	 
	 /** 
	  * 判断当前像素是否为黑色不透明的像素（-16777216） 
	  * @param pixel 要判断的像素 
	  * @return 是背景像素返回true，否则返回false 
	  */  
	 private static boolean isBackPixel(int pixel){  
	     int back[]={-16777216};  
	     for(int i=0;i<back.length;i++){  
	         if(back[i]==pixel) return true;  
	     }  
	     return false;  
	 }
	 /** 
	  * 将背景为黑色不透明的图片转化为背景透明的图片 
	  * @param image 背景为黑色不透明的图片（用555格式转化后的都是黑色不透明的） 
	  * @return 转化后的图片 
	  */  
	 private static BufferedImage getConvertedImage(BufferedImage image){  
	     int width=image.getWidth();  
	     int height=image.getHeight();  
	     BufferedImage convertedImage=null;  
	     Graphics2D g2D=null;  
	     //采用带1 字节alpha的TYPE_4BYTE_ABGR，可以修改像素的布尔透明  
	     convertedImage=new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);  
	     g2D = (Graphics2D) convertedImage.getGraphics();  
	     g2D.drawImage(image, 0, 0, null);  
	     //像素替换，直接把背景颜色的像素替换成0  
	     for(int i=0;i<width;i++){  
	         for(int j=0;j<height;j++){  
	             int rgb=convertedImage.getRGB(i, j);  
	             if(isBackPixel(rgb)){  
	                 convertedImage.setRGB(i, j,0);  
	             }  
	         }  
	     }  
	     g2D.drawImage(convertedImage, 0, 0, null);  
	     return convertedImage;  
	 } 
	 
	 public static String getImageType(InputStream ins){
		 byte[] filedata = new byte[10];
		 try {
			ins.read(filedata);
		} catch (IOException e) {
			log.info("获取图片类型异常");
			new DesignException(Throwables.getStackTraceAsString(e));
		}finally{
			try {
				ins.close();
			} catch (IOException e) {
				
			}
		}
		 /*GIF: 012
		 JFIF(JPG): 6789
		 PNG: 123
		 这样我们可以通过判断这几个字节值来得到Image文件格式: */
		 String type = "";
		 byte b0 = filedata[0];
		 byte b1 = filedata[1];
		 byte b2 = filedata[2];
		 byte b3 = filedata[3];
		 byte b6 = filedata[6];
		 byte b7 = filedata[7];
		 byte b8 = filedata[8];
		 byte b9 = filedata[9];
		 // GIF
		 if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F')
		 type = "GIF";
		 // PNG
		 else if (b1 == (byte) 'P' && b2 == (byte) 'N' && b3 == (byte) 'G')
		 type = "PNG";
		 // JPG
		 else if (b6 == (byte) 'J' && b7 == (byte) 'F' && b8 == (byte) 'I' && b9 == (byte) 'F')
		 type = "JPG";
		 else
		 type = "Unknown";
		return type;
	 }
}
