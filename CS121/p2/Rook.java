import java.util.*;

public class Rook extends Piece {
    public Rook(Color c) { this.color = c; }
    // implement appropriate methods

    public String toString() {
        if(this.color() == Color.BLACK)
        {
            return "br";
        }
        else{
            return "wr";
        }
    }

    public List<String> moves(Board b, String loc) {
        List<String> moves = new ArrayList<String>();
        addRookMoves(b, moves, colAt(loc.charAt(0)), rowAt(loc.charAt(1)));
        return moves;
    }

}