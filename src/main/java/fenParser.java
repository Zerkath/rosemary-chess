import java.util.*;
class fenParser {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        String result;
        char [][] board = new char[8][8]; 
        System.out.println("Enter FEN");
        result = scn.nextLine();
        String [] split = result.split(" ");
        String [] rows = split[0].split("/");
        for (int i = 0; i < 8; i++) {
            int c = 0;
            for (Character ch : rows[i].toCharArray()) {
                if(Character.isDigit(ch)) {
                    int numOfEmpty = Character.digit(ch, 10);
                    for (int j = 0; j < numOfEmpty; j++) {
                        board[i][c] = '0';
                        c++;
                    }
                } else {
                    board[i][c] = ch;
                    c++;
                }
            }
        }
        for (char[] row : board) {
            for (char square : row) {
                System.out.print(square + " ");
            }
            System.out.println();
        }
        scn.close();
    }
}