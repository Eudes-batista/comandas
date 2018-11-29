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
   private Integer ITENS;
   private Double COMISSAO;

    public VendaGarcom() {
    }

    public VendaGarcom(String GARCOM, Double VENDAS) {
        this.GARCOM = GARCOM;
        this.VENDAS = VENDAS;
    }

    public VendaGarcom(String GARCOM, Double VENDAS, Integer ITENS) {
        this.GARCOM = GARCOM;
        this.VENDAS = VENDAS;
        this.ITENS = ITENS;
    }

    public VendaGarcom(String GARCOM, Double VENDAS, Integer ITENS, Double COMISSAO) {
        this.GARCOM = GARCOM;
        this.VENDAS = VENDAS;
        this.ITENS = ITENS;
        this.COMISSAO = COMISSAO;
    }
     
}
