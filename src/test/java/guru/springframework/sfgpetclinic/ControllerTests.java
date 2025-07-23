package guru.springframework.sfgpetclinic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;

@Tag("controllers")
public interface ControllerTests {

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all controller tests");
    }
}
