package interview;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarRepository {

    private List<Car> cars;

    public CarRepository(){
        getSomeCars();
    }


    Car findCarById(String id){
        Optional<Car> carOptional= cars.stream().filter(car -> car.getId().equals(id)).findFirst();
        return carOptional.isPresent()?carOptional.get():null;
    }

    Optional<Car> findCarByIdWithOptional(String id){
        return cars.stream().filter(car -> car.getId().equals(id)).findFirst();
    }

    private void getSomeCars(){
        cars = new ArrayList<>();
        cars.add(new Car("a", "1A9 4321", "blue"));
        cars.add(new Car("volkswagen", "2B1 1292", "blue"));
        cars.add(new Car("skoda", "5C9 9984", "green"));
        cars.add(new Car("audi", "8E4 4321", "silver"));
        cars.add(new Car("mercedes", "3B4 5555", "black"));
        cars.add(new Car("seat", "6U5 3123", "white"));
    }
}
