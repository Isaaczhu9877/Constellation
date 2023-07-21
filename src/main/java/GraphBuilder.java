import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GraphBuilder {

    // TODO: Note to the Team 3 Members - for the time being I have created a new object type called GraphNodes.
    // GraphNodes fulfill essentially the same function as MethodNodes (though with significant changes),
    // and I considered replacing MethodNodes altogether with what is now GraphNodes;
    // however I decided against that in case we still want to use the TreeBuilder for providing another visualization
    // to the user.  If we decide we want to completely axe the tree visual from our project, I will go ahead and replace
    // MethodNode et all with this new implementation.

    // TODO: You will notice that the buildMethodGraph returns an ArrayList of GraphNodes; this is to ensure that
    // when traversing in GraphNodes in the Visualizer, infinite traversal does not result from cycles in the Graph.
    // The Visualizer may instead *iterate through the ArrayList* to visit each GraphNode. All parent/child information
    // is carried within each GraphNode in the form of HashMaps.  If need be, I can provide ArrayList<String> of the
    // parent/ child method names for each node as well. Please feel free to reach out to me for questions.
    private static ArrayList<String[]> timeStampPackages = new ArrayList<>();
    // this maps out GraphNodes by their method name (NOT SIGNATURE)
    private static HashMap<String, GraphNode> graphNodeMap = new HashMap<>();

    private static void main(String[] args) {}

    public static ArrayList<GraphNode> buildMethodGraph() {

        parseTimeLogs();
        GraphNode root = new GraphNode();
        root.name = "root";
        root.parents = null;
        Stack<GraphNode> methodStack = new Stack<>();
        ArrayList<GraphNode> graphNodes = new ArrayList<>();
        methodStack.push(root);
        int numPackages = timeStampPackages.size();
        graphNodes.add(root);

        for (int i = 0; i < numPackages; i++) {

            String[] methodStamp = timeStampPackages.get(i);
            buildNodes(methodStamp, methodStack, graphNodes);
        }

        Map.Entry<GraphNode, Double> entry = root.children.entrySet().iterator().next();
        GraphNode mainMethod = entry.getKey();
        Double totalDuration = mainMethod.grossDuration;
        root.programDuration = totalDuration;
        methodGraphFinisher(graphNodes);
        return graphNodes;
    }

    private static void parseTimeLogs() {

        // read timeLogs.txt and place the values into timeStampPackages

        try {

            File myObj = new File("timeLogs.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] pkg = data.split("\\s+");
                timeStampPackages.add(pkg);
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File could not be read.");
        }

    }

    private static void buildNodes(String[] methodStamp, Stack<GraphNode> methodStack, ArrayList<GraphNode> graphNodes) {

        if (methodStamp[0] == "START") {

            mNodeInitializer(methodStamp, methodStack, graphNodes);

        } else {

            mNodeCompleter(methodStamp, methodStack);

        }
    }

    private static void mNodeInitializer(String[] methodStamp, Stack<GraphNode> methodStack, ArrayList<GraphNode> graphNodes) {

        GraphNode parent = methodStack.peek();
        String name = methodStamp[1];

        GraphNode curr;

        if (graphNodeMap.containsKey(name)) {

            curr = graphNodeMap.get(name);

        } else {

            curr = new GraphNode();
            curr.name = name;
            graphNodes.add(curr);
            graphNodeMap.put(name, curr);
        }

        curr.startTimes.push(Double.parseDouble(methodStamp[2]));
        curr.frequency = curr.frequency + 1;

        // check to see if the parent already contains current.
        if (!parent.children.containsKey(curr)) {

            parent.children.put(curr, 0.0);
            curr.parents.put(parent, 1);

        } else {

            int numParentCalls = curr.parents.get(parent) + 1;
            curr.parents.put(parent, numParentCalls);

        }

        methodStack.push(curr);

    }

    private static void mNodeCompleter(String[] methodStamp, Stack<GraphNode> methodStack) {

        GraphNode curr = methodStack.pop();

        // check that pull matching packages together
        if (!curr.name.equals(methodStamp[1])) {
            // throw an exception
        }

        Double endTime = Double.parseDouble(methodStamp[2]);
        Double startTime = curr.startTimes.pop();
        Double currDuration = (endTime - startTime);
        curr.grossDuration += currDuration;

        GraphNode parent = methodStack.peek();

        if (parent.children.containsKey(curr)) {

            Double childDuration = parent.children.get(curr) + currDuration;
            parent.children.put(curr, childDuration);
            parent.childrenDuration += currDuration;

        } else {
            // throw an exception
        }
    }

    private static void methodGraphFinisher(ArrayList<GraphNode> graphNodes) {

        int numNodes = graphNodes.size();
        GraphNode root = graphNodes.get(0);
        double programDuration = root.programDuration;

        for (int i = 1; i < numNodes; i++) {

            GraphNode curr = graphNodes.get(i);
            double netDuration = curr.grossDuration - curr.childrenDuration;
            curr.netDuration = netDuration;
            curr.programDuration = programDuration;
            int frequency = curr.frequency;
            double aggPercent = (netDuration / programDuration)*100;
            curr.aggPercentDur = aggPercent;
            double avgPercent = (aggPercent / frequency);
            curr.avgPercentDur = avgPercent;

        }
    }


}
