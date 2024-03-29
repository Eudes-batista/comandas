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
import org.hibernate.annotations.Index;

@Entity
@Table(name = "cancelamento_mesa")
@EqualsAndHashCode(of = {"numero"})
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cancelamento implements Serializable {

    @Id
    @Column(name = "numero", length = 15, nullable = false)
    private String numero;

    @Index(name = "mesa", columnNames = {"mesa"})
    @Column(name = "mesa", length = 4, nullable = false)
    private String mesa;

    @Index(name = "comanda", columnNames = {"comanda"})
    @Column(name = "comanda", length = 4, nullable = false)
    private String comanda;

    @Index(name = "pedido", columnNames = {"pedido"})
    @Column(name = "pedido", length = 9, nullable = false)
    private String pedido;

    @Column(name = "item", length = 3, nullable = false)
    private String item;

    @Column(name = "produto", length = 20, nullable = false)
    private String produto;

    @Column(name = "quantidade", precision = 6, scale = 3, nullable = false)
    private Double quantidade;

    @Column(name = "data", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @Column(name = "garcom", length = 12, nullable = false)
    private String garcom;

    @Column(name = "responsavel", length = 50, nullable = false)
    private String responsavel;

    @Column(name = "codigo_motivo", nullable = false)
    private Integer codigoMotivo;

    @Column(name = "observacao_motivo", nullable = true, length = 200)
    private String observacaoMotivo;

    @Column(name = "foi_produzido", nullable = true)
    private Boolean foiProduzido;

    @Column(name = "observacao_destino", nullable = true, length = 200)
    private String observacaoDestino;

}
