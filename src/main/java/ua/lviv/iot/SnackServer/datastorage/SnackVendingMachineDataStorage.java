package ua.lviv.iot.SnackServer.datastorage;

import org.springframework.stereotype.Component;
import ua.lviv.iot.SnackServer.model.SnackVendingMachine;


import java.io.FileOutputStream;
import java.io.Writer;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Objects;

@Component
public class SnackVendingMachineDataStorage {
    public void saveTodayMachinesReport(List<SnackVendingMachine> machines, boolean test, boolean loadTest) {
        // Made a different dayNow for our load tests
        int  dayNow;
        if (loadTest) {
            dayNow = 3;
        } else {
            dayNow = LocalDate.now().getDayOfMonth();
        }

        // Made a different test paths for our load and save tests
        String testPath = "";
        if (test) {
            testPath += "save test ";
        } else if (loadTest) {
            testPath += "load test ";
        }

        String year = Integer.toString(LocalDate.now().getYear());
        String month;
        String day;

        if (LocalDate.now().getMonthValue() < 10) {
            month = "0" + LocalDate.now().getMonthValue();
        } else {
            month = Integer.toString(LocalDate.now().getMonthValue());
        }

        if (dayNow < 10) {
            day = "0" + dayNow;
        } else {
            day = Integer.toString(dayNow);
        }


        File file = new File("src/main/resources/" + testPath + "report/snack-vending-machines_" + year + "-"
            + month + "-" + day + ".csv");



        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(machines.get(0).getHeaders() + "\n");
            for (SnackVendingMachine snackVendingMachine: machines) {
                writer.write(snackVendingMachine.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<SnackVendingMachine> loadMonthMachineReport(boolean test) throws IOException {
        List<SnackVendingMachine> resultList = new LinkedList<>();
        File file;



        String year = Integer.toString(LocalDate.now().getYear());
        String month;

        if (LocalDate.now().getMonthValue() < 10) {
            month = "0" + LocalDate.now().getMonthValue();
        } else {
            month = Integer.toString(LocalDate.now().getMonthValue());
        }

        // Made a different path for our tests
        String testPath = "";
        if (test) {
            testPath += "load test ";
        }

        // Made a different dayNow for our tests
        int  dayNow;
        if (test) {
            dayNow = 3;
        } else {
            dayNow = LocalDate.now().getDayOfMonth();
        }

        for (int day = 1; day <= dayNow; day++) {
            if (day < 10) {
                if (Files.exists(Paths.get("src/main/resources/" + testPath + "report/snack-vending-machines_"
                        + year + "-" + month + "-0" + day + ".csv"))) {
                    file = new File("src/main/resources/" + testPath + "report/snack-vending-machines_"
                            + year + "-" + month + "-0" + day + ".csv");
                    resultList.addAll(readMachineFile(file));
                }
            } else {
                if (Files.exists(Paths.get("src/main/resources/" + testPath + "report/snack-vending-machines_"
                        + year + "-" + month + "-" + day + ".csv"))) {
                    file = new File("src/main/resources/" + testPath + "report/snack-vending-machines_"
                            + year + "-" + month + "-" + day + ".csv");
                    resultList.addAll(readMachineFile(file));
                }
            }
        }

        return resultList;
    }

    private List<SnackVendingMachine> readMachineFile(File file) throws IOException {
        List<SnackVendingMachine> resultMachine = new LinkedList<>();
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
        boolean first = true;
        while (scanner.hasNextLine()) {
            List<String> values = null;
            if (first) {
                scanner.nextLine();
                first = false;

            } else {
                List<String> unprocessedValues = Arrays.stream(scanner.nextLine().split(", ")).toList();
                values = processWithLists(unprocessedValues);
            }
            if (values != null) {
                resultMachine.add(fillMachine(values));
            }

        }

        return resultMachine;
    }

    private List<String> processWithLists(List<String> unprocessedValues) {

        List<String> values = new LinkedList<>();

        boolean inList = false;
        String listStuffing = "";

        for (String value : unprocessedValues) {
            if (value.equals("null")) {
                values.add("");
            } else if (value.contains("[") && value.contains("]")) {
                if (value.equals("[]")) {
                    values.add("");
                } else {
                    int i = 1;
                    while (value.charAt(i) != ']') {
                        listStuffing += value.charAt(i);
                        i++;
                    }
                    values.add(listStuffing);
                    inList = false;
                }
            } else if (value.contains("[")) {
                inList = true;
                for (int i = 1; i < value.length(); i++) {
                    listStuffing += value.charAt(i);
                }
                listStuffing += ", ";
            } else if (inList) {
                if (value.contains("]")) {
                    int i = 0;
                    while (value.charAt(i) != ']') {
                        listStuffing += value.charAt(i);
                        i++;
                    }
                    values.add(listStuffing);
                    inList = false;
                } else {
                    listStuffing += value + ", ";
                }
            } else {
                values.add(value);
            }

        }
        return values;
    }

    private SnackVendingMachine fillMachine(List<String> values) {
        SnackVendingMachine machine = new SnackVendingMachine();
        int index = 0;
        for (String value: values) {
            switch (index) {
                case 0 -> machine.setId(Long.parseLong(value));
                case 1 -> machine.setAddress(value);
                case 2 -> machine.setLatitude(Double.parseDouble(value));
                case 3 -> machine.setLongitude(Double.parseDouble(value));
                case 4 -> machine.setCapacityOfCell(Integer.parseInt(value));
                case 5 -> machine.setQuantityOfCells(Integer.parseInt(value));
                case 6 -> machine.setModel(value);
                case 7 -> {
                    if (Objects.equals(value, "")) {
                        List<Long> snackIds = new LinkedList<>();
                        machine.setSnackIds(snackIds);
                    } else {
                        machine.setSnackIds(Arrays.stream(value.split(", "))
                                .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                    }
                }
                case 8 -> {
                    if (Objects.equals(value, "")) {
                        List<Long> soldSnackIds = new LinkedList<>();
                        machine.setSoldSnackIds(soldSnackIds);
                    } else {
                        machine.setSoldSnackIds(Arrays.stream(value.split(", "))
                                .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                    }
                }
            }

            index++;
        }
        return machine;
    }




}
