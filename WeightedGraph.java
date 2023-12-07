import java.util.*;

/**
 * The WeightedGraph class represents a weighted graph, allowing the addition of vertices and edges,
 * as well as finding the shortest path between two vertices using Dijkstra's algorithm.
 */
class WeightedGraph {
    private Map<String, Vertex> vertices; // Map of vertices in the graph

    /**
     * Constructs a new WeightedGraph with an empty set of vertices.
     */
    public WeightedGraph() {
        this.vertices = new HashMap<>();
    }

     /**
     * Adds a new vertex to the graph with the specified name.
     * @param name The name of the vertex to be added.
     */
    public void addVertex(String name) {
        vertices.put(name, new Vertex(name));
    }

    /**
     * Adds a weighted edge between two vertices in the graph.
     * @param vertex1 The name of the first vertex.
     * @param vertex2 The name of the second vertex.
     * @param weight  The weight of the edge between the vertices.
     */
    public void addEdge(String vertex1, String vertex2, int weight) {
        // Retrieve the Vertex objects associated with the specified names.
        Vertex v1 = vertices.get(vertex1);
        Vertex v2 = vertices.get(vertex2);
        
        // Check if both vertices are present in the graph.
        if (v1 != null && v2 != null) {
            // Add a neighbor relationship with the specified weight for both vertices.
            v1.addNeighbor(v2, weight);
            v2.addNeighbor(v1, weight);
        }
    }

    /**
     * Gets a vertex from the graph based on its name.
     * @param name The name of the vertex to retrieve.
     * @return The vertex with the specified name, or null if not found.
     */
    public Vertex getVertex(String name) {
        return vertices.get(name);
    }

    /**
     * Prints the graph, displaying each vertex and its neighboring vertices along with edge weights.
     */
    public void printGraph() {
        // Iterate through each vertex in the graph.
        for (Vertex vertex : vertices.values()) {
            // Print the name of the current vertex.
            System.out.print(vertex.getName() + " -> ");
            // Iterate through the neighboring vertices and their corresponding edge weights.
            for (Map.Entry<Vertex, Integer> entry : vertex.getNeighbors().entrySet()) {
                Vertex neighbor = entry.getKey();
                int weight = entry.getValue();
                // Print the neighboring vertex and its edge weight.
                System.out.print("(" + neighbor.getName() + ", " + weight + ") ");
            }
            // Move to the next line for the next vertex.
            System.out.println();
        }
    }

    /**
     * Finds the shortest path between two vertices using Dijkstra's algorithm.
     * @param startCountry The name of the starting country.
     * @param endCountry   The name of the ending country.
     * @return A list of weighted edges representing the shortest path, or an empty list if no path is found.
     */
    public List<WeightedEdge> findShortestPath(String startCountry, String endCountry) {
        // Maps to store cost, known vertices, and path information during the algorithm.
        Map<Vertex, Integer> cost = new HashMap<>();
        Map<Vertex, Boolean> known = new HashMap<>();
        Map<Vertex, Vertex> path = new HashMap<>();
        // Priority queue to efficiently retrieve vertices with minimum cost.
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(vertex -> cost.get(vertex)));
        // Retrieve the Vertex objects associated with the specified names.
        Vertex startVertex = getVertex(startCountry);
        Vertex endVertex = getVertex(endCountry);
        
        if (startVertex == null || endVertex == null) {
            return Collections.emptyList(); // Return empty list if start or end country not found
        }

        // Initialize distances and add the start vertex to the priority queue.
        for (Vertex vertex : vertices.values()) {
            cost.put(vertex, Integer.MAX_VALUE);
            known.put(vertex, false);
            path.put(vertex, null);
        }
        cost.put(startVertex, 0);
        priorityQueue.add(startVertex);

        // Dijkstra's algorithm
        while (!priorityQueue.isEmpty()) {
            Vertex current = priorityQueue.poll();
            known.put(current, true);
            // Update costs and paths for neighboring vertices.
            for (Map.Entry<Vertex, Integer> entry : current.getNeighbors().entrySet()) {
                Vertex neighbor = entry.getKey();
                int weight = entry.getValue();

                if (!known.get(neighbor)) {
                    int newCost = cost.get(current) + weight;

                    if (newCost < cost.get(neighbor)) {
                        cost.put(neighbor, newCost);
                        path.put(neighbor, current);
                        priorityQueue.add(neighbor);
                    }
                }
            }
        }

        // Construct the path.
        List<WeightedEdge> result = new ArrayList<>();
        Vertex current = endVertex;
        // Traverse the path backward to construct the result list.
        while (current != null && path.get(current) != null) {
            Vertex previousVertex = path.get(current);
            int weight = current.getNeighbors().get(previousVertex);
            result.add(new WeightedEdge(previousVertex, current, weight));
            current = previousVertex;
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * Checks if the graph contains a vertex with the specified name.
     * @param vertex The name of the vertex to check for.
     * @return True if the graph contains the specified vertex, false otherwise.
     */
	public boolean containsVertex(String vertex) {
        return vertices.containsKey(vertex);
	}

}