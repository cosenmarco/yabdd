package yabdd.rules;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * It helps, hopefully.
 * Created by Marco Cosentino on 25/05/15.
 */
public class RuleHelper {
    public static Object convertMatchBasedOnParameterType(Class<?> parameterType, String match) {
        Object convertedValue = fromStringToSupportedType(parameterType, match);
        if(convertedValue != null) {
            return convertedValue;
        } else if(List.class.isAssignableFrom(parameterType)) {
            // Only list of Strings can be supported due to limitations in reflection
            List<Object> result = new ArrayList<Object>();
            Iterable<String> parts = Splitter.on(',').omitEmptyStrings().split(match);
            return ImmutableList.copyOf(parts);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Object fromStringToSupportedType(Class<?> aType, String aString) {
        if(String.class.isAssignableFrom(aType)) {
            return aString;
        } else if(Boolean.class.isAssignableFrom(aType)){
            return Boolean.valueOf(aString);
        } else if(Byte.class.isAssignableFrom(aType)){
            return Byte.valueOf(aString);
        } else if(Short.class.isAssignableFrom(aType)){
            return Short.valueOf(aString);
        } else if(Integer.class.isAssignableFrom(aType)){
            return Integer.valueOf(aString);
        } else if(Long.class.isAssignableFrom(aType)){
            return Long.valueOf(aString);
        } else if(Float.class.isAssignableFrom(aType)){
            return Float.valueOf(aString);
        } else if(Double.class.isAssignableFrom(aType)) {
            return Double.valueOf(aString);
        }
        return null;
    }

    public static String getFullMethodName(Method aMethod) {
        return aMethod.getDeclaringClass().getCanonicalName() + "." + aMethod.getName() + "()";
    }


}
