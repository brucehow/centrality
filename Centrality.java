import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * A class containing several methods to compute the centrality of a given
 * graph. This includes degree centrality, closeness centrality, betweenness
 * centrality and Katz centrality. This class utilizes the graph class'
 * representation of a graph.
 * @author Bruce How (22242664) & Haolin Wu (21706137)
 */
public class Centrality {

	/**
	 * Returns an array of integer ArrayList, containing the nodes with the
	 * highest degree centrality, for each component of the graph. This is the
	 * node which has the highest number of incident edges.
	 * @param graph The graph to be checked
	 * @return An array of nodes with the highest degree centrality for each
	 *         component
	 */
	public ArrayList<Integer>[] degreeCentrality(Graph graph) {
		int componentSize = graph.getComponents().size();
		PriorityQueue<Integer> top = new PriorityQueue<>();
		ArrayList<Integer> degree[] = new ArrayList[componentSize];
		for (int i = 0; i < componentSize; i++) {
			int highest = -1;
			for (int node : graph.getComponents().get(i)) {
				int incidentEdges = graph.getConnectedNodes(node).size();
				if (incidentEdges > highest) {
					ArrayList<Integer> degreeNodes = new ArrayList<>();
					degreeNodes.add(node);
					degree[i] = degreeNodes;
					highest = incidentEdges;
				} else if (incidentEdges == highest && degree[i].size() < 5) {
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
	 * Performs a BFS on a given graph to fetch the nodes with the highest
	 * closeness centrality for each component of the graph. This is the node
	 * with the smallest average shortest paths to all other nodes in the graph.
	 * @param graph The graph to be checked
	 * @return An array of nodes with the highest closeness centrality for each
	 *         component
	 */
	public ArrayList<Integer>[] closenessCentrality(Graph graph) {
		int componentSize = graph.getComponents().size();
		ArrayList<Integer> closeness[] = new ArrayList[componentSize];
		for (int i = 0; i < componentSize; i++) {
			int shortest = Integer.MAX_VALUE;
			ArrayList<Integer> component = graph.getComponents().get(i);
			for (Integer source : component) {
				/*
				 * Runs a BFS algorithm on the graph to find the shortest
				 * distances to all other nodes from the current source node
				 */
				HashMap<Integer, Integer> distance = new HashMap<>();
				Queue<Integer> queue = new LinkedList<>();
				distance.put(source, 0);
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
				 * Caculate total distance for the SP of the source to each node
				 * and update the variables respectively. This is the far(j) as
				 * described in the project brief
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
				} else if (totalDistance == shortest && closeness[i].size() < 5) {
					ArrayList<Integer> results = closeness[i];
					results.add(source);
					closeness[i] = results;
				}
			}
		}
		return closeness;
	}

	/**
	 * Performs Brandes algorithm on a given graph to fetch the nodes with the
	 * highest bewtweenness centrality for each component of the graph. This is
	 * the node which passes through the most shortest paths in a graph
	 * @param graph The graph to be checked
	 * @return An array of nodes with the highest closeness centrality for each
	 *         component.
	 */
	public ArrayList<Integer>[] betweennessCentrality(Graph graph) {
		int componentSize = graph.getComponents().size();
		ArrayList<Integer>[] betweenness = new ArrayList[componentSize];

		for (int i = 0; i < componentSize; i++) {
			ArrayList<Integer> component = graph.getComponents().get(i);

			// stores the Betweenness Centrality value for each node
			HashMap<Integer, Double> centrality = new HashMap<>();

			for (Integer source : component) {
				/*
				 * Runs a BFS algorithm on the graph to find the shortest
				 * distances to all other nodes, preceding nodes that pass
				 * within all the SP, and number of SP from the source node
				 */
				Stack<Integer> stack = new Stack<>();
				Queue<Integer> queue = new LinkedList<>();
				HashMap<Integer, ArrayList<Integer>> precedingNode = new HashMap<>();
				HashMap<Integer, Integer> distance = new HashMap<>();
				HashMap<Integer, Integer> paths = new HashMap<>();

				queue.add(source);
				paths.put(source, 1);
				distance.put(source, 0);

				while (!queue.isEmpty()) {
					int current = queue.poll();

					// used for the dependency accumulation algorithm later
					stack.push(current);

					for (Integer node : graph.getConnectedNodes(current)) {

						// node is founded for the first time
						if (!distance.containsKey(node)) {
							queue.add(node);
							distance.put(node, distance.get(current) + 1);
							paths.put(node, paths.get(current));
							ArrayList<Integer> parent = new ArrayList<>();
							parent.add(current);
							precedingNode.put(node, parent);

							// found another SP for existing node
						} else if (distance.get(node) == distance.get(current) + 1) {
							paths.put(node, (paths.get(node) + paths.get(current)));
							ArrayList<Integer> precede = precedingNode.get(node);
							precede.add(current);
							precedingNode.put(node, precede);
						}
					}
				}
				/*
				 * Runs Brandes' dependency accumulation algorithm to compute
				 * the betweenness centrality for each node.
				 */
				HashMap<Integer, Double> dependency = new HashMap<>();
				for (Integer node : component) {
					dependency.put(node, 0.0);
				}
				while (!stack.isEmpty()) {
					int current = stack.pop();
					if (current != source) {
						for (Integer node : precedingNode.get(current)) {
							double result = ((double) paths.get(node)
									/ paths.get(current)) * (1 + dependency.get(current));
							dependency.put(node, dependency.get(node) + result);
						}
						if (!centrality.containsKey(current)) {
							centrality.put(current, dependency.get(current) / 2);
						} else {
							centrality.put(current, centrality.get(current)
									+ dependency.get(current) / 2);
						}
					}
				}
			}
			/*
			 * Identify the node(s) with the highest centrality value and update
			 * the betweenness ArrayList array appropiately.
			 */
			double minDependency = -1;
			for (Integer node : centrality.keySet()) {
				if (centrality.get(node) > minDependency) {
					minDependency = centrality.get(node);
					ArrayList<Integer> results = new ArrayList<>();
					results.add(node);
					betweenness[i] = results;
				} else if (centrality.get(node) == minDependency
						&& betweenness[i].size() < 5) {
					ArrayList<Integer> results = betweenness[i];
					results.add(node);
					betweenness[i] = results;
				}
			}
		}
		return betweenness;
	}

	/**
	 * Performs a BFS on a given graph to fetch the nodes with the highest Katz
	 * centrality for each component of the graph. This is the node which has
	 * the highest overall number of edges, that are close by.
	 * @param graph The graph to be checked
	 * @return An array of nodes with the highest Katz centrality for each
	 *         component
	 */
	public ArrayList<Integer>[] katzCentralitry(Graph graph, double alpha) {
		int componentSize = graph.getComponents().size();
		ArrayList<Integer>[] katz = new ArrayList[componentSize];

		for (int i = 0; i < componentSize; i++) {
			double highest = 0;
			ArrayList<Integer> component = graph.getComponents().get(i);

			for (Integer source : component) {
				/*
				 * Runs a BFS algorithm on the graph to identify the node level
				 * of each node form the source and compute the Katz centrality
				 * value for the source
				 */
				HashMap<Integer, Integer> level = new HashMap<>();
				Queue<Integer> queue = new LinkedList<>();
				level.put(source, 0);

				// stores the Katz centrality value of the source node
				double centrality = 0;
				int current = source;

				while (level.size() != component.size()) {
					for (Integer node : graph.getConnectedNodes(current)) {
						if (!level.containsKey(node)) {
							int newLevel = level.get(current) + 1;
							centrality += Math.pow(alpha, newLevel);
							level.put(node, newLevel);
							queue.add(node);
						}
					}
					if (!queue.isEmpty()) {
						current = queue.poll();
					}
				}
				/*
				 * Identify whether the current source has the highest Katz
				 * centrality value and update the katz ArrayList array
				 * appropiately.
				 */
				if (centrality > highest) {
					ArrayList<Integer> results = new ArrayList<>();
					results.add(source);
					highest = centrality;
					katz[i] = results;
				} else if (centrality == highest && katz[i].size() < 5) {
					ArrayList<Integer> results = katz[i];
					results.add(source);
					katz[i] = results;
				}
			}
		}
		return katz;
	}
}
