/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GarcomGrafico implements Serializable{
    
    private String label;
    private double data[];
    private int borderWidth;
    private String backgroundColor;
    private String borderColor;

    public GarcomGrafico() {
    }

    public GarcomGrafico(String label, double[] data, int borderWidth, String backgroundColor, String borderColor) {
        this.label = label;
        this.data = data;
        this.borderWidth = borderWidth;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    
    
}
