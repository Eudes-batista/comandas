/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table (name="acompanhamento")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Acompanhamento implements Serializable{
 
    @Id
    @Column(name="codigo", nullable = false)
    private Integer codigo;
    
    @ManyToOne
    @JoinColumn(name="grupo", nullable = false)
    private GrupoAcompanhamento grupo; 
    
    @Column(name="nome",nullable = false, length = 50 )
    private String nome;
        
}
