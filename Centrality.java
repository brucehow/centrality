import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * <description>
 * @author Bruce How (22242664) & Haolin Wu (21706137)
 */
public class Centrality {

	/**
	 * Returns an array of integer ArrayList, containing the nodes with the highest
	 * degree centrality, for each component(s). This is the node which has the highest
	 * number of incident edges.
	 * @param graph The graph to be checked
	 * @return An array of nodes with the highest degree centrality for each component.
	 */
	public static ArrayList<Integer>[] degreeCentrality(Graph graph) {
		int componentSize = graph.getComponents().size();
		ArrayList<Integer> degree[] = new ArrayList[componentSize];
		for (int i = 0; i < componentSize; i++) {
			int highest = 0;
			for (int node : graph.getComponents().get(i)) {
				int incidentEdges = graph.getConnectedNodes(node).size();
				if (incidentEdges > highest) {
					ArrayList<Integer> degreeNodes = new ArrayList<>();
					degreeNodes.add(node);
					degree[i] = degreeNodes;
					highest = incidentEdges;
				} else if (incidentEdges == highest && !degree[i].contains(node)) {
					ArrayList<Integer> results = degree[i];
					results.add(node);
					degree[i] = results;
					highest = incidentEdges;
				}
			}
		}
		return degree;
	}

	/**
	 * Runs a BFS on a given graph to fetch the nodes with the highest closeness
	 * centrality for each component(s). This is the node with the smallest average
	 * shortest paths to all other nodes in the graph.
	 * @param graph The graph to be checked
	 * @return An array of nodes with the highest closeness centrality for each
	 *         component.
	 */
	public ArrayList<Integer>[] closenessCentrality(Graph graph) {
		int componentSize = graph.getComponents().size();
		ArrayList<Integer> closeness[] = new ArrayList[componentSize];
		for (int i = 0; i < componentSize; i++) {
			int shortest = Integer.MAX_VALUE;
			ArrayList<Integer> component = graph.getComponents().get(i);
			for (Integer source : component) {
				/*
				 * Runs a BFS algorithm on the graph to find the shortest distances to
				 * all other nodes from the current source node
				 */
				// Map each distance to using a HashMap
				HashMap<Integer, Integer> distance = new HashMap<>();
				Queue<Integer> queue = new LinkedList<>();
				distance.put(source, 0); // Set source distance to itself as 0
				int current = source;
				while (distance.size() != component.size()) {
					for (Integer node : graph.getConnectedNodes(current)) {
						if (!distance.containsKey(node)) {
							distance.put(node, distance.get(current) + 1);
							queue.add(node);
						}
					}
					if (!queue.isEmpty()) {
						current = queue.poll();
					}
				}

				/*
				 * Caculate total distance for the SP of the source to each node and
				 * update the variables respectively
				 */
				int totalDistance = 0;
				for (Integer nodeDistance : distance.values()) {
					totalDistance += nodeDistance;
				}
				if (totalDistance < shortest) {
					shortest = totalDistance;
					ArrayList<Integer> results = new ArrayList<>();
					results.add(source);
					closeness[i] = results;
				} else if (totalDistance == shortest) {
					ArrayList<Integer> results = closeness[i];
					results.add(source);
					closeness[i] = results;
				}
			}
		}
		return closeness;
	}

	/**
	 * Performs Brandes algorithm on a given graph to fetch the nodes with the highest
	 * bewtweenness centrality for each component(s). This is the node which passes
	 * through the most shortest paths in a graph
	 * @param graph The graph to be checked
	 * @return An array of nodes with the highest closeness centrality for each
	 *         component.
	 */
	public ArrayList<Integer>[] betweennessCentrality(Graph graph) {
		int componentSize = graph.getComponents().size();
		ArrayList<Integer>[] betweenness = new ArrayList[componentSize];

		return betweenness;
	}

	/**
	 * TODO desc of katzCentrality method
	 * @param graph The graph to be checked
	 * @return TODO return info
	 */
	public ArrayList<Integer>[] katzCentralitry(Graph graph, int alpha) {
		int componentSize = graph.getComponents().size();
		ArrayList<Integer>[] katz = new ArrayList[componentSize];

		return katz;
	}

}
