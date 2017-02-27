package com.design.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionUtils {
	
	public static boolean isEmpty(Collection<?> collection) {
		return !isNotEmpty(collection);
	}
	
	public static boolean isNotEmpty(Collection<?> collection){
		return collection!=null&&collection.size()!=0;
	}
	public static boolean isNotEmptyDataNotNull(Collection<?> collection){
		if(isNotEmpty(collection)){
			Iterator<?> it = collection.iterator();
			while(it.hasNext()){
				if(it.next()==null){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 此方法只支持可变类型
	 * @param fromList
	 * @param toClazz
	 * @param listTransfer
	 * @return
	 */
	public static <U,V> List<V> transfer(List<U> fromList,Class<V> toClazz,MyTransfer<U,V> myTransfer) {
		List<V> result = new ArrayList<V>();
		for(U u:fromList){
			V v = (V) DozerUtils.transfer(u, toClazz);
			myTransfer.transfer(u, v);
			result.add(v);
		}
		return result;
	}
	
}
