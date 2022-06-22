package ua.lviv.iot.SnackServer.service;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.lviv.iot.SnackServer.datastorage.SnackVendingMachineToCSV;
import ua.lviv.iot.SnackServer.model.Snack;
import ua.lviv.iot.SnackServer.model.SnackVendingMachine;
import ua.lviv.iot.SnackServer.utils.DateNow;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


@Service
public class SnackVendingMachineService {
    @Autowired
    private SnackService snackService;
    @Autowired
    private SnackVendingMachineToCSV snackVendingMachineToCSV;

    private final HashMap<Long, SnackVendingMachine> snackVendingMachines = new HashMap<>();
    private final List<Long> soldSnackIds = new LinkedList<>();
    private final List<Long> snackIds = new LinkedList<>();

    private Long snackId = 1L;

    //Methods that manipulate with machines
    @SuppressFBWarnings
    public HashMap<Long, SnackVendingMachine> getAllMachines() {
        return snackVendingMachines;
    }

    public SnackVendingMachine getMachineById(Long id) {
        if (snackVendingMachines.containsKey(id)) {
            return snackVendingMachines.get(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

    }

    public void createMachine(SnackVendingMachine snackVendingMachine) {
        if (!snackVendingMachines.containsKey(snackVendingMachine.getId())) {
            this.snackVendingMachines.put(snackVendingMachine.getId(), snackVendingMachine);
        }
    }

    public void updateMachine(SnackVendingMachine snackVendingMachine) {
        if (snackVendingMachines.containsKey(snackVendingMachine.getId())) {
            this.snackVendingMachines.replace(snackVendingMachine.getId(), snackVendingMachine);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    public void deleteMachine(Long id) {
        if (snackVendingMachines.containsKey(id)) {
            this.snackVendingMachines.remove(id);
        }
    }


    //Methods to manipulate snacks in machine

    public double getDailyRevenue(Long id) {
        if (LocalTime.now().getHour() < 19) {
             return 0;
        } else {
            double result = 0;
            for (Snack snack : snackService.getAllSnacks()) {
                if (Objects.equals(snack.getMachineId(), id)) {
                    if (soldSnackIds.contains(snack.getSnackId())) {
                        result += snack.getPriceInUSD();
                    }
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
                if (Objects.equals(snack.getMachineId(), id)) {
                    if (soldSnackIds.contains(snack.getSnackId())) {
                        result.add(snack);
                    }
                }
            }
            return result;
        }
    }


    public HashMap<String, Integer> getMenu(Long id) {
        HashMap<String, Integer> menu = new HashMap<>();
        if (getSnacksInMachine(id) != null) {
            for (Snack snack: getSnacksInMachine(id)) {
                menu.put(snack.getName(), getQuantityOfSnackByName(id, snack.getName()));
            }
        }
        return menu;
    }


    public void sellSnack(Long id, Long snackId) {
        if (getSnackIdsInMachine(id).contains(snackId)) {
            snackService.getSnackById(snackId).setSold(true);
            snackIds.remove(snackId);
            soldSnackIds.add(snackId);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }


    public void addSnack(Long id, Snack snack) {
        if (snackIds.contains(snack.getSnackId()) || soldSnackIds.contains(snack.getSnackId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "this id is already used, please use another id"
            );
        } else {
            if ((snackVendingMachines.get(id).getQuantityOfCells() > getQuantityOfNames(id)) || (snackListToNameList(getSnacksInMachine(id)).contains(snack.getName()))) {
                if (this.snackVendingMachines.get(id).getCapacityOfCell() > getQuantityOfSnackByName(id, snack.getName())) {
                    snack.setMachineId(id);
                    snack.setSold(false);
                    snack.setSnackId(snackId);
                    snackId += 1;
                    snackService.addSnack(snack);
                    snackIds.add(snack.getSnackId());
                    throw new ResponseStatusException(
                            HttpStatus.CREATED, "entity is created"
                    );
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT, "The Cell is full, please use another type of snack"
                    );
                }
            } else {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Snack vending machine is full"
                );
            }
        }
    }

    public List<Snack> getSnacksInMachine(Long id) {
        List<Snack> snacks = new LinkedList<>();
        for (Snack snack: snackService.getAllSnacks()) {
            if (Objects.equals(snack.getMachineId(), id)) {
                snacks.add(snack);
            }
        }
        return snacks;
    }

    //Supplementary methods to help
    public List<String> snackListToNameList(List<Snack> snacks) {
        List<String> names = new LinkedList<>();
        for (Snack snack: snacks) {
            names.add(snack.getName());
        }
        return names;
    }

    public int getQuantityOfNames(Long id) {
        HashMap<String, Integer> mapOfNames = new HashMap<>();
        int result = 0;
        List<Snack> snacks = getSnacksInMachine(id);
        if (snacks != null) {
            for (Snack snack: snacks) {
                mapOfNames.put(snack.getName(), 1);
            }
            result = mapOfNames.size();
        }
        return result;
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

    public List<Long> getSnackIdsInMachine(Long id) {
        List<Long> snackIds = new LinkedList<>();
        for (Snack snack: getSnacksInMachine(id)) {
            snackIds.add(snack.getSnackId());
        }
        return snackIds;
    }


    //Methods that happen at the beginning and at the end of program

    @PreDestroy
    public void saveMachines() throws IOException {
        String path = "main";
        String date = DateNow.getDateNow();
        List<SnackVendingMachine> list = this.snackVendingMachines.values().stream().toList();
        snackVendingMachineToCSV.saveTodayMachinesReport(list, path, date);
    }

    @PostConstruct
    public void loadMachines() throws IOException {
        int day = LocalDate.now().getDayOfMonth();
        List<SnackVendingMachine> result = snackVendingMachineToCSV.loadMonthMachineReport(day);
        if (result != null) {
            for (SnackVendingMachine machine: result) {
                this.snackVendingMachines.put(machine.getId(), machine);
            }
        }
    }
    @PostConstruct
    public void initializeSnackId() {

        for (int i = 0; i < snackService.getAllSnacks().size(); i++) {
            snackId += 1;
        }

    }
}
