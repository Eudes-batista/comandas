package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Table(name = "ESPELHO_COMANDA")
@Getter
@Setter
public class EspelhoComanda implements Serializable {

    @Id
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "pedido", length = 6, nullable = false)
    private String pedido;

    @Column(name = "numero_item", length = 8, nullable = false)
    private String numeroItem;

    @Column(name = "comanda", length = 4, nullable = false)
    private String comanda;

    @Column(name = "referencia", length = 20, nullable = false)
    private String referencia;

    @Column(name = "quantidade", precision = 6, scale = 3, nullable = false)
    private Double quantidade;

    @Column(name = "data", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @Column(name = "vendedor", length = 12, nullable = false)
    private String vendedor;

    @Column(name = "observacao", length = 400, nullable = true)
    private String observacao;

    @Column(name = "mesa", length = 4, nullable = false)
    private String mesa;

    @Column(name = "status", length = 1, nullable = true)
    private String status;

    @Column(name = "impressao", length = 1, nullable = true)
    private String impressao;

    @Column(name = "porcentagem", precision = 6, scale = 2, nullable = true)
    private Double porcentagem;

    @Column(name = "valor_porcentagem", precision = 6, scale = 3, nullable = true)
    private Double valorPorcentagem;

    @Column(name = "status_item", length = 1, columnDefinition = "varchar(1) not null")
    private String statusItem;

    @Column(name = "data_preconta", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPreconta;

    @Column(name = "pessoas_mesa", length = 3, columnDefinition = "varchar(3)")
    private String pessoasMesa;

    @Column(name = "pessoas_pagantes", length = 3, columnDefinition = "varchar(3)")
    private String pessoasPagantes;

    @Column(name = "motivo_cancelamento", nullable = true)
    private Integer codigoMotivoCancelamento;

    @Column(name = "observacao_motivo", nullable = true, length = 255)
    private String observacaoMotivo;

    @Column(name = "foi_produzido", nullable = true)
    private Boolean foiProduzido;

    @Column(name = "observacao_destino", nullable = true, length = 255)
    private String observacaoDestino;

    @Column(name = "responsavel_cancelamento", nullable = true, length = 50)
    private String respansavelCancelamento;
    
    @Column(name = "responsavel_reipressao", nullable = true, length = 50)
    private String respansavelReipressao;
    
    @Column(name = "responsavel_transferencia", nullable = true, length = 50)
    private String respansavelTransferencia;
    
    @Column(name = "quantidade_cancelada", nullable = true, precision = 6,scale = 3)
    private Double quantidadeCancelada;
    
    @Column(name = "valor_item", precision = 18, scale = 3, nullable = false)
    private Double valorItem;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.numero);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EspelhoComanda other = (EspelhoComanda) obj;
        return Objects.equals(this.numero, other.numero);
    }

    public EspelhoComanda(Integer numero, String pedido, String numeroItem, String comanda, String referencia, Double quantidade, Date data, String vendedor, String observacao, String mesa, String status, String impressao, Double porcentagem, Double valorPorcentagem, String statusItem, Date dataPreconta, String pessoasMesa, String pessoasPagantes) {
        this.numero = numero;
        this.pedido = pedido;
        this.numeroItem = numeroItem;
        this.comanda = comanda;
        this.referencia = referencia;
        this.quantidade = quantidade;
        this.data = data;
        this.vendedor = vendedor;
        this.observacao = observacao;
        this.mesa = mesa;
        this.status = status;
        this.impressao = impressao;
        this.porcentagem = porcentagem;
        this.valorPorcentagem = valorPorcentagem;
        this.statusItem = statusItem;
        this.dataPreconta = dataPreconta;
        this.pessoasMesa = pessoasMesa;
        this.pessoasPagantes = pessoasPagantes;
    }

    public EspelhoComanda(Integer numero) {
        this.numero = numero;
    }

    public EspelhoComanda() {
    }

}
