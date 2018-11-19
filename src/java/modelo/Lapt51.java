package modelo;

import java.io.File;
import javax.faces.context.FacesContext;

public class Lapt51  implements java.io.Serializable {


     private String t51cdgrp;
     private String t51dsgrp;
     private Double t51modco;
     private Double t51depro;

    public Lapt51() {
    }

	
    public Lapt51(String t51cdgrp, String t51dsgrp) {
        this.t51cdgrp = t51cdgrp;
        this.t51dsgrp = t51dsgrp;
    }
    public Lapt51(String t51cdgrp, String t51dsgrp, Double t51modco, Double t51depro) {
       this.t51cdgrp = t51cdgrp;
       this.t51dsgrp = t51dsgrp;
       this.t51modco = t51modco;
       this.t51depro = t51depro;
    }
   
    public String getT51cdgrp() {
        return this.t51cdgrp;
    }
    
    public void setT51cdgrp(String t51cdgrp) {
        this.t51cdgrp = t51cdgrp;
    }
    public String getT51dsgrp() {
        return this.t51dsgrp;
    }
    
    public void setT51dsgrp(String t51dsgrp) {
        this.t51dsgrp = t51dsgrp;
    }
    public Double getT51modco() {
        return this.t51modco;
    }
    
    public void setT51modco(Double t51modco) {
        this.t51modco = t51modco;
    }
    public Double getT51depro() {
        return this.t51depro;
    }
    
    public void setT51depro(Double t51depro) {
        this.t51depro = t51depro;
    }

    
    public String getImagem(){
        String realPath = FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/imagens/");
        File file = new File(realPath);
        File files[] = file.listFiles();
        for(File arquivo : files){
            if(arquivo.getName().toUpperCase().contains(getT51dsgrp().toUpperCase()))
                  return arquivo.getName();
        }
        return "grupo.png";
    }

}


