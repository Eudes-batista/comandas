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
public class VendaGarcom implements Serializable{
    
   private String GARCOM;
   private Double VENDAS;

    public VendaGarcom() {
    }

    public VendaGarcom(String GARCOM, Double VENDAS) {
        this.GARCOM = GARCOM;
        this.VENDAS = VENDAS;
    }

   
}
