import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame {

    private final JTextArea display;

    public SimulationFrame() throws HeadlessException {
        super("TerrorismCore");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setSize(1000, 1200);
        setLocation(400, 50);
        setLayout(new FlowLayout());

        JButton start = new JButton("START");
        JLabel peopleLabel = new JLabel("People");
        JLabel daysLabel = new JLabel("Days");
        JLabel hotelsLabel = new JLabel("Hotels");
        JLabel probabilityLabel = new JLabel("Probability");

        JTextField people = new JTextField("1000");
        JTextField days = new JTextField("100");
        JTextField hotels = new JTextField("100");
        JTextField probability = new JTextField("0.3");

        JPanel middlePanel = new JPanel();
        middlePanel.setBorder(new TitledBorder(new EtchedBorder(), "Logs"));


        display = new JTextArea(16, 58);
        display.setEditable(false);
        JScrollPane scroll = new JScrollPane(display);

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JTextArea keyValue = new JTextArea();
        keyValue.setEditable(false);
        final ChartPanel[] CP = new ChartPanel[1];
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CP[0] != null) {
                    remove(CP[0]);
                    validate();
                    repaint();
                }
                int parsedPeople = Integer.parseInt(people.getText());
                int parsedDays = Integer.parseInt(days.getText());
                int parsedHotels = Integer.parseInt(hotels.getText());
                double parsedProbability = Double.parseDouble(probability.getText());
                Simulation simulation = new Simulation(parsedPeople, parsedDays, parsedHotels, parsedProbability);
                simulation.start();
                keyValue.setText(simulation.getVisitsPairs());
                display.setText(simulation.getLogs());
                CP[0] = new ChartPanel(simulation.getBarChart());
                add(CP[0], BorderLayout.CENTER);
                validate();


            }
        });
        start.setSize(100, 100);
        add(peopleLabel);
        add(people);
        add(daysLabel);
        add(days);
        add(hotelsLabel);
        add(hotels);
        add(probabilityLabel);
        add(probability);
        add(start);
        add(keyValue);

        middlePanel.add(scroll);

        add(middlePanel);
    }


}
