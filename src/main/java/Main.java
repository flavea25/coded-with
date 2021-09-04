import com.google.inject.Guice;
import com.google.inject.Injector;
import helpers.CodedWithProgram;
import services.MyInjector;

public class Main {

    public static void main(String[] args) {
        CodedWithProgram program = new CodedWithProgram();
        program.run(args);
    }
}
