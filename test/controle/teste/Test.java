package controle.teste;

public class Test {

    public static void main(String[] args) {
        double valorPago = 163.32,valorCompra = 148.47;
        
        System.out.println("Valor da compra: "+valorCompra);
        System.out.println("Valor Pago: "+valorPago);
        System.out.println("Porcentagem: "+( valorPago/(valorCompra/100d) -100 ) );
        System.out.println("Porcentagem: "+(valorCompra *(( valorPago/(valorCompra/100d) -100 )/100)) + valorCompra);
    }

}
