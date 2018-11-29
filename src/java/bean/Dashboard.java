package bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.ChartSeries;

@ManagedBean(name = "dashboard")
@ViewScoped
@Getter
@Setter
public class Dashboard implements Serializable {

    private BarChartModel barChartModel;

    public void init() {
        this.barChartModel = initBarModel();
    }

    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();

        BarChartSeries eudes = new BarChartSeries();
        eudes.setLabel("Eudes");
        eudes.set("Dia 20", 120.0);
        eudes.setDisableStack(true);

        ChartSeries nathalia = new ChartSeries();
        nathalia.setLabel("Nathalia");
        nathalia.set("Dia 20", 220.0);

        ChartSeries thalya = new ChartSeries();
        thalya.setLabel("Thalya");
        thalya.set("Dia 20", 440.0);

        model.addSeries(eudes);
        model.addSeries(nathalia);
        model.addSeries(thalya);
        model.setAnimate(true);
        model.setTitle("Vendas Garçons");
        model.setLegendPosition("ne");

        return model;
    }

}
