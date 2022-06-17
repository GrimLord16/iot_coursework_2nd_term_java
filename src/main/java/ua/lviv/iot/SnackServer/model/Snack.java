package ua.lviv.iot.SnackServer.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Snack {

    private Long snackId;

    private String name;

    private String type;
    private int weight;
    private double priceInUSD;
    private String brand;


    public String getHeaders() {
        return "snackId, name, type, weight, priceInUSD, brand";
    }

    public String toCSV() {
        return snackId + ", " + name + ", " + type + ", " + weight + ", " + priceInUSD + ", " + brand;
    }
}
