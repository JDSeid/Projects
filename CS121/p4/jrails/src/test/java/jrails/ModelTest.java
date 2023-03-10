package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import books.Book;
import java.util.*;

public class ModelTest {

    private Book model1;
    private Book model2;

    @Before
    public void setup(){
        model1 = new Book();
        model1.author = "Author 1";
        model1.title = "Title 1";
        model1.num_copies = 1;
        model2 = new Book();
        model2 = new Book();
        model2.author = "Author 2";
        model2.title = "Title 2";
        model2.num_copies = 2;
    }

    @Test
    public void id() {
        //assertThat(model1.id(), notNullValue());
        //model1.save();
        //model2.save();
    }

    @Test
    public void find(){
        model1.save();
        Book b1 = Model.find(model1.getClass(), model1.id());
        Book b2 = Model.find(model1.getClass(), model1.id());
        assert (b1.id() == b2.id());
        System.out.println("Find assert pass");
    }

    // @Test
    // public void copyFile(){
    //     model1.save();
    //     model2.save();
    //     Book b = Model.find(model2.getClass(), model2.id());
    //     assert(b.id() == model2.id());
    //     b.title = "new title 2";
    //     b.author = "new author 2";
    //     b.num_copies = 10000;
    //     b.save();
    //     Book b2 = new Book();
    //     b2.title = "title 3";
    //     b2.author = "author 3";
    //     b2.num_copies = 3; 
    //     b2.save();
    //     Book b3 = Model.find(model1.getClass(), model1.id());
    //     b3.title = "title 4";
    //     b3.save();
    // }

    @Test
    public void all() {
        model1.save();
        model2.save();
        Class<?> c = model1.getClass();
        for(Model m : Model.all(model1.getClass())){
            System.out.println(((Book) m).title);
            System.out.println(((Book) m).author);
            System.out.println(((Book) m).num_copies);
        }
        Model.reset();
    }

    @After
    public void tearDown() throws Exception {
    }
}