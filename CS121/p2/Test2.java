import java.util.*;

public class Test2 {

    public static void test1() {
        Board b = Board.theBoard();
        Piece.registerPiece(new PawnFactory());
        Pawn p = (Pawn) Piece.createPiece("wp");
        b.addPiece(p, "a2");
        assert b.getPiece("a2") == p;
        // for (String s : p.moves(b, "a2")){ 
        //     System.out.println(s);
        // }
        b.clear();
    }
    public static void testPawnHomeMoveTwo() {
        Board b = Board.theBoard();
        Piece.registerPiece(new PawnFactory());
        Piece p1 = Piece.createPiece("bp");
        b.addPiece(p1, "a7");
        assert b.getPiece("a7") == p1;
        List<String> bp_moves = p1.moves(b, "a7");
        assert !bp_moves.contains("b6");
        assert bp_moves.contains("a5");
        b.movePiece("a7", "a5");
        assert b.getPiece("a5") == p1;
        b.clear();
    }
    public static void checkMoveWhenPieceThere() {
        Board b = Board.theBoard();
        Piece.registerPiece(new PawnFactory());
        Piece p1 = Piece.createPiece("bp");
        b.addPiece(p1, "a7");
        Piece p2 = Piece.createPiece("bp");
        b.addPiece(p2, "a6");

        List<String> bp_moves = p1.moves(b, "a7");
        // for (String p : bp_moves) {
        //     System.out.println(p);
        // }
        assert !bp_moves.contains("a6");
        assert !bp_moves.contains("a5");

        // b.movePiece("a7", "a6");
        // assert b.getPiece("a6") == p2;
        b.clear();
    }
    public static void checkPawnMoveOnOpponent() {
        Board b = Board.theBoard();
        Piece.registerPiece(new PawnFactory());
        Piece p1 = Piece.createPiece("bp");
        b.addPiece(p1, "a7");
        Piece p2 = Piece.createPiece("wp");
        b.addPiece(p2, "b6");

        List<String> bp_moves = p1.moves(b, "a7");
        List<String> wp_moves = p2.moves(b, "b6");
        assert bp_moves.contains("a6");
        assert bp_moves.contains("a5");
        assert bp_moves.contains("b6");
        assert wp_moves.contains("a7");
        assert wp_moves.contains("b7");
        b.clear();
    }
    public static void checkRookMoveOnOpponent() {
        Board b = Board.theBoard();
        Piece.registerPiece(new RookFactory());
        Piece p1 = Piece.createPiece("br");
        b.addPiece(p1, "a8");
        Piece p2 = Piece.createPiece("wr");
        b.addPiece(p2, "a1");

        List<String> bp_moves = p1.moves(b, "a8");
        List<String> wp_moves = p2.moves(b, "a1");
        assert bp_moves.contains("a1");
        assert wp_moves.contains("a8");
        b.clear();

    }
    public static void checkBishopMoveOnOpponent() {
        Board b = Board.theBoard();
        Piece.registerPiece(new BishopFactory());
        Piece.registerPiece(new PawnFactory());
        Piece bb = Piece.createPiece("bb");
        b.addPiece(bb, "c8");
        b.addPiece(Piece.createPiece("bp"), "h6");
        b.addPiece(Piece.createPiece("wp"), "f5");
        Piece wb = Piece.createPiece("wb");
        b.addPiece(bb, "c1");

        List<String> bb_moves = bb.moves(b, "c8");
        List<String> wb_moves = wb.moves(b, "c1");
        assert bb_moves.contains("d7");
        assert bb_moves.contains("e6");
        assert bb_moves.contains("f5");
        assert bb_moves.contains("b7");
        assert bb_moves.contains("a6");
        assert !bb_moves.contains("g4");
        assert (bb_moves.size() == 5);

        assert wb_moves.contains("d2");
        assert wb_moves.contains("e3");
        assert wb_moves.contains("f4");
        assert wb_moves.contains("g5");
        assert wb_moves.contains("h6");
        assert wb_moves.contains("b2");
        assert wb_moves.contains("a3");
        assert (wb_moves.size() == 7);

        

    }
    public static void checkKnightMoveOnOpponent() {
        Board b = Board.theBoard();
        Piece.registerPiece(new KnightFactory());
        Piece.registerPiece(new PawnFactory());
        Piece bn = Piece.createPiece("bn");
        b.addPiece(bn, "b8");
        b.addPiece(Piece.createPiece("bp"), "h3");
        b.addPiece(Piece.createPiece("wp"), "d7");
        Piece wn = Piece.createPiece("wn");
        b.addPiece(wn, "g1");

        List<String> bb_moves = bn.moves(b, "b8");
        List<String> wb_moves = wn.moves(b, "g1");
       
        
        assert(bb_moves.contains("c6"));
        assert(bb_moves.contains("a6"));
        assert(bb_moves.contains("d7"));
        assert(bb_moves.size() == 3);

        assert(wb_moves.contains("h3"));
        assert(wb_moves.contains("f3"));
        assert(wb_moves.contains("e2"));
        assert(wb_moves.size() == 3);

        b.clear();

    }
    public static void checkKingMoveOnOpponent() {
        Board b = Board.theBoard();
        Piece.registerPiece(new KingFactory());
        Piece.registerPiece(new PawnFactory());
        Piece bk = Piece.createPiece("bk");
        b.addPiece(bk, "d7");
        b.addPiece(Piece.createPiece("wp"), "d8");
        b.addPiece(Piece.createPiece("wp"), "d6");
        b.addPiece(Piece.createPiece("bp"), "d3");
        b.addPiece(Piece.createPiece("bp"), "d1");
        Piece wk = Piece.createPiece("wk");
        b.addPiece(wk, "d2");

        List<String> b_moves = bk.moves(b, "d7");
        List<String> w_moves = wk.moves(b, "d2");
       
        assert(b_moves.contains("c7"));
        assert(b_moves.contains("c6"));
        assert(b_moves.contains("e8"));
        assert(b_moves.contains("e7"));
        assert(b_moves.contains("e6"));
        assert(b_moves.contains("d8"));
        assert(b_moves.contains("d6"));
        assert(b_moves.size() == 8);

        assert(w_moves.contains("e1"));
        assert(w_moves.contains("e2"));
        assert(w_moves.contains("e3"));
        assert(w_moves.contains("c1"));
        assert(w_moves.contains("c2"));        
        assert(w_moves.contains("c3"));
        assert(w_moves.contains("d1"));
        assert(w_moves.contains("d3"));
        assert(w_moves.size() == 8);
        b.clear();
    }
    public static void checkQueenMoveOnOpponent() {
        Board b = Board.theBoard();
        Piece.registerPiece(new QueenFactory());
        Piece.registerPiece(new PawnFactory());
        Piece bq = Piece.createPiece("bq");
        b.addPiece(bq, "d7");
        b.addPiece(Piece.createPiece("wp"), "a7");
        b.addPiece(Piece.createPiece("wp"), "f5");
        b.addPiece(Piece.createPiece("bp"), "h2");
        b.addPiece(Piece.createPiece("bp"), "f4");
        Piece wq = Piece.createPiece("wq");
        b.addPiece(wq, "d2");

        List<String> b_moves = bq.moves(b, "d7");
        List<String> w_moves = wq.moves(b, "d2");
       
        assert(b_moves.contains("c8"));
        assert(b_moves.contains("c7"));
        assert(b_moves.contains("c6"));
        assert(b_moves.contains("e8"));
        assert(b_moves.contains("e7"));
        assert(b_moves.contains("e6"));
        assert(b_moves.contains("d8"));
        assert(b_moves.contains("d6"));
        assert(b_moves.contains("f5"));
        assert(b_moves.contains("a4"));
        assert(!b_moves.contains("g4"));
        // assert(b_moves.size() == 8);

        assert(w_moves.contains("e1"));
        assert(w_moves.contains("e2"));
        assert(w_moves.contains("e3"));
        assert(w_moves.contains("c1"));
        assert(w_moves.contains("c2"));        
        assert(w_moves.contains("c3"));
        assert(w_moves.contains("d1"));
        assert(w_moves.contains("d3"));
        assert(w_moves.contains("f4"));
        assert(!w_moves.contains("g5"));
        // assert(w_moves.size() == 8);
        
        
       

        

    }
    public static void checkBishop() {
        Board b = Board.theBoard();
        Piece.registerPiece(new BishopFactory());
        // Bishop p1 = (Bishop)Piece.createPiece("wb");
        Piece p1 = Piece.createPiece("wb");
        b.addPiece(p1, "d5");
        // Bishop p2 = (Bishop)Piece.createPiece("wb");
        // b.addPiece(p2, "d2");
        List<String> bb_moves = p1.moves(b, "d5");
        assert bb_moves.contains("e6");
        assert bb_moves.contains("f7");
        assert bb_moves.contains("g8");
        assert bb_moves.contains("c6");
        assert bb_moves.contains("b7");
        assert bb_moves.contains("a8");
        assert bb_moves.contains("c4");
        assert bb_moves.contains("b3");
        assert bb_moves.contains("a2");
        assert bb_moves.contains("e4");
        assert bb_moves.contains("f3");
        assert bb_moves.contains("g2");
        assert bb_moves.contains("h1");

        Bishop p2 = (Bishop)Piece.createPiece("wb");
        b.addPiece(p2, "a1");
        List<String> bb2_moves = p2.moves(b, "a1");
        Bishop p3 = (Bishop)Piece.createPiece("wb");
        b.addPiece(p3, "h1");
        List<String> bb3_moves = p3.moves(b, "h1");
        Bishop p4 = (Bishop)Piece.createPiece("wb");
        b.addPiece(p4, "h8");
        List<String> bb4_moves = p4.moves(b, "h8");
        Bishop p5 = (Bishop)Piece.createPiece("wb");
        b.addPiece(p5, "a8");
        List<String> bb5_moves = p5.moves(b, "a8");
        b.clear();
    }
    public static void checkRook() {
        Board b = Board.theBoard();
        Piece.registerPiece(new RookFactory());
        Piece.registerPiece(new BishopFactory());
        // Rook p1 = (Rook)Piece.createPiece("wr");
        Piece p1 = Piece.createPiece("wr");
        b.addPiece(p1, "a1");
        Piece p2 = Piece.createPiece("br");
        b.addPiece(p2, "a7");
        List<String> bb_moves = p1.moves(b, "a1");
        List<String> wb_moves = p2.moves(b, "a7");
        assert(bb_moves.size() == 13);
        assert(wb_moves.size() == 14);
        assert(bb_moves.contains("a7"));
        assert(bb_moves.contains("a6"));
        assert(bb_moves.contains("a5"));
        assert(bb_moves.contains("a4"));
        assert(bb_moves.contains("a3"));
        assert(bb_moves.contains("a2"));
        assert(bb_moves.contains("b1"));
        assert(bb_moves.contains("c1"));
        assert(bb_moves.contains("d1"));
        assert(bb_moves.contains("e1"));
        assert(bb_moves.contains("f1"));
        assert(bb_moves.contains("g1"));
        assert(bb_moves.contains("h1"));
        assert(!bb_moves.contains("a8"));

        assert(wb_moves.contains("a8"));
        assert(wb_moves.contains("a6"));
        assert(wb_moves.contains("a5"));
        assert(wb_moves.contains("a4"));
        assert(wb_moves.contains("a3"));
        assert(wb_moves.contains("a2"));
        assert(wb_moves.contains("a1"));
        assert(wb_moves.contains("b7"));
        assert(wb_moves.contains("c7"));
        assert(wb_moves.contains("d7"));
        assert(wb_moves.contains("e7"));
        assert(wb_moves.contains("f7"));
        assert(wb_moves.contains("g7"));
        assert(wb_moves.contains("h7"));

        b.clear();
    }
    public static void checkKing() {
        Board b = Board.theBoard();
        Piece.registerPiece(new KingFactory());
        // King p1 = (King)Piece.createPiece("wk");
        Piece p1 = Piece.createPiece("wk");
        b.addPiece(p1, "c4");
        List<String> bb_moves = p1.moves(b, "c4");
        // for (String p : bb_moves) {
        //     System.out.println(p);
        // }

        b.clear();
    }
    public static void checkQueen() {
        Board b = Board.theBoard();
        Piece.registerPiece(new QueenFactory());
        // Queen p1 = (Queen)Piece.createPiece("wq");
        Piece p1 = Piece.createPiece("wq");
        b.addPiece(p1, "d5");
        
        List<String> bb_moves = p1.moves(b, "d5");
        assert bb_moves.contains("e6");
        assert bb_moves.contains("f7");
        assert bb_moves.contains("g8");
        assert bb_moves.contains("c6");
        assert bb_moves.contains("b7");
        assert bb_moves.contains("a8");
        assert bb_moves.contains("c4");
        assert bb_moves.contains("b3");
        assert bb_moves.contains("a2");
        assert bb_moves.contains("e4");
        assert bb_moves.contains("f3");
        assert bb_moves.contains("g2");
        assert bb_moves.contains("h1");

        b.clear();
    }
    public static void checkKnight(){
        Board b = Board.theBoard();
        Piece.registerPiece(new KnightFactory());
        // Knight p1 = (Knight)Piece.createPiece("wn");
        Piece p1 = Piece.createPiece("wn");
        b.addPiece(p1, "b1");
        List<String> bb_moves = p1.moves(b, "b1");
        // for (String p : bb_moves) {
        //     System.out.println(p);
        // }
        // System.out.println(p1);

        b.clear();
    }
    public static void checkToString(){
        Board b = Board.theBoard();
        Piece.registerPiece(new KingFactory());
        Piece.registerPiece(new QueenFactory());
        Piece.registerPiece(new KnightFactory());
        Piece.registerPiece(new BishopFactory());
        Piece.registerPiece(new RookFactory());
        Piece.registerPiece(new PawnFactory());
        King wk = (King)Piece.createPiece("wk");
        King bk = (King)Piece.createPiece("bk");
        Queen wq = (Queen)Piece.createPiece("wq");
        Queen bq = (Queen)Piece.createPiece("bq");
        Knight wn = (Knight)Piece.createPiece("wn");
        Knight bn = (Knight)Piece.createPiece("bn");
        Bishop wb = (Bishop)Piece.createPiece("wb");
        Bishop bb = (Bishop)Piece.createPiece("bb");
        Rook wr = (Rook)Piece.createPiece("wr");
        Rook br = (Rook)Piece.createPiece("br");
        Pawn wp = (Pawn)Piece.createPiece("wp");
        Pawn bp = (Pawn)Piece.createPiece("bp");
        assert(wk.toString() == "wk");
        assert(bk.toString() == "bk");
        assert(wq.toString() == "wq");
        assert(bq.toString() == "bq");
        assert(wn.toString() == "wn");
        assert(bn.toString() == "bn");
        assert(wb.toString() == "wb");
        assert(bb.toString() == "bb");
        assert(wr.toString() == "wr");
        assert(br.toString() == "br");
        assert(wp.toString() == "wp");
        assert(bp.toString() == "bp");
        
        b.clear();

    }
    public static void BigTests(){
		Piece.registerPiece(new KingFactory());
		Piece.registerPiece(new QueenFactory());
		Piece.registerPiece(new KnightFactory());
		Piece.registerPiece(new BishopFactory());
		Piece.registerPiece(new RookFactory());
		Piece.registerPiece(new PawnFactory());
		Board.theBoard().registerListener(new Logger());

        Board b = Board.theBoard();
		//white
		b.addPiece(Piece.createPiece("wk"),"e1");
		b.addPiece(Piece.createPiece("wq"),"d1");
		b.addPiece(Piece.createPiece("wb"),"c1");
		b.addPiece(Piece.createPiece("wn"),"b1");
		b.addPiece(Piece.createPiece("wr"),"a1");
		b.addPiece(Piece.createPiece("wp"),"h2");
		b.addPiece(Piece.createPiece("wp"),"f2");

        //black
		b.addPiece(Piece.createPiece("bk"),"e8");
		b.addPiece(Piece.createPiece("bq"),"d8");
		b.addPiece(Piece.createPiece("bb"),"f8");
		b.addPiece(Piece.createPiece("bn"),"b8");
		b.addPiece(Piece.createPiece("br"),"a8");
		b.addPiece(Piece.createPiece("br"),"h8");
		b.addPiece(Piece.createPiece("bp"),"c7");
		b.addPiece(Piece.createPiece("bp"),"d7");
		b.addPiece(Piece.createPiece("bp"),"f7");
		b.addPiece(Piece.createPiece("bp"),"h7");

        //move king
		// try{
		// 	b.movePiece("e1","d1");
		// }catch (BadMove e){
		// 	System.out.println("this is bad king move!");
		// 	e.printStackTrace();
		// }
		b.movePiece("e1","e2");
		assert b.getPiece("e1")==null;
		assert b.getPiece("e2").toString().equals("wk");

        //move queen
		b.movePiece("d1","b3");
		assert b.getPiece("d1")==null;
		assert b.getPiece("b3").toString().equals("wq");
		b.movePiece("b3","f7");
		assert b.getPiece("b3")==null;
		assert b.getPiece("f7").toString().equals("wq");

        //move Bishop
		b.movePiece("f8","c5");
		assert b.getPiece("f8")==null;
		assert b.getPiece("c5").toString().equals("bb");

        //move Knight
		b.movePiece("b8","c6");
		assert b.getPiece("b8")==null;
		assert b.getPiece("c6").toString().equals("bn");

        //move Rock
		b.movePiece("a1","a8");
		assert b.getPiece("a1")==null;
		assert b.getPiece("a8").toString().equals("wr");

        //move Pawn
		b.movePiece("f7","e6");
		b.movePiece("d7","e6");
		assert b.getPiece("d7")==null;
		assert b.getPiece("e6").toString().equals("bp");
		b.movePiece("h7","h5");
		assert b.getPiece("h7")==null;
		assert b.getPiece("h5").toString().equals("bp");
		// try{
		// 	b.movePiece("h5","h3");
		// }catch (BadMove e){
		// 	System.out.println("this is bad pawn move!");
		// 	e.printStackTrace();
		// }
		b.movePiece("h5","h4");
		assert b.getPiece("h5")==null;
		assert b.getPiece("h4").toString().equals("bp");

        // System.out.println("Final board:");
		// Board.theBoard().iterate(new BoardPrinter());

        b.clear();
		// System.out.println("Final board:");
		// Board.theBoard().iterate(new BoardPrinter());
	}  
    public static void checkClear(){
        Board b = Board.theBoard();
        Piece.registerPiece(new PawnFactory());
        b.addPiece(Piece.createPiece("wp"),"c1");
        b.addPiece(Piece.createPiece("wp"),"c2");
        b.addPiece(Piece.createPiece("wp"),"c3");
        b.addPiece(Piece.createPiece("wp"),"c4");
        b.addPiece(Piece.createPiece("wp"),"c5");
        b.clear();
        assert(b.getPiece("c1") == null);
        assert(b.getPiece("c2") == null);
        assert(b.getPiece("c3") == null);
        assert(b.getPiece("c4") == null);
        assert(b.getPiece("c5") == null);

        
    }
    public static void checkLog() {
        
        Board b = Board.theBoard();
        Logger l = new Logger();
        b.registerListener(new Logger());
        b.registerListener(l);

        Piece.registerPiece(new QueenFactory());
        Piece.registerPiece(new PawnFactory());
        b.addPiece(Piece.createPiece("bq"), "e7");
        b.addPiece(Piece.createPiece("wp"), "e6");
        b.addPiece(Piece.createPiece("wp"), "f6");
        b.addPiece(Piece.createPiece("wp"), "b4");
        b.addPiece(Piece.createPiece("wp"), "e8");
        b.movePiece("e7", "e8");
        b.movePiece("e8", "e6");
        b.movePiece("e6", "e7");
        b.removeListener(l);
        b.movePiece("e7", "b4");
        b.removeAllListeners();
        b.movePiece("b4", "b8");

    }
    
    public static void misc() {
        Board b = Board.theBoard();      

        Piece.registerPiece(new QueenFactory());
        Piece.registerPiece(new PawnFactory());
        b.addPiece(Piece.createPiece("bq"), "e7");
        b.movePiece("e5", "e2");
    }

    public static void testPawn() {
        Board b = Board.theBoard();      

        Piece.registerPiece(new PawnFactory());
        Piece p = Piece.createPiece("bp");
        b.addPiece(p, "g7");

        assert p.moves(b, "g7").contains("g5"); 
        assert p.moves(b, "g7").contains("g6"); 

    
    }
    public static void main(String[] args) {
	    test1();
        testPawnHomeMoveTwo();
        checkMoveWhenPieceThere();
        checkBishop();
        checkRook();
        checkKing();
        checkQueen();
        checkKnight();
        checkToString();
        //BigTests();
        checkClear();
        checkPawnMoveOnOpponent();
        checkRookMoveOnOpponent();
        checkBishopMoveOnOpponent();
        checkKnightMoveOnOpponent();
        checkKingMoveOnOpponent();
        checkQueenMoveOnOpponent();
        checkLog();
        //misc();
        testPawn();
    }

}