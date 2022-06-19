package ua.lviv.iot.SnackServer.datastorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.SnackServer.model.Snack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class SnackDataStorageTest {
    List<Snack> list = new LinkedList<>();
    SnackDataStorage snackDataStorage = new SnackDataStorage();

    @BeforeEach
    void setUp(){
        Snack snack1 = new Snack(1L, "lays", "chips", 23, 32.2, "nestle");
        Snack snack2 = new Snack(2L, "snickers", "chocolate bar", 10, 11.1, "mars");
        Snack snack3 = new Snack(3L, "bounty", "chocolate bar", 12, 22.2, "nestle");
        list.add(snack1);
        list.add(snack2);
        list.add(snack3);
    }


    @Test
    void saveTodaySnacksReport() {
        snackDataStorage.saveTodaySnacksReport(list, true, false);

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
        String readerEx = String.format("%s%s%s%s%s", System.getProperty("user.dir"), File.separator,
                "src\\main\\resources\\snack save test report", File.separator, "expected.csv" );
        String readerRes = String.format("%s%s%s%s%s", System.getProperty("user.dir"), File.separator,
                "src\\main\\resources\\snack save test report", File.separator, "snack_" + year + "-" + month + "-" +
                        day + ".csv");

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
    void loadMonthSnacksReport() throws IOException, IndexOutOfBoundsException {
        List<Snack> result = snackDataStorage.loadMonthSnacksReport(true);
        snackDataStorage.saveTodaySnacksReport(result,false, true);
        String readerEx = String.format("%s%s%s%s%s", System.getProperty("user.dir"), File.separator, "src\\main\\resources\\snack load test report", File.separator, "expected.csv" );
        String readerRes = String.format("%s%s%s%s%s", System.getProperty("user.dir"), File.separator, "src\\main\\resources\\snack load test report", File.separator, "snack_2022-06-03.csv");

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
