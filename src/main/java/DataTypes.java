import java.util.LinkedList;

class Coordinate {
    public Coordinate(int column, int row) {
        this.column = column;
        this.row = row;
    }
    public Coordinate() {}
    public int column;
    public int row;
}
class Move {
    public Move(Coordinate origin, Coordinate destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Move() {}
    public Coordinate origin;
    public Coordinate destination;
}

class Moves extends LinkedList<Move>{
}

enum PlayerTurn {
    WHITE,
    BLACK,
}

enum CastlingRights {
    QUEENSIDE,
    KINGSIDE,
    BOTH,
    NONE
}