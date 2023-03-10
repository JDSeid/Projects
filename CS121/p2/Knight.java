import java.util.*;

public class Knight extends Piece {
    public Knight(Color c) { this.color = c; }
    // implement appropriate methods

    public String toString() {
        if(this.color() == Color.BLACK){
            return "bn";
        }
        else{
            return "wn";
        }
    }

    public List<String> moves(Board b, String loc) {
        List<String> moves = new ArrayList<String>();
        int row = rowAt(loc.charAt(1));
        int col = colAt(loc.charAt(0));
        int j;
        for(int i = -2; i <= 2; i++){
            if(i == 0) { continue; }
            j = Math.abs(i % 2) + 1;
            addIfValid(b, moves, col + i, row + j);
            addIfValid(b, moves, col + i, row - j);

        }
        return moves;
    }

}