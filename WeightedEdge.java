/**
 * The WeightedEdge class represents an edge in a weighted graph, storing information about its start and end vertices
 * along with the weight of the edge.
 */
class WeightedEdge {
    private Vertex startVertex; // The starting vertex of the edge
    private Vertex endVertex; // The ending vertex of the edge
    private int weight; // The weight of the edge

    /**
     * Constructs a new WeightedEdge with the specified start and end vertices and the given weight.
     * @param startVertex The starting vertex of the edge.
     * @param endVertex   The ending vertex of the edge.
     * @param weight      The weight of the edge.
     */
    public WeightedEdge(Vertex startVertex, Vertex endVertex, int weight) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.weight = weight;
    }

    /**
     * Gets the starting vertex of the edge.
     * @return The starting vertex of the edge.
     */
    public Vertex getStartVertex() {
        return startVertex;
    }

    /**
     * Gets the ending vertex of the edge.
     * @return The ending vertex of the edge.
     */
    public Vertex getEndVertex() {
        return endVertex;
    }

    /**
     * Gets the weight of the edge.
     * @return The weight of the edge.
     */
    public int getWeight() {
        return weight;
    }
}