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
import java.util.LinkedList;
import java.util.List;

public class SnackToCSVTest {
    List<Snack> list = new LinkedList<>();
    SnackToCSV snackToCSV = new SnackToCSV();

    @BeforeEach
    void setUp(){

        Snack snack1 = new Snack(1L, 2L, "lays", "chips", 23, 32.2, "nestle");
        Snack snack2 = new Snack(2L, 2L, "snickers", "chocolate bar", 10, 11.1, "mars");
        Snack snack3 = new Snack(3L, 2L, "bounty", "chocolate bar", 12, 22.2, "nestle");
        list.add(snack1);
        list.add(snack2);
        list.add(snack3);
    }


    @Test
    void saveTodaySnacksReport() throws IOException {
        snackToCSV.saveTodaySnacksReport(list, true, false);
        String date = DateNow.getDateNow();


        Path expected = Paths.get("src/test/resources/report/expected.csv");
        Path actual = Paths.get("src/test/resources/report/actual.csv");
        byte[] file1 = Files.readAllBytes(expected);
        byte[] file2 = Files.readAllBytes(actual);
        Assertions.assertArrayEquals(file1, file2);

    }
    @Test
    void loadMonthSnacksReport() throws IOException, IndexOutOfBoundsException {
        String date = DateNow.getDateNow();
        List<Snack> result = snackToCSV.loadMonthSnacksReport(true);
        snackToCSV.saveTodaySnacksReport(result,false, true);
        String readerEx = String.format("%s%s%s%s%s", System.getProperty("user.dir"), File.separator, "src\\main\\resources\\snack load test report", File.separator, "expected.csv" );
        String readerRes = String.format("%s%s%s%s%s", System.getProperty("user.dir"), File.separator, "src\\main\\resources\\snack load test report", File.separator, "snack_2022-06-03.csv");

        Path expected = Paths.get("src/test/resources/parkingSpot_" + date + ".csv");
        Path actual = Paths.get("src/main/resources/parkingSpot.csv");
        byte[] file1 = Files.readAllBytes(expected);
        byte[] file2 = Files.readAllBytes(actual);
        Assertions.assertArrayEquals(file1, file2);

    }
}
