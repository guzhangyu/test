package interview;

import org.junit.Assert;
import org.junit.Test;

public class OptionalTest {

    CarRepository repository=new CarRepository();

    @Test
    void getCarById(){
        Car car = repository.findCarById("1A9 4321");
        Assert.assertNotNull(car);

        Car nullCar = repository.findCarById("M 432 KT");

    }
}
