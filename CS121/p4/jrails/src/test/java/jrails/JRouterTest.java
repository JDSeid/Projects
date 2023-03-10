package jrails;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import books.BookController;
import java.util.*;
import books.Book;

public class JRouterTest {

    private JRouter jRouter;
    Book model1;
    Book model2;
    @Before
    public void setUp() throws Exception {
        jRouter = new JRouter();
        model1 = new Book();
        model1.author = "Author 1";
        model1.title = "Title 1";
        model1.num_copies = 1;
        model2 = new Book();
        model2 = new Book();
        model2.author = "Author 2";
        model2.title = "Title 2";
        model2.num_copies = 2;
        model1.save();
        model2.save();
    }

    @Test
    public void addRoute() {
        Book b = new Book();

        jRouter.addRoute("GET", "/", String.class, "index");
        assertThat(jRouter.getRoute("GET", "/"), is("java.lang.String#index"));
        JRouter r = new JRouter();
        // r.addRoute("GET", "/", BookController.class, "index");
         r.addRoute("GET", "/show", BookController.class, "show");
        // r.addRoute("GET", "/new", BookController.class, "new_book");
        // r.addRoute("GET", "/edit", BookController.class, "edit");
        // r.addRoute("POST", "/create", BookController.class, "create");
        // r.addRoute("POST", "/update", BookController.class, "update");
        // r.addRoute("GET", "/destroy", BookController.class, "destroy");
        // System.out.println("Current routes: ");
        // for (Route route : r.routes){
        //     System.out.println("Route: " + route.verb + "\t" + route.path + "\t" + route.clazz + "\t" + route.method);
        // }
        HashMap<String, String> map = new HashMap<>();
        map.put("id", "1");
        //System.out.println(r.route("GET", "/show", map));
    }
}