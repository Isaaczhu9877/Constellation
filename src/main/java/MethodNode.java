import java.util.ArrayList;
import java.util.Stack;

class MethodNode {
    String name = "";
    Stack<Integer> startTimes = new Stack<>();
    int duration = 0;
    int programDuration = 0;
    int frequency = 0;
    String callSignature = "";
    double aggPercentDur = 0.0;
    double avgPercentDur = 0.0;
    ArrayList<MethodNode> children = new ArrayList<>();
    MethodNode parent;

    public MethodNode(String name) {
        this.name = name;
    }

    public MethodNode() {
        // Alternate
    }
    public String getName() {
        return this.name;
    }
}