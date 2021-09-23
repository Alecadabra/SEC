import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Reflector
{
    private static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
        try
        {
            Class<?> cls = getClassFromInput();

            Constructor<?> constructor = getStringConstructor(cls);
            
            Object obj = getInstanceFrom(constructor);

            List<Method> intMethods = getAllInstanceIntMethods(cls);

            Method selectedMethod = selectMethod(intMethods);

            invokeMethodWithInput(selectedMethod, obj);

            System.out.println("Done! Exiting.");
        }
        catch(NullPointerException e)
        {
            System.out.println("\nThere was an error. Exiting.");
        }

        sc.close();
    }

    private static Class<?> getClassFromInput()
    {
        String className = null;
        Class<?> cls = null;

        try
        {
            System.out.println("Enter a java class name");
            className = sc.nextLine();

            cls = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            System.out.println(
                "[Err] The given class name \"" + className + 
                "\" does not exist"
            );
            className = null;
            cls = null;
        }

        return cls;
    }

    private static Constructor<?> getStringConstructor(Class<?> cls)
    {
        Constructor<?> constructor = null;
        try
        {
            constructor = cls.getConstructor(String.class);
        }
        catch(NoSuchMethodException e)
        {
            System.out.println(
                "[Err] This class has no constructor that takes " +
                "in a single String as an argument"
            );
        }
        catch (SecurityException e)
        {
            System.out.println(
                "[Err] This class has no public constructor that " + 
                "takes in a single String as an argument"
            );
        }

        return constructor;
    }

    private static Object getInstanceFrom(Constructor<?> constructor)
    {
        Object obj = null;

        try
        {
            System.out.println(
                "Enter string to pass into " + 
                constructor.getName() + " constructor"
            );
            String inputString = sc.nextLine();

            obj = constructor.newInstance(inputString);
        }
        catch (InstantiationException | IllegalArgumentException e)
        {
            System.out.println("[Err] The given class cannot be instatiated");
        }
        catch (IllegalAccessException e)
        {
            System.out.println(
                "[Err] Insufficient permissions to instatiate " + 
                "the given class"
            );
        }
        catch (InvocationTargetException e)
        {
            System.out.println(
                "[Err] The constructor threw an exception for " +
                "the given input string"
            );
        }

        return obj;
    }

    private static List<Method> getAllInstanceIntMethods(Class<?> cls)
    {
        Method[] all = cls.getMethods();
        List<Method> intMethods = new LinkedList<>();

        for (Method method : all)
        {
            if(
                method.getParameterCount() == 1 &&
                method.getParameterTypes()[0] == int.class &&
                !Modifier.isStatic(method.getModifiers())
            )
            {
                intMethods.add(method);
            }
        }

        if (intMethods.size() == 0)
        {
            System.out.println(
                "[Err] There are no instance methods of this class with a " +
                "single int as a parameter"
            );
            intMethods = null;
        }

        return intMethods;
    }

    private static Method selectMethod(List<Method> all)
    {
        Method selected = null;
        String inputString;
        String names = "";

        for (Method method : all)
        {
            names = names + "  * " + method.getName() + "\n";
        }

        System.out.println(
            "Method names:\n" + names + 
            "Enter the name of the desired method to call"
        );
        inputString = sc.nextLine();

        for(Method method : all)
        {
            if(method.getName().equals(inputString))
            {
                selected = method;
            }
        }

        if(selected == null)
        {
            System.out.println("[Err] That is not a valid name");
        }

        return selected;
    }

    private static void invokeMethodWithInput(Method intMethod, Object obj)
    {
        Object noReturn = new Object();
        Object retVal = noReturn;

        System.out.println(
            "Enter integer parameter for " + intMethod.getName() + "(int)"
        );
        int inputInt = sc.nextInt();

        try
        {
            retVal = intMethod.invoke(obj, inputInt);
        }
        catch (IllegalAccessException e)
        {
            System.out.println(
                "[Err] Insufficient permissions to invoke " + 
                "the given method"
            );
        }
        catch (IllegalArgumentException | InvocationTargetException e)
        {
            System.out.println("[Err] The given method cannot be invoked");
        }

        if (retVal != noReturn)
        {
            System.out.println(
                "The method returned the following\n" + retVal
            );
        }
    }
}
