public class FakeClass {
    public int firstField;

    public int secondField;

    public int thirdField;

    public FakeSubClass fourthField;

    public static void doOne() {
        Logger.generateLogCallMethod("doOne", true);
        FakeClass.doTwo();
        Logger.generateLogCallMethod("doOne", false);;
        return;
    }

    public static void doTwo() {
        Logger.generateLogCallMethod("doTwo", true);
        if (false) {
            FakeClass.doThree();
            java.lang.System.out.println("Hello");
        }
        FakeClass.doFour();
        Logger.generateLogCallMethod("doTwo", false);;
        return;
    }

    public static void doThree() {
        Logger.generateLogCallMethod("doThree", true);
        Logger.generateLogCallMethod("doThree", false);;
        return;
    }

    public static void doFour() {
        Logger.generateLogCallMethod("doFour", true);
        java.lang.System.out.println("hello");
        FakeSubClass test = new FakeSubClass();
        test.doSubOne();
        Logger.generateLogCallMethod("doFour", false);;
        return;
    }

    public static void main(java.lang.String[] args) {
        Logger.generateLogCallMethod("main", true);
        // Here we go
        FakeClass.doOne();
        Logger.generateLogCallMethod("main", false);;
        Logger.writePackagesToFile();
    }
}