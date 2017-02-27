package com.design.common.utils;

import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

public class DozerUtils {
	private static DozerBeanMapper dozer = new DozerBeanMapper();
	private DozerUtils(){
		
	}
	/**
	 * 基于Dozer转换对象的类型.
	 */
	public static <T> T transfer(Object source,Class<T> destinationClass){
		if(source==null){
			return null;
		}
		return dozer.map(source, destinationClass);
	}
	/**
	 * 基于Dozer转换Collection中对象的类型.
	 */
	public static <T> List<T> transferList(Collection<?> source,Class<T> destinationClass){
		if(source==null){
			return null;
		}
		List<T> destinationList = Lists.newArrayList();
		for(Object tmp:source){
			T destinationObject = dozer.map(tmp, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}
	
	/**
	 * 基于Dozer将对象A的值拷贝到对象B中.
	 */
	public static void copy(Object source,Object destination){
		dozer.map(source, destination);
	}
	
	/**
	 * 基于Dozer转换对象的类型.
	 * @param <U>
	 */
	public static <T, U> T transfer(U source,Class<T> destinationClass,MyTransfer<U, T> myTransfer){
		if(source==null){
			return null;
		}
		T to = dozer.map(source, destinationClass);
		myTransfer.transfer(source,to);
		return to;
	}
	
	
}
