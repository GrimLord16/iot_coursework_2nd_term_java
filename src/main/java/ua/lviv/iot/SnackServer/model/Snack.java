package ua.lviv.iot.SnackServer.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Snack {

    private Long snackId;

    private Long machineId;

    private String name;

    private String type;
    private int weight;
    private double priceInUSD;
    private String brand;


    public String getHeaders() {
        return "snackId, machineId, name, type, weight, priceInUSD, brand";
    }

    public String toCSV() {
        return snackId + ", " + machineId + ", " + name + ", " + type + ", " + weight + ", " + priceInUSD + ", " + brand;
    }
}
