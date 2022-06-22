package ua.lviv.iot.SnackServer.datastorage;

import org.springframework.stereotype.Component;
import ua.lviv.iot.SnackServer.model.SnackVendingMachine;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Component
public class SnackVendingMachineToCSV {
    public void saveTodayMachinesReport(List<SnackVendingMachine> machines, String path, String fileName) throws IOException {
        // Made a different dayNow for our load tests


        File file = new File("src/" + path + "/resources/report/snack-vending-machines_" + fileName + ".csv");

        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        writer.write(machines.get(0).takeHeaders() + "\n");
        for (SnackVendingMachine snackVendingMachine: machines) {
            writer.write(snackVendingMachine.toCSV() + "\n");
        }
        writer.close();


    }

    public List<SnackVendingMachine> loadMonthMachineReport(int dayNow) throws IOException {
        List<SnackVendingMachine> resultList = new LinkedList<>();
        File file;



        String year = Integer.toString(LocalDate.now().getYear());
        String month;

        if (LocalDate.now().getMonthValue() < 10) {
            month = "0" + LocalDate.now().getMonthValue();
        } else {
            month = Integer.toString(LocalDate.now().getMonthValue());
        }


        for (int day = 1; day <= dayNow; day++) {
            if (day < 10) {
                if (Files.exists(Paths.get("src/main/resources/report/snack-vending-machines_"
                        + year + "-" + month + "-0" + day + ".csv"))) {
                    file = new File("src/main/resources/report/snack-vending-machines_"
                            + year + "-" + month + "-0" + day + ".csv");
                    resultList.addAll(readMachineFile(file));
                }
            } else {
                if (Files.exists(Paths.get("src/main/resources/report/snack-vending-machines_"
                        + year + "-" + month + "-" + day + ".csv"))) {
                    file = new File("src/main/resources/report/snack-vending-machines_"
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
                values = Arrays.stream(scanner.nextLine().split(", ")).toList();

            }
            if (values != null) {
                resultMachine.add(fillMachine(values));
            }

        }

        return resultMachine;
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

            }

            index++;
        }
        return machine;
    }




}
