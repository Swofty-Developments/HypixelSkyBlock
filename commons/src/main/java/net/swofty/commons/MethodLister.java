package net.swofty.commons;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodLister {

    public static void listMethods(Class<?> clazz) {
        // Get all declared methods including public, protected, default (package) access,
        // and private methods, but excluding inherited methods.
        Method[] methods = clazz.getDeclaredMethods();

        System.out.println("Methods of " + clazz.getName() + ":");

        for (Method method : methods) {
            // Get the method name
            String methodName = method.getName();

            // Get the method modifiers
            String modifier = Modifier.toString(method.getModifiers());

            // Get the return type
            Class<?> returnType = method.getReturnType();
            String returnTypeName = returnType.getSimpleName();

            // Get the parameter types
            Class<?>[] parameterTypes = method.getParameterTypes();
            String parameters = getParameterString(parameterTypes);

            // Print method signature
            System.out.println(modifier + " " + returnTypeName + " " + methodName + "(" + parameters + ")");
        }
    }

    private static String getParameterString(Class<?>[] parameterTypes) {
        StringBuilder parameters = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> paramType = parameterTypes[i];
            parameters.append(paramType.getSimpleName());
            if (i < parameterTypes.length - 1) {
                parameters.append(", ");
            }
        }
        return parameters.toString();
    }
}
