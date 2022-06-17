package ua.lviv.iot.SnackServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.lviv.iot.SnackServer.model.Snack;
import ua.lviv.iot.SnackServer.service.SnackService;

import java.util.List;

@RestController
public class SnackController {
    @Autowired
    private SnackService snackService;

    @GetMapping
    private List<Snack> getAllSnacks() {
        return snackService.getAllSnacks();
    }

    @GetMapping("/{snackId}")
    public Snack getSnackById(@PathVariable Long snackId) {
        return snackService.getSnackById(snackId);
    }

}
