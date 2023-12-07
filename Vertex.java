import java.util.*;

/**
 * The Vertex class represents a vertex in a graph, storing information about its name and neighboring vertices.
 */
class Vertex {
    private String name; // The name of the vertex
    private Map<Vertex, Integer> neighbors; // Map of neighboring vertices and edge weights

    /**
     * Constructs a new Vertex with the specified name.
     * @param name The name of the vertex.
     */
    public Vertex(String name) {
        this.name = name;
        this.neighbors = new HashMap<>();
    }

    /**
     * Gets the name of the vertex.
     * @return The name of the vertex.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the map of neighboring vertices and their corresponding edge weights.
     * @return The map of neighboring vertices and edge weights.
     */
    public Map<Vertex, Integer> getNeighbors() {
        return neighbors;
    }

    /**
     * Adds a neighboring vertex with the specified edge weight.
     * @param neighbor The neighboring vertex to be added.
     * @param weight   The weight of the edge connecting this vertex to the neighbor.
     */
    public void addNeighbor(Vertex neighbor, int weight) {
        neighbors.put(neighbor, weight);
    }

}