package ua.lviv.iot.SnackServer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.SnackServer.datastorage.SnackToCSV;
import ua.lviv.iot.SnackServer.model.Snack;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
@Service
public class SnackService {
    @Autowired
    private SnackToCSV snackToCSV;

    private final HashMap<Long, Snack> snacks = new HashMap<>();

    public List<Snack> getAllSnacks() {
        return new LinkedList<>(this.snacks.values());
    }

    public Snack getSnackById(Long snackId) {
        return this.snacks.get(snackId);
    }

    public void addSnack(Snack snack) {
        snacks.put(snack.getSnackId(), snack);
    }

    @PreDestroy
    public void saveSnacksToFile() throws IOException {
        List<Snack> list = this.snacks.values().stream().toList();
        snackToCSV.saveTodaySnacksReport(list, "main/resources/report", "");
    }

    @PostConstruct
    public void loadSnacks() throws IOException {
        List<Snack> result = snackToCSV.loadMonthSnacksReport(false);
        if (result != null) {
            for (Snack snack: result) {
                this.snacks.put(snack.getSnackId(), snack);
            }
        }
    }
}
