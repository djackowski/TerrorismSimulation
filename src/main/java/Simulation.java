import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Simulation {
    private final String LOG_NAME = "logs.txt";
    private int people;
    private double probability;
    private int hotels;
    private int days;
    private List<Person> personList = new ArrayList<>();
    private List<Hotel> hotelList = new ArrayList<>();
    private List<Day> dayList = new ArrayList<>();
    private Map<Day, Integer> visitsPerDay = new HashMap<>();
    private int pair = 0;
    private List<Integer> allList = new ArrayList<>();
    private List<Integer> pairList = new ArrayList<>();
    private Map<Integer, Integer> visitsPair = new HashMap<>();
    private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private int pairs;
    private int peopleThatMakePairs;
    private StringBuilder logs = new StringBuilder();
    private JFreeChart barChart;
    private SimulationListener simulationListener;

    public Simulation(int people, int days, int hotels, double probability) {
        this.people = people;
        this.days = days;
        this.hotels = hotels;
        this.probability = probability;
    }

    public void setSimulationListener(SimulationListener simulationListener) {
        this.simulationListener = simulationListener;
    }

    public int getPairs() {
        return pairs;
    }

    public String getLogs() {
        return logs.toString();
    }

    public String getVisitsPairs() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : visitsPair.entrySet()) {

            sb.append("Visits: ");
            sb.append(entry.getKey());
            sb.append(", pairs: ");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        sb.append("All pairs: ");
        sb.append(pairs);
        sb.append("\n");
        sb.append("People that make pairs: ");
        sb.append(peopleThatMakePairs / 2);
        return sb.toString();
    }

    public void start() {
        generateGraph();
        removeFile();
        for (int i = 0; i < people; i++) {
            personList.add(new Person(i));
        }


        for (int i = 0; i < hotels; i++) {
            hotelList.add(new Hotel(i));
        }

        for (int i = 0; i < days; i++) {
            Day day = new Day(i);
            dayList.add(day);
            visitsPerDay.put(day, 0);
        }

        for (int i = 0; i < days; i++) {
            Day day = new Day(i);
            for (int j = 0; j < people; j++) {
                if (isGoingToHotel()) {
                    try {
                        Person person = personList.get(j);
                        Hotel hotelById = getHotelById(drawHotel());
                        person.getDayHotelMap().put(day, hotelById);
                        int personNo = person.getId() + 1;
                        int hotelNo = hotelById.getId() + 1;
                        int dayNo = i + 1;
                        writeToFile("Person " + personNo + " Hotel: " + hotelNo + " Day: " + dayNo);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }


        for (int i = 0; i < personList.size(); i++) {
            for (int j = i + 1; j < personList.size(); j++) {
                Map<Day, Hotel> dayHotelMapFirst = personList.get(i).getDayHotelMap();
                Map<Day, Hotel> dayHotelMapSecond = personList.get(j).getDayHotelMap();
                Set<Day> firstDays = new HashSet<>(dayHotelMapFirst.keySet());
                Set<Day> secondDays = new HashSet<>(dayHotelMapSecond.keySet());
                Set<Day> commonDays = new HashSet<>(firstDays);
                commonDays.retainAll(secondDays);

                commonDays.forEach(new Consumer<Day>() {
                    @Override
                    public void accept(Day day) {
                        if (dayHotelMapFirst.get(day).equals(dayHotelMapSecond.get(day))) {
                            pair++;
                        }
                    }
                });
                if (pair == 0) {
                    continue;
                }
                allList.add(pair);
                pair = 0;
            }
        }
        int i = 1;
        pairs = 0;
        int occurrences;
        while (true) {
            occurrences = Collections.frequency(allList, i);
            if (occurrences == 0) {
                break;
            }
            visitsPair.put(i, occurrences);
            System.out.println(occurrences);

            pairs += calculatePairs(i, occurrences);
            peopleThatMakePairs += calculatePeople(i, occurrences);
            saveToGraph(i, occurrences);
            i++;
        }
        generateGraph();
        System.out.println("Pairs: " + pairs);
        System.out.println("People: " + peopleThatMakePairs / 2);
    }

    private int calculatePeople(int i, int occurrences) {
        if (i > 1) {
            return occurrences;
        }
        return 0;
    }

    private void saveToGraph(int visits, int pairs) {
        dataset.addValue(pairs, "Pairs", String.valueOf(visits));
    }

    private void removeFile() {
        try {

            File file = new File(LOG_NAME);

            file.delete();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public JFreeChart getBarChart() {
        return barChart;
    }


    private void writeToFile(String data) {
        try (FileWriter fw = new FileWriter(LOG_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(data);
            logs.append(data);
            logs.append("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateGraph() {
        barChart = ChartFactory.createBarChart3D(
                "Terrorism",
                "Visits",
                "Pairs",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        int width = 640; /* Width of the image */
        int height = 480; /* Height of the image */
        File barChart3D = new File("barChart3D.jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(barChart3D, barChart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private long calculatePairs(int visits, int occurrences) {
        return factorial(visits) / (factorial(2) * factorial(visits - 2)) * occurrences;

    }


    private long factorial(int N) {
        long multi = 1;
        for (int i = 1; i <= N; i++) {
            multi = multi * i;
        }
        return multi;
    }

    private boolean isGoingToHotel() {
        return Math.random() < probability;
    }

    private Hotel getHotelById(int id) throws Throwable {
        return hotelList.stream()
                .filter(hotel -> hotel.getId() == id)
                .findFirst().orElseThrow((Supplier<Throwable>) () -> null);
    }

    private int drawHotel() {
        Random random = new Random();
        return random.nextInt(hotels);
    }


}
