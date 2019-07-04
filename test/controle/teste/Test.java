package controle.teste;

import controle.CancelamentoControle;

public class Test {

    public static void main(String[] args) {
       
        CancelamentoControle cancelamentoControle = new CancelamentoControle();
        String chave = cancelamentoControle.gerarChavePrimaria();
        System.out.println("chave = " + chave);
    }

}
