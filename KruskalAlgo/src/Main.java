import java.util.*;
import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.math.*;


class Main {
	
	static int sizeComponent;

	public static class Component implements Comparable<Component>{
		//parent node
		int parent;
		//size
		int rank;
		
		public Component(int parent, int rank) {

			this.parent = parent;
			this.rank = rank;
			
		}
		public String toString() {
			return Integer.toString(this.parent);
		}
		
		@Override
		public int compareTo(Component o) {
			return o.rank-this.rank;
		}
		
	}
	
	
	public static class Edge implements Comparable<Edge> {
		int src;
		int dst;
		int cost;
		
		public Edge(int first, int second, int c) {
			this.src = first;
			this.dst = second;
			this.cost = c;
		}
		
		@Override
		public int compareTo(Edge e) {
			if (e.cost == this.cost){
				return 0;
			}
			else if (e.cost < this.cost) {
				return 1;
			}
			else {
				return -1;
			}
		}
		
		public String toString() {
			return Integer.toString(this.cost);
			
		}
			
		}
	
	// finds the component of a node i
	public static int find(Component[] components, int i) {
		if (components[i].parent != i) {
			components[i].parent = find(components, components[i].parent);
		}
		return components[i].parent;
	}
	
	
	
		
	//store the next m lines of input into array
	public static void setUp(BufferedReader reader, int m, Edge[] array) throws IOException {
		for (int i=0; i<m; i++) {
			int temp[] = Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
			Edge edge1 = new Edge(temp[0],temp[1],temp[2]);
			array[i] = edge1;
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		// number of nodes
		int n = Integer.parseInt(reader.readLine());
		
		// number of edges
		int m = Integer.parseInt(reader.readLine());
		
		// Array to track the edge objects
		Edge[] edgeArray = new Edge[m];
		
		setUp(reader, m, edgeArray);
		
		reader.close();
		
		// sort edgeArray
		Arrays.sort(edgeArray);
		
//		for (int i=0;i<n;i++) {
//			System.out.println(edgeArray[i].toString());
//		}
		
		// CORRECT UP TO THIS POINT
		
		//components, initalize in the beginning with all single nodes
		Component[] componentName= new Component[n];
		
		for(int i=0; i<n;i++) {
			componentName[i] = new Component(i,1);
		}
		
		int i = 0;
		
		sizeComponent = n;

		// pick next edge, and work your way through until there are three components left
		while (sizeComponent > 3) {
			// next edge
			Edge nextEdge = edgeArray[i];
			//System.out.println("i: "+ Integer.toString(i));
			i++;
			
			int x = find(componentName, nextEdge.src);
			int y = find(componentName, nextEdge.dst);
			
			// checking that cycle doesn't exist
			if (x != y) {
				union(componentName, x, y);
			}
		}
		
		
		//System.out.println(sizeComponent);
		
//		for (int w =0; w<componentName.length; w++) {
//			System.out.println(componentName[w].toString());
//		}
		
		Arrays.sort(componentName); 
		for (int a=2;a>=0;a--) {
			System.out.println(componentName[a].rank);
		}
		
		
		
		// return three numbers, the sizes of the three component in increasing size
	
		
		
	}
	
	public static void union(Component[] components, int firstNode, int secondNode) {
		int xroot = find(components, firstNode); 
        int yroot = find(components, secondNode); 
  
		// Attach smaller rank tree under root of high rank tree 
        if (components[xroot].rank < components[yroot].rank) {
            components[xroot].parent = yroot; 
            components[yroot].rank = components[yroot].rank + components[xroot].rank;
            components[xroot].rank = 0;
            sizeComponent--;
        }
        else if (components[xroot].rank > components[yroot].rank) {
            components[yroot].parent = xroot; 
            components[xroot].rank = components[xroot].rank + components[yroot].rank;
            components[yroot].rank = 0;
            sizeComponent--;
        }
  
        // If ranks are same, then make one as root and increment the rank by one
        else
        { 
            components[yroot].parent = xroot; 
            components[xroot].rank = components[xroot].rank + components[yroot].rank;
            components[yroot].rank = 0;
            sizeComponent--;
        } 
	}
	


}
