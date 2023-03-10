public class AssertObject {
    protected Object testObj;

 
    public AssertObject(Object testObj){
        this.testObj = testObj;
    }

    public AssertObject isNotNull(){
        if(testObj == null){
            throw new RuntimeException("Object is null");
        }
        return this;
    }

    public AssertObject isNull(){
        if(testObj != null){
            throw new RuntimeException("Object is null");
        }
        return this;
    }

    public AssertObject isEqualTo(Object other){
        if(!testObj.equals(other)){
            throw new RuntimeException("Objects are not equal");
        }
        return this;
    }

    public AssertObject isNotEqualTo(Object other){
        if(testObj.equals(other)){
            throw new RuntimeException("Objects are equal");
        }
        return this;
    }

    public AssertObject isInstanceOf(Class c){
        if(!(c.isInstance(testObj))){
            throw new RuntimeException("Not an instance of class");
        }
        return this;
    }
}