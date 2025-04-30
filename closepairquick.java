/*
Connor Carene
G Turini
Assignment 2 - Closest Pair Divide & Conquer
*/
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

class closestPairQuickSolve
{
// Main method that calls all other methods to solve the 2D closest pair
// Problem recursively.
public static void main(String[] args)
{
   // pointCount will determine how many random pairs of points are generated
   // To decide how many pairs you want, change this variable. Must be > 2
   int pointCount = 1000;
   Point2D tempPoint = new Point2D(0,0); // make default constructor
   Point2D[] coordinatePairs = new Point2D[pointCount];
   Point2DPair solution = new Point2DPair(tempPoint, tempPoint);
   long startTime = System.nanoTime();
      
   // Call to generate point set
   coordinatePairs = Point2D.generatePairs(pointCount);
   
   // Shallow copy of point array to P and Q
   Point2D[] P = copyPointArr(coordinatePairs);
   Point2D[] Q = copyPointArr(coordinatePairs);
   
   System.out.print("Unsorted Point Array:");
   printPoints(coordinatePairs);
   
   // Total brute force solve and solution of the 2D closest pair problem
   // Which is used to ensure correctness. 
   //To improve time complexity, comment this block out.
   solution = bruteForceSolve(coordinatePairs);
   System.out.print("Brute Force Method");
   solution.printPair();
   solution.printPairDist();
   
   mergeSort(P, 0, coordinatePairs.length - 1, "x");       
   mergeSort(Q, 0, coordinatePairs.length - 1, "y");
    
   solution = EfficientClosestPair(P, Q);      
   System.out.print("\nEfficient Method");
   solution.printPair();
   solution.printPairDist();
   long endTime = System.nanoTime();
   long duration = endTime - startTime;
   double seconds = (double) duration / 1_000_000_000.0;
   System.out.println("\nRuntime: " + seconds + " seconds");
}

public static class Point2D 
{ // Data members
	public int x;
	public int y; // Should be read only but it would be inconvinient so I won't
	public boolean flag; // Not Read-Only

   // Constructor for 2D Point
	public Point2D( int x, int y) 
   { 
      this.x = x; this.y = y; 
   }
   
   // Point2D Class Method
   // Overriding hash method for our scenario
   @Override 
   public int hashCode()
   {
      return Objects.hash(x, y);
   }
   
   // Point2D Class Method
   // Overriding equals method for our scenario.
   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      } // Checks if object is null or of an incompatible class
      if (obj == null || getClass() != obj.getClass())
      {
         return false;
      }
      
      Point2D other = (Point2D) obj;
      return x == other.x && y == other.y;
   }
   
   // Point2D Class Method
   // Method to generate the random pairs utilizing HashSet and Random
   // To ensure all pairs are unique and within a number range
   // Generalized to linear time assuming there is little to no collision
   public static Point2D[] generatePairs(int pairCount)
   {
   // min and max determine the highest and lowest allowed values in points
      int min = -10000;
      int max = 10000;
   
      Random random = new Random();
      Set<Point2D> uniqueValues = new HashSet<>();
      Point2D[] randomPoints = new Point2D[pairCount]; 
      
      while (uniqueValues.size() < pairCount)
      {
         int x = random.nextInt(max - min + 1) + min;
         int y = random.nextInt(max - min + 1) + min;
         Point2D point = new Point2D(x, y);
         
         if (uniqueValues.add(point))
         {
            randomPoints[uniqueValues.size() - 1] = point;
         }
      }
      return randomPoints;
   }
} 
  
// Method to copy an array of Point2D objects.
// Linear Time Process
public static Point2D[] copyPointArr(Point2D[] orig)
{
   Point2D[] copy = new Point2D[orig.length];
   for (int i = 0; i < orig.length; i++)
   {
      copy[i] = orig[i];
   }
   return copy;
}

// New class used to hold a pair of Point2D Objects
public static class Point2DPair
{
   // Data members
	public Point2D pointA;
	public Point2D pointB;

   // Constructor
	public Point2DPair(Point2D a, Point2D b) 
   {
       this.pointA = a;
       this.pointB = b; 
   }  // Unsafe, but doesn't really matter here
   
   // Point2DPair Class Method
   // Method to reassign the points within the Point2DPair Object
   // Constant Time Process
   public void assignPoints(Point2D a, Point2D b)
   {
      pointA = a;
      pointB = b;
   }
   
   // Point2DPair Class Method
   // Print Method for Pair with pretty formatting
   // Constant Time Process
   public void printPair()
   {
       System.out.print("\nClosest Pairs: (" + pointA.x + ", " + pointA.y + ") and (" + pointB.x + ", " + pointB.y + ")");
   }
   
   // Point2DPair Class Method
   // Print Method for the Distance between the 2 pairs in the point variables
   // Constant Time Process
   public void printPairDist()
   {
       System.out.print("\nClosest Pair Distance: " + distance(pointA, pointB) + "\n");
   }
}

// Function to calculate the Euclidean Distance between the 2 points
// Constant Time Process
public static double distance(Point2D p1, Point2D p2)
{
   return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y ) * (p1.y - p2.y ));
}

// Algorithm to brute force solve the closest pair. This is used in 
// Verification and to complete calculations when a base case is 
// Reached in our recursive solution. Compares each point pair's
// Euclidean Distance and returns the pair with smallest distance
// Technically n^2 class of efficiency, but since we have a 
// Restricted input set of 3 points or less this is not relevant
// When this is used in the recursive solution.
public static Point2DPair bruteForceSolve(Point2D[] points)
{
   // This min number MUST be greater than any possible number in the points
   double min = 2147483647; // Inf
   double currMin = 0;
   Point2DPair temp = new Point2DPair(points[0], points[1]);
   
   for (int i = 0; i < points.length; i++)
      for (int j = i + 1; j < points.length; j++)
         {
            currMin = distance(points[i], points[j]);
            if (currMin < min)
            {
               min = currMin;
               temp.assignPoints(points[i], points[j]);
            }
         }
   return temp;
}

// Method to print the points in a Point2D object array.
// Linear Time Process
public static void printPoints(Point2D[] values)
{
   for (int i = 0; i < values.length; i++)
   {
      System.out.print("\n(" + values[i].x + ", " + values[i].y + ")");
   }
   System.out.print("\n\n");
}

/*  
Main recursive algorithm to solve problem. Takes two arrays of the same set of
points organized by x and y respectively. Algorithm has a base case of the size
of the passed in array being equal to 3 or less elements. From this point, we
will use our brute force method to compute the closest pairs in "near constant
time" due to the trivial size of the input. The recursive nature of this method
splits the array into left and right subarrays until the base is reached while
maintaining the organization of the data. Returns Point2DPair object of the two
pairs that have the least distance between them.
Linearithmic Time Process.
*/
public static Point2DPair EfficientClosestPair(Point2D[] P, Point2D[] Q)
{
   // Base case:
	if (P.length < 4)
   {
      Point2DPair Min1 = bruteForceSolve(P);
      return Min1;
   }
   // Recursive Case:
	else
	{
      // Splitting arrays into half initializations
      // P is sorted by x Coord and Q is sorted by y Coord
		int leftSize = ((P.length - 1) / 2)  + 1; // ceil formula
		int rightSize = P.length - leftSize; // floor formula
		Point2D[] PLeft = new Point2D[leftSize];
		Point2D[] PRight = new Point2D[rightSize];
		Point2D[] QLeft = new Point2D[leftSize];
		Point2D[] QRight = new Point2D[rightSize];
      
		// Initialize PLeft and PRight
      for (int i = 0; i < leftSize; i++)
      {
         PLeft[i] = P[i]; 
      }
      for (int i = 0; i < rightSize; i++)
      {
         PRight[i] = P[i + leftSize];
      }
      
   	int iPLeft = 0;
   	int iPRight = 0;
   
      // Assigning points to the P array and marking them if they are in the
      // Left side for the Q array to know which points go in which array
      // Since they share a reference to the same point array.
   	for (int iP = 0; iP < P.length; iP++)
   	{
   		if (iP < leftSize)
   		{
   			PLeft[iPLeft] = P[iP];
            PLeft[iPLeft].flag = true;
   			iPLeft++;
     		}
   		else
   		{
            PRight[iPRight] = P[iP];
            PRight[iPRight].flag = false;
            iPRight++;
   		}
   	}
        
      // Initialize Qleft and QRight
      for (int i = 0; i < leftSize; i++)
      {
         QLeft[i] = Q[i]; 
      }
      for (int i = 0; i < rightSize; i++)
      {
         QRight[i] = Q[i + leftSize];
      }
      
      int iQLeft = 0;
   	int iQRight = 0;
   
      // Using the flags we set when assigning P points, we assign the points
      // To the Q array.
   	for (int iQ = 0; iQ < Q.length; iQ++)
   	{
   		if (Q[iQ].flag)
   		{
   			QLeft[iQLeft] = Q[iQ];
   			iQLeft++;
   		}
   		else
   		{
            QRight[iQRight] = Q[iQ];
            iQRight++;
   		}
   	}

      // Recursive calls (divide and conquer by 2)
		Point2DPair resultLeft = EfficientClosestPair(PLeft, QLeft);
		Point2DPair resultRight = EfficientClosestPair(PRight, QRight);
      Point2DPair result = resultLeft; // temp assignment to create object

      // Checks if the found result for a given left or right side of the 
      // Current array is the new min distance. If so, set it as result.
      if (distance(resultLeft.pointA, resultLeft.pointB) < distance(resultRight.pointA, resultRight.pointB))
      {
         result = resultLeft;
      }
      else
      {
         result = resultRight;
      }
      
      // Call to solve the strip for pairs that stradle between left and right
      result = SolveStrip(result, P, Q);
      
      // After recursion has unwrapped, last result shall be the solution.
      return result;
   }
}
  
/*
I decided to break up the section to solve the strip into a diffferent method 
becuase I thought it was a bit cleaner because the main solve function was
already very long and did a lot of things. It mirrors for the most part, the
strategy outlined in the original paper with slight modifications to return
pair of points instead of just the distance.
*/
public static Point2DPair SolveStrip(Point2DPair minPair, Point2D[] P, Point2D[] Q)
{    
      // Settings variables for distance (quicker way to write it than redoing
      // The calc each time), and the midpoint.
      double dist = distance(minPair.pointA, minPair.pointB);
      int midPoint = P[(int) (Math.ceil((P.length / 2)))].x; // Make a get x and get y function of the class Point2D?
      int num = 0;
      
      // Figuring out which number to assign for index of array S.
      for (int i = 0; i < Q.length; i++)
      {
         if (Math.abs(Q[i].x - midPoint) < dist)
         {
            num += 1;
         }
      }
      
      Point2D[] S = new Point2D[num];
      double dminsq = dist * dist;
      int j = 0, k = 0;
      
      // Filling S with points that pertain to the strip conditions.
      for (int i = 0; i < Q.length; i++)
      {
         if (Math.abs(Q[i].x - midPoint) < dist)
         {
            S[j] = Q[i];
            j++;
         }
      }
      
    // Compares points in S to determine if any pair has a smaller distance
    // Than the current min distance taking advantage of geometrical properties
    // If a strip smaller pair is found, minPair is reassigned to it.
      for (int i = 0; i <= num - 2; i++)
      {
         k = i + 1;
         while ((k <= num - 1) && ((S[k].y - S[i].y) * (S[k].y - S[i].y)) < dminsq)
         {
            if (((S[k].x - S[i].x) * (S[k].x - S[i].x) + (S[k].y - S[i].y) * (S[k].y - S[i].y)) < dminsq)
            {
               minPair.assignPoints(S[k], S[i]);
               dminsq = distance(minPair.pointA, minPair.pointB) * distance(minPair.pointA, minPair.pointB);
            }
            k += 1;
         }
      }
   return minPair;
}

// The rest of the file is just a standard merge sort implementation with the
// Slight modification that we have an additional variable to choose which 
// Coordinate we sort by, x or y.
// Class of efficiency Theta(nlog(n))
public static void mergeSort(Point2D[] points, int left, int right, String sortBy)
{
   if (left < right)
   {
      int mid = left + (right - left) / 2;
      
      mergeSort(points, left, mid, sortBy);
      mergeSort(points, mid + 1, right, sortBy);
      merge(points, left, mid, right, sortBy);
   }
}
public static void merge(Point2D[] points, int left, int mid, int right, String sortBy)
{
   int n1 = mid - left + 1;
   int n2 = right - mid;
   
   Point2D[] pointsLeft = new Point2D[n1];
   Point2D[] pointsRight = new Point2D[n2];
   
   for (int i = 0; i < n1; i++)
   {
      pointsLeft[i] = points[left + i];
   }
   for (int i = 0; i < n2; i++)
   {
      pointsRight[i] = points[mid + i + 1];
   }  
   
   int i = 0, j = 0, k = left;
   
   while (i < n1 && j < n2)
   {
      int comparisonResult;
      if (sortBy == "x")
      {
         comparisonResult = Integer.compare(pointsLeft[i].x, pointsRight[j].x);
      }
      else
      {
         comparisonResult = Integer.compare(pointsLeft[i].y, pointsRight[j].y);
      }
      
      if (comparisonResult <= 0)
      {
         points[k] = pointsLeft[i];
         i++;
      }
      else
      {
         points[k] = pointsRight[j];
         j++;
      }
      k++;
   }
   
   while (i < n1)
   {
      points[k] = pointsLeft[i];
      i++;
      k++;
   }
   
   while (j < n2)
   {
      points[k] = pointsRight[j];
      j++;
      k++;
   }
}
}
