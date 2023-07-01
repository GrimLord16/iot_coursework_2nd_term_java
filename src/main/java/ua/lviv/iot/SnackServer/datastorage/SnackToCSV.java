package ua.lviv.iot.SnackServer.datastorage;

import org.springframework.stereotype.Component;
import ua.lviv.iot.SnackServer.model.Snack;


import java.io.File;
import java.io.Writer;
import java.io.FileOutputStream;
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

@Component
public class SnackToCSV {
    public void saveTodaySnacksReport(List<Snack> snacks, String path, String fileName) throws IOException {

        File file = new File("src/" + path + "/resources/report/snack_" + fileName + ".csv");
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

        writer.write(snacks.get(0).takeHeaders() + "\n");
        for (Snack snack : snacks) {
            writer.write(snack.toCSV() + "\n");
        }
        writer.close();

    }

    public List<Snack> loadMonthSnacksReport(int dayNow) throws IOException {
        List<Snack> resultList = new LinkedList<>();
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
                if (Files.exists(Paths.get("src/main/resources/report/snack_" + year + "-" + month
                        + "-0" + day + ".csv"))) {
                    file = new File("src/main/resources/report/snack_" + year + "-" + month
                            + "-0" + day + ".csv");
                    resultList.addAll(readSnackFile(file));
                }
            } else {
                if (Files.exists(Paths.get("src/main/resources/report/snack_" + year + "-" + month
                        + "-" + day + ".csv"))) {
                    file = new File("src/main/resources/report/snack_" + year + "-" + month
                            + "-" + day + ".csv");
                    resultList.addAll(readSnackFile(file));
                }
            }
        }

        return resultList;
    }

    private List<Snack> readSnackFile(File file) throws IOException {
        List<Snack> resultSnack = new LinkedList<>();
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
        boolean firstLine = true;
        while (scanner.hasNextLine()) {
            List<String> values = null;
            if (firstLine) {
                scanner.nextLine();
                firstLine = false;

            } else {
                values = Arrays.stream(scanner.nextLine().split(", ")).toList();

            }
            if (values != null) {
                resultSnack.add(fillSnack(values));
            }
        }
        return resultSnack;
    }

    private Snack fillSnack(List<String> values) {

        return new Snack(Long.parseLong(values.get(0)), Long.parseLong(values.get(1)), values.get(2),
                values.get(3), Integer.parseInt(values.get(4)), Double.parseDouble(values.get(5)), values.get(6),
                Boolean.parseBoolean(values.get(7)), Integer.parseInt(values.get(8)));
    }
}


