package Project2;

import java.util.Scanner;
import java.util.Random;

public class BattleShipGame{
	public static void main(String[] args){
		BattleShipGame game = new BattleShipGame();
		game.makeBoard();
		game.giveDirections();
		game.playGame();
	}

	protected String[][] gameBoard;	//2-dimensional array of string characters that represents the game board
	protected int[] boardDimensions = new int[2];   //array that stores the dimensions of the board as the user enters them
	protected int numShips, turnsTaken, successfulHits, hitsNeeded;
	protected boolean debugMode = false;//boolean that indicates if user is using game mode or debug mode
	protected boolean showInstructions;//boolean that indicates if user wants to see the game instructions
	
	//construct the gameBoard
	public void makeBoard(){
		//ask user if they want to play in game mode or debug mode
		Scanner s = new Scanner(System.in);
		debugMode = false;
		System.out.println("If you would like to play BattleShip in game mode enter 'g', if you would like to debug BattleShip enter 'd': ");
		String mode = s.nextLine();
		while(!mode.equalsIgnoreCase("g") && !mode.equalsIgnoreCase("d")){
			System.out.println("Invalid input. Enter 'g' for game mode or 'd' for debug mode: ");
			mode = s.nextLine();
		}
		if(mode.equalsIgnoreCase("d"))
			debugMode = true;
		//solicit the size of the board from the user
		System.out.println("Enter horizontal(x) board length (3 <= integer <= 10): ");
		boardDimensions[0] = s.nextInt();
		while(3 > boardDimensions[0] || boardDimensions[0] > 10){
			System.out.println("Invalid input, make sure vertical board size is an integer (3 <= integer <= 10): ");
			boardDimensions[0] = s.nextInt();
		}
		System.out.println("Enter vertical(y) board size (3 <= integer <= 10): ");
		boardDimensions[1] = s.nextInt();
		while(3 > boardDimensions[1] || boardDimensions[1] > 10){
			System.out.println("Invalid input, make sure horizontal board size is an integer (3 <= integer <= 10): ");
			boardDimensions[1] = s.nextInt();
		}
		showInstructions = false;
		s = new Scanner(System.in);
		//ask user if they would like to see instructions
		System.out.println("Would you like to see the instructions before you play BattleShip? (enter 'y' or 'n'): ");
		String answer = s.nextLine();
		if(answer.equalsIgnoreCase("y"))
			showInstructions = true;
		
		//initialize String[][] gameBoard
		gameBoard = new String[boardDimensions[0]][boardDimensions[1]];
			
		for(int i = 0; i < gameBoard.length; i++){
			for(int j = 0; j < gameBoard[i].length; j++){
				gameBoard[i][j] = "n";
			}
		}
		
		//determine how many ships will be placed on the gameBoard
		if(boardDimensions[0]*boardDimensions[1] <= 16){
			numShips = 1;
		}
		else if(boardDimensions[0]*boardDimensions[1] <= 36){
			numShips = 2;
		}
		else {
			numShips = 3;
		}	
		hitsNeeded = numShips*3;
	
		//select random locations for the ships and mines
		Random rGenerator = new Random();
		int xLocation;	//stores random x location
		int yLocation;	//stores random y location
		int shipsPlaced = 0; //stores the number of ships that have been successfully stored on the board
		int minesPlaced = 0; //stores the number of ships that have been successfully stored on the board
		
		while(shipsPlaced < numShips){
			//generate a random number to determine orientation of the ship
			int verticalOrHorizontal = rGenerator.nextInt(2);
			//generate location for horizontally oriented ship
			if(verticalOrHorizontal == 0){
				xLocation = rGenerator.nextInt(boardDimensions[0]-2);
				yLocation = rGenerator.nextInt(boardDimensions[1]);
				//verify that the random location is a valid location to place a ship
				if(gameBoard[xLocation][yLocation].equals("n") && gameBoard[xLocation+1][yLocation].equals("n")
						&& gameBoard[xLocation+2][yLocation].equals("n")){
					gameBoard[xLocation][yLocation] = "s";
					gameBoard[xLocation+1][yLocation] = "s";
					gameBoard[xLocation+2][yLocation] = "s";
					shipsPlaced++;
				}
			}
			
			//else generate location for vertically oriented ship
			else {
				xLocation = rGenerator.nextInt(boardDimensions[0]);
				yLocation = rGenerator.nextInt(boardDimensions[1]-2);
				//verify that the random location is a valid location to place a ship
				//then change String characters at the proper locations to 's'
				if(gameBoard[xLocation][yLocation].equals("n") && gameBoard[xLocation][yLocation+1].equals("n")
						&& gameBoard[xLocation][yLocation+2].equals("n")){
					gameBoard[xLocation][yLocation] = "s";
					gameBoard[xLocation][yLocation+1] = "s";
					gameBoard[xLocation][yLocation+2] = "s";
					shipsPlaced++;
				}
			}
		}
		
		while(minesPlaced < numShips){
			//generate random location for a mine
			xLocation = rGenerator.nextInt(boardDimensions[0]);
			yLocation = rGenerator.nextInt(boardDimensions[1]);
			if(gameBoard[xLocation][yLocation].equals("n")){
				gameBoard[xLocation][yLocation] = "m";
				minesPlaced++;
			}
		}
		
		//add 'c' and 'a' characters to the String[][] to indicate Close and Very Close misses.
		for(int v = 0; v < boardDimensions[0]; v++){
			for(int h = 0; h < boardDimensions[1] -2; h++){
				if(gameBoard[v][h+2].equals("s"))
					if(gameBoard[v][h].equals("n"))
						gameBoard[v][h] = "c";
				if(gameBoard[v][h].equals("s"))
					if(gameBoard[v][h+2].equals("n"))
						gameBoard[v][h+2] = "c";
			}
		}
		for(int v = 0; v < boardDimensions[0] -2; v++){
			for(int h = 0; h < boardDimensions[1]; h++){
				if(gameBoard[v+2][h].equals("s"))
					if(gameBoard[v][h].equals("n"))
						gameBoard[v][h] = "c";
				if(gameBoard[v][h].equals("s"))
					if(gameBoard[v+2][h].equals("n"))
						gameBoard[v+2][h] = "c";
			}
		}


		for(int v = 0; v < boardDimensions[0]; v++){
			for(int h = 0; h < boardDimensions[1] -1; h++){
				if(gameBoard[v][h+1].equals("s"))
					if(gameBoard[v][h].equals("n")||gameBoard[v][h].equals("c"))
						gameBoard[v][h] = "a";
				if(gameBoard[v][h].equals("s"))
					if(gameBoard[v][h+1].equals("n")||gameBoard[v][h+1].equals("c"))
						gameBoard[v][h+1] = "a";
			}
		}
		for(int v = 0; v < boardDimensions[0] -1; v++){
			for(int h = 0; h < boardDimensions[1]; h++){
				if(gameBoard[v+1][h].equals("s"))
					if(gameBoard[v][h].equals("n")||gameBoard[v][h].equals("c"))
						gameBoard[v][h] = "a";
				if(gameBoard[v][h].equals("s"))
					if(gameBoard[v+1][h].equals("n")||gameBoard[v+1][h].equals("c"))
						gameBoard[v+1][h] = "a";
				}
			}
	}
	
	//tell user how game works
	//give user an illustration of the board
	public void giveDirections(){
		System.out.println("\n");
		if(debugMode){
			System.out.println("YOU ARE IN DEBUG MODE:\nLowercase letters will become uppercase letters after they have been targeted.");
			System.out.println("Object representations:\nMine: 'm', Ship: 's', Very Close Miss: 'a', Close Miss: 'c', Miss: 'n'.");
			displayGameBoard();
		}
		if(showInstructions){
			System.out.println();
			System.out.println("INSTRUCTIONS:\nThe bottom left of the board (indicated by B below) is at coordinates (0,0).\nThe top right "
			+ "location (indicated by T below) is at coordinates ("+(boardDimensions[0]-1)+","+(boardDimensions[1]-1)+").");
			//describes the orientation of the board to the user
			//prints out a representation of the board
				
			//show user board that indicates upper left and lower right coordinate locations
			String temp1 = gameBoard[0][0];
			String temp2 = gameBoard[boardDimensions[0]-1][boardDimensions[1]-1];
			gameBoard[0][0] = "T";
			gameBoard[boardDimensions[0]-1][boardDimensions[1]-1] = "B";
			displayUserBoard();
			//reset upper left and lower right String values to "n"
			gameBoard[0][0] = temp1;
			gameBoard[boardDimensions[0]-1][boardDimensions[1]-1] = temp2;
			
			//shows user how to enter coordinates
			System.out.println("This means you must enter target locations in the range of (0,0) and ("+(boardDimensions[0]-1)+","+(boardDimensions[1]-1)+").");
			System.out.println("To enter the location of your target you must enter the x coordinate,\nthen a space, "
					+ "then the y coordinate, and finally the enter key.");
			System.out.println("\nExample: ");
			System.out.println("Enter target coordinates: \n2 5\nThis will result in a strike at location (2,5).\n");
			//informs user that misses within 1 location will be marked with an A
			//hits within 2 locations will be marked with a C
			System.out.println("To help you out, I'll let you know if your miss was 1 space away from a ship with "
					+ "the phrase 'A Miss, but Very Close.'");
			System.out.println("I'll tell you that you were 2 spaces from a ship by saying 'A Miss, but Close.'");
			System.out.println("Mines will be represented with 'M' and hits with 'H'.\nBut I will only show you the "
					+ "ships and mines that you have hit each time you have hit a ship.");
		}
	}
	
	//execute the BattleShip game
	public void playGame(){
		turnsTaken = 0;
		successfulHits = 0;
		int xCoordinate = 0, yCoordinate = 0; //store location of the users current guess
		Scanner s = new Scanner(System.in);
		System.out.println(numShips+" ship(s) and " + numShips +" mine(s) are hidden, let's begin!");
		while(successfulHits < hitsNeeded){
			System.out.println("Turns Taken: "+turnsTaken);
			System.out.println("Enter target location: ");
			xCoordinate = s.nextInt();
			yCoordinate = s.nextInt();
			boolean validInput = false;	//determines if the user has entered coordinates that are within the boundaries of the board
			if(xCoordinate >= 0 && xCoordinate < boardDimensions[0] && yCoordinate >= 0 && yCoordinate < boardDimensions[1]){
				validInput = true;
				System.out.println("\n\n");
			}
			while(!validInput){
				System.out.println("Coordinates out of bounds! Try again: ");
				xCoordinate = s.nextInt();
				yCoordinate = s.nextInt();
				if(xCoordinate >= 0 && xCoordinate < boardDimensions[0] && yCoordinate >= 0 && yCoordinate < boardDimensions[1]){
					validInput = true;
					System.out.println("\n\n");
				}
			}
			//make sure that the user has not guessed this location already
			/*if(!gameBoard[xCoordinate][yCoordinate].equals("a")&&!gameBoard[xCoordinate][yCoordinate].equals("c")&&
					!gameBoard[xCoordinate][yCoordinate].equals("s")&&!gameBoard[xCoordinate][yCoordinate].equals("n")&&
					!gameBoard[xCoordinate][yCoordinate].equals("m")){*/
			if(Character.isUpperCase(gameBoard[xCoordinate][yCoordinate].charAt(0))){
				System.out.println("You have already targeted this location! You lose 2 turns for wasting ammo.");
				turnsTaken+=2;
			}
			//check to see if user has hit a ship
			else if(gameBoard[xCoordinate][yCoordinate].equals("s")){
				System.out.println("Hit!");
				gameBoard[xCoordinate][yCoordinate] = "H";
				turnsTaken++;
				successfulHits++;
				if(!debugMode)
					displayUserBoard();
			}
			//check to see if the user's guess is within 1 space of ship
			else if(gameBoard[xCoordinate][yCoordinate].equals("a")){
				System.out.println("A Miss, but Very Close");
				gameBoard[xCoordinate][yCoordinate] = "A";					
				turnsTaken++;
			}
			//check to see if the user's guess is within two spaces of ship
			else if(gameBoard[xCoordinate][yCoordinate].equals("c")){
				System.out.println("A Miss, but Close");
				gameBoard[xCoordinate][yCoordinate] = "C";
				turnsTaken++;
			}
			//check to see if user hit a mine
			else if(gameBoard[xCoordinate][yCoordinate].equals("m")){
				System.out.println("Uh-oh! You hit a mine! You lose one turn.");
				gameBoard[xCoordinate][yCoordinate] = "M";
				turnsTaken += 2;
			}
			else if(gameBoard[xCoordinate][yCoordinate].equals("n")){
				System.out.println("Miss.");
				gameBoard[xCoordinate][yCoordinate] = "N";
				turnsTaken += 2;
			}
			else
				System.out.println("ERROR IN playGame()!");
			validInput = false;
			if(debugMode)
				displayGameBoard();
		}
		System.out.println("\nGood job! You sunk the ships in "+turnsTaken+" turns!");
		System.out.println("Would you like to play again? (enter 'y' or 'n'): ");
		Scanner scan = new Scanner(System.in);
		String answer = (String)scan.nextLine();
		if(answer.equals("y")){
			makeBoard();
			giveDirections();
			playGame();
		}		
		else {
			System.out.println("Thank you for playing.");
			s.close();
			scan.close();
		}
	}
		
	//display board while in debug mode
	public void displayGameBoard(){
		System.out.println("\ngameBoard for debugging: ");
		for(int i = boardDimensions[1] - 1; i >= 0; i--){
			for(int j = 0; j < boardDimensions[0]; j++){
				System.out.print(gameBoard[j][i] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	//display board for user that only shows hit ships and mines
	public void displayUserBoard(){
		System.out.println("\nYour Hit History: ");
		for(int i = boardDimensions[1]-1; i >= 0; i--){
			for(int j = 0; j < boardDimensions[0]; j++){
				if(!gameBoard[j][i].equals("H")&&!gameBoard[j][i].equals("T")&&!gameBoard[j][i].equals("B"))
					System.out.print("n ");
				else
					System.out.print(gameBoard[j][i]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	//print all attributes of BattleShipGame class
	public String toString(){
		displayGameBoard();
		displayUserBoard();
		System.out.println("boardDimensions: [" + boardDimensions[0] + "," + boardDimensions[1] + "]");
		System.out.println("numShips: "+numShips+", turnsTaken: "+turnsTaken+", successfulHits: "+successfulHits+", "
				+ "hitsNeeded: "+ hitsNeeded+", debugMode: "+debugMode);
		return"";
	}
}
