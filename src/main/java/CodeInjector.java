import spoon.Launcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeInjector {

	// These must be converted to relative paths.
    String sourceProjectPath = "C:\\Users\\aleem\\Desktop\\CPSC 410\\Projects\\FakeProgram\\src"; // Path to FakePrograme src file
    String outputModifiedPath = "./modified/"; // Path to output modified source code

    public void modifyCode(){
        Launcher launcher = new Launcher ();
        launcher.addInputResource(sourceProjectPath);
        launcher.setSourceOutputDirectory(outputModifiedPath);
        launcher.addProcessor(new MethodProcessor());
        launcher.run();

    }

    public static void main(String[] args) {
        // Need to clear the modified folder.
        try {
            Files.walk(Paths.get("modified/"))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path src = Paths.get("helper/Logger.java");
        Path dest = Paths.get("modified/Logger.java");
        try {
            Files.copy(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CodeInjector ci = new CodeInjector();
        ci.modifyCode();

        String[] cmd = new String[]{"javac", "./modified/*.java"};
        String[] cmd2 = new String[]{"java","-cp", "modified/","FakeClass"};
        try {
            Process pr = Runtime.getRuntime().exec(cmd);
            try {
                pr.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Process pr2 = Runtime.getRuntime().exec(cmd2);
            try {
                pr2.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("This isnt working");
        }

        MethodNode myTree = TreeBuilder.buildMethodTree();

    }
}
