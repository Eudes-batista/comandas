package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.Transferencia;
import modelo.dto.FiltroTransferencia;
import servico.TransferenciaService;

@ManagedBean(name="transferenciaDetalhadaBean")
@ViewScoped
@Getter 
@Setter
public class TransferenciaDetalhadaBean implements Serializable{
    
    @ManagedProperty(value="#{transferenciaService}")
    private TransferenciaService transferenciaService;
    
    private List<Transferencia> transferencias;
    
    private FiltroTransferencia filtroTransferencia;
    
    
    public void init() {
        this.filtroTransferencia = new FiltroTransferencia();
        String data = LocalDate.now().toString();
        this.filtroTransferencia.setDataInicial(data);
        this.filtroTransferencia.setDataFinal(data);
        this.listarTransferencias();
    }
    
    public void listarTransferencias() {
        this.transferencias = this.transferenciaService.listarTransferencias(this.filtroTransferencia);
    }
    
    
}
