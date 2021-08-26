package com.inlacou.inkkotlinextensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

fun Any.toJson(): String {
	return Gson().toJson(this)
}

inline fun <reified T: Any> String.fromJson(): T? = try {
	val aux: T? = if(this=="" || this=="{}") null else Gson().fromJson(this, typeToken<T>())
	aux
} catch (e: Exception) {
	System.err.println("Error raised during fromJson: ${e.message} ${e.stackTrace}")
	null
}

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
inline fun <reified T: Any> typeToken(): Type {
	val type = gsonTypeToken<T>()
	
	if (type is ParameterizedType && type.isWildcard())
		return type.rawType
	
	return removeTypeWildcards(type)
}

fun ParameterizedType.isWildcard() : Boolean {
	var hasAnyWildCard = false
	var hasBaseWildCard = false
	var hasSpecific = false
	
	val cls = this.rawType as Class<*>
	cls.typeParameters.forEachIndexed { i, variable ->
		val argument = actualTypeArguments[i]
		
		if (argument is WildcardType) {
			val hit = variable.bounds.firstOrNull { it in argument.upperBounds }
			if (hit != null) {
				if (hit == Any::class.java)
					hasAnyWildCard = true
				else
					hasBaseWildCard = true
			}
			else
				hasSpecific = true
		}
		else
			hasSpecific = true
		
	}
	
	require(!(hasAnyWildCard && hasSpecific)) { "Either none or all type parameters can be wildcard in $this" }
	
	return hasAnyWildCard || (hasBaseWildCard && !hasSpecific)
}

@Suppress("PROTECTED_CALL_FROM_PUBLIC_INLINE")
inline fun <reified T: Any> gsonTypeToken(): Type = object : TypeToken<T>() {} .type

fun removeTypeWildcards(type: Type): Type {
	return if (type is ParameterizedType) {
		val arguments = type.actualTypeArguments
				.map { if (it is WildcardType) it.upperBounds[0] else it }
				.map { removeTypeWildcards(it) }
				.toTypedArray()
		TypeToken.getParameterized(type.rawType, *arguments).type
	} else type
}
