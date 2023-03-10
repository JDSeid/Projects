package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;
import jrails.Html;
import jrails.View;
import books.Book;

public class ViewTest {

    @Test
    public void empty() {
        assertThat(View.empty().toString(), isEmptyString());
    }

    @Test
    public void table_nested() 
    {
        Book b = new Book();
        b.title = "Harry Potter";
        b.author = "JK Rowling";
        b.num_copies = 5;
        b.save();
        

        System.out.println(View.t("text"));
        )
    }
}