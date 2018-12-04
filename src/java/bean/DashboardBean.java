package bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import modelo.dto.TotalVenda;
import servico.DashboardService;

@ManagedBean(name="DashboardBean")
@ViewScoped
@Getter @Setter
public class DashboardBean implements Serializable{
    
    @ManagedProperty(value = "#{dashboardService}")
    private DashboardService dashboardService; 
    
    private TotalVenda totalVenda;
    private int quantidadeClientesAtendidos;
        
    public void init() {
        totalVenda= dashboardService.listarTotasVendasDoDia();
        quantidadeClientesAtendidos = dashboardService.listarClientesAtendidos().stream().mapToInt(p -> Integer.parseInt(p.getPESSOAS())).sum();
    }
    
    
}
