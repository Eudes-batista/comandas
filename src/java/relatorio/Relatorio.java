/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import modelo.Empresa;
import modelo.Lancamento;
import servico.ComandaService;
import servico.EmpresaService;
import servico.MesaService;
import util.GerenciaArquivo;

public class Relatorio {

    private ComandaService comandaService;
    private EmpresaService empresaService;

    private String mesa;

    public Relatorio() {
    }

    public Relatorio(String mesa) {
        this.mesa = mesa;
    }

    public Relatorio(ComandaService comandaService, EmpresaService empresaService, String mesa) {
        this.comandaService = comandaService;
        this.empresaService = empresaService;
        this.mesa = mesa;
    }

    public StringBuilder montarCupomPedido(List<Lancamento> lancamentos, Lancamento lanc) {
        StringBuilder str = new StringBuilder();
        str.append("------------------------------------------------");
        str.append("Mesa: ").append(lancamentos != null ? lancamentos.get(0).getMesa() : lanc.getMesa())
                .append("\t\t\tComanda: ").append(lancamentos != null ? lancamentos.get(0).getComanda() : lanc.getComanda()).append("\n");
        str.append("------------------------------------------------");
        str.append("QUANTIDADE\tITEM");
        str.append("\n")
                .append("------------------------------------------------");
        if (lancamentos != null) {
            for (Lancamento lancamento : lancamentos) {
                str.append(carregarItens(lancamento, str));
            }
        } else {
            str.append(carregarItens(lanc, str));
        }
        str.append("\t\tData: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()))
                .append("\n")
                .append("\t\tVendedor: ").append(lancamentos != null ? lancamentos.get(0).getVendedor() : lanc.getVendedor()).append("\n")
                .append("------------------------------------------------")
                .append("\n\n\n\n");
        String text = String.valueOf((char) 27);
        String text1 = String.valueOf((char) 109);
        str.append(text).append(text1);
        return str;
    }

    private StringBuilder carregarItens(Lancamento lancamento, StringBuilder str) {
        str.append("    ").append(lancamento.getQuantidade()).append("\t\t").append(lancamento.getDescricao());
        str.append("\n")
                .append("------------------------------------------------");
        if (lancamento.getObservacao() != null) {
            if (!lancamento.getObservacao().isEmpty()) {
                str.append("OBS:");
                str.append("\n");
                str.append("    ").append(lancamento.getObservacao())
                        .append("\n");
            }
        }
        return str;
    }

    public StringBuilder montarCupomComanda(String comanda) {
        DecimalFormat df = new DecimalFormat("##,###0.00");
        StringBuilder str = new StringBuilder();
        List<Object[]> lancamentos = comandaService.ListarLancamentos(comanda, mesa);
        Empresa empresa = getEmpresa();
        str.append(empresa.getRazao()).append("\n");
        str.append(empresa.getNomeFantasia()).append("\n");
        str.append(empresa.getEndereco()).append("N ").append("\n");
        str.append("Telefone:").append(empresa.getTelefone()).append(" - ").append("CEP: ").append(empresa.getCep()).append("\n");
        str.append("CNPJ: ").append(empresa.getCnpj()).append(" - Ins.: ").append(empresa.getInscricaoEstadual()).append("\n");
        str.append("-----------------------------------------").append("\n");
        str.append("Mesa: ").append(mesa).append("\tComanda: ").append(comanda).append("\n");
        str.append("-----------------------------------------").append("\n");
        double totalComanda = 0.0;
        str.append(" ITEM \t QTD \tV.unit\tV.Total").append("\n");
        str.append("---------------------------------------------").append("\n");
        for (Object[] l : lancamentos) {
            String descricao = String.valueOf(l[4]);
            if (descricao.length() > 20) {
                descricao = descricao.substring(0, 20) + "\t";
            }
            str.append(descricao).append("\t").append(String.valueOf(l[5])).append("\t").append(l[6]).append("\t").append(l[7]);
            str.append("\n");
            totalComanda += Double.parseDouble(String.valueOf(l[7]));
        }
        String text = String.valueOf((char) 27);
        String text1 = String.valueOf((char) 109);
        str.append("\t\t Total: ").append(df.format(totalComanda)).append("\n\n\n\n")
                .append(text).append(text1);
        return str;
    }

    public StringBuilder montarCupomMesa(String mesa, MesaService mesaService) {
        Map<String, List<Object[]>> mapComanda = mesaService.listarComandasPorMesa(mesa).stream().collect(Collectors.groupingBy(c -> String.valueOf(c[0])));
        List<String> numerosComandas = mapComanda.keySet().stream().map(String::valueOf).collect(Collectors.toList());
        Collections.sort(numerosComandas);
        StringBuilder str = new StringBuilder();
        Empresa empresa = getEmpresa();
        str.append(empresa.getRazao()).append("\n");
        str.append(empresa.getNomeFantasia()).append("\n");
        str.append(empresa.getEndereco()).append("N ").append("\n");
        str.append("Telefone:").append(empresa.getTelefone()).append(" - ").append("CEP: ").append(empresa.getCep()).append("\n");
        str.append("CNPJ: ").append(empresa.getCnpj()).append(" - Ins.: ").append(empresa.getInscricaoEstadual()).append("\n");
        str.append("------------------------------------------------");
        str.append("\n");
        double total = 0;
        DecimalFormat df = new DecimalFormat("##,###0.00");
        for (String numerosComanda : numerosComandas) {
            str.append("Mesa: ").append(mesa).append("\t\tComanda: ").append(numerosComanda)
                    .append("\n");
            str.append("------------------------------------------------");
            str.append(" ITEM \t QTD \tV.unit\tV.Total")
                    .append("\n");
            str.append("------------------------------------------------");
            double totalComanda = 0.0;
            List<Object[]> comandas = comandaService.listarComandas(numerosComanda);
            for (Object[] comanda : comandas) {
                String descricao = String.valueOf(comanda[1]);
                double quantidade = Double.parseDouble(String.valueOf(comanda[2]));
                double vUnitario = Double.parseDouble(String.valueOf(comanda[3]));
                double vTotal = Double.parseDouble(String.valueOf(comanda[4]));
                int tamanhoDescricao = descricao.length();
                if (tamanhoDescricao >= 20) {
                    descricao = descricao.substring(0, 20) + "\n";
                }
                str.append(descricao).append("\t ").append(quantidade).append("\t").append(vUnitario).append("\t").append(vTotal);
                str.append("\n");
                totalComanda += vTotal;
            }
            str.append("\n");
            str.append("\t\t Total: ").append(df.format(totalComanda))
                    .append("\n")
                    .append("----------------------------------------")
                    .append("\n");
            total += totalComanda;
        }
        String text = String.valueOf((char) 27);
        String text1 = String.valueOf((char) 109);
        str.append("\n\t\t\t Total: ").append(df.format(total)).append("\n\n\n\n")
                .append(text).append(text1);
        return str;
    }

    public Empresa getEmpresa() {
        final Object[] listaEmpresa = empresaService.buscarEmpresa(new GerenciaArquivo().bucarInformacoes().getConfiguracao().getEmpresa());
        if(listaEmpresa == null){
            return null;
        }
        final Empresa empresa = new Empresa(String.valueOf(listaEmpresa[0]), String.valueOf(listaEmpresa[1]),
                String.valueOf(listaEmpresa[2]), String.valueOf(listaEmpresa[3]),
                String.valueOf(listaEmpresa[4]), String.valueOf(listaEmpresa[5]),
                String.valueOf(listaEmpresa[6]), String.valueOf(listaEmpresa[7]),
                String.valueOf(listaEmpresa[8]), String.valueOf(listaEmpresa[9]),
                String.valueOf(listaEmpresa[10]));
        empresa.setCodigo(String.valueOf(listaEmpresa[11]));
        return empresa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getMesa() {
        return mesa;
    }

    public Relatorio setEmpresaService(EmpresaService empresaService) {
        this.empresaService = empresaService;
        return this;
    }
    
}
