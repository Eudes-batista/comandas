package modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = {"item","pedido"})
@ToString
@Entity
@Table(name="item_acompanhamento")
public class ItemAcompanhamento implements Serializable{
    
    @Id
    @Column(name="item",length = 3,nullable = false)
    private Integer item;
    
    @Id
    @Column(name="pedido",length = 9,nullable = false)
    private String pedido; 
    
    @Id
    @Column(name="numero_item",length = 2,nullable = false)
    private String numeroItem;
        
    @Column(name="acompanhamento",length = 50,nullable = false)
    private String acompanhamento; 
    
    @Column(name="status",nullable = true,length = 1)    
    private String status;

    public ItemAcompanhamento() {
    }

    public ItemAcompanhamento(String acompanhamento) {
        this.acompanhamento = acompanhamento;
    }

    public ItemAcompanhamento(Integer item, String pedido) {
        this.item = item;
        this.pedido = pedido;
    }    
    
    public ItemAcompanhamento(Integer item, String pedido, String numeroItem) {
        this.item = item;
        this.pedido = pedido;
        this.numeroItem = numeroItem;
    }
    
}
