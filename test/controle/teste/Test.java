package controle.teste;

public class Test {

    public static void main(String[] args) {
        System.out.println("Valor da compra: "+90d);
        System.out.println("Valor Pago: "+95d);
        System.out.println("Porcentagem: "+(95/(90d/100d)-100));
        System.out.println("Valor Pago: "+((((95/(90d/100d)-100)/100)*90)+90));
    }

}
