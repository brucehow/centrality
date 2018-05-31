import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The main class which utilizes the Graph and Centrality class to compute the
 * degree, closeness, betweenness and katz centrality for a given graph. This
 * class requires an edge file argument and takes an optional katz alpha value
 * argument.
 * @author Bruce How (22242664) & Haolin Wu (21706137)
 */
public class Main {

	private static String filepath;
	private static Graph graph;
	private static Centrality centrality;
	private static double alpha;

	public static void main(String[] args) throws IOException {
		if (args.length != 1 && args.length != 2) {
			System.err.println("Invalid Arguments\n\tUsage: java Main <filepath>");
			System.err.println(
					"\nCustom Katz alpha value\n\tUsage: java Main <filepath> <alpha>");
			return;
		}
		// appropiately assign the second argument to the katz variable
		if (args.length == 2) {
			alpha = Double.parseDouble(args[1]);
		} else {
			alpha = 0.5;
		}
		filepath = args[0];

		// tries to create a Graph object with the given file path
		try {
			graph = new Graph(filepath);
		} catch (FileNotFoundException e) {
			System.err.println("File not found\nPlease check the provided file path");
			return;
		}

		centrality = new Centrality();
		System.out.println(filepath);

		for (int i = 0; i < graph.getComponents().size(); i++) {
			System.out.println("==================");
			System.out.println("Component #" + (i + 1));
			System.out.println("Degree centers:");
			for (Integer node : centrality.degreeCentrality(graph).get(i)) {
				System.out.println("\t" + node);
			}
			System.out.println("Closeness centers:");
			for (Integer node : centrality.closenessCentrality(graph).get(i)) {
				System.out.println("\t" + node);
			}
			System.out.println("Betweenness centers:");
			for (Integer node : centrality.betweennessCentrality(graph).get(i)) {
				System.out.println("\t" + node);
			}
			System.out.println("Katz centers (a = " + alpha + "):");
			for (Integer node : centrality.katzCentrality(graph, alpha).get(i)) {
				System.out.println("\t" + node);
			}
		}
	}
}
