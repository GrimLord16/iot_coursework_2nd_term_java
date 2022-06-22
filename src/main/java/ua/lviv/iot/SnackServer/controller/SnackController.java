package ua.lviv.iot.SnackServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.SnackServer.model.Snack;
import ua.lviv.iot.SnackServer.service.SnackService;
import ua.lviv.iot.SnackServer.service.SnackVendingMachineService;

import java.util.List;

@RestController
public class SnackController {
    @Autowired
    private SnackService snackService;
    @Autowired
    private SnackVendingMachineService snackVendingMachineService;

    @GetMapping("/snacks")
    private List<Snack> getAllSnacks() {
        return snackService.getAllSnacks();
    }

    @GetMapping("/snacks/{snackId}")
    public Snack getSnackById(@PathVariable Long snackId) {
        return snackService.getSnackById(snackId);
    }

    @DeleteMapping("/snack-vending-machines/{id}/snacks/{snackId}")
    public void sellSnack(@PathVariable Long id, @PathVariable Long snackId) {
        snackVendingMachineService.sellSnack(id, snackId);
    }

    @PostMapping("/snack-vending-machines/{id}/snacks")
    public void addSnack(@PathVariable Long id, @RequestBody Snack snack) {
        snackVendingMachineService.addSnack(id, snack);
    }

}
