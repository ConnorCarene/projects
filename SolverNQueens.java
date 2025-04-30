// Connor Carene
// G Turini
// CS 203
// NQueens Problem with fast sorting algo


/*
   This code is my implementation of the fast N-Queens Problem algorithm
   described in the paper provided in class. The code will generate a
   random permutation of the board with no like columns or rows and from
   there will look to make swaps that lower the total amount of queen 
   collisions on the board. If it ever runs into a situation where it 
   can't make any moves that decrease collisions, it will restart with
   a new random permutation and repeat until the puzzle is solved.
*/
import java.util.Arrays;
import java.util.Random;

// This class encompasses all methods for the project include the main method.
public class SolverNQueens
{
   /*
      The main method simply prompts user for a size n to solve the puzzle and
      then it will create the array and call the solveNQueens function that
      houses all the other methods.
   */
   public static void main(String[] args)
   {
      long startTime = System.nanoTime(); 
      int n = 8; // Enter the desired number of queens left of here.
      int[] solvedBoard = new int[n];
      solvedBoard = solveNQueens(n);
      System.out.print("\nSolved Board: \n");
      printBoard(solvedBoard);
      long endTime = System.nanoTime();
      long duration = endTime - startTime;
      double seconds = (double) duration / 1_000_000_000.0;
      System.out.println("Runtime: " + seconds + " seconds");
   }
   
   /*
      This function is what runs the program. We begin by declaring the
      necessary variables. Next, we initialize our diagonal arrays as well
      as create the game board. Then we start the main control loop which
      checks for two conditions. It only stops in the situation where both
      the swaps performed are zero and the total collisions as seen in the
      diagonal arrays are also zero. Inside the code we iterate through all
      the pairs of columns to see if either one in each situation is under
      attack. If so we look to see if swapping them will yield less collisions
      overall. If so, we will execute the swap, update the diagonals, and 
      increment the swaps performed variable used in the control loop. As
      a check, if no swaps were performed and there are still detected 
      collisions, we will restart the program with a new permutation.
      After this when the loop is exited, we have a solution to the problem
      that we return to the main function.
   */
   public static int[] solveNQueens(int n)
   { // Swaps performed is 99 to allow it to enter the loop.
      int swaps_performed = 99, preSwapAttacks = 0, operationCount = 0;
      int[] d1 = new int[2*n-1];
      int[] d2 = new int[2*n-1];
      int[] board = new int[n];
      
      createGameBoard(board);
      System.out.print("Starting Board:\n");
      printBoard(board);
      initializeDiagonal1(board, d1);
      initializeDiagonal2(board, d2);
            
      while (swaps_performed != 0 || totalCollisions(d1, d2) != 0)
      {
         preSwapAttacks = 0;
         swaps_performed = 0;
         for (int i = 0; i < board.length; i++)
         {
            for (int j = i + 1; j < board.length; j++)
            {
               if (queenAttackedBy(board, i) + queenAttackedBy(board, j) > 0)
               {
                  preSwapAttacks = (queenAttackedBy(board, i) + queenAttackedBy(board, j));
                  if (preSwapAttacks > checkSwap(board, i, j))
                  {
                     swapCols(board, i, j);
                     updateDiags(board, i, j, d1, d2);
                     swaps_performed += 1;
                  }
               }
            }
         }
         
         if (swaps_performed == 0 && totalCollisions(d1, d2) > 0)
         {
            System.out.print("No solution found, board reset. New Board:\n");
            shuffleBoard(board);
            printBoard(board);
            initializeDiagonal1(board, d1);
            initializeDiagonal2(board, d2);
         }
      }
      
      return board;
      
   }
   
   // The createGameBoard function simply fills the array down the main
   // Diagonal and then calls the shuffleBoard function to mix it up.
   private static void createGameBoard(int[] board)
   {
      for (int i = 0; i < board.length; i++)
      {         
         board[i] = i;
      }
      shuffleBoard(board);
   }
   
   // The swapCols function just swaps two of the columns in the array.
   private static void swapCols(int[] board, int i, int j)
   {
      int temp = board[i];
      board[i] = board[j];
      board[j] = temp;
   }
   
   // The shuffleBoard function utilizies the random function to go 
   // through the array are randomly swap columns.
   private static void shuffleBoard(int[] arr)
   {
      for (int i = arr.length - 1; i > 0; i--)
      {
         int j = (int) (Math.random() * (i + 1));
         swapCols(arr, i, j);
      }
   }
   
   // This print voard function is how I chose to visualze the game
   // boards. Q represents a slot with a queen and . is empty.
   private static void printBoard(int[] board)
   {
      for (int i = 0; i < board.length; i++)
      {
         for( int j = 0; j < board.length; j++)
         {
            if (board[j] == i)
            {
               System.out.print("Q ");
            }
            else
            {
               System.out.print(". ");
            }
         }
         System.out.println();
      }
   }
   
   /*
      The initializeDiagonal1 and initializeDiagonal2 functions use the
      properties of our array in order to go through and count where the
      queens are. Since we already knew that every diagonal array will be
      2n -1 elements, we create the arrays and assign the number of queens
      in each of them based on our count.
   */
   private static void initializeDiagonal1(int[] arr, int[] d1)
   {
      int k = 0;
      int diaglen = 2*arr.length - 1;
      
      for (int i = 0; i < diaglen; i++)
         {
            d1[i] = 0;
         }
         
      for (int i = 0; i < diaglen; i++) // Iterates correct amount of times for diagonals.
      {
         k = 0;
         if (i < arr.length)
            {
               for (int j = 0; j <= i; j++)
               {
                  if (arr[j] == (arr.length - 1 - i + j)) 
                  {
                     d1[i] += 1;
                  }
               } 
            }
         else
            {
               for (int j = i - arr.length + 1; j < arr.length; j++)
               {
                  if (arr[j] == (k)) 
                  {
                     d1[i] += 1;
                  }
                  k += 1;
               } 
            }
      }
   }
   
   private static void initializeDiagonal2(int[] arr, int[] d2)
   {
      int diaglen = 2*arr.length - 1;
      for (int i = 0; i < diaglen; i++)
      {
         d2[i] = 0;
      }
      for (int i = 0; i <= diaglen; i++) // Iterates correct amount of times for diagonals.
      {
         if (i < arr.length)
            {
               for (int j = 0; j <= i; j++)
               {
                  if (arr[j] == (i - j)) 
                  {
                     d2[i] += 1;
                  }
               } 
            }
         else
            {
               for (int j = 0; j <= diaglen - i; j++)
               {
                  if (arr[arr.length - j - 1] == (j + i - arr.length + 1)) 
                  {
                     d2[i] += 1;
                  }
               } 
            }
      }
   }
   
   // Simple method to count the collisions in the diagonal arrays.
   // the amount of queens - 1 in the diagonal is the amount of attacks
   // for each diagonal.
   private static int totalCollisions(int[] d1, int[] d2)
   {
      int attacks = 0;
      for (int i = 0; i < d1.length; i++)
      {
         if (d1[i] > 1)
         {
            attacks += d1[i] - 1;
         }
         if (d2[i] > 1)
         {
            attacks += d2[i] - 1;
         }
      }
      return attacks;
   }
   
   /*
      The queenAttackedBy method takes the column where a queen is
      and checks in all four diagonal directions for additional queens
      to get a quick count of how many queens are attacking any individual
      queen in the column you chose.
   */
   private static int queenAttackedBy(int[] arr, int col)
   {
      int attacks = 0;
      
      for (int i = 0; i <= arr.length - col - 1; i++)
      {
         if ((arr[col + i] == arr[col] - i) && (i != 0))
         {
            attacks += 1;
         }
      }
      
      for (int i = 0; i <= col; i++)
      {
         if ((arr[col - i] == arr[col] + i) && (i != 0))
         {
            attacks += 1;
         }
      }
      
      for (int i = 0; i <= col; i++)
      {
         if ((arr[col - i] == arr[col] - i) && (i != 0))
         {
            attacks += 1;
         }
      }
      
      for (int i = 0; i <= arr.length - col - 1; i++)
      {
         if ((arr[col + i] == arr[col] + i) && (i != 0))
         {
            attacks += 1;
         }
      }
      
      return attacks;
   }
   
   /*
      checkSwap duplicates the main array and will get the new collision
      count for the swapped version of the array (the two new positions
      verse the old two positions). It returns how many attacks exist on
      the two queens after the swap.
   */
   private static int checkSwap(int [] arr, int col1, int col2)
   {
      int temp = 0, col1Attacks = 0, col2Attacks = 0;
      int[] tempArr = new int[arr.length];
      System.arraycopy(arr, 0, tempArr, 0, arr.length);
      
      swapCols(tempArr, col1, col2);
      col1Attacks = queenAttackedBy(tempArr, col1);
      col2Attacks = queenAttackedBy(tempArr, col2);
      
      return (col1Attacks + col2Attacks);
      
   }
   /*
      Along with a helper function findDiag, update Diags will update the
      diagonal arrays by incrementing / decrementing the 8 important diagonals 
      after each swap.
   */
   private static void updateDiags(int[] arr, int col1, int col2, int[] d1, int[] d2)
   {
      int[] tempArr = new int[arr.length];
      System.arraycopy(arr, 0, tempArr, 0, arr.length);

      // New diag had a queen moved to it so add one
      d1[findDiag1(arr, col1)] += 1;
      d2[findDiag2(arr, col1)] += 1;
      
      d2[findDiag2(arr, col2)] += 1;
      d1[findDiag1(arr, col2)] += 1;
      
      // Old diag had a queen removed so subtract one
      tempArr[col2] = arr[col1];
      tempArr[col1] = arr[col2];
      d1[findDiag1(tempArr, col1)] -= 1;
      d2[findDiag2(tempArr, col1)] -= 1;
      
      tempArr[col1] = arr[col1];
      tempArr[col2] = arr[col1];
      d2[findDiag2(tempArr, col2)] -= 1;
      d1[findDiag1(tempArr, col2)] -= 1;
   }
   
   /*
      the findDiag functions utilize the properties of the array to figure
      out which diagonal a certain element in the array belongs to. It figures
      this out by taking the column where the queen is and following it to the 
      edge by incrementing or decrementing the column and row until one is zero
      or arr.length - 1. At this point, it is easy to figure out which diagonal
      each column queen is in.
   */
   private static int findDiag1(int[] arr, int col)
   {
      int temp = arr[col];
      int diagindex = 0;

      while (col != 0 && temp != 0)
      {
         col--;
         temp--;
      }
      
      if (col == 0)
      {
         diagindex = arr.length - temp - 1;
         return diagindex;
      }
      else
      {
        diagindex = arr.length - 1 + col;
        return diagindex;  
      }
   }
   
   private static int findDiag2(int[] arr, int col)
   {
      int temp = arr[col];
      int diagindex = 0;

      while (col != 0 && temp != arr.length - 1)
      {
         col--;
         temp++;
      }
      
      if (col == 0)
      {
         diagindex = temp;
         return diagindex;
      }
      else
      {
        diagindex = arr.length - 1 + col;
        return diagindex;  
      }
   }
}
 