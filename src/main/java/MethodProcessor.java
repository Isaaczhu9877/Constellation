import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.Filter;

public class MethodProcessor extends AbstractProcessor<CtMethod> {
    public void process(CtMethod myMethod) {
        System.out.println(myMethod.getSimpleName());

        CtCodeSnippetStatement startSnip = getFactory().Core().createCodeSnippetStatement();

        String name = myMethod.getSimpleName();
        String startStamp = "Logger.generateLogCallMethod(" + "\"" + name + "\"" + ", true)";
        startSnip.setValue(startStamp);


        CtCodeSnippetStatement endSnip = getFactory().Core().createCodeSnippetStatement();
        String endStamp = "Logger.generateLogCallMethod("  + "\"" +  name + "\"" + ", false);";
        endSnip.setValue(endStamp);

        // Determines whether there is a return statement for placement of end stamp
        CtStatement maybeRet = null;
        for (CtStatement st: myMethod.getBody().getStatements()){
            if (st instanceof CtReturn){
                maybeRet = st;
            }
        }

        if (maybeRet == null) {
            myMethod.getBody().insertEnd(endSnip);
        }else {
            maybeRet.insertBefore(endSnip);
        }
        myMethod.getBody().insertBegin(startSnip);

        // Inject a call to build tree
        if (name.equals("main")) {
            CtCodeSnippetStatement saveLogsSnip = getFactory().Core().createCodeSnippetStatement();
            String saveLogsToFile = "Logger.writePackagesToFile()";
            saveLogsSnip.setValue(saveLogsToFile);
            myMethod.getBody().insertEnd(saveLogsSnip);
        }

    }
}
