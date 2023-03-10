

import java.util.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;


public class Unit {
    public static HashMap<String, Throwable> testClass(String name) {
        HashMap<String, Throwable> results = new HashMap<String, Throwable>();
        HashMap<String, ArrayList<Method>> methodMap = new HashMap<String, ArrayList<Method>>();
        initTypeMap(methodMap);
        try{
            Method[] methods = Class.forName(name).getDeclaredMethods();
            Object instance = Class.forName(name).getDeclaredConstructor().newInstance();
            for(Method m : methods){
                Annotation[] annotations = m.getDeclaredAnnotations();
                if(annotations.length > 1){
                    throw new IllegalArgumentException("Too many annotations");
                }
                if(annotations.length != 0 && methodMap.containsKey(annotations[0].toString())){
                    methodMap.get(annotations[0].toString()).add(m);
                }
            }    
            runAllMethods(methodMap, results, instance);                                    
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return results;
    }


    private static void runAllMethods(HashMap<String, ArrayList<Method>> methodMap, HashMap<String, Throwable> results, Object instance) throws IllegalAccessException
    {
        for(String type : methodMap.keySet()){
            methodMap.get(type).sort(new Comparator<Method>(){
                public int compare(Method m1, Method m2){
                    return m1.toString().compareTo(m2.toString());
                }
            });
        }
        for(Method m : methodMap.get("@BeforeClass()")){
            if(!Modifier.isStatic(m.getModifiers())){
                throw new IllegalAccessException("BeforeClass method must be static");
            }
            runNonTestMethod(m, instance);
        }
        for(Method test : methodMap.get("@Test()")){
            for (Method before : methodMap.get("@Before()")){
                runNonTestMethod(before, instance);
            }
            runTestMethod(test, results, instance);
            for (Method after : methodMap.get("@After()")){
                runNonTestMethod(after, instance);
            }
        }
        for(Method m : methodMap.get("@AfterClass()")){
            if(!Modifier.isStatic(m.getModifiers())){
                throw new IllegalAccessException("AfterClass method must be static");
            }
            runNonTestMethod(m, instance);
        }
    }

    private static void runTestMethod(Method m, HashMap<String, Throwable> map, Object instance)
    {
        map.put(m.getName(), null);
                try{
                    m.invoke(instance);
                }
                catch(Throwable e)
                {
                    map.put(m.getName(), e.getCause());
                }
    }

    private static void runNonTestMethod(Method m, Object instance){
        try {
            m.invoke(instance);
        }
        catch (Throwable e) {
            throw new RuntimeException("Assertion failure in nontest function", e.getCause());
        }
    }

    private static void initTypeMap(HashMap<String, ArrayList<Method>> methodMap){
        methodMap.put("@BeforeClass()", new ArrayList<Method>());
        methodMap.put("@Before()", new ArrayList<Method>());
        methodMap.put("@After()", new ArrayList<Method>());
        methodMap.put("@AfterClass()", new ArrayList<Method>());
        methodMap.put("@Test()", new ArrayList<Method>());
        
    }

    private static void printFailedTests(HashMap<String, Throwable> results)
    {
        System.out.println("\nPrinting map of functions and errors created");
        for(String key : results.keySet()){
            System.out.println("Test method: " + key + "\t");
            System.out.println("Error: " + results.get(key));
            System.out.println();
        }
    }

    public static HashMap<String, Object[]> quickCheckClass(String name) {
        HashMap<String, Object[]> failingParams = new HashMap<String, Object[]>();
        try{
            Class<?> c = Class.forName(name); 
            Method[] methods = c.getDeclaredMethods();
            Object instance = c.getConstructor().newInstance();
            for(Method m: methods){
                if(m.isAnnotationPresent(Property.class)){
                    runMethod(m, failingParams, instance);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Reflection error");
        }
        return failingParams;
    }

    private static void runMethod(Method m, HashMap<String, Object[]> failingParams, Object instance){
        ArrayList<Parameter> paramList = new ArrayList<Parameter>();
        int numParams = m.getParameters().length;
        for(int i = 0; i < numParams; i++){
            paramList.add(m.getParameters()[i]);
        }
        List<List<?>> paramSets = new ArrayList<>();
        for(Parameter p : paramList){
            paramSets.add(buildParamSet(p, instance));
        }
        List<Object> currParamList = new ArrayList<Object>();
        for (int i = 0; i < paramSets.size(); i++){
           currParamList.add(paramSets.get(i).get(0));
        }
        List<List<?>> combinations = buildCartesianProduct(paramSets);
        invokeMethod(m, failingParams, instance, combinations);
    }

    private static void invokeMethod (Method m, HashMap<String, Object[]> failingParams, Object instance,
                                                                                             List<List<?>> params){
        for(List<?> currParams : params){                                                                                         
            try{
                    Object result = m.invoke(instance, currParams.toArray());
                    if(!(Boolean) result){
                        failingParams.put(m.getName(), currParams.toArray());
                        return;
                    }
                }
            catch (Throwable e){
                failingParams.put(m.getName(), currParams.toArray());
                return;
            }
        }
        failingParams.put(m.getName(), null);
    }
    //refactor to take in method m, rather than annotation a
    private static List<?> buildParamSet(Parameter p, Object instance){
        Annotation a = p.getAnnotations()[0];
        if(a instanceof IntRange){
           return buildIntSet((IntRange) a);
        }
        if (a instanceof StringSet){
            return buildStringSet((StringSet) a);
        }
        if (a instanceof ListLength){
            Annotation paramAnn = ((AnnotatedParameterizedType)(p.getAnnotatedType())).getAnnotatedActualTypeArguments()[0].getAnnotations()[0];
            return buildListSet((ListLength) a, paramAnn, instance);
        }
        if (a instanceof ForAll){
            return buildObjectSet((ForAll) a, instance);
        }
        throw new UnsupportedOperationException();
    }

    private static ArrayList<Object> buildIntSet(IntRange a){
        ArrayList<Object> paramSet = new ArrayList<Object>(); 
        int min = a.min();
        int max = a.max();
        for(int i = min; i <= max; i++){
            paramSet.add(i);
        }
        return paramSet;
    }

    private static List<List<?>> buildListSet(ListLength listAnn, Annotation paramAnn, Object instance){
        List<?> buildingBlocks = new ArrayList<>();
        List<List<?>> listSet = new ArrayList<>();
        int minLength = listAnn.min();
        int maxLength = listAnn.max();
        if(paramAnn instanceof IntRange){
            buildingBlocks = buildIntSet((IntRange) paramAnn);
         }
        else if (paramAnn instanceof StringSet){
            buildingBlocks = buildStringSet((StringSet) paramAnn);
        } 
        else if (paramAnn instanceof ForAll){
            buildingBlocks = buildObjectSet((ForAll) paramAnn, instance);
        }
        for(int i = minLength; i <= maxLength; i++){
            List<List<?>> paramOptions = Collections.nCopies(i, buildingBlocks);
            listSet.addAll(buildCartesianProduct(paramOptions));
        }
        return listSet;
    }

    private static ArrayList<Object> buildObjectSet(ForAll a, Object instance){
        ArrayList<Object> paramSet = new ArrayList<Object>();
        try{
            Method m = instance.getClass().getDeclaredMethod(a.name());
            int times = a.times();
            for(int i = 0; i < times; i++){
                paramSet.add(m.invoke(instance));
            }
        }
        catch (Throwable e){
            throw new RuntimeException("Reflection Error");
        }

        return paramSet;
    }

    private static List<Object> buildStringSet(StringSet a){
        ArrayList<Object> stringSet = new ArrayList<Object>();
        for(String s : a.strings()){
            stringSet.add(s);
        }
        return stringSet;
    }

    private static List<List<?>> buildCartesianProduct(List<List<?>> lists){
        List<List<?>> permutations = new ArrayList<>();
        List<Integer> maxIndices = new ArrayList<Integer>();
        int numPermutations = 1;
        //Loops thorugh list of lists to find how long they are
        for(List<?> currList : lists){
            numPermutations *= currList.size();
            maxIndices.add(currList.size() - 1);
        }
        //Holds the indices of the current permutations
        List<Integer> currIndices = new ArrayList<Integer>(Collections.nCopies(lists.size(), 0));
        //Loops through all possible permutations
        for(int i = 0; i < numPermutations; i++){
            ArrayList<Object> currPermutation = new ArrayList<Object>();
            //Loops through list of lists to get each element of the current permutation
            for(int j = 0; j < lists.size(); j++){
                List<?> currList = lists.get(j);
                currPermutation.add(currList.get(currIndices.get(j)));
            }
            permutations.add(currPermutation);
            if(permutations.size() == 100){
                return permutations;
            }
            currIndices = rippleAdd(currIndices, maxIndices);
        }
        return permutations;
    }

    private static List<Integer> rippleAdd(List<Integer> indices, List<Integer> maxIndices){
        int numLists = indices.size();
        if(indices.size() == 0){
            return new ArrayList<Integer>();
        }
        int val = indices.get(0);
        if(val == maxIndices.get(0)){
            List<Integer> result = new ArrayList<Integer>();
            result.add(0);
            result.addAll(rippleAdd(indices.subList(1, numLists), maxIndices.subList(1, numLists)));
            return result;
        }
        indices.set(0, val + 1);
        return indices;
    }
}

 