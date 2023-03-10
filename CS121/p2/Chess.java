import java.io.*;

public class Chess {
    public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java Chess layout moves");
			throw new NullPointerException("Invalid arguments");
		}
		Piece.registerPiece(new KingFactory());
		Piece.registerPiece(new QueenFactory());
		Piece.registerPiece(new KnightFactory());
		Piece.registerPiece(new BishopFactory());
		Piece.registerPiece(new RookFactory());
		Piece.registerPiece(new PawnFactory());

		Board.theBoard().registerListener(new Logger());
		Board b = Board.theBoard();
		
		setupBoard(args[0], b);
		readMoves(args[1], b);

		System.out.println("Final board:");
		Board.theBoard().iterate(new BoardPrinter());
	}

	private static void setupBoard(String filename, Board b)
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line, loc, p;
			while ((line = br.readLine()) != null) {
				if(line.charAt(0) != '#'){
					loc = line.substring(0, 2);
					p = line.substring(3);
					if(line.charAt(2) != '='){ throw new NullPointerException(); }
					checkValidLoc(loc);
					checkValidPiece(p);
					if(b.getPiece(loc) != null){ 
						throw new NullPointerException();
					}
					b.addPiece(Piece.createPiece(p), loc);
					
				}
			}
		}
		catch (Exception e) {
			throw new NullPointerException("Invalid layout file");
		}


	}

	private static void readMoves(String filename, Board b)
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line, to, from;
			while ((line = br.readLine()) != null) {
				if(line.charAt(0) != '#'){
					from = line.substring(0, 2);
					to = line.substring(3);
					if(line.charAt(2) != '-'){ throw new NullPointerException("Invalid move file"); }
					checkValidLoc(to);
					checkValidLoc(from);
					b.movePiece(from, to);
					
				}
			}
		}
		catch (Exception e) {
			throw new NullPointerException("Invalid move file");
		}
	}

	private static void checkValidLoc(String loc) {

		if(loc.length() != 2){
			throw new NullPointerException("Invalid location");
		}

		char col = loc.charAt(0);
		char row = loc.charAt(1);

		if(!(col >= 'a' && col <= 'h' && row >= '1' && row <= '8'))
		{
			throw new NullPointerException("Invalid location");
		}
	}

	private static void checkValidPiece(String piece) 
	{
		if(piece.length() != 2){ throw new NullPointerException("Invalid piece"); }
		char c = piece.charAt(0);
		char p = piece.charAt(1);
		if(p != 'p' && p != 'b' && p != 'n' && p != 'r' && p != 'q' && p != 'k')
		{
			throw new NullPointerException("Invalid piece");
		}
		if(c != 'w' && c!= 'b'){ throw new NullPointerException("Invalid piece color"); }
	}




}