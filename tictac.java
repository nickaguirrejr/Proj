
/*
Authors: Nicholas Aguirre and Rita Mathis
Date of Creation: 10/14/15
*/

//Playable within terminal!

import java.util.*;
public class tictac
{
	public static char[][] board;

	private static String stat;

	public static int turn = 0;

	public tictac(){
		beginGame(3,3);
	}

	public static String getStat(){
		return stat;
	}

	public static int getRow(){
		return board.length;
	}

	public static int getCol(){
		return board[0].length;
	}

	public static void beginGame(int rows, int cols){
		board = new char[rows][cols];
		stat = "play";
		reset();
	}

	private static void reset(){
		for (int i = 0; i < board.length; i++){
			Arrays.fill(board[i], ' ');
		}
	}

	public static boolean validIndex(int i, int j){
		if (i >= 0 && i < 3 && j >= 0 && j < 3)
			return true;
		return false;
	}

	public static void print(){
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[i].length; j++){
				System.out.print(board[i][j]);
				if (j != board[i].length - 1)
					System.out.print("|");
			}
			System.out.println();
			if (i != board.length - 1)
				System.out.println("- - -");
		}
	}

	public static void mark(int r, int c){
		if (!stat.equals("play"))
			return;
		if (!(validIndex(r, c)))
			return;
		if (board[r][c] != ' '){
			System.out.println("That spot is already taken. Please pick another.");
			return;
		}
		if (turn % 2 == 0){
			board[r][c] = 'O';
		}
		else
			board[r][c] = 'X';
		turn++;
		if (turn>8) 
			stat = "tie";
		if (gameWon() == true)
			stat = "won";
	}

	public static boolean gameWon(){
		for (int row = 0; row < board.length; row++){
			if (board[row][0] == board[row][1] && board[row][0] == board[row][2] && board[row][0] != ' ')
				return true;
		}

		for (int col = 0; col < board[0].length; col++){
			if(board[0][col] == board[1][col] && board[0][col] == board[2][col] && board[0][col] != ' '){
				return true;
			}
		}

		if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != ' ') return true;
		if (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != ' ') return true;
		return false;
	}


	public static void main (String[] args){
		tictac game = new tictac();
		game.print();
		Scanner sc = new Scanner(System.in);
		int row;
		int col;
		while (stat.equals("play")){
			if (turn % 2 == 0)
				System.out.println("It is player 1's turn.");
			else
				System.out.println("It is player 2's turn.");
			System.out.println("Which row would you like to mark?");
			row = sc.nextInt();
			System.out.println("Which column would you like to mark?");
			col = sc.nextInt();
			if (!validIndex(row - 1, col - 1)){
				System.out.println("That is not a valid spot. Please try again.");
				continue;
			}
			else{
				game.mark(row - 1, col - 1);
				game.print();
			}
		}
		if (stat == "tie") System.out.println("It is a tie.");
		else if (turn % 2 == 1) System.out.println("Player 1 has won.");
		else if (turn % 2 == 0) System.out.println("Player 2 has won.");
	}
}
