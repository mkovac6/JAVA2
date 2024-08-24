package hr.algebra.trivialpursuit.trivialpursuit;

import java.lang.reflect.Method;

public class DocumentationGen {

    public static void main(String[] args) {
        generateDocumentation(HelloController.class);
    }

    public static String generateDocumentation(Class<?> clazz) {

        StringBuilder documentation = new StringBuilder();
        documentation.append("Class : ").append(clazz.getSimpleName()).append("\n\n");


        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            documentation.append("Method: ").append(method.getName()).append("\n");
            documentation.append("Return Type: ").append(method.getReturnType().getName()).append("\n");
            documentation.append("Modifiers: ").append(java.lang.reflect.Modifier.toString(method.getModifiers())).append("\n");
            documentation.append("Parameter Types: \n");
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> paramType : parameterTypes) {
                documentation.append(" - ").append(paramType.getName()).append("\n");
            }
            documentation.append("\n");
        }
        return documentation.toString();
    }
}
