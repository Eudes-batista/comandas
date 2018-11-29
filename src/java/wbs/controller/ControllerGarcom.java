/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wbs.controller;

import com.google.gson.Gson;
import controle.DashboardControle;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import modelo.dto.GarcomGrafico;
import modelo.dto.RejeicaoPorcentagemVendedor;
import modelo.dto.VendaGarcom;

@Path("/grafico")
public class ControllerGarcom {

    private DashboardControle dashboardControle;
    private Gson gson;

    public ControllerGarcom() {
        this.dashboardControle = new DashboardControle();
        this.gson = new Gson();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public String getVendasGarons() {
        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
        List<VendaGarcom> vendasGarcons = dashboardControle.listarVendasPorGarcom();
        List<GarcomGrafico> garcomGraficos = new ArrayList<>();
        for (VendaGarcom vendasGarcon : vendasGarcons) {
            String valorFormatado = decimalFormat.format(vendasGarcon.getVENDAS());
            valorFormatado = valorFormatado.replace(".", "").replace(",", ".");
            garcomGraficos.add(new GarcomGrafico(vendasGarcon.getGARCOM(), new double[]{Double.parseDouble(valorFormatado)}, 0, "rgba(160, 196, 255,0.6)", ""));
        }
        return gson.toJson(garcomGraficos);
    }

    @GET
    @Path("vendedores")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public String getRejecaoPorcentagemVendedor() {

        List<RejeicaoPorcentagemVendedor> rejeicaoPorcentagemVendedores = dashboardControle.listarRejeicaoPorcentagemVendedor();
        Map<String, Integer> mapa = new HashMap<>();
        rejeicaoPorcentagemVendedores.forEach((rejeicaoPorcentagemVendedor) -> {
            Integer qtd = mapa.get(rejeicaoPorcentagemVendedor.getVENDEDOR());
            if (qtd == null) {
                qtd = 0;
            }
            qtd++;
            mapa.put(rejeicaoPorcentagemVendedor.getVENDEDOR(), qtd);
        });
        List<GarcomGrafico> garcomGraficos = new ArrayList<>();
        mapa.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            Integer value = entry.getValue();
            garcomGraficos.add(new GarcomGrafico(key, new double[]{value}, 0, "rgba(200, 130, 130,0.6)", ""));
        });
        return gson.toJson(garcomGraficos);
    }

    @GET
    @Path("comissao")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public String getComissaoGarcom() {
        List<VendaGarcom> vendaGarcoms = dashboardControle.listarComissaoGarcom();
        List<GarcomGrafico> garcomGraficos = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
        for (VendaGarcom vendaGarcom : vendaGarcoms) {
            String valorFormatado = decimalFormat.format(vendaGarcom.getVENDAS());
            valorFormatado = valorFormatado.replace(".", "").replace(",", ".");
            garcomGraficos.add(new GarcomGrafico(vendaGarcom.getGARCOM(), new double[]{Double.parseDouble(valorFormatado)}, 0, "rgba(100,150,75,0.6)", ""));

        }
        return gson.toJson(garcomGraficos);
    }
}
