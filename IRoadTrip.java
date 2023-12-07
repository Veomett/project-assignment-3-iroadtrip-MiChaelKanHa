import java.io.*;
import java.util.*;

/**
 * The IRoadTrip class represents a program that builds a graph of countries and their borders,
 * calculates distances between countries, and finds the shortest path between two countries.
 * It accepts user input to find paths and distances between countries.
 */
public class IRoadTrip {

    /**
     * The graph representing countries and their borders.
     */
    private static WeightedGraph countryGraph;

    /**
     * Mapping for handling special country names.
     */
    private static HashMap <String, String> nameMatching = new HashMap<>();

    /**
     * Constructs an IRoadTrip object and initializes the country graph based on provided files.
     * @param args Array of three file names: borders.txt, capdist.csv, and state_name.tsv.
     */
    public IRoadTrip (String [] args) {
        // Check if exactly three file names are provided; otherwise, print an error message and exit.
    	if (args.length != 3) {
            System.out.println("Please provide three file names: borders.txt capdist.csv state_name.tsv!");
            System.exit(0);
        }
    	
        try {
            // Build the country graph using the specified files.
        	countryGraph = buildCountryGraph(args[0], args[1], args[2]);
        } catch (IOException e) {
            // If an IOException occurs during the process, throw a RuntimeException with the error message.
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Builds a weighted graph of countries based on input files containing borders and distances.
     * @param bordersFile   File containing information about country borders.
     * @param capDistFile   File containing information about distances between countries.
     * @param stateNameFile File containing information about country names and IDs.
     * @return The constructed weighted graph.
     * @throws IOException If an I/O error occurs while reading files.
     */
    private static WeightedGraph buildCountryGraph(String bordersFile, String capDistFile, String stateNameFile) throws IOException {
        WeightedGraph countryGraph = new WeightedGraph(); // Initialize an empty weighted graph for countries.

        // Step 1: Read borders.txt file.
        Map<String, List<String>> bordersData = readBordersFile(bordersFile);

        // Step 2: Read capdist.csv file.
        Map<String, Integer> capDistData = readCapDistFile(capDistFile);

        // Step 3: Read state_name.tsv file and keep track of the most recent entry for each country.
        Map<String, String> stateNameData = readStateNameFile(stateNameFile);
        
        // Create name matching for some country.
        createNameMatching();

        // Step 4: Combine data and build the graph.
        for (String countryName : bordersData.keySet()) {
            // Handle special cases for country names.
        	String standardName = specialCountryNameHandler(countryName);
            // Get the country ID from the stateNameData map.
            String countryId = stateNameData.get(standardName);

            // If the country is not already in the graph, add it as a vertex.
            if (countryGraph.getVertex(standardName) == null) {
               	countryGraph.addVertex(standardName);
            }
            
            // Get the neighbors of the current country from the bordersData map.
            List<String> neighbors = bordersData.getOrDefault(countryName, Collections.emptyList());
            for (String neighborInfo : neighbors) { // Iterate through neighbors and add vertices and edges to the graph.
            	String neighborName = specialCountryNameHandler(neighborInfo); // Handle special cases for neighbor names.
            	String neighborId = stateNameData.get(neighborName); // Get the neighbor's ID from the stateNameData map.
            	if (countryGraph.getVertex(neighborName) == null) { // If the neighbor is not already in the graph, add it as a vertex.
            		countryGraph.addVertex(neighborName);
            	}

                // Retrieve the distance between 2 countries from the capDistData map.
            	int borderLength = -1;
            	if (capDistData.containsKey(countryId + "_" + neighborId)) {
                   	borderLength = capDistData.get(countryId + "_" + neighborId);
                } else if (capDistData.containsKey(neighborId + "_" + countryId)) {
                    borderLength = capDistData.get(neighborId + "_" + countryId);
                }

                // If a valid distance is found, add an edge to the graph.
            	if (borderLength > -1) {
            		countryGraph.addEdge(specialCountryNameHandler(countryName), neighborName, borderLength);
                }
            } 
        }
        // Return the constructed country graph.
        return countryGraph;
    }

    /**
     * Handles special cases for country names, providing an alternative name if available.
     * @param countryName The input country name.
     * @return The standard or alternative name for the given country.
     */
    private static String specialCountryNameHandler(String countryName) {
    	return nameMatching.getOrDefault(countryName, countryName);
    }
    
    /**
     * Initializes the nameMatching map with alternative names for certain countries.
     */
    private static void createNameMatching() {
    	nameMatching.put("US", "United States of America");
    	nameMatching.put("United States", "United States of America");
    	nameMatching.put("Suriname", "Surinam");
    	nameMatching.put("UK", "United Kingdom");
    	nameMatching.put("Spain (Ceuta)", "Spain");
    	nameMatching.put("Germany", "German Federal Republic");
    	nameMatching.put("Czechia", "Czech Republic");
    	nameMatching.put("Italy", "Italy/Sardinia");
    	nameMatching.put("North Macedonia", "Macedonia (Former Yugoslav Republic of)");
    	nameMatching.put("Macedonia", "Macedonia (Former Yugoslav Republic of)");
    	nameMatching.put("Bosnia and Herzegovina", "Bosnia-Herzegovina");
    	nameMatching.put("Romania", "Rumania");
    	nameMatching.put("Russia", "Russia (Soviet Union)");
    	nameMatching.put("Belarus", "Belarus (Byelorussia)");
    	nameMatching.put("Denmark (Greenland)", "Denmark");
    	nameMatching.put("cabo verde", "cape verde");
    	nameMatching.put("Gambia, The", "Gambia");
    	nameMatching.put("The Gambia", "Gambia");
    	nameMatching.put("Cote d'Ivoire", "Cote D'Ivoire");
    	nameMatching.put("Burkina Faso", "Burkina Faso (Upper Volta)");
    	nameMatching.put("Congo, Democratic Republic of the", "Congo, Democratic Republic of (Zaire)");
    	nameMatching.put("Democratic Republic of the Congo", "Congo, Democratic Republic of (Zaire)");
    	nameMatching.put("Congo, Republic of the", "Congo");
    	nameMatching.put("Republic of the Congo", "Congo");
    	nameMatching.put("Tanzania", "Tanzania/Tanganyika");
    	nameMatching.put("Zimbabwe", "Zimbabwe (Rhodesia)");
    	nameMatching.put("Eswatini", "Swaziland");
    	nameMatching.put("Iran", "Iran (Persia)");
    	nameMatching.put("Morocco (Ceuta)", "Morocco");
    	nameMatching.put("Turkey", "Turkey (Ottoman Empire)");
    	nameMatching.put("Yemen", "Yemen (Arab Republic of Yemen)");
    	nameMatching.put("UAE", "United Arab Emirates");
    	nameMatching.put("Kyrgyzstan", "Kyrgyz Republic");
    	nameMatching.put("North Korea", "Korea, People's Republic of");
    	nameMatching.put("Korea, North", "Korea, People's Republic of");
    	nameMatching.put("Korea, South", "Korea, Republic of");
    	nameMatching.put("South Korea", "Korea, Republic of");
    	nameMatching.put("Burma", "Myanmar (Burma)");
    	nameMatching.put("Sri Lanka", "Sri Lanka (Ceylon)");
    	nameMatching.put("Cambodia", "Cambodia (Kampuchea)");
    	nameMatching.put("Vietnam", "Vietnam, Democratic Republic of");
    	nameMatching.put("Timor-Leste", "East Timor");
    	nameMatching.put("Gaza Strip", "West Bank");

    }
    
    /**
     * Reads a file containing country border information and returns a mapping of countries to their neighbors.
     * @param fileName The name of the file containing country border information.
     * @return A mapping of countries to their neighboring countries.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    private static Map<String, List<String>> readBordersFile(String fileName) throws IOException {
        // Initialize a map to store country border information.
        Map<String, List<String>> bordersData = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Read each line from the file.
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into country and its neighbors.
                String[] tokens = line.split("=");
                String country = tokens[0].trim();
                String[] neighborsWithLength = tokens[1].split(";");
                
                // Initialize a list to store the neighbors of the current country.
                List<String> neighbors = new ArrayList<>();
                for (String neighborWithLength : neighborsWithLength) {
                    // Extract the neighbor name from the information.
                    String neighborName = neighborWithLength.split("\\d+")[0].trim();
                    neighbors.add(neighborName); 
                }
                // Add the country and its neighbors to the map.
                bordersData.put(country, neighbors);
            }
        } catch (FileNotFoundException e) {
            // Throw an IOException if the file is not found.
            throw new IOException("File not found: " + fileName, e);
        } catch (IOException e) {
            // Throw an IOException for general file reading errors.
            throw new IOException("Error reading file: " + fileName, e);
        }
        // Return the map containing country border information.
        return bordersData;
    }

     /**
     * Reads a file containing capital distances and returns a mapping of country pairs to their distances.
     * @param fileName The name of the file containing capital distances.
     * @return A mapping of country pairs to their distances.
     * @throws IOException If an I/O error occurs while reading the file or parsing distances.
     */
    private static Map<String, Integer> readCapDistFile(String fileName) throws IOException {
        // Initialize a map to store country pairs and their distances.
        Map<String, Integer> capDistData = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        	// Skip the first line containing headers.
            reader.readLine();
            // Read each line from the file.
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into tokens and extract relevant information.
                String[] tokens = line.split(",");
                String country1Id = tokens[0].trim();
                String country2Id = tokens[2].trim();
                int distance = Integer.parseInt(tokens[4].trim());
                // Add the country pair and its distance to the map.
                capDistData.put(country1Id + "_" + country2Id, distance);
            }
        } catch (FileNotFoundException e) {
            // Throw an IOException if the file is not found.
            throw new IOException("File not found: " + fileName, e);
        } catch (IOException | NumberFormatException e) {
            // Throw an IOException for general file reading errors or distance parsing errors.
            throw new IOException("Error reading file or parsing distance: " + fileName, e);
        }

        // Return the map containing country pairs and their distances.
        return capDistData;
    }

    /**
     * Reads a file containing state names and returns a mapping of country names to their corresponding IDs.
     * @param fileName The name of the file containing state names.
     * @return A mapping of country names to their corresponding IDs.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    private static Map<String, String> readStateNameFile(String fileName) throws IOException {
        // Initialize a map to store country names and their corresponding IDs.
        Map<String, String> stateNameData = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Read each line from the file.
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into tokens and extract relevant information.
                String[] tokens = line.split("\t");
                String countryId = tokens[0].trim();
                String countryName = tokens[2].trim();
                String endDate = tokens[4].trim();

                // Check if the entry is for the date "2020-12-31" and add it to the map.
                if (endDate.equals("2020-12-31")) {
                    stateNameData.put(countryName, countryId);
                }
            }
        } catch (FileNotFoundException e) {
            // Throw an IOException if the file is not found.
            throw new IOException("File not found: " + fileName, e);
        } catch (IOException e) {
            // Throw an IOException for general file reading errors.
            throw new IOException("Error reading file: " + fileName, e);
        }
        // Return the map containing country names and their corresponding IDs.
        return stateNameData;
    }

    /**
     * Find and returns the distance between two countries that exist and share border.
     * @param country1 The name of the first country.
     * @param country2 The name of the second country.
     * @return The distance between the two neighbor and exist countries, or -1 if there is no valid path.
     */
    public int getDistance(String country1, String country2) {
        // Standardize country names using the specialCountryNameHandler method.
    	String standardName1 = specialCountryNameHandler(country1);
        String standardName2 = specialCountryNameHandler(country2);
        // Check if both countries are present in the country graph.
        if (countryGraph.containsVertex(standardName1) && countryGraph.containsVertex(standardName2)) {
            // Return 0 if the countries are the same.
        	if (standardName1.equals(standardName2)) {
                return 0;
            }
            // Return -1 if there is no direct border between the countries.
        	if (!shareBorder(standardName1, standardName2)) {
        		return -1;
        	}
        	// Find the shortest path between the countries in the graph.
            List<WeightedEdge> path = countryGraph.findShortestPath(standardName1, standardName2);

            // If a valid path is found, calculate and return the total distance.
            if (path != null) { 
                int totalDistance = 0;
                for (WeightedEdge edge : path) {
                    totalDistance += edge.getWeight();
                }
                return totalDistance;
            }
        }

        // Return -1 if no valid path or if countries are not present in the graph.
        return -1;
    }
    
    /**
     * Checks if two countries share a border.
     * @param country1 The name of the first country.
     * @param country2 The name of the second country.
     * @return True if the countries share a border, false otherwise.
     */
    private boolean shareBorder(String country1, String country2) {
        // Get vertices for the given country names from the country graph.
    	Vertex vertex1 = countryGraph.getVertex(country1);
        Vertex vertex2 = countryGraph.getVertex(country2);

        // Check if both vertices are present in the graph.
        if (vertex1 != null && vertex2 != null) {
            // Get neighbors of the first country.
        	Map<Vertex, Integer> names1 = vertex1.getNeighbors();
            // Check if the second country is among the neighbors of the first country.
            for (Vertex v: names1.keySet()) {
            	if (v.getName().equals(country2)) {
            		return true;
            	}
            }
        }
        // Return false if the countries do not share a border or are not present in the graph.
        return false;
    }
    
    /**
     * Finds the shortest path between two countries and returns a list of formatted steps.
     * @param country1 The name of the first country.
     * @param country2 The name of the second country.
     * @return A list of formatted steps in the path, or an empty list if no path is found.
     */
    public List<String> findPath(String country1, String country2) {
        // Standardize country names using the specialCountryNameHandler method.
        String standardName1 = specialCountryNameHandler(country1);
        String standardName2 = specialCountryNameHandler(country2);
        // Check if both countries are present in the country graph.
        if (countryGraph.containsVertex(standardName1) && countryGraph.containsVertex(standardName2)) {
        	// Find the shortest path between the countries in the graph.
            List<WeightedEdge> path = countryGraph.findShortestPath(standardName1, standardName2);
            // If a valid path is found, format and return the path steps.
            if (path != null) {
                return formatPath(path, country1, country2);
            }
        }
        // Return an empty list if no valid path or if countries are not present in the graph.
        return Collections.emptyList();
    }

    /**
     * Formats a list of weighted edges into a list of formatted path steps.
     * @param path      The list of weighted edges representing the path.
     * @param country1  The name of the first country.
     * @param country2  The name of the second country.
     * @return A list of formatted path steps.
     */
    private List<String> formatPath(List<WeightedEdge> path, String country1, String country2) {
        // Initialize a list to store the formatted path steps.
        List<String> formattedPath = new ArrayList<>();
        // Iterate through the weighted edges in the path and format each step.
        for (int i = 0; i < path.size(); i++) {
            WeightedEdge currentEdge = path.get(i);
            String currentCountry = mapToStandardName(currentEdge.getStartVertex().getName(), country1, country2);
            String nextCountry = mapToStandardName(currentEdge.getEndVertex().getName(), country1, country2);
            int distance = currentEdge.getWeight();
            formattedPath.add(String.format("%s --> %s (%d km.)", currentCountry, nextCountry, distance));
        }
        // Return the list of formatted path steps.
        return formattedPath;
    }
    
    /**
     * Maps an input name to a standard name based on special cases and mappings.
     * @param inputName The input name to be mapped.
     * @param country1  The name of the first country.
     * @param country2  The name of the second country.
     * @return The mapped standard name.
     */
    private String mapToStandardName(String inputName, String country1, String country2) {
        // Check if the input name matches either of the two countries, and return the corresponding country name.
        if (specialCountryNameHandler(country1).equals(inputName)) {
            return country1;
        } else if (specialCountryNameHandler(country2).equals(inputName)) {
            return country2;
        } else {
            // Return the input name if no match is found.
            return inputName;
        }
    }
    
    /**
     * Accepts user input to find the distance or path between two countries.
     */
    public void acceptUserInput() {
        String country1;
        String country2;
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter the name of the first country (type EXIT to quit): ");
                country1 = scanner.nextLine();
                
                // Exit the loop if the user enters "EXIT."
                if (country1.equalsIgnoreCase("EXIT")) {
                    break;
                }
                // Check if the entered country name is valid.
                if (!countryGraph.containsVertex(specialCountryNameHandler(country1))) {
                    System.out.println("Invalid country name. Please enter a valid country name.");
                    continue;
                }
                
                do {
                    // Prompt the user to enter the name of the second country.
                    System.out.print("Enter the name of the second country (type EXIT to quit): ");
                    country2 = scanner.nextLine();
                    // Exit the program if the user enters "EXIT."
                    if (country2.equalsIgnoreCase("EXIT")) {
                        return;
                    }
                    // Check if the entered country name is valid.
                    if (!countryGraph.containsVertex(specialCountryNameHandler(country2))) {
                        System.out.println("Invalid country name. Please enter a valid country name.");
                    }

                } while (!countryGraph.containsVertex(specialCountryNameHandler(country2)));
                // Find and display the path between the two countries.
                List<String> path = findPath(country1, country2);
                System.out.println("Route from " + country1 + " to " + country2 + ":");
                System.out.println("Path is as follows: " + path);
                // Display the formatted path steps.
                if (!path.isEmpty()) {
                    for (String step : path) {
                        System.out.println("* " + step);
                    }
                } else {
                    System.out.println("No route found between " + country1 + " and " + country2 + ".");
                }
            }
		} catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Exiting the program.");
    }

    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        System.out.println("1st test case: " + a3.getDistance("USF", "My House"));
        System.out.println("2nd test case: " + a3.getDistance("France", "Spain"));
        System.out.println("3rd test case: " + a3.getDistance("Canada", "Panama"));
        System.out.println("4th test case: " + a3.getDistance("", "Panama"));
        System.out.println("5th test case: " + a3.getDistance("Vietnam", "Cambodia"));
        System.out.println("6th test case: " + a3.getDistance("US", "Canada"));
        a3.acceptUserInput();
    }

}

