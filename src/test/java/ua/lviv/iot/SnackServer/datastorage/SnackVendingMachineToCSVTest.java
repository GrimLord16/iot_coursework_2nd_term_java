package ua.lviv.iot.SnackServer.datastorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.SnackServer.model.SnackVendingMachine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SnackVendingMachineToCSVTest {
    List<SnackVendingMachine> list = new LinkedList<>();
    SnackVendingMachineToCSV machineToCSV = new SnackVendingMachineToCSV();

    @BeforeEach
    void setUp() {
        SnackVendingMachine machine1 = new SnackVendingMachine(1L, "Terniv 17",
                37.0, 26.0, 4, 4, "bosch");
        SnackVendingMachine machine2 = new SnackVendingMachine(2L, "Terniv 17",
                37.0, 26.0, 4, 4, "bosch");


        list.add(machine1);
        list.add(machine2);
    }

    @Test
    void saveTodayMachinesReport() throws IOException {
        String path = "test";

        File directory = new File("src/test/resources/report");
        if (!directory.exists()) {
            directory.mkdir();
        }
        machineToCSV.saveTodayMachinesReport(list, path, path);

        BufferedReader expectedBf = new BufferedReader(new FileReader("src/main/resources/report/snack-vending-machines_2022-06-15.csv"));
        BufferedReader actualBf = new BufferedReader(new FileReader("src/test/resources/report/snack-vending-machines_test.csv"));

        String line;
        while (((line = expectedBf.readLine()) != null) || (actualBf.readLine() != null)) {
            Assertions.assertEquals(line, actualBf.readLine());
        }

    }

    @Test
    void loadMonthMachinesReport() throws IOException {

        File directory = new File("src/test/resources/report");
        if (!directory.exists()) {
            directory.mkdir();
        }

        String path = "test";
        String fileName = "load test";
        List<SnackVendingMachine> result = machineToCSV.loadMonthMachineReport(15);
        machineToCSV.saveTodayMachinesReport(result, path, fileName);
        BufferedReader expectedBf = new BufferedReader(new FileReader("src/main/resources/report/snack-vending-machines_2022-06-15.csv"));
        BufferedReader actualBf = new BufferedReader(new FileReader("src/test/resources/report/snack-vending-machines_load test.csv"));

        String line;
        while (((line = expectedBf.readLine()) != null) || (actualBf.readLine() != null)) {
            Assertions.assertEquals(line, actualBf.readLine());
        }

    }

}
