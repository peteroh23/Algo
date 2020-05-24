import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;



class Main {

	int n; // number of candidates
	int k; // number of recruiters

	// provided data structures (already filled in)
	ArrayList<ArrayList<Integer>> neighbors;
	int[] recruiterCapacities;
	int[] preliminaryAssignment;

	// provided data structures (you need to fill these in)
	boolean existsValidAssignment;
	int[] validAssignment;
	int[] bottleneckRecruiters;

	// reading the input
	void input()
	{
		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader(new InputStreamReader(System.in));

			String text = reader.readLine();
			String[] parts = text.split(" ");

			n = Integer.parseInt(parts[0]);
			k = Integer.parseInt(parts[1]);
			neighbors = new ArrayList<ArrayList<Integer>>(n+k);
			recruiterCapacities = new int[n+k];
			preliminaryAssignment = new int[n];

			for (int j = 0; j < n+k; j++) {
				neighbors.add(new ArrayList<Integer>());
			}
			for (int i = 0; i < n; i++) {
				text = reader.readLine();
				parts = text.split(" ");
				int numRecruiters = Integer.parseInt(parts[0]);
				for (int j = 0; j < numRecruiters; j++) {
					int recruiter = Integer.parseInt(parts[j+1]);
					neighbors.get(i).add(recruiter);
					neighbors.get(recruiter).add(i);
				}
			}

			text = reader.readLine();
			parts = text.split(" ");
			for (int j = 0; j < k; j++) {
				recruiterCapacities[n+j] = Integer.parseInt(parts[j]);
			}

			text = reader.readLine();
			parts = text.split(" ");
			for (int i = 0; i < n-1; i++) {
				preliminaryAssignment[i] = Integer.parseInt(parts[i]);
			}

			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// writing the output
	void output()
	{
		try
		{
			PrintWriter writer = new PrintWriter(System.out);

			if (existsValidAssignment) {
				writer.println("Yes");
				for (int i = 0; i < n-1; i++) {
					writer.print(validAssignment[i] + " ");
				}
				writer.println(validAssignment[n-1]);
			} else {
				writer.println("No");
				for (int j = 0; j < n+k-1; j++) {
					writer.print(bottleneckRecruiters[j] + " ");
				}
				writer.println(bottleneckRecruiters[n+k-1]);
			}

			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public Main()
	{
		input();

		// n is number of candidates
		// k is number of recruiters
		
		// true if there exists a way to assign every job candidate to a recruiter without increasing limits, false otherwise
		existsValidAssignment = false; 
		
		// filled out if existsValidAssignment = true, validAssignment[i] is the index of the recruiter assigned to cnadidate in in your full valid assignment
		// i = 0 ...  n-1
		// output = n... n+k-1
		validAssignment = new int[n];
		
		// filled out if existsValidAssignment = false, bottleneckRecruiters[i] set to 1 if increasing limit of recuiter i by 1 would allow us to make a full assignment possible
		// 0 otherwise
		// i = n...n+k - 1
		// output = 0 or 1
		bottleneckRecruiters = new int[n+k];

		// already filled in data structures I can use
		// neighbors, neighbors[i] is list of all people who can match with i, i = 0...n-1
		// recruiterCapacities, recruiterCapacities[i] is the limit of candidates that recruiter i can interview, i = n...n+k-1
		// preliminaryAssignment, preliminaryAssignment[i] is the recruiter assigned to interview ith candidated, i = 0...n-1
			// preliminaryAssignment[n-1] is blank
		
		// Code Process:
		// 1. create a hashmap that acts as a residualGraph
			// use a for loop to look through all the nodes to make sure the hashmap is updated
			// check to make sure none of the nodes have a null value
		// 2. BFS traversal
			// keep track of a queue for each node to know its neighbors
			// while Queue is not empty, keep going through all the 
		
		
		
		
		//YOUR CODE STARTS HERE
		
		HashMap<Integer, ArrayList<Integer>> residualGraph = new HashMap<Integer, ArrayList<Integer>>();
		
		for (int i = 0; i< n+k; i++) {
			if (residualGraph.get(i) == null) {
				residualGraph.put(i, new ArrayList<Integer>());
			}
		}
		
		
		for (int i = 0; i<n; i++) {
			
			ArrayList<Integer> currentNeighbor = neighbors.get(i);
			
			for (Integer c : currentNeighbor) {
				int starting;
				int ending;
				
				if (preliminaryAssignment[i] != c) {
					starting = i;
					ending = c;
					
				}
				
				else {
					starting = c;
					ending = i;	
				}
				
				if (residualGraph.containsKey(starting)){
					ArrayList<Integer> existingList = residualGraph.get(starting);
					existingList.add(ending);
					residualGraph.replace(starting, existingList);
				}
				
				else {
					ArrayList<Integer> newList = new ArrayList<Integer>();
					newList.add(ending);
					residualGraph.put(starting, newList);
				}
			}
			
		}
		
		int currentNode = n-1;
		
		// seen recruiters for bottleNeck
		ArrayList<Integer> visitedRecruiters = new ArrayList<Integer>();
		
		// keep track of visited nodes for BFS traversal
		boolean[] visited = new boolean[n+k];
				
		
		// Used for BFS traversal
		LinkedList<Integer> queue = new LinkedList<Integer>();
		
		// used to backtrack nodes
		int[] previousNodes = new int[n+k];
		
		// initialization
		queue.add(currentNode);
		visited[currentNode] = true;
		previousNodes[currentNode] = -1;
		
		
		// BFS traversal
		while(!queue.isEmpty()) {
			currentNode = queue.poll();
			visited[currentNode] = true;
			boolean firstCondition = currentNode >= n;
			boolean secondCondition = currentNode <= n+k-1;
			
			if (firstCondition && secondCondition && residualGraph.get(currentNode).size() >= recruiterCapacities[currentNode]) {
				visitedRecruiters.add(currentNode);
			}

			else if (firstCondition && secondCondition && residualGraph.get(currentNode).size() < recruiterCapacities[currentNode]) {
				existsValidAssignment = true;
				break;
			}

            // to go through all the nieghbors
			Iterator<Integer> iterationIntegers = residualGraph.get(currentNode).listIterator();
            while (iterationIntegers.hasNext())
            {
				int n = iterationIntegers.next();
                if (!visited[n])
                {
                    visited[n] = true;
					queue.add(n);
					previousNodes[n] = currentNode;
                }
			}
		}
		
		if (!existsValidAssignment) {
			for (Integer v: visitedRecruiters) {
				bottleneckRecruiters[v] = 1;
			}
		}
		
		else {
			int node = currentNode;
			while (node != n-1 && node != -1) {
				int u = previousNodes[node];
				preliminaryAssignment[u] = node;
				node = previousNodes[u];
			
			}
			validAssignment = preliminaryAssignment;
		}
	
		//YOUR CODE ENDS HERE

		output();
	}

    // Strings in Args are the name of the input file followed by
    // the name of the output file.
	public static void main(String [] Args) 
	{
		new Main();
	}

}
