package guru.springframework.sfgpetclinic.model;

import guru.springframework.sfgpetclinic.CustomArgsProvider;
import guru.springframework.sfgpetclinic.ModelTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class OwnerTest implements ModelTests {

    @Test
    void dependentAssertions() {
        Owner owner = new Owner(1l, "Joe", "Bucks");
        owner.setCity("Kanye West");
        owner.setTelephone("123456789");

        assertAll("Properties test",
                () -> assertAll("Person properties",
                        () -> assertEquals("Joe", owner.getFirstName()),
                        () -> assertEquals("Bucks", owner.getLastName())),
                () -> assertAll("Owner properties",
                        () -> assertEquals("Kanye West", owner.getCity()),
                        () -> assertEquals("123456789", owner.getTelephone()))
        );

        assertThat(owner.getCity(), is("Kanye West"));
    }

    @DisplayName("Value Source Test")
    @ParameterizedTest(name = "{displayName} - [{index} {arguments}]")
    @ValueSource(strings = {"Spring", "Framework", "Etjen"})
    void testValueSource(String val) {
        System.out.println(val);
    }

    @DisplayName("Enum Source Test")
    @ParameterizedTest(name = "{displayName} - [{index} {arguments}]")
    @EnumSource(OwnerType.class)
    void enumTest(OwnerType ownerType) {
        System.out.println(ownerType);
    }

    @DisplayName("CSV Input Test")
    @ParameterizedTest(name = "{displayName} - [{index} {arguments}]")
    @CsvSource({
            "FL, 1, 1",
            "MI, 2, 2",
            "OH, 3, 3"
    })
    void csvInputTest(String stateName, int val1, int val2) {
        System.out.println("State: " + stateName + " - " + val1 + " : " + val2);
    }

    @DisplayName("CSV File Source Test")
    @ParameterizedTest(name = "{displayName} - [{index} {arguments}]")
    @CsvFileSource(resources = "/input.csv", numLinesToSkip = 1)
    void csvFileSourceTest(String stateName, int val1, int val2) {
        System.out.println("State: " + stateName + " - " + val1 + " : " + val2);
    }

    @DisplayName("Method Source Test")
    @ParameterizedTest(name = "{displayName} - [{index} {arguments}]")
    @MethodSource("getargs")
    void methodSourceTest(String stateName, int val1, int val2) {
        System.out.println("State: " + stateName + " - " + val1 + " : " + val2);
    }

    static Stream<Arguments> getargs() {
        return Stream.of(
            Arguments.of("FL", 100, 101),
            Arguments.of("AL", 900, 905),
            Arguments.of("TX", 200, 201)
        );
    }

    @DisplayName("Custom Provider Source Test")
    @ParameterizedTest(name = "{displayName} - [{index} {arguments}]")
    @ArgumentsSource(CustomArgsProvider.class)
    void customProviderSourceTest(String stateName, int val1, int val2) {
        System.out.println("State: " + stateName + " - " + val1 + " : " + val2);
    }
}