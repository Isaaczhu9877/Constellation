import java.io.IOException;
import java.util.Random;

public class VisualizerMain {
    static int counter = 0;
    public static void main(String[] args)
            throws IOException {
        MethodNode root = TreeBuilder.buildMethodTree(); // Visualizer call that uses saved tree
        new DSLVisualizer(root.children.get(0), (int) root.programDuration);
    }

    private static MethodNode createDummyTree() {
        MethodNode root = new MethodNode("root");
        createDummyChildren(root, 1);
        return root;
    }

    private static void createDummyChildren(MethodNode node, int level) {
        node.duration = Math.abs(new Random().nextInt() % 100) + 1;
        node.frequency = Math.abs(new Random().nextInt() % 10) + 1;
        if (level >= 3)
            return;

        int childNum = level % 2 + 1;
        for (int i = 0; i < childNum; i++) {
            MethodNode n = new MethodNode(String.format("child %d", counter++));
            node.children.add(n);
            createDummyChildren(n, level + 1);
        }
    }
}
