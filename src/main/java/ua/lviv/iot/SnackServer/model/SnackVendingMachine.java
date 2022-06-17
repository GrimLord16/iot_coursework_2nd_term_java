package ua.lviv.iot.SnackServer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SnackVendingMachine {
    private Long id;
    private String address;
    private double latitude;
    private double longitude;
    private int capacityOfCell;
    private int quantityOfCells;
    private String model;
    List<Long> snackIds = new LinkedList<>();
    List<Long> soldSnackIds = new LinkedList<>();

    public String getHeaders() {
        return "id, address, latitude, longitude, capacityOfCell, quantityOfCells, model, snackIds, soldSnackIds";
    }

    public String toCSV() {
        return id + ", " + address + ", " + latitude + ", " + longitude + ", "
                + capacityOfCell + ", " + quantityOfCells + ", " + model + ", " + snackIds + ", " + soldSnackIds ;
    }


}
