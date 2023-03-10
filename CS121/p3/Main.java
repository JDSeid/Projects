import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        Unit.testClass("PropertyFunctions");
        HashMap<String,Object []> failedTests = Unit.quickCheckClass("PropertyFunctions");

        for(String s : failedTests.keySet()){
            Object [] failedParams = failedTests.get(s);
            if(failedTests.get(s) != null){
                System.out.println("Method " + s + " failed when run with parameters ");
                for(Object o : failedParams){
                    System.out.println(o + "\t");
                }
            }
        }

    }
}
