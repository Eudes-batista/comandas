package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "transferencia_mesa")
@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transferencia implements Serializable {

    @Id
    @Column(name = "numero", length = 15, nullable = false)
    private String numero;

    @Column(name = "pedido_destino", length = 9, nullable = false)
    private String pedidoDestino;

    @Column(name = "pedido_origem", length = 9, nullable = false)
    private String pedidoOrigem;

    @Column(name = "item", length = 3, nullable = false)
    private String item;

    @Column(name = "produto", length = 20, nullable = false)
    private String produto;

    @Column(name = "quantidade", precision = 6, scale = 3, nullable = false)
    private Double quantidade;

    @Column(name = "data", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @Column(name = "mesa_origem", length = 4, nullable = false)
    private String mesaOrigem;

    @Column(name = "mesa_destino", length = 4, nullable = false)
    private String mesaDestino;

    @Column(name = "comanda_origem", length = 4, nullable = false)
    private String comandaOrigem;

    @Column(name = "comanda_destino", length = 4, nullable = false)
    private String comandaDestino;

    @Column(name = "garcom", length = 12, nullable = false)
    private String garcom;

    @Column(name = "responsavel", length = 50, nullable = false)
    private String responsavel;

}
