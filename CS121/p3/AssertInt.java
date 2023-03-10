public class AssertInt {
    int testInt;

    public AssertInt(int n){
        testInt = n;
    }

    public AssertInt isEqualTo(int i2){
        if(testInt != i2){
            throw new RuntimeException("Ints are not equal");
        }
        return this;
    }
    public AssertInt isLessThan(int i2){
        if(testInt >= i2){
            throw new RuntimeException("Int is not less than");
        }
        return this;
    }
    public AssertInt isGreaterThan(int i2){
        if(testInt <= i2){
            throw new RuntimeException("Int is not greater than");
        }
        return this;
    }

}
