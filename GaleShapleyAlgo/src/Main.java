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
	
	public static class Person {
		
		// number of this woman or man
		int name;
		
		// names in order or preferences
		// index 0 tells you the male/female that is highest preferred
		// names[0] tells you the name of the highest preferred
		int[] names;
		
		// preferences in order of names
		// index 0 tells you m0 or w0
		// preference[0] tells you the preference of m0 or w0
		int[] preferences;
		
		int proposingValue;
		//whoever this is matched to
		Person spouse;
		
		//int numberOfProposals;
		
		public Person(int id, int[] listOne, int[] listTwo) {
			this.name = id;
			this.names = listOne;
			this.preferences = listTwo;
			this.spouse = null;
			this.proposingValue = 0;
			//this.numberOfProposals = 0;
			
		}
		
		public void propose(Person p, Stack<Person> stack) { 
			if (p.spouse == null) {
				this.spouse = p;
				p.spouse = this;
				this.proposingValue ++;
			}
			
			else {
				Person currentSpouse = p.spouse;
				if (p.preferences[this.name] <  p.preferences[currentSpouse.name]) {
					this.spouse = p;
					p.spouse = this;
					currentSpouse.spouse = null;
					stack.push(currentSpouse);
				}else {
					stack.push(this);
					this.proposingValue ++;
					//System.out.println("stuck in stack");
				}
			}
		}
		
		
		public void proposeModified(Person p, Stack<Person> stack) { // the first male that proposes should not be accepted by his first pick, but only the first time. 
			
			if (p.spouse == null && p.proposingValue == 0) {
				this.proposingValue ++;
				stack.push(this);
			}
			else if (p.spouse == null && p.proposingValue > 0) {
				this.spouse = p; 
				p.spouse = this;
				this.proposingValue ++;
			}
			else {
				Person currentSpouse = p.spouse;
				if (p.preferences[this.name] <  p.preferences[currentSpouse.name]) {
					this.spouse = p;
					p.spouse = this;
					currentSpouse.spouse = null;
					stack.push(currentSpouse);
				}else {
					stack.push(this);
					this.proposingValue ++;

				}
			}
		
		}
		
		public String toString () {
			String name = Integer.toString(this.name);
			String arr1 = Arrays.toString(this.names);
			String arr2 = Arrays.toString(this.preferences);
			return "name: " + name + "\n names: " + arr1 + "\n preferences: " + arr2;
			
		}
		
		
		
	}
	

	
	
	public static void setUp(BufferedReader reader, Person[] p, int n) throws IOException {
		for (int i=0;i<n; i++) {
			int temp[] = Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
			int temp2[] = new int[temp.length];
			for(int j =0;j<temp.length;j++) {
				temp2[temp[j]] = j; 
			}
			Person inputData = new Person(i,temp,temp2);	
			p[i] = inputData;
		}
	}
	
	

	public static void main(String[] args) throws IOException {
		// 1. Reading the data from stdin
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		int n = Integer.parseInt(reader.readLine());
		Person[] men = new Person[n];
		Person[] women = new Person[n];
	
			
		
		setUp(reader,men, n)	;
		setUp(reader,women,n);
		reader.close();
		
		
		Stack<Person> freeMen = new Stack<Person>();
		for (int i = 0; i< men.length; i++) {
			freeMen.push(men[i]); 
		}

		//valid data for part b 
		Person[] men1 = men;
		Person[] women1 = women;
		
		

		// 2. running the standard algorithm
		
		while (true) {
			
			Person freeMan = freeMen.pop(); // unmatched man
			Person woman = women[freeMan.names[freeMan.proposingValue]]; // highest ranked woman f on m's list
			// M proposes to his highest ranked woman and update stack
			//System.out.println(freeMen.size());
			freeMan.propose(woman, freeMen);
			if (freeMen.size() == 0) {
				break;
			}
		}
		
		
		
		// 3. producing stdout 
		// first number is the number of the proposer that is matched with w0
		// second number is the 1 2 or 3 depending on what happens to the first women w on m0's list
		// 1. w may not get matched at all, 2. w may be part of an instability, 3. w matched to same partner or better partner 
		
		Person p1 = women[0].spouse;
		int r0 = p1.name;
		System.out.println(r0);
		
		
		
		
		// Part B
		
		
		
		
		
		
		Stack<Person> freeMenModified = new Stack<Person>();
		for (int i = 0; i< men1.length; i++) {
			freeMenModified.push(men1[i]); 
		
		}
		
		while (true) {
			Person m = freeMenModified.pop();
			Person woman = women[m.names[Math.min(m.proposingValue,women.length-1)]];
			m.proposeModified(woman, freeMenModified);
			if (m.proposingValue > men1.length) { // could be wrong conditional
				break;
			}
		}
		
		
		
		// second number is the 1 2 or 3 depending on what happens to the first women w on m0's list
		// 1. w may not get matched at all, 2. w may be part of an instability, 3. w matched to same partner or better partner 
				
		int r1;
		Person m0 = men1[0];
		Person mainWomen = women1[m0.names[0]];
		int[] a = m0.names;
		int[] b = mainWomen.names;
		// array c is a reversed
		int[] c = Arrays.copyOfRange(a, 1,a.length-1);
		int[] d = Arrays.copyOfRange(b, 0,b.length-2);
		
		if (mainWomen.spouse == null || Arrays.equals(c, d)) {
			r1 = 1;
		}
		else if (mainWomen.preferences[m0.name] > mainWomen.preferences[mainWomen.spouse.name]) {
			r1 = 3;
			
		}
		else {
			r1 = 2;
		}
		
		
		System.out.println(r1);
		
		
	

	}




}
