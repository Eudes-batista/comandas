
package modelo.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Garcom implements Serializable{

    private String label;
    private double[] data;
    private int borderWidth;
    private String backgroundColor;

    public Garcom() {
    }

    public Garcom(String label, double[] data, int borderWidth, String backgroundColor) {
        this.label = label;
        this.data = data;
        this.borderWidth = borderWidth;
        this.backgroundColor = backgroundColor;
    }
    
    
}
