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



	public static void setUp(BufferedReader reader, int n, int[] d, int[] c) throws IOException{
		for (int i=0; i<n; i++) {
			int temp[] = Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
			d[i] = temp[0];
			c[i] = temp[1];
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		int temp[] = Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
		
		// number of possible gas stations
		int n = temp[0]; 
		// total miles on highway (max distance)
		int M = temp[1];
		// distance constraint between two consecutive gas stations
		int m = temp[2];
		
		//keeps track of GasStation objects, ordered by distance
		int[] distances = new int[n];
		int[] costs = new int[n];
					
		setUp(reader, n, distances, costs);
		
		reader.close();
		
		// stores opt[i] for 0...n-1
		ArrayList<Long> optimalValues = new ArrayList<Long>(n);
	
		Comparator<Integer> CostComparator = (Integer i1, Integer i2) -> (optimalValues.get(i1).compareTo(optimalValues.get(i2)));
		
		//stores indices of the processes opt i's
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(n, CostComparator);
		pq.add(0);
		
		//compute optimalValues
		for (int i=0; i<n; i++) {
			if (distances[i] <= m) {
				optimalValues.add((long)costs[i]);
			}
			else {
				while (distances[i] - distances[pq.peek()] > m) {
					pq.poll();
				}
				
				optimalValues.add((long)costs[i] + optimalValues.get(pq.peek()));
			}
			// add current index to priorityQueue
			pq.add(i);
		}
		
//		for(int i=0; i< optimalValues.length; i++) {
//			System.out.println(optimalValues[i]);	
//		}
		
		// determine stations that are have distances greater than M-m
		ArrayList<Integer> lastStations = new ArrayList<Integer>();
		for (int i = 0; i< n; i++) {
			if (M - distances[i] <= m) {
				lastStations.add(i);
			}
		}
		
		// find the station that has the lowest optimal value, update end
		Long end = optimalValues.get(lastStations.get(0));
		int endPosition = lastStations.get(0);
		for (int i = 1; i< lastStations.size(); i++) {
			Long next = optimalValues.get(lastStations.get(i));
			if (next < end) {
				end = next;
				endPosition = lastStations.get(i);
			}
		}
		System.out.println(end);
		
		ArrayList<Integer> output = new ArrayList<Integer>();
		
		while (end > 0) {
			output.add(distances[endPosition]);
			end = end - costs[endPosition];
			endPosition = optimalValues.indexOf(end);
		}
		Object[] output2 = output.toArray();
		Arrays.sort(output2);
		
		for (int i = 0; i<output2.length; i++) {
			if (i < output2.length-1) {
				System.out.print(output2[i] + " ");
			}
			if (i == output2.length-1) {
				System.out.println(output2[i]);
			}
		}
	}
}
