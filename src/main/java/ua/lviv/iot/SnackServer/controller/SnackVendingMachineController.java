package ua.lviv.iot.SnackServer.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.SnackServer.service.SnackVendingMachineService;
import ua.lviv.iot.SnackServer.model.Snack;
import ua.lviv.iot.SnackServer.model.SnackVendingMachine;


import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@RestController
@RequestMapping("/snack-vending-machines")
public class SnackVendingMachineController {
    @Autowired
    private SnackVendingMachineService snackVendingMachineService;

    //Methods to work with Snacks Vending Machine

    @GetMapping
    public HashMap<Long, SnackVendingMachine> getAllMachines(){
        return snackVendingMachineService.getAllMachines();
    }

    @GetMapping("/{id}")
    public SnackVendingMachine getMachineById(@PathVariable Long id){
        return snackVendingMachineService.getMachineById(id);
    }
    @PostMapping
    public void createMachine(@RequestBody SnackVendingMachine snackVendingMachine){
        snackVendingMachineService.createMachine(snackVendingMachine);
    }

    @PutMapping
    public void updateMachine(@RequestBody SnackVendingMachine snackVendingMachine){
        snackVendingMachineService.updateMachine(snackVendingMachine);
    }

    @DeleteMapping("/{id}")
    public void deleteMachineById(@PathVariable Long id){
        snackVendingMachineService.deleteMachine(id);
    }

    // Other methods

    @GetMapping("/{id}/sold-snacks")
    public List<Snack> getDailySoldSnacks(@PathVariable Long id){
        return snackVendingMachineService.getDailySoldSnacks(id);
    }

    @GetMapping("/{id}/revenue")
    public double getDailyRevenue(@PathVariable Long id){
        return snackVendingMachineService.getDailyRevenue(id);
    }



    @GetMapping("/{id}/menu")
    public HashMap<String, Integer> getMenu(@PathVariable Long id){
        return snackVendingMachineService.getMenu(id);

    }

    @GetMapping("/{id}/snacks")
    public List<Snack> getSnacksInMachine(@PathVariable Long id){
        return snackVendingMachineService.getSnacksInMachine(id);
    }

    @DeleteMapping("/{id}/snacks/{snackId}")
    public void sellSnack(@PathVariable Long id,@PathVariable Long snackId){
        snackVendingMachineService.sellSnack(id,snackId);
    }

    @PostMapping("/{id}/snacks")
    public void addSnack(@PathVariable Long id,@RequestBody Snack snack){
        snackVendingMachineService.addSnack(id, snack);
    }

}
