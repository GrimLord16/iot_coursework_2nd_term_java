package ua.lviv.iot.SnackServer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

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
    private List<Long> snackIds = new LinkedList<>();
    private List<Long> soldSnackIds = new LinkedList<>();

    public String getHeaders() {
        return "id, address, latitude, longitude, capacityOfCell, quantityOfCells, model, snackIds, soldSnackIds";
    }

    public String toCSV() {
        return id + ", " + address + ", " + latitude + ", " + longitude + ", "
                + capacityOfCell + ", " + quantityOfCells + ", " + model + ", " + snackIds + ", " + soldSnackIds;
    }


}
