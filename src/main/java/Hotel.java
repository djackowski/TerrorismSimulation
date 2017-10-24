public class Hotel {
    private int id;

    public Hotel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hotel hotel = (Hotel) o;

        return id == hotel.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
