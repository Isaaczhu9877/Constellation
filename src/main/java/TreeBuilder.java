import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TreeBuilder {

    private static ArrayList<String[]> timeStampPackages = new ArrayList<>();
    private static HashMap<String, MethodNode> signatureRegistry = new HashMap<>();
    public static MethodNode buildMethodTree() {

        // parseTimeLogs();
        // ArrayList<String[]> timeStampPackagesCopy = timeStampPackages;
        Stack<MethodNode> methodStack = new Stack<>();
        MethodNode root = new MethodNode();
        root.name = "root";
        root.callSignature = "root";
        root.parent = null;
        root.frequency = 1;

        methodStack.push(root);
        int numPackages = timeStampPackages.size();

        for (int i = 0; i < numPackages; i++) {

            String[] methodStamp = timeStampPackages.get(i);
            mNodeMaker(methodStack, methodStamp);
        }

        root.duration = root.children.get(0).duration;
        root.programDuration = root.children.get(0).duration;
        methodTreeFinisher(root);
        return root;
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


    private static void mNodeMaker(Stack<MethodNode> methodStack, String[] methodStamp) {

        if (methodStamp[0].equals("START")) {

            mNodeInitializer(methodStamp, methodStack);

        } else {

            mNodeCompleter(methodStamp, methodStack);

        }
    }


    private static void mNodeInitializer(String[] methodStamp, Stack<MethodNode> methodStack) {

        MethodNode parent = methodStack.peek();
        String callSignature = methodStamp[1] + "." + parent.callSignature;

        MethodNode currNode;

        if (signatureRegistry.containsKey(callSignature)) {

            currNode = signatureRegistry.get(callSignature);

        } else {

            currNode = new MethodNode();
            currNode.callSignature = callSignature;
            currNode.name = methodStamp[1];
            parent.children.add(currNode);
            currNode.parent = parent;
            signatureRegistry.put(callSignature, currNode);
        }

        currNode.startTimes.push(Integer.parseInt(methodStamp[2]));
        currNode.frequency = currNode.frequency + 1;
        methodStack.push(currNode);
    }


    private static void mNodeCompleter(String[] methodStamp, Stack<MethodNode> methodStack) {

        MethodNode currNode = methodStack.pop();

        // check that pull matching packages together
        if (!currNode.name.equals(methodStamp[1])) {
            // throw an exception - methods do not match
        }

        int endTime = Integer.parseInt(methodStamp[2]);
        int startTime = currNode.startTimes.pop();
        currNode.duration += (endTime - startTime);
    }


    private static void methodTreeFinisher(MethodNode root) {

        Stack<MethodNode> formatStack = new Stack<>();
        formatStack.push(root);

        while (!formatStack.empty()) {

            MethodNode curr = formatStack.pop();
            mNodeMetaFiller(curr);
            childStacker(curr, formatStack);

        }
    }


    private static void mNodeMetaFiller(MethodNode curr) {

        int duration = curr.duration;
        int programDuration = curr.programDuration;
        int frequency = curr.frequency;
        double aggPercentDur = ((duration*1.0) / programDuration)*100;
        double avgPercentDur = (aggPercentDur / frequency);
        curr.aggPercentDur = aggPercentDur;
        curr.avgPercentDur = avgPercentDur;
    }


    private static void childStacker(MethodNode curr, Stack<MethodNode> formatStack) {

        ArrayList<MethodNode> children = curr.children;

        for (MethodNode child : children) {

            child.programDuration = curr.programDuration;
            formatStack.push(child);
        }
    }
}
