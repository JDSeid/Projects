import java.util.*;

public class King extends Piece {
    public King(Color c) { this.color = c; }
    // implement appropriate methods

    public String toString() {
        if(this.color() == Color.BLACK)
        {
            return "bk";
        }
        else{
            return "wk";
        }
    }

    public List<String> moves(Board b, String loc) {
        List<String> moves = new ArrayList<String>();
        int row = rowAt(loc.charAt(1));
        int col = colAt(loc.charAt(0));

        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0){ continue; }
                addIfValid(b, moves, col + i, row + j);
            }
        }

        return moves;
    }

}