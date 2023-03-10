public class AssertBool{

    boolean testBool;
    public AssertBool(boolean b){
        testBool = b;
    }

    public AssertBool isEqualTo(boolean b){
        if (b!=testBool){
            throw new RuntimeException("Bools not equal");
        }
        return this;
    }
    public AssertBool isTrue(){
        if (!testBool){
            throw new RuntimeException("Bool is not true");
        }
        return this;
    }
    public AssertBool isFalse(){
        if (testBool){
            throw new RuntimeException("Bool is not false");
        }
        return this;
    }
}
