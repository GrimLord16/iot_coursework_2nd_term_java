package ua.lviv.iot.SnackServer.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SnackVendingMachine {
    private Long id;
    private String address;
    private double latitude;
    private double longitude;
    private int capacityOfCell;
    private int quantityOfCells;
    private String model;

    public String takeHeaders() {
        return "id, address, latitude, longitude, capacityOfCell, quantityOfCells, model";
    }

    public String toCSV() {
        return id + ", " + address + ", " + latitude + ", " + longitude + ", "
                + capacityOfCell + ", " + quantityOfCells + ", " + model;
    }


}
