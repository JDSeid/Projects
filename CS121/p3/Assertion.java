public class Assertion {

    static AssertObject assertThat(Object o) {
        return new AssertObject(o);
    }
    static AssertString assertThat(String s) {
        return new AssertString(s);
    }
    static AssertBool assertThat(boolean b) {
        return new AssertBool(b);
    }
    static AssertInt assertThat(int i) {
        return new AssertInt(i);
    }
}