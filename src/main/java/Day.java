public class Day {
    private int id;

    public Day(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Day day = (Day) o;

        return id == day.id;
    }

    @Override
    public int hashCode() {
        return id;
    }


    public int getId() {
        return id;
    }
}
