import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class Main {

	public static void main(String[] args) throws IOException {
		String filepath = "/Users/bruce/Documents/workspace/Project2018/src/78813.edges";
		Graph g = new Graph(filepath);
		Centrality c = new Centrality();
		//System.out.println("Node count : " + g.getNodes().size());

		// Test connected Nodes
		//System.out.println(g.getConnectedNodes(759251) + "\nCount: " + g.getConnectedNodes(759251).size());
		
		// Test toString
		//System.out.println(g.toString());
	
		// Test Degree
		//System.out.println(Arrays.toString(c.degreeCentrality(g)));
		
		// Test components
		/*Iterator<Integer> it = g.getNodes().iterator();
		for (ArrayList<Integer> comp : g.getComponents()) {
			System.out.println(comp);
			int count = 0;
			for (Integer x : comp) {
				count++;
			}
			System.out.println("Count : " + count);
		}*/
		
		// Closeness Test
		//System.out.println(Arrays.toString(c.closenessCentrality(g)));
		
		//Betweenness Test
		System.out.println(Arrays.toString(c.betweennessCentrality(g)));
	}


}
