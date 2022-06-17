package ua.lviv.iot.SnackServer.datastorage;

import org.springframework.stereotype.Component;
import ua.lviv.iot.SnackServer.model.Snack;

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
public class SnackDataStorage {
    public void saveTodaySnacksReport(List<Snack> snacks, boolean test, boolean loadTest) {

        // Made a different dayNow for our load tests
        int  dayNow;
        if(loadTest){
            dayNow = 3;
        }
        else{
            dayNow = LocalDate.now().getDayOfMonth();
        }

        // Made a different test paths for our load and save tests
        String testPath = "";
        if(test) {
            testPath += "snack save test ";
        } else if (loadTest){
            testPath += "snack load test ";
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


        File file = new File("src/main/resources/" + testPath + "report/snack_" + year + "-"
                + month + "-" + day + ".csv");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(snacks.get(0).getHeaders() + "\n");
            for (Snack snack : snacks) {
                writer.write(snack.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Snack> loadMonthSnacksReport(boolean test) throws IOException {
        List<Snack> resultList = new LinkedList<>();
        File file;

        String year = Integer.toString(LocalDate.now().getYear());
        String month;

        String testPath = "";
        if(test){
            testPath += "snack load test ";
        }

        // Made a different dayNow for our tests
        int  dayNow;
        if(test){
            dayNow = 3;
        }
        else{
            dayNow = LocalDate.now().getDayOfMonth();
        }

        if (LocalDate.now().getMonthValue() < 10) {
            month = "0" + LocalDate.now().getMonthValue();
        } else {
            month = Integer.toString(LocalDate.now().getMonthValue());
        }

        for (int day = 1; day <= dayNow; day++) {
            if (day < 10) {
                if (Files.exists(Paths.get("src/main/resources/" + testPath + "report/snack_" + year + "-" + month + "-0" + day + ".csv"))) {
                    file = new File("src/main/resources/" + testPath + "report/snack_" + year + "-" + month + "-0" + day + ".csv");
                    resultList.addAll(readSnackFile(file));
                }
            } else {
                if (Files.exists(Paths.get("src/main/resources/" + testPath + "report/snack_" + year + "-" + month + "-" + day + ".csv"))) {
                    file = new File("src/main/resources/" + testPath + "report/snack_" + year + "-" + month + "-" + day + ".csv");
                    resultList.addAll(readSnackFile(file));
                }
            }
        }

        return resultList;
    }

    private List<Snack> readSnackFile(File file) throws IOException {
        List<Snack> resultSnack = new LinkedList<>();
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
            if(values!=null)
                resultSnack.add(fillSnack(values));

        }
        return resultSnack;
    }

    private Snack fillSnack(List<String> values) {
        Snack snack = new Snack();
        int index = 0;
        for (String value : values) {
            switch (index) {
                case 0 -> snack.setSnackId(Long.parseLong(value));
                case 1 -> snack.setName(value);
                case 2 -> snack.setType(value);
                case 3 -> snack.setWeight(Integer.parseInt(value));
                case 4 -> snack.setPriceInUSD(Float.parseFloat(value));
                case 5 -> snack.setBrand(value);
            }
            index++;
        }
        return snack;
    }
}


