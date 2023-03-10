package jrails;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;
import books.Book;

public class HtmlTest {

    private Html html;

    @Before
    public void setUp() throws Exception {
        html = new Html();
    }

    @Test
    public void empty() {
        assertThat(View.empty().toString(), isEmptyString());
        System.out.println("HTML test passed");
    }
    @Test
    public void pTest(){
        System.out.println(View.p(View.t("Hello")));
        System.out.println(View.submit("v18"));
    }

    
    
}
