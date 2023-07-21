import java.util.*;

public class GraphNode {

    // TODO: Team 3 Members - please see note in GraphBuilder.java.
    String name = "";
    // start times of the node calls.  Ends up being empty (crucial for building other data up)
    Stack<Double> startTimes = new Stack<>();
    // this is the duration of the time spent in the node PLUS all its descendants.
    double grossDuration = 0.0;
    // this is the duration of time spent in the node EXCLUDING time spent in its descendants.
    double netDuration = 0.0;
    // this is the duration of time spent in the node's descendants cumulatively.
    double childrenDuration = 0.0;
    // this is the total time spent in the program. Used to calculate percentage information.
    double programDuration = 0.0;
    // this is the number of times the node (method) has been called, cumulatively.
    int frequency = 0;
    // this is not used for this particular implementation
    String callSignature = "";
    // this is the percentage of program's time spent in node (method), for all the calls
    double aggPercentDur = 0.0;
    // this is the average percentage of program's time spent in EACH of the node (method) calls.
    double avgPercentDur = 0.0;
    // this is a map of all a node's children, and the duration spent in each child for cumulative parent calls
    // Note that if it is easier for the Visualizer implementation, I can also include / replace with ArrayList<String> of method name
    HashMap<GraphNode, Double> children = new HashMap<>();
    // this is a map of all the node's parents, and the number of times each parent calls on the node.
    // Note that if it is easier for the Visualizer implementation, I can also include or replace with ArrayList<String> of method name
    HashMap<GraphNode, Integer> parents = new HashMap<>();

    public GraphNode(String name) {
        this.name = name;
    }

    public GraphNode() {
        // Alternate
    }
    public String getName() {
        return this.name;
    }
}
