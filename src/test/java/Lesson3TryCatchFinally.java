import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Lesson3TryCatchFinally {
    @Test
    public void test(){
        File someFile = new File("1.txt");
        String[] strings = someFile.list();
        try {
            new FileReader(someFile).read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            System.out.println("End of code");
        }
    }
}
