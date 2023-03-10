public class AssertString{

    String testString;
    public AssertString(String s){
        testString = s;
    }

    public AssertString startsWith(String s){
        if (!(testString.substring(0, s.length()).equals(s))){
            throw new RuntimeException("String does not start with " + s);
        }
        return this;
    }

    public AssertString isEmpty(){
        if (!(testString.equals(""))){
            throw new RuntimeException("String is not empty");
        }
        return this;
    }

    public AssertString contains(String s){
        if (!(testString.contains(s))){
            throw new RuntimeException("String does not contains " + s);
        }
        return this;
    }
    public AssertString isNotNull(){
        if(testString == null){
            throw new RuntimeException("Object is null");
        }
        return this;
    }

    public AssertString isNull(){
        if(testString != null){
            throw new RuntimeException("Object is null");
        }
        return this;
    }

    public AssertString isEqualTo(Object other){
        if(!testString.equals(other)){
            throw new RuntimeException("Objects are not equal");
        }
        return this;
    }

    public AssertString isNotEqualTo(Object other){
        if(testString.equals(other)){
            throw new RuntimeException("Objects are equal");
        }
        return this;
    }


}