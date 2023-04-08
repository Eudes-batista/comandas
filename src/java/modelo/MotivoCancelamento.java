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
@Table (name="motivo_cancelamento")
@Getter
@Setter
@EqualsAndHashCode(of={"codigo"})
@ToString
public class MotivoCancelamento implements Serializable{
    
    @Id
    @Column(name="codigo", nullable = false)
    private Integer codigo;
    
    @Column(name="nome", nullable = false, length = 20)
    private String nome;
    
    @Column(name="observacao_obrigatoria", nullable = true, length = 1)
    private Integer observacaoObrigatoria;
    
}
