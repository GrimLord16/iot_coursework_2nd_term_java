package ua.lviv.iot.SnackServer.datastorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.SnackServer.model.SnackVendingMachine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class SnackVendingMachineDataStorageTest {
    List<SnackVendingMachine> list = new LinkedList<>();
    SnackVendingMachineDataStorage machineDataStorage = new SnackVendingMachineDataStorage();

    @BeforeEach
    void setUp() {
        List<Long> snackIds1 = new LinkedList<>();
        List<Long> snackSoldIds1 = new LinkedList<>();
        snackIds1.add(1L);
        snackIds1.add(2L);
        snackSoldIds1.add(3L);
        snackIds1.add(4L);
        SnackVendingMachine machine1 = new SnackVendingMachine(1L, "Herbs Street",
                23.3, 23.3, 4, 4, "dorcel", snackIds1, snackSoldIds1);

        List<Long> snackIds2 = new LinkedList<>();
        List<Long> snackSoldIds2 = new LinkedList<>();
        snackIds2.add(5L);
        snackIds2.add(6L);
        snackIds2.add(7L);
        snackSoldIds2.add(8L);
        SnackVendingMachine machine2 = new SnackVendingMachine(2L, "Dirt Street",
                26.4, 43.4, 4, 4, "samsung", snackIds2, snackSoldIds2);

        List<Long> snackIds3 = new LinkedList<>();
        List<Long> snackSoldIds3 = new LinkedList<>();
        snackIds3.add(9L);
        snackSoldIds3.add(10L);
        snackIds3.add(11L);
        snackIds3.add(12L);
        SnackVendingMachine machine3 = new SnackVendingMachine(3L, "Pond Street",
                54.4, 67.5, 4, 4, "samsung", snackIds3, snackSoldIds3);

        list.add(machine1);
        list.add(machine2);
        list.add(machine3);
    }

    @Test
    void saveTodayMachinesReport() {
        machineDataStorage.saveTodayMachinesReport(list, true, false);
        String year = Integer.toString(LocalDate.now().getYear());
        String month;
        String day;

        if (LocalDate.now().getMonthValue() < 10) {
            month = "0" + LocalDate.now().getMonthValue();
        } else {
            month = Integer.toString(LocalDate.now().getMonthValue());
        }

        if (LocalDate.now().getDayOfMonth() < 10) {
            day = "0" + LocalDate.now().getDayOfMonth();
        } else {
            day = Integer.toString(LocalDate.now().getDayOfMonth());
        }
        String readerEx = String.format("%s%s%s%s%s", System.getProperty("user.dir"), File.separator, "src\\main\\resources\\save test report", File.separator, "expected.csv" );
        String readerRes = String.format("%s%s%s%s%s", System.getProperty("user.dir"), File.separator, "src\\main\\resources\\save test report", File.separator, "snack-vending-machines_" + year + "-" + month + "-" +
                day +  ".csv");

        try {
            BufferedReader readerResult = new BufferedReader(new FileReader(readerRes));
            BufferedReader readerExpected = new BufferedReader(new FileReader(readerEx));
            Assertions.assertEquals(readerExpected.readLine(), readerResult.readLine());
            Assertions.assertNotEquals(null, readerResult.readLine());
            Assertions.assertNotEquals(readerExpected.readLine(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void loadMonthMachinesReport() throws IOException {
        List<SnackVendingMachine> result = machineDataStorage.loadMonthMachineReport(true);
        machineDataStorage.saveTodayMachinesReport(result,false, true);
        String readerEx = String.format("%s%s%s%s%s", System.getProperty("user.dir"), File.separator,
                "src\\main\\resources\\load test report", File.separator, "expected.csv" );
        String readerRes = String.format("%s%s%s%s%s", System.getProperty("user.dir"), File.separator,
                "src\\main\\resources\\load test report", File.separator, "snack-vending-machines_2022-06-03.csv");

        try {
            BufferedReader readerResult = new BufferedReader(new FileReader(readerRes));
            BufferedReader readerExpected = new BufferedReader(new FileReader(readerEx));
            Assertions.assertEquals(readerExpected.readLine(), readerResult.readLine());
            Assertions.assertNotEquals(null, readerResult.readLine());
            Assertions.assertNotEquals(readerExpected.readLine(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
