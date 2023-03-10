import java.util.*;

public class PropertyFunctions {

    @Test public void test(){
        System.out.println("Running test");
    }

    @Property
    public boolean sumLessThan5(@IntRange(min=0, max = 3) Integer i, @IntRange(min = 0, max = 3) Integer j, 
                            @IntRange(min = 0, max = 3) Integer k) {
        //System.out.println("Running with values: " + i + ", " + j + " and " + k);

        return (i + j + k) < 5;
    }

    @Property
    public boolean bothNegative(@IntRange(min=-4, max = 3) Integer i, @IntRange(min = -3, max = 3) Integer j) {
        //System.out.println("Running with values: " + i + ", " + j);

        return i < 0 && j < 0;
    }

    public boolean shouldntRun(@IntRange(min=-4, max = 3) Integer i, @IntRange(min = -3, max = 3) Integer j) {
        //System.out.println("Running with values: " + i + ", " + j);

        return i < 0 && j < 0;
    }

    @Property
    public boolean stringsNotEqual(@StringSet(strings = {"s3", "s4", "s5"}) String i, @StringSet(strings = {"s1", "s2", "s3"}) String j, @IntRange(min=-4, max = 3) Integer k) {
        System.out.println("Running with values: " + i + ", " + j + ", " + k);

        return !(i.equals(j));
    }
        int count = 0;
    @Property public boolean listTest(@ListLength(min=0, max=2) List<@IntRange(min=5, max=7) Integer> list, 
        @ListLength(min = 0, max = 3) List<@StringSet(strings = {"s1", "s2", "s3"}) String> s){
        System.out.println("Count: " + count + "\t");
        System.out.print("Running with list: " + list + " and list: " + s);
        // if(list.size() > 1 && list.get(0)+list.get(1) == 14 && s.contains("s3")){
        //     return false;
        // }
        count ++;
        return true;
    }
    

    // @Property public boolean smallerThan8Elements(@ForAll(name="genIntSet", times=10) Object o) {
    //     System.out.println("Running with: " + o);
    //     Set s = (Set) o;
    //     return s.size() < 8;
    //   }
      
    //   int count = 0;
    //   public Object genIntSet() {
    //     Set s = new HashSet();
    //     for (int i=0; i<count; i++) { s.add(i); }
    //     count++;
    //     return s;
    //   }
}
