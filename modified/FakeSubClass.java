public class FakeSubClass {
    int firstVar;

    int secondVar;

    public void doSubOne() {
        Logger.generateLogCallMethod("doSubOne", true);
        doSubTwo();
        Logger.generateLogCallMethod("doSubOne", false);;
        return;
    }

    public void doSubTwo() {
        Logger.generateLogCallMethod("doSubTwo", true);
        Logger.generateLogCallMethod("doSubTwo", false);;
        return;
    }
}