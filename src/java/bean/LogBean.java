package bean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import servico.MesaService;
import util.Log;

@ManagedBean
@ViewScoped
public class LogBean implements Serializable{
    
    @Getter @Setter
    private String log;
    @Getter @Setter
    @ManagedProperty(value = "#{mesaService}")
    private MesaService mesaService;
    private Log controleLog;

    @PostConstruct
    public void init() {
        controleLog= new Log(mesaService);
        log=controleLog.recuperarLog().toString();  
        
    }
    
    
}
