import java.util.*;

public class Board {

    private Piece[][] pieces = new Piece[8][8];
    
    private static Board theBoard;
    private  List<BoardListener> listeners = new ArrayList<BoardListener>();

    private Board() { }
    
    public static Board theBoard() {
        if(theBoard == null){ 
            theBoard = new Board();
        }
        return theBoard;
    }

    // Returns piece at given loc or null if no such piece
    // exists
    public Piece getPiece(String loc) {
        checkValidLoc(loc);
        int row = rowAt(loc.charAt(1));
        int col = colAt(loc.charAt(0));
        return pieces[row][col];
    }

    public void addPiece(Piece p, String loc) {
        checkValidLoc(loc);
        int row = rowAt(loc.charAt(1));
        int col = colAt(loc.charAt(0));
        if(pieces[row][col] != null)
        {
            throw new NullPointerException("Location already occupied");
        }
        pieces[row][col] = p;
    }

    public void movePiece(String from, String to) {
        checkValidLoc(from);
        checkValidLoc(to);
        int row = rowAt(from.charAt(1));
        int col = colAt(from.charAt(0));
        Piece p = pieces[row][col];
        if (p == null || !p.moves(theBoard, from).contains(to)){
            throw new NullPointerException("Invalid Move");
        }

        pieces[row][col] = null;
        int destRow = rowAt(to.charAt(1));
        int destCol = colAt(to.charAt(0));

        if(pieces[destRow][destCol] == null){
            for(BoardListener bl : listeners)
            {
                bl.onMove(from, to, p);
            }
        }  
        else{
            for(BoardListener bl : listeners){
                bl.onMove(from, to, p);
                bl.onCapture(p, pieces[destRow][destCol]);
            }
        }

        pieces[destRow][destCol] = p;


    }

    public void clear() {
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                pieces[i][j] = null;
            }
        }
    }

    public void registerListener(BoardListener bl) {
        listeners.add(bl);
    }

    public void removeListener(BoardListener bl) {
        if(!listeners.remove(bl))
        {
            throw new NullPointerException("Listener not found");
        }
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    public void iterate(BoardExternalIterator bi) {
        for(int i = 7; i >= 0; i--){
            for (int j = 0; j < 8; j++){
                bi.visit(rowColToLoc(i, j), pieces[i][j]);
            }
        }
    }


    private int rowAt(char row)
    {
        return row - 49;
    }

    private int colAt(char col)
    {
        return col - 97;
    }

    private String rowColToLoc(int row, int col)
    {
        char r = (char) (row + 49);
        char c = (char) (col + 97);
        return Character.toString(c) + Character.toString(r);
    }

    private void checkValidLoc(String loc) {

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

}