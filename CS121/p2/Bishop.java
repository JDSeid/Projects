import java.util.*;

public class Bishop extends Piece {
    public Bishop(Color c) { this.color = c; }
    // implement appropriate methods

    public String toString() {
        if(this.color() == Color.BLACK)
        {
            return "bb";
        }
        else{
            return "wb";
        }
    }

    public List<String> moves(Board b, String loc) {
        List<String> moves = new ArrayList<String>();
        addDiagonalMoves(b, moves, colAt(loc.charAt(0)), rowAt(loc.charAt(1)));
        return moves;
    }

}