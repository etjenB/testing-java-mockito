package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.ControllerTests;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class IndexControllerTest implements ControllerTests {
    IndexController indexController;

    @BeforeEach
    void setUp() {
        indexController = new IndexController();
    }

    @DisplayName("Test proper view is returned for index")
    @Test
    void index() {
        assertEquals("index", indexController.index(), "Wrong view returned for index method");

        assertThat(indexController.index()).isEqualTo("index");
    }

    @Test
    @DisplayName("Test exception view")
    void oopsHandler() {
        assertThrows(ValueNotFoundException.class, () -> {
           indexController.oopsHandler();
        });

//        assertTrue("notimplemented".equals(indexController.oopsHandler()), () -> {
//            int number = 1;
//            return "Wrong view for not implemented retured " + number;
//        });
    }

    @Test
    @Disabled
    void testTimeout() {
        assertTimeout(Duration.ofMillis(100), () -> {
            Thread.sleep(1000);
            System.out.println("Test timeout");
        });
    }

    @Test
    @Disabled
    void testTimeoutPree() {
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            Thread.sleep(1000);
            System.out.println("Test timeout preemptively");
        });
    }

    @Test
    void testAssumptionTrue() {
        assumeTrue("ETJEN".equals(System.getenv("ETJEN_RUNTIME")));
    }

    @Test
    void testAssumptionTrueIsTrue() {
        assumeTrue("ETJEN".equals("ETJEN"));
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testMeOnMac() {
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testMeOnWindows() {
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "USER", matches = "bulju")
    void testIfUserBulju() {
    }
}