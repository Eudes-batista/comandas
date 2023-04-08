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
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "COMANDAS_EXCLUIDAS")
public class ComandaExcluida implements Serializable {

    @Id
    @Column(name = "comanda", length = 4, nullable = false)
    private String comanda;

}
