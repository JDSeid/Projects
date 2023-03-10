import java.util.*;

public class Queen extends Piece {
    public Queen(Color c) { this.color = c; }
    // implement appropriate methods

    public String toString() {
        if(this.color() == Color.BLACK)
        {
            return "bq";
        }
        else{
            return "wq";
        }
    }

    public List<String> moves(Board b, String loc) {
        List<String> moves = new ArrayList<String>();
        addDiagonalMoves(b, moves, colAt(loc.charAt(0)), rowAt(loc.charAt(1)));
        addRookMoves(b, moves, colAt(loc.charAt(0)), rowAt(loc.charAt(1)));
        return moves;
    }

}