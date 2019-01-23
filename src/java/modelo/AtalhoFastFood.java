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

@Entity
@Table(name = "atalho_fastfood")
@Getter
@Setter
@EqualsAndHashCode(of={"codigo"})
@ToString
public class AtalhoFastFood implements Serializable {

    @Id
    @Column(name="codigo",nullable = false)
    private Integer codigo;

    @Column(name = "referencia_01", length = 20, nullable = true)
    private String referencia01;

    @Column(name = "referencia_02", length = 20, nullable = true)
    private String referencia02;

    @Column(name = "referencia_03", length = 20, nullable = true)
    private String referencia03;

    @Column(name = "referencia_04", length = 20, nullable = true)
    private String referencia04;

    @Column(name = "referencia_05", length = 20, nullable = true)
    private String referencia05;

    @Column(name = "referencia_06", length = 20, nullable = true)
    private String referencia06;

    @Column(name = "referencia_07", length = 20, nullable = true)
    private String referencia07;

    @Column(name = "referencia_08", length = 20, nullable = true)
    private String referencia08;

    @Column(name = "referencia_09", length = 20, nullable = true)
    private String referencia09;

    @Column(name = "referencia_10", length = 20, nullable = true)
    private String referencia10;

    @Column(name = "referencia_11", length = 20, nullable = true)
    private String referencia11;

    @Column(name = "referencia_12", length = 20, nullable = true)
    private String referencia12;


    @Column(name = "descricao_01", length = 35, nullable = true)
    private String descricao01;

    @Column(name = "descricao_02", length = 35, nullable = true)
    private String descricao02;

    @Column(name = "descricao_03", length = 35, nullable = true)
    private String descricao03;

    @Column(name = "descricao_04", length = 35, nullable = true)
    private String descricao04;

    @Column(name = "descricao_05", length = 35, nullable = true)
    private String descricao05;

    @Column(name = "descricao_06", length = 35, nullable = true)
    private String descricao06;

    @Column(name = "descricao_07", length = 35, nullable = true)
    private String descricao07;

    @Column(name = "descricao_08", length = 35, nullable = true)
    private String descricao08;

    @Column(name = "descricao_09", length = 35, nullable = true)
    private String descricao09;

    @Column(name = "descricao_10", length = 35, nullable = true)
    private String descricao10;

    @Column(name = "descricao_11", length = 35, nullable = true)
    private String descricao11;

    @Column(name = "descricao_12", length = 35, nullable = true)
    private String descricao12;

    public AtalhoFastFood() {
    }

    public AtalhoFastFood(Integer codigo) {
        this.codigo = codigo;
    }
}
