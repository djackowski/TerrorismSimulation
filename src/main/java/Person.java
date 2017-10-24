import java.util.HashMap;
import java.util.Map;

public class Person {
    private int id;
    private Map<Day, Hotel> dayHotelMap = new HashMap<>();

    public Person(int id) {
        this.id = id;
    }

    public Map<Day, Hotel> getDayHotelMap() {
        return dayHotelMap;
    }

    public int getId() {
        return id;
    }
}
