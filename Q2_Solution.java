
import java.util.Scanner;

public class Q2_Solution {

	public static boolean Check(char[][] board, String word) {
		if(word.length() == 0)
			return false;
		if(board == null || board.length == 0 || (board.length == 1 && board[0].length == 0))
			return false;
		
		int m = board.length;
		int n = board[0].length;
		
		// cannot visit the same point twice
		boolean[][] visited = new boolean[m][n];
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				visited[i][j] = false;
			}
		}
		
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				if(board[i][j] == word.charAt(0)) {	  // find the first letter
					visited[i][j] = true;
					
					if(word.length() == 1)
						return true;
					else if(Find(board, visited, word.substring(1), i, j))
						return true;
					
					visited[i][j] = false;
				}
			}
		}
		return false;
	}
	

	static boolean Find(char[][] board, boolean [][]visited, String word, int i, int j) {
		int [][]direction = {
			{1, 0},		// down
			{-1, 0},	// up
			{0, 1},		// right
			{0, -1}		// left
		};
		
		// check 4 directions for the next letter
		for(int k = 0; k < direction.length; k++) {	  
			int x = direction[k][0] + i;
			int y = direction[k][1] + j;
			
			if(x >= 0 && x < board.length && y >= 0 && y < board[0].length && board[x][y] == word.charAt(0) && visited[x][y] == false) {
				visited[x][y] = true;
				
				if(word.length() == 1)
					return true;
				else if(Find(board, visited, word.substring(1), x, y))	 // recursive calls
					return true;
				
				visited[x][y] = false;		// restore to unvisited for the next search
			}	
		}
		return false;	
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Please input the widthm, length of the 2D board and the tested word:");
		
		Scanner sc = new Scanner(System.in);
		int m, n;
		
		System.out.println("width:");
		m = sc.nextInt();
		System.out.println("length:");
		n = sc.nextInt();
		
		char [][]board = new char[m][n];
		String word;
		System.out.println("board:");
		for(int i = 0; i < m; i++)
			for(int j = 0; j < n; j++)
				board[i][j] = sc.next().charAt(0);
	
		while(true) {
			System.out.println("word:");
			word = sc.next();
			if(word.equals("#"))
				break;
			if(Check(board, word))
				System.out.println("true. (Enter '#' to exit)" );
			else
				System.out.println("false. (Enter '#' to exit)");
		}
		sc.close();
	}

}
