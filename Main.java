import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		/////////////////////////////////////////////////////////////////////////////
		// File Predefined Directories - Change this to your own project directory //
		/////////////////////////////////////////////////////////////////////////////
		String dir = "/Users/bruce/Documents/workspace/Project2018/src/";

		System.out.print("Enter edge file name: ");
		String filepath = dir + sc.next() + ".edges";
		Graph g;
		try {
			g = new Graph(filepath);
		} catch (FileNotFoundException e) {
			System.err
					.println("Invalid file. Please check file name and predefined dir.");
			return;
		}
		Centrality c = new Centrality();
		int count = 1;
		/** Get Node Sizes */
		// System.out.println("Node count : " + g.getNodes().size());

		/** Test Graph.getConnectedNodes() */
		// System.out.println(g.getConnectedNodes(759251) + "\nCount: " +
		// g.getConnectedNodes(759251).size());

		/** Test Graph.toString() */
		// System.out.println(g.toString());

		/** Test Graph.getComponents() */
		/*
		 * Iterator<Integer> it = g.getNodes().iterator(); for (ArrayList<Integer> comp :
		 * g.getComponents()) { System.out.println(comp); int count = 0; for (Integer x :
		 * comp) { count++; } System.out.println("Count : " + count); }
		 */
		for (int i = 0; i < g.getComponents().size(); i++) {
			System.out.println("\nComponent " + (i+1) + "\n------------------------");
			/////////////////////////////////////////////////////////////////////////////
			/////////////////////////// Degree Centrality Test //////////////////////////
			/////////////////////////////////////////////////////////////////////////////
			count = 1;
			System.out.println("Degree Centrality:");
			for (Integer node : c.degreeCentrality(g)[i]) {
				System.out.println(node);
			}

			/////////////////////////////////////////////////////////////////////////////
			////////////////////////// Closeness Centrality Test ////////////////////////
			/////////////////////////////////////////////////////////////////////////////
			count = 1;
			System.out.println("Closness Centrality:");
			for (Integer node : c.closenessCentrality(g)[i]) {
				System.out.println(node);
			}

			/////////////////////////////////////////////////////////////////////////////
			//////////////////////// Betweenness Centrality Test ////////////////////////
			/////////////////////////////////////////////////////////////////////////////
			count = 1;
			System.out.println("Betweeness Centrality:");
			for (Integer node : c.betweennessCentrality(g)[i]) {
				System.out.println(node);
			}
		}
	}
}
