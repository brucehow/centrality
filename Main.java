import java.io.FileNotFoundException;
import java.io.IOException;

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
		if (args.length == 2) {
			alpha = Double.parseDouble(args[1]);
		} else {
			alpha = 0.5;
		}
		filepath = args[0];
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
