public class TestFunctions {
    @Test
    public void testID(){
        Book b1 = new Book();
        b1.title = "The Hunger Games";
        b1.author = "Suzanne Collins";
        b1.numCopies = 100;
        assert(b1.id() == 0);
        b1.save();
        assert(b1.id() != 0);
    }
}
