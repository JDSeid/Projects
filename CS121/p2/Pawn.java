import java.util.*;

public class Pawn extends Piece {
    public Pawn(Color c) { this.color = c; }
    // implement appropriate methods

    public String toString() {

        if(this.color() == Color.BLACK)
        {
            return "bp";
        }
        else{
            return "wp";
        }
        
    }

    public List<String> moves(Board b, String loc) {


        int row = rowAt(loc.charAt(1));
        int col = colAt(loc.charAt(0));
        List<String> moves = new ArrayList<String>();
        Color color = this.color();
        if((color == Color.BLACK && row == 0) || (color == Color.WHITE && row == 7)) { 
            moves.add("Pawn at end of board");
            return moves; }

        if(b.getPiece(rowColToLoc(moveFoward(color, row, 1), col)) == null && row > 0)
        {
            moves.add(rowColToLoc(moveFoward(color, row, 1), col));
            if(row == getHomeRow(color) && b.getPiece(rowColToLoc(moveFoward(color, row, 2), col)) == null)
            {
                moves.add(rowColToLoc(moveFoward(color, row, 2), col));
            }       
        }
        Piece p1 = null;
        Piece p2 = null;
        if(col != 0){  p1 = b.getPiece(rowColToLoc(moveFoward(color, row, 1), col - 1)); }
        if(col != 7){  p2 = b.getPiece(rowColToLoc(moveFoward(color, row, 1), col + 1));}
       

        if(p1 != null && p1.color() != color)
        {
            moves.add(rowColToLoc(moveFoward(color, row, 1), col - 1));
        }
        if(p2 != null && p2.color() != color)
        {
            moves.add(rowColToLoc(moveFoward(color, row, 1), col + 1));
        }
        return moves;
    }


    private int moveFoward(Color color, int row, int amount)
    {
        if(color == Color.WHITE)
        {
            return row + amount;
        }
        return row - amount;
    }

    private int getHomeRow(Color color)
    {
        if (color == Color.WHITE){
            return 1;
        }
        return 6;
    }
}