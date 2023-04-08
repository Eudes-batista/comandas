package util;

import java.util.List;
import java.util.function.Predicate;
import modelo.Configuracao;
import modelo.Lancamento;

public class CalcularPreconta {

    private List<Lancamento> lancamentos;
    private final Configuracao configuracao;

    public CalcularPreconta(List<Lancamento> lancamentos,Configuracao configuracao) {
        this.lancamentos = lancamentos;
        this.configuracao = configuracao;
    }
    
    public double realizarCalculoValorOpcional() {
        double valorTotal = realizarCalculoTotalItensSemCouvert();
        double valorOpconal = (valorTotal * 0.10);
        return valorOpconal;
    }

    public double realizarCalculoItensCouvert() {
        double couvert = calcularItens(lancamento -> lancamento.getDescricao().toUpperCase().contains("couvert".toUpperCase()));
        return couvert;
    }

    public double realizarCalculoTotalItensSemCouvert() {
        double valorTotal = calcularItens(lancamento -> !lancamento.getDescricao().toUpperCase().contains("couvert".toUpperCase()));
        return valorTotal;
    }

    public double calcularItens(Predicate<Lancamento> condicao) {
        double valorTotal = lancamentos.stream().filter(condicao).mapToDouble(Lancamento::getPrecoTotal).sum();
        return valorTotal;
    }

    public double realizaCalculoTotal() {
        double valorTotal = realizarCalculoTotalItensSemCouvert();
        double valorOpcional = realizarCalculoValorOpcional();
        double valorCouvert = realizarCalculoItensCouvert();
        double resultado = (valorTotal + valorOpcional) + valorCouvert;
        return resultado;
    }

    public boolean verificarDezPorcento() {
        return !this.configuracao.getCobraDezPorcento().isEmpty();
    }

    public void setLancamentos(List<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
    }

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }
}
