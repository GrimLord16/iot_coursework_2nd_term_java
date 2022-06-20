package ua.lviv.iot.SnackServer.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.SnackServer.datastorage.SnackVendingMachineDataStorage;
import ua.lviv.iot.SnackServer.model.Snack;
import ua.lviv.iot.SnackServer.model.SnackVendingMachine;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Service
public class SnackVendingMachineService {
    @Autowired
    private SnackService snackService;
    @Autowired
    private SnackVendingMachineDataStorage snackVendingMachineDataStorage;

    private HashMap<Long, SnackVendingMachine> snackVendingMachines = new HashMap<>();

    public HashMap<Long, SnackVendingMachine> getAllMachines() {
        return snackVendingMachines;
    }

    public SnackVendingMachine getMachineById(Long id) {

        return snackVendingMachines.get(id);

    }

    public void sellSnack(Long id, Long nameId) {
        List<Long> newIds = snackVendingMachines.get(id).getSoldSnackIds();
        newIds.add(nameId);
        snackVendingMachines.get(id).setSoldSnackIds(newIds);
        snackVendingMachines.get(id).getSnackIds().remove(nameId);
    }

    public double getDailyRevenue(Long id) {
        if (LocalTime.now().getHour() < 19) {
             return 0;
        } else {
            double result = 0;
            for (Snack snack : snackService.getAllSnacks()) {
                if (snackVendingMachines.get(id).getSoldSnackIds().contains(snack.getSnackId())) {
                    result += snack.getPriceInUSD();
                }
            }
            return result;
        }
    }

    public List<Snack> getDailySoldSnacks(Long id) {
        if (LocalTime.now().getHour() < 19) {
            return null;
        } else {
            List<Snack> result = new LinkedList<>();
            for (Snack snack: snackService.getAllSnacks()) {
                if (snackVendingMachines.get(id).getSoldSnackIds().contains(snack.getSnackId())) {
                    result.add(snack);
                }
            }
            return result;
        }
    }

    public List<Snack> getSnacksInMachine(Long id) {
        List<Snack> result = new LinkedList<>();
        for (Snack snack: snackService.getAllSnacks()) {
            if (snackVendingMachines.get(id).getSnackIds().contains(snack.getSnackId())) {
                result.add(snack);
            }
        }
        return result;
    }

    public HashMap<String, Integer> getMenu(Long id) {
        HashMap<String, Integer> menu = new HashMap<>();
        List<Snack> snacks = getSnacksInMachine(id);
        if (getMachineById(id).getSnackIds() != null) {
            for (Snack snack: snacks) {
                menu.put(snack.getName(), getQuantityOfSnackByName(id, snack.getName()));
            }
        }
        return menu;
    }

    public void createMachine(SnackVendingMachine snackVendingMachine) {
        if (!snackVendingMachines.containsKey(snackVendingMachine.getId())) {
            this.snackVendingMachines.put(snackVendingMachine.getId(), snackVendingMachine);
        }
    }
    public void updateMachine(SnackVendingMachine snackVendingMachine) {
        if (snackVendingMachines.containsKey(snackVendingMachine.getId())) {
        this.snackVendingMachines.replace(snackVendingMachine.getId(), snackVendingMachine);
        }
    }

    public void deleteMachine(Long id) {
        if (snackVendingMachines.containsKey(id)) {
            this.snackVendingMachines.remove(id);
        }
    }

    public void addSnack(Long id, Snack snack) {
        if (snackVendingMachines.get(id).getQuantityOfCells() > getQuantityOfNames(id)) {
            if (this.snackVendingMachines.get(id).getCapacityOfCell() > getQuantityOfSnackByName(id, snack.getName())) {
                List<Long> newSnackIds = this.snackVendingMachines.get(id).getSnackIds();
                newSnackIds.add(snack.getSnackId());
                this.snackVendingMachines.get(id).setSnackIds(newSnackIds);
                snackService.addSnack(snack);
            }
        }
    }

    public int getQuantityOfNames(Long id) {
        HashMap<String, Integer> mapOfNames = new HashMap<>();
        List<Snack> snacks = getSnacksInMachine(id);
        for (Snack snack: snacks) {
            mapOfNames.put(snack.getName(), 1);
        }
        return mapOfNames.size();
    }

    public int getQuantityOfSnackByName(Long id, String name) {
        int snackQuantity = 0;
        if (getSnacksInMachine(id) != null) {
            List<Snack> snacks = getSnacksInMachine(id);
            for (Snack snack: snacks) {
                if (Objects.equals(snack.getName(), name)) {
                    snackQuantity += 1;
                }
            }
        }
        return snackQuantity;
    }

    @PreDestroy
    public void saveMachines() {
        List<SnackVendingMachine> list = this.snackVendingMachines.values().stream().toList();
        snackVendingMachineDataStorage.saveTodayMachinesReport(list, false, false);
    }

    @PostConstruct
    public void loadMachines() throws IOException {
        if (snackVendingMachineDataStorage.loadMonthMachineReport(false) != null) {
            List<SnackVendingMachine> result = snackVendingMachineDataStorage.loadMonthMachineReport(false);
            for (SnackVendingMachine machine: result) {
                this.snackVendingMachines.put(machine.getId(), machine);
            }
        }
    }
}
