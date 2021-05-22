class Utils {

    //todo change functions to return Coordinates
    static public char toColumnCharacter(int i) {
        return (char)(i + 'a');
    }

    static public int toColumnNumber(char c) {
        return c - 'a';
    }

    static public char toRowNumberChar(int i) {
        return Character.forDigit(8-i, 10);
    }

    static public int toRowNumber(char c) {
        return 8 - Integer.parseInt(String.valueOf(c));
    }

    //give a string with 2 characters fen format (en passant)
    static public Coordinate parseCoordinate(String str) {
        if(str.equals("-")) return null;
        char[] c = str.toCharArray();
        return new Coordinate(toColumnNumber(c[0]), toRowNumber(c[1]));
    }

    static public String parseCoordinate(Coordinate coords) {
        return String.valueOf(toColumnCharacter(coords.column)) + toRowNumberChar(coords.row);
    }

    static public String parseCommand(Move move) {
        return parseCoordinate(move.origin) + parseCoordinate(move.destination);
    }

    static public Move parseCommand(String command) {
        return new Move(parseCoordinate(command.substring(0, 2)), parseCoordinate(command.substring(2, 4)));
    }

    static public String toFenString(BoardState boardState) {
        char [][] board = boardState.board;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int empty = 0;
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] == '-') {
                    empty++;
                } else if(board[i][j] != '-') {
                    if (empty != 0) {
                        result.append(empty);
                        empty = 0;
                    }
                    result.append(board[i][j]);
                }
            }
            if(empty != 0) {
                result.append(empty);
            }
            if(i != 7) result.append("/");
        }

        result.append(boardState.turn == PlayerTurn.BLACK ? " b" : " w");

        String WhiteCastlingString = "";
        switch (boardState.whiteCastling) {
            case KINGSIDE: WhiteCastlingString = "K"; break;
            case QUEENSIDE: WhiteCastlingString = "Q"; break;
            case BOTH: WhiteCastlingString = "KQ"; break;
        }

        String BlackCastlingString = "";
        switch (boardState.blackCastling) {
            case KINGSIDE: BlackCastlingString = "k"; break;
            case QUEENSIDE: BlackCastlingString = "q"; break;
            case BOTH: BlackCastlingString = "kq"; break;
        }

        if(WhiteCastlingString.length() < 1 && BlackCastlingString.length() < 1) {
            result.append(" - ");
        } else {
            result.append(" ");
            result.append(WhiteCastlingString);
            result.append(BlackCastlingString);
            result.append(" ");
        }

        if(boardState.enPassant != null) {
            result.append(parseCoordinate(boardState.enPassant)); //todo using coordinate
        } else {
            result.append("-");
        }

        result.append(" ").append(boardState.halfMove).append(" ");
        result.append(boardState.turnNumber);
        return result.toString();
    }

    static public BoardState parseFen(String fen) {

        /*
        split data indexes
        0 = fen board data
        1 = white or black turn
        2 = castling rights
        3 = en passant move
        4 = half-move clock (how many turns since last capture or pawn move 50 move rule)
        5 = full-move number starts at 1 incremented after blacks move or at the start of white move
        */
        String [] split = fen.split(" ");

        BoardState boardState = new BoardState();

        if(split.length != 6) return null; //todo give out errors
        String [] rows = split[0].split("/");
        if(rows.length != 8) return null;

        if(split[1].equals("w")) {
            boardState.turn = PlayerTurn.WHITE;
        } else if (split[1].equals("b")) {
            boardState.turn = PlayerTurn.BLACK;
        }

        boardState.setCastling(split[2].toCharArray()); //set castling rights
        boardState.turnNumber = Integer.parseInt(split[5]);

        boardState.enPassant = parseCoordinate(split[3]);

        // Add pieces to the board
        for (int i = 0; i < rows.length; i++) {
            boardState.addRow(rows[i], i);
        }

        return boardState;
    }

    static public void printBoard(BoardState board) {
        System.out.println(getVisualBoardString(board.board));
    }

    static public String getVisualBoardString(char [][] board) {
        StringBuilder str = new StringBuilder();
        str.append("  0 1 2 3 4 5 6 7\n");
        for(int i = 0; i < board.length; i++) {
            str.append(i).append(" ");
            for(int j = 0; j < board[i].length; j++) {
                char piece = board[i][j];
                if(piece != '-') {
                    str.append(piece);
                } else {
                    str.append(" ");
                }
                str.append(" ");
            }
            str.append("\n");
        }
        str.append("\n");

        return str.toString();
    }
}