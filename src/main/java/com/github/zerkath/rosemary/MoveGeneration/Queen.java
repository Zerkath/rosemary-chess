package com.github.zerkath.rosemary.MoveGeneration;

import com.github.zerkath.rosemary.BoardRepresentation.BoardState;
import com.github.zerkath.rosemary.DataTypes.Coordinate;
import com.github.zerkath.rosemary.DataTypes.Moves;

public class Queen {

    public static void getMoves(Coordinate origin, BoardState boardState, Moves moves) {
        Rook.getMoves(origin, boardState, moves);
        Bishop.getMoves(origin, boardState, moves);
    }
}
