

public class TestFunctions {

    static Integer x0, x1, x2, x3;
    public static @BeforeClass void z(){
        x0 = null;
        x1 = 1;
        x2 = 1;
        x3 = 3;
    }

    public @Test void assertThatObject(){
        System.out.println("Starting assertion");
        Assertion.assertThat(x1).isNotNull().isEqualTo(x2).isInstanceOf(Integer.class);
        Assertion.assertThat(x0).isNull();
        Assertion.assertThat(x1).isNotNull().isEqualTo(Integer.valueOf(1)).isEqualTo(x2);
        Assertion.assertThat(x2).isNotNull().isNotEqualTo(x3);
    }


    public @Test void assertThatString(){
        
        String empty = "";
        String n = null;
        String a = "a";
        String b = "b";
        String abcd = "abcd";
        Assertion.assertThat(n).isNull();
        Assertion.assertThat(empty).isNotNull();
        Assertion.assertThat(a).isNotEqualTo(b);
        Assertion.assertThat(abcd).startsWith(a).startsWith(a+b);
        //this test should fail
        //Assertion.assertThat(abcd).startsWith("b");
        Assertion.assertThat(empty).isEmpty();
        //Should fail
        //Assertion.assertThat(a).isEmpty();
        Assertion.assertThat(abcd).contains(b).contains("bcd").contains("abcd");
        //Should fail
        //Assertion.assertThat(abcd).contains("e");
    }

    public @Test void assertThatBool(){
        boolean t = true;
        boolean f = false;
        Assertion.assertThat(t).isTrue().isEqualTo(true);
        Assertion.assertThat(f).isFalse().isEqualTo(false);
        //Should fail
        //Assertion.assertThat(f).isEqualTo(t);
    }

    public @Test void assertThatInt(){

        Assertion.assertThat(10).isEqualTo(10).isGreaterThan(-10).isLessThan(11);
        //Should fail
        Assertion.assertThat(10).isEqualTo(-10);
    }

}
