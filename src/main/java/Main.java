import com.google.inject.Guice;
import com.google.inject.Injector;
import helpers.MyHelper;
import lombok.extern.slf4j.Slf4j;
import services.MyInjector;

@Slf4j
public class Main {

    private static final Injector injector = Guice.createInjector(new MyInjector());
    private static final MyHelper program = injector.getInstance(MyHelper.class);

    public static void main(String[] args) {
        program.run(args);
    }
}
