import java.util.*;

public class Test {

    // Run "java -ea Test" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)

    
    
    public static void main(String[] args) {
	    test1();
        testBlackPawn();
        testWhitePawn();
        testKnight();
        testKing();
        testBishop();
        testRook();
        testQueen();
        testMovePiece();
    }

    public static void test1() {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new PawnFactory());
        Piece p = Piece.createPiece("bp");
        b.addPiece(p, "a3");
        assert b.getPiece("a3") == p;
    }

    public static void testBlackPawn()
    {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new PawnFactory());
        Pawn p = (Pawn) Piece.createPiece("bp");

        b.addPiece(p, "b7");
        List<String> moves = p.moves(b, "b7");

        assert moves.contains("b6"); //Pawn move forward one square
        assert moves.contains("b5");  //Pawn can move forward two squares
        assert !moves.contains("a6"); 
        assert !moves.contains("c6"); //Pawn cannot capture left or right, because no peice is there

        b.addPiece(Piece.createPiece("bp"), "b6"); // Add another black pawn blocking the first one

        moves = p.moves(b, "b7");
        assert !moves.contains("b6");
        assert !moves.contains("b5"); //Pawn should no longer be able to move forward

        b.addPiece(Piece.createPiece("wp"), "a6"); //Add a white pawn diagonally

        moves = p.moves(b, "b7");
        assert moves.contains("a6"); //Black pawn should be able to capture white pawn

        b.addPiece(Piece.createPiece("bp"), "c6"); //Add a black pawn diagonally

        moves = p.moves(b, "b7");
        assert !moves.contains("c6"); //Black pawn should not be able to capture the other black pawn
    }

    public static void testWhitePawn()
    {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new PawnFactory());
        Pawn p = (Pawn) Piece.createPiece("wp");
        
        b.addPiece(p, "b2");
        List<String> moves = p.moves(b, "b2");

        assert moves.contains("b3"); //Pawn move forward one square
        assert moves.contains("b4");  //Pawn can move forward two squares
        assert !moves.contains("a3"); 
        assert !moves.contains("c3"); //Pawn cannot capture left or right, because no peice is there

        b.addPiece(Piece.createPiece("bp"), "b3"); // Add another white pawn blocking the first one

        moves = p.moves(b, "b2");
        assert !moves.contains("b3");
        assert !moves.contains("b4"); //Pawn should no longer be able to move forward

        b.addPiece(Piece.createPiece("bp"), "a3"); //Add a black pawn diagonally

        moves = p.moves(b, "b2");
        assert moves.contains("a3"); //White pawn should be able to capture black pawn

        b.addPiece(Piece.createPiece("wp"), "c3"); //Add a white pawn diagonally

        moves = p.moves(b, "b2");
        assert !moves.contains("c3"); //White pawn should not be able to capture the other white pawn
    }

    public static void testKnight()
    {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new KnightFactory());
        Knight n = (Knight) Piece.createPiece("wn");
        b.addPiece(n, "c3");
        List<String> moves = n.moves(b, "c3");
        assert moves.contains("b1");
        assert moves.contains("d1");
        assert moves.contains("a2");
        assert moves.contains("a4");
        assert moves.contains("b5");
        assert moves.contains("d5");
        assert moves.contains("e4");
        assert moves.contains("e2");
        assert moves.size() == 8;
        
        b.addPiece(Piece.createPiece("wn"), "b1");
        b.addPiece(Piece.createPiece("bn"), "d1");
        moves = n.moves(b, "c3");

        assert moves.contains("d1");
        assert !moves.contains("b1");
        assert moves.size() == 7;

        moves = n.moves(b, "b2");
        assert moves.size() == 4;
    }

    public static void testKing()
    {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new KingFactory());
        King k = (King) Piece.createPiece("wk");
        b.addPiece(k, "b3");

        List<String> moves = k.moves(b, "b3");
        assert moves.contains("a3");
        assert moves.contains("c3");
        assert moves.contains("b4");
        assert moves.contains("b2");
        assert moves.contains("a4");
        assert moves.contains("c4");
        assert moves.contains("a2");
        assert moves.contains("c2");
        assert moves.size() == 8;

        Piece.registerPiece(new PawnFactory());

        b.addPiece(Piece.createPiece("wp"), "a3");
        b.addPiece(Piece.createPiece("wp"), "a2");
        b.addPiece(Piece.createPiece("bp"), "b2");
        b.addPiece(Piece.createPiece("bp"), "b4");
        moves = k.moves(b, "b3");
        assert !moves.contains("a3");
        assert !moves.contains("a2");
        assert moves.contains("b2");
        assert moves.contains("b4");
        assert moves.size() == 6;

        moves = k.moves(b, "h1");
        assert moves.size() == 3;
    }

    public static void testBishop()
    {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new BishopFactory());
        Bishop bishop = (Bishop) Piece.createPiece("wb");
        b.addPiece(bishop, "d4");
        
        List<String> moves = bishop.moves(b, "d4");
        assert moves.size() == 13;
        
        assert moves.contains("c3");
        assert moves.contains("b2");
        assert moves.contains("a1");
        assert moves.contains("c5");
        assert moves.contains("b6");
        assert moves.contains("a7");
        assert moves.contains("e5");
        assert moves.contains("f6");
        assert moves.contains("g7");
        assert moves.contains("h8");
        assert moves.contains("e3");
        assert moves.contains("f2");
        assert moves.contains("g1");

        b.addPiece(Piece.createPiece("wb"), "f2");
        moves = bishop.moves(b, "d4");
        assert !moves.contains("f2");
        assert !moves.contains("g1");
        assert moves.size() == 11;

        b.addPiece(Piece.createPiece("bb"), "c3");
        moves = bishop.moves(b, "d4");
        assert moves.contains("c3");
        assert !moves.contains("b2");
        assert !moves.contains("a1");
        assert moves.size() == 9;
        


    }

    public static void testRook()
    {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new RookFactory());
        Rook rook = (Rook) Piece.createPiece("wr");
        b.addPiece(rook, "d4");

        List<String> moves = rook.moves(b, "d4");
        assert moves.size() == 14;

        assert moves.contains("c4");
        assert moves.contains("b4");
        assert moves.contains("a4");
        assert moves.contains("e4");
        assert moves.contains("f4");
        assert moves.contains("g4");
        assert moves.contains("h4");
        assert moves.contains("d5");
        assert moves.contains("d6");
        assert moves.contains("d7");
        assert moves.contains("d8");
        assert moves.contains("d1");
        assert moves.contains("d2");
        assert moves.contains("d3");
        
        b.addPiece(Piece.createPiece("wr"), "d3");
        moves = rook.moves(b, "d4");
        assert !moves.contains("d3");
        assert !moves.contains("d2");
        assert moves.size() == 11;

        b.addPiece(Piece.createPiece("br"), "b4");
        moves = rook.moves(b, "d4");
        assert moves.contains("c4");
        assert moves.contains("b4");
        assert !moves.contains("a4");
        assert moves.size() == 10;
    }

    public static void testQueen()
    {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new QueenFactory());
        Queen queen = (Queen) Piece.createPiece("wq");
        b.addPiece(queen, "b2");
        List<String> moves = queen.moves(b, "b2");

        assert moves.size() == 23;

        assert moves.contains("a1");
        assert moves.contains("a2");
        assert moves.contains("b1");
        assert moves.contains("d4");
        assert moves.contains("h8");
        assert moves.contains("b4");
        assert moves.contains("h8");
        assert moves.contains("d2");
        assert moves.contains("h2");

        b.addPiece(Piece.createPiece("wq"), "b4");
        b.addPiece(Piece.createPiece("wq"), "c2");
        moves = queen.moves(b, "b2");
        assert moves.contains("b3");
        assert !moves.contains("b4");
        assert !moves.contains("b4");
        assert !moves.contains("b5");
        assert !moves.contains("c2");
        assert !moves.contains("h2");
        assert moves.size() == 12;

        b.addPiece(Piece.createPiece("bq"), "d4");
        moves = queen.moves(b, "b2");
        assert moves.contains("c3");
        assert moves.contains("d4");
        assert !moves.contains("e5");
        assert moves.size() == 8;

    }


    public static void testMovePiece()
    {
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new QueenFactory());
        Queen q1 = (Queen) Piece.createPiece("wq");
        b.addPiece(q1, "b2");
        assert b.getPiece("b2") != null;
        b.movePiece("b2", "d4");
        assert b.getPiece("b2") == null;
        assert b.getPiece("d4") != null;
        try{
            b.movePiece("b1", "b2");
            assert false;
        }
        catch(Exception e) {

        }
        b.addPiece(Piece.createPiece("wq"), "d5");
        try{
            b.movePiece("d4", "d6");
            assert false;
        }
        catch (Exception e) {

        }

        
    }

    public static void printBoard(Board b)
    {
        Piece.registerPiece(new QueenFactory());
        Queen q = (Queen) Piece.createPiece("wq");
        for(int i = 7; i >= 0; i--){
            System.out.print(i + "\t");
            for(int j =	0; j < 8; j++){
                if(b.getPiece(q.rowColToLoc(i, j)) == null)
                {
                    System.out.print("x  ");
                }
                else{
                    System.out.print(b.getPiece(q.rowColToLoc(i, j)).toString() + " ");
                }
            }
            System.out.println();
        }
    }
}
