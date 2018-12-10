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
   private Double ITENS;
   private Double COMISSAO;
   private Double META;
   private Double VALOR_A_ALCANCAR;

    public VendaGarcom() {
    }

    public VendaGarcom(String GARCOM, Double VENDAS) {
        this.GARCOM = GARCOM;
        this.VENDAS = VENDAS;
    }

    public VendaGarcom(String GARCOM, Double VENDAS, Double ITENS, Double COMISSAO) {
        this.GARCOM = GARCOM;
        this.VENDAS = VENDAS;
        this.ITENS = ITENS;
        this.COMISSAO = COMISSAO;
    }

    public VendaGarcom(String GARCOM, Double VENDAS, Double ITENS) {
        this.GARCOM = GARCOM;
        this.VENDAS = VENDAS;
        this.ITENS = ITENS;
    }

    public VendaGarcom(String GARCOM, Double VENDAS, Double ITENS, Double COMISSAO, Double META, Double VALOR_A_ALCANCAR) {
        this.GARCOM = GARCOM;
        this.VENDAS = VENDAS;
        this.ITENS = ITENS;
        this.COMISSAO = COMISSAO;
        this.META = META;
        this.VALOR_A_ALCANCAR = VALOR_A_ALCANCAR;
    }

    
}
