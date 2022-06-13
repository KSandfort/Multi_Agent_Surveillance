package gui;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableDataModel {

    // Variables
    private String name;
    private Double value;

    /**
     * Constructor
     * @param name
     * @param value
     */
    public TableDataModel(String name, Double value) {
        this.name = name;
        this.value = value;
    }
}
