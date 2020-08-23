package interview;

import java.util.Arrays;
import java.util.Optional;

public class OptionTest {

    public static void main(String[] args) {
        Optional<String> carOption = Arrays.asList("A").stream().filter(car->car.equals("A")).findFirst();
        System.out.println(carOption.isPresent());
    }
}
