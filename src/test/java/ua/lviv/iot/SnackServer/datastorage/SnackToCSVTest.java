package ua.lviv.iot.SnackServer.datastorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.SnackServer.model.Snack;
import ua.lviv.iot.SnackServer.utils.DateNow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SnackToCSVTest {
    List<Snack> list = new ArrayList<>();
    HashMap<Long, Snack> snacks = new HashMap<>();
    SnackToCSV snackToCSV = new SnackToCSV();

    @BeforeEach
    void setUp(){

        Snack snack1 = new Snack(1L, 1L, "snickers medium", "chocolate bar", 4, 12.0, "mars");
        Snack snack2 = new Snack(2L, 1L, "snickers medium", "chocolate bar", 4, 12.0, "mars");
        Snack snack3 = new Snack(3L, 1L, "snickers medium", "chocolate bar", 4, 12.0, "mars");
        list.add(snack1);
        list.add(snack2);
        list.add(snack3);
    }


    @Test
    void saveTodaySnacksReport() throws IOException {
        String path = "test";

        File directory = new File("src/test/resources/report");
        if (!directory.exists()) {
            directory.mkdir();
        }
        snackToCSV.saveTodaySnacksReport(list, path, path);

        BufferedReader expectedBf = new BufferedReader(new FileReader("src/main/resources/report/snack_2022-06-19.csv"));
        BufferedReader actualBf = new BufferedReader(new FileReader("src/test/resources/report/snack_test.csv"));

        String line;
        while (((line = expectedBf.readLine()) != null) || (actualBf.readLine() != null)) {
            Assertions.assertEquals(line, actualBf.readLine());
        }




    }
    @Test
    void loadMonthSnacksReport() throws IOException, IndexOutOfBoundsException {
        String path = "test";
        String fileName = "load test";
        File directory = new File("src/test/resources/report");
        if (!directory.exists()) {
            directory.mkdir();
        }
        List<Snack> result = snackToCSV.loadMonthSnacksReport(19);
        snackToCSV.saveTodaySnacksReport(result, path, fileName);
        BufferedReader expectedBf = new BufferedReader(new FileReader("src/main/resources/report/snack_2022-06-19.csv"));
        BufferedReader actualBf = new BufferedReader(new FileReader("src/test/resources/report/snack_load test.csv"));

        String line;
        while (((line = expectedBf.readLine()) != null) || (actualBf.readLine() != null)) {
            Assertions.assertEquals(line, actualBf.readLine());
        }


    }
}
