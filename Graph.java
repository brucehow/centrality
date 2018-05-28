import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * A graph class implemented through the use of an adjancency list.
 * @author Bruce How (22242664) & Haolin Wu (21706137)
 */
public class Graph {

	private HashMap<Integer, ArrayList<Integer>> adjList;
	private ArrayList<ArrayList<Integer>> components;

	/**
	 * Constructor for the graph class to generate the graph from an edge list.
	 * @param path The file path to the edge list
	 * @throws IOException if the file path does not exist
	 */
	public Graph(String path) throws IOException {
		adjList = new HashMap<>();
		components = new ArrayList<ArrayList<Integer>>();
		generateGraph(path);
		generateComponents();
	}

	/**
	 * Check for all the connected nodes to a specified node
	 * @param u The node to check
	 * @return An ArrayList containing the list of connected nodes to the specified node
	 */
	public ArrayList<Integer> getConnectedNodes(int u) {
		return adjList.get(u);
	}

	/**
	 * Fetch a list of all nodes in the graph
	 * @return An Integer array of all the nodes in a graph
	 */
	public Set<Integer> getNodes() {
		Set<Integer> nodes = adjList.keySet();
		return nodes;
	}

	/**
	 * Check if two nodes are connected to each other
	 * @param u The first node
	 * @param v The second node
	 * @return True if both nodes are connected, false otherwise
	 */
	public boolean isConnected(int u, int v) {
		if (adjList.get(u).size() < adjList.get(v).size()) {
			return contains(adjList.get(u), v);
		}
		return contains(adjList.get(v), u);
	}

	/**
	 * Generate a string representation of the adjacency list.
	 * @return A string represenation of the adjacency list.
	 */
	public String toString() {
		String s = "";
		for (int key : adjList.keySet()) {
			s += key + ", " + adjList.get(key).toString() + "\n";
		}
		return s;
	}
	
	/**
	 * 
	 */
	public ArrayList<ArrayList<Integer>> getComponents() {
		return components;
	}

	/**
	 * Runs a BFS on the graph to fetch each componenet of the graph
	 * @return An ArrayList containing an ArrayList of nodes in a particular component
	 */
	private void generateComponents() {
		Queue<Integer> queue = new LinkedList<>();
		ArrayList<Integer> visited = new ArrayList<>();
		ArrayList<Integer> componentSet = new ArrayList<>();
		Iterator<Integer> it = getNodes().iterator();
		int current = it.next();
		visited.add(current);
		componentSet.add(current);

		while (visited.size() != getNodes().size()) {
			for (Integer node : getConnectedNodes(current)) {
				if (!visited.contains(node)) {
					visited.add(node);
					queue.offer(node);
					componentSet.add(node);
				}
			}
			if (queue.isEmpty()) {
				components.add(componentSet);
				componentSet = new ArrayList<>();
				int nextCurrent = current;
				while (nextCurrent == current) {
					int next = it.next();
					if (!visited.contains(next)) {
						current = next;
						visited.add(current);
						componentSet.add(current);
					}
				}
			} else if (visited.size() == getNodes().size()) {
				components.add(componentSet);
			} else {
				current = queue.poll();
			}
		}
	}

	/**
	 * Helper method to read the content of the edge file
	 * @param path The file path to the edge list
	 * @throws IOException if the file path does not exist
	 */
	private void generateGraph(String path) throws IOException {
		FileReader reader = new FileReader(new File(path));
		BufferedReader buffer = new BufferedReader(reader);
		String row;
		while ((row = buffer.readLine()) != null) {
			String[] node = row.split(" ");
			int u = Integer.parseInt(node[0]);
			int v = Integer.parseInt(node[1]);
			addConnection(u, v);
			addConnection(v, u); // added twice as edges are mutual
		}
		reader.close();

	}

	/**
	 * Helper method to add an edge to the graph given two nodes
	 * @param u The first node
	 * @param v The second node
	 */
	private void addConnection(int u, int v) {
		ArrayList<Integer> connected;
		if (!adjList.containsKey(u)) {
			connected = new ArrayList<>();
			connected.add(v);
			adjList.put(u, connected);
		} else {
			if (!contains(adjList.get(u), v)) {
				connected = adjList.get(u);
				connected.add(v);
				Collections.sort(connected);
				adjList.put(u, connected);
			}
		}
	}

	/**
	 * Helper method which performs a binary search for an item in a given ArrayList.
	 * Used to quickly check if a node exists in an ArrayList.
	 * @param list The ArrayList of connected nodes
	 * @param item The node to search for
	 * @return True if the node is in the ArrayList, false otherwise
	 */
	private boolean contains(ArrayList<Integer> list, int item) {
		int lower = 0;
		int upper = list.size() - 1;

		while (lower <= upper) {
			int middle = (lower + upper) / 2;
			if (list.get(middle) < item) {
				lower = middle + 1;
			} else if (list.get(middle) > item) {
				upper = middle - 1;
			} else if (list.get(middle) == item) {
				return true;
			}
		}
		return false;
	}
}