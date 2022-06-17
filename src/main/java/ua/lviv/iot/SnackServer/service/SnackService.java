package ua.lviv.iot.SnackServer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.SnackServer.datastorage.SnackDataStorage;
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
    private SnackDataStorage snackDataStorage;

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
    public void saveSnacksToFile() {
        List<Snack> list = this.snacks.values().stream().toList();
        snackDataStorage.saveTodaySnacksReport(list, false, false);
    }

    @PostConstruct
    public void loadMachines() throws IOException {
        if(snackDataStorage.loadMonthSnacksReport(false) != null){
            List<Snack> result = snackDataStorage.loadMonthSnacksReport(false);
            for(Snack snack: result){
                this.snacks.put(snack.getSnackId(), snack);
            }
        }
    }
}
