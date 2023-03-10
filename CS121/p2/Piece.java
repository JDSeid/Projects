import java.util.*;

abstract public class Piece {

    protected Color color;
    private static HashMap<Character, PieceFactory> factoryMap = new HashMap<Character, PieceFactory>();

    public static void registerPiece(PieceFactory pf) {
        factoryMap.put(pf.symbol(), pf);
    }

    public static Piece createPiece(String name) {

        if(factoryMap.get(name.charAt(1)) == null){
            throw new NullPointerException("Invalid Piece Character");
        }
        if(name.charAt(0) == 'b'){
           return factoryMap.get(name.charAt(1)).create(Color.BLACK);
        }
        else if (name.charAt(0) == 'w'){ 
            return factoryMap.get(name.charAt(1)).create(Color.WHITE);
        }
        throw new NullPointerException("Invalid Piece Color");
    }

    public Color color() {
        return this.color;
    }

    protected int rowAt(char row)
    {
        return row - 49;
    }

    protected int colAt(char col)
    {
        return col - 97;
    }

    protected String rowColToLoc(int row, int col)
    {
        char r = (char) (row + 49);
        char c = (char) (col + 97);
        return Character.toString(c) + Character.toString(r);
    }

    protected boolean friendlyPieceAt(Board b, String loc, Color c)
    {
        return !(b.getPiece(loc) == null || b.getPiece(loc).color != c);

    }

    protected boolean enemyPieceAt(Board b, String loc, Color c)
    {
        return !(b.getPiece(loc) == null || b.getPiece(loc).color == c);
    }

    protected boolean inBounds(String loc)
    {
        int row = rowAt(loc.charAt(1));
        int col = colAt(loc.charAt(0));
        return  row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }

    protected boolean addIfValid(Board b, List<String> moves, int col, int row)
    {
        String loc = rowColToLoc(row, col);
        if(!inBounds(loc)){ ;
            return false; }
        if(friendlyPieceAt(b, loc, this.color())) { 
            return false; }
        moves.add(loc);
        return true;
    }

    protected void addDiagonalMoves(Board b, List<String> moves, int col, int row)
    {
        int i = 1;
        while(addIfValid(b, moves, col + i, row + i))
        {
            if(enemyPieceAt(b, rowColToLoc(row + i, col + i), color)) { break; }
            i++;
        }
        i = 1;
        while(addIfValid(b, moves, col + i, row - i))
        {
            if(enemyPieceAt(b, rowColToLoc(row - i, col + i), color)) { break; }
            i++;
        }
        i = 1;
        while(addIfValid(b, moves, col - i, row + i))
        {
            if(enemyPieceAt(b, rowColToLoc(row + i, col - i), color)) { break; }
            i++;
        }
        i = 1;
        while(addIfValid(b, moves, col - i, row - i))
        {
            if(enemyPieceAt(b, rowColToLoc(row - i, col - i), color)) { break; }
            i++;
        }
            
    }

    protected void addRookMoves(Board b, List<String> moves, int col, int row)
    {
        int i = 1;
        while(addIfValid(b, moves, col + i, row))
        {
            if(enemyPieceAt(b, rowColToLoc(row, col + i), color)) { break; }
            i++;
        }
        i = 1;
        while(addIfValid(b, moves, col - i, row))
        {
            if(enemyPieceAt(b, rowColToLoc(row, col - i), color)) { break; }
            i++;
        }
        i = 1;
        while(addIfValid(b, moves, col, row + i))
        {
            if(enemyPieceAt(b, rowColToLoc(row + i, col), color)) { break; }
            i++;
        }
        i = 1;
        while(addIfValid(b, moves, col, row - i))
        {
            if(enemyPieceAt(b, rowColToLoc(row - i, col), color)) { break; }
            i++;
        }
    }


    abstract public String toString();

    abstract public List<String> moves(Board b, String loc);
}