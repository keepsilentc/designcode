package com.design.common.utils;

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.design.common.assist.DesignException;
import com.google.common.collect.Maps;
/**
 * 校验工具类
 * @author tc
 *
 */
public class ValidateUtils {
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	public static <T> void validateEntiryThrows(T obj) throws DesignException{
		validateEntiryThrows(obj,Default.class);
	}
	
	public static <T> void validateEntiryThrows(T obj,Class<?> clazz) throws DesignException{
		Set<ConstraintViolation<T>> set = validator.validate(obj, clazz);
		if(CollectionUtils.isNotEmpty(set)){
			StringBuilder builder = new StringBuilder();
			for(ConstraintViolation<T> tmp:set){
				builder.append(tmp.getPropertyPath());
				builder.append(" ");
				builder.append(tmp.getMessage());
				builder.append(",");
			}
			builder.deleteCharAt(builder.length()-1);
			throw new DesignException(builder.toString());
		}
		
	}
	
	public static <T> ValidationResult validateEntiry(T obj){
		return validateEntiry(obj,Default.class);
	}
	public static <T> ValidationResult validateEntiry(T obj,Class<?> clazz){
		Set<ConstraintViolation<T>> set = validator.validate(obj, clazz);
		return generateValidationResult(set);
	}
	public static <T> ValidationResult validateProperty(T obj,String propertyName){
		return validateProperty(obj, Default.class, propertyName);
	}
	public static <T> ValidationResult validateProperty(T obj,Class<?> clazz,String propertyName){
		Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, clazz);
		return generateValidationResult(set);
	}
	
	
	private static <T> ValidationResult generateValidationResult(Set<ConstraintViolation<T>> set) {
		ValidationResult result = new ValidationResult();
		if(CollectionUtils.isNotEmpty(set)){
			result.setHasErrors(true);
			Map<String,String> errorMsg = Maps.newHashMap();
			for(ConstraintViolation<T> tmp:set){
				errorMsg.put(tmp.getPropertyPath().toString(), tmp.getMessage());
			}
			result.setErrorMsg(errorMsg);
		}
		return result;
	}
}
