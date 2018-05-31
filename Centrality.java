import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
	public ArrayList<ArrayList<Integer>> degreeCentrality(Graph graph) {
		int componentSize = graph.getComponents().size();
		ArrayList<ArrayList<Integer>> degree = new ArrayList<>();
		for (int i = 0; i < componentSize; i++) {
			// stores the degree centrality value for each node
			ArrayList<Node> centralityValue = new ArrayList<>();

			for (int node : graph.getComponents().get(i)) {
				int incidentNodes = graph.getConnectedNodes(node).size();
				centralityValue.add(new Node(node, incidentNodes));
			}

			// sorts the nodes according to its degree centrality
			ArrayList<Integer> results = new ArrayList<>();
			Collections.sort(centralityValue, Collections.reverseOrder());
			Iterator<Node> it = centralityValue.iterator();

			while (it.hasNext() && results.size() < 5) {
				results.add((int) it.next().id);
			}
			degree.add(results);
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
	public ArrayList<ArrayList<Integer>> closenessCentrality(Graph graph) {
		int componentSize = graph.getComponents().size();
		ArrayList<ArrayList<Integer>> closeness = new ArrayList<>();
		for (int i = 0; i < componentSize; i++) {
			// stores the closeness centrality value for each node
			ArrayList<Node> centralityValue = new ArrayList<>();

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
				 * Caculate total SP distance from source to each node and adds
				 * it to the centralityValue ArrayList.
				 */
				int totalDistance = 0;
				for (Integer nodeDistance : distance.values()) {
					totalDistance += nodeDistance;
				}
				centralityValue.add(new Node(source, totalDistance));
			}

			// sorts the nodes according to its closeness centrality
			ArrayList<Integer> results = new ArrayList<>();
			// values represent far(j) thus reverse sort is not needed
			Collections.sort(centralityValue);
			Iterator<Node> it = centralityValue.iterator();

			while (it.hasNext() && results.size() < 5) {
				results.add((int) it.next().id);
			}
			closeness.add(results);
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
	public ArrayList<ArrayList<Integer>> betweennessCentrality(Graph graph) {
		int componentSize = graph.getComponents().size();
		ArrayList<ArrayList<Integer>> betweenness = new ArrayList<>();

		for (int i = 0; i < componentSize; i++) {
			ArrayList<Integer> component = graph.getComponents().get(i);

			// stores the betweenness Centrality value for each node
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
							// divided by 2 due to the graph's undirected nature
							centrality.put(current, dependency.get(current) / 2);
						} else {
							centrality.put(current, centrality.get(current)
									+ dependency.get(current) / 2);
						}
					}
				}
			}
			// stores the centrality value for each node as a Node object
			ArrayList<Node> centralityValue = new ArrayList<>();
			for (Integer node : centrality.keySet()) {
				centralityValue.add(new Node(node, centrality.get(node)));
			}
			// sorts the nodes according to its closeness centrality
			ArrayList<Integer> results = new ArrayList<>();
			Collections.sort(centralityValue, Collections.reverseOrder());
			Iterator<Node> it = centralityValue.iterator();
			while (it.hasNext() && results.size() < 5) {
				results.add((int) it.next().id);
			}
			betweenness.add(results);
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
	public ArrayList<ArrayList<Integer>> katzCentrality(Graph graph, double alpha) {
		int componentSize = graph.getComponents().size();
		ArrayList<ArrayList<Integer>> katz = new ArrayList<>();

		for (int i = 0; i < componentSize; i++) {
			ArrayList<Integer> component = graph.getComponents().get(i);

			// store the centrality value for each node 
			ArrayList<Node> centralityValue = new ArrayList<>();

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
				centralityValue.add(new Node(source, centrality));
			}
			// sorts the nodes according to its closeness centrality
			ArrayList<Integer> results = new ArrayList<>();
			Collections.sort(centralityValue, Collections.reverseOrder());
			Iterator<Node> it = centralityValue.iterator();
			while (it.hasNext() && results.size() < 5) {
				results.add((int) it.next().id);
			}
			katz.add(results);
		}
		return katz;
	}

	/**
	 * Private subclass which stores each Node and their respective centrality
	 * value. This class implements the Comparable interface to compare each
	 * Node and its respective centrality value
	 */
	private class Node implements Comparable<Node> {
		private int id;
		private double value;

		/**
		 * Constructor for the subclass to create a node object
		 * @param id The node ID
		 * @param value The centrality value of the node
		 */
		public Node(int id, double value) {
			this.id = id;
			this.value = value;
		}

		/**
		 * Compares this object with the specified object for ordering
		 */
		@Override
		public int compareTo(Node node) {
			return Double.compare(value, node.value);
		}
	}
}
