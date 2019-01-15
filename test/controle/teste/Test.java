package controle.teste;

public class Test {

    public static void main(String[] args) {

        String numeros = "123456,789789,45646,787987,456465,456446";
        String numero = "123456,45646,456446";
        String[] splitNumeros = numeros.split(",");
        for (String splitNumero1 : splitNumeros) {
            if(numero.contains(splitNumero1)){                 
                 int index = numeros.indexOf(splitNumero1);
                 int tamanho = splitNumero1.length();
                 int indexFinal = tamanho+index;
                 index = index-1 == -1 ? 0 : index-1;
                 String numeroEcontrado =numeros.substring(index,indexFinal);
                 numeros =numeros.replaceFirst(numeroEcontrado,"");
            }
        }
        System.out.println("numeros = " + numeros.replaceFirst(",", ""));
//        String nome = "WAGNER CAMINHA";
//
//        String n = "";
//        String[] nomes = nome.split(" ");
//        nome = "";
//
//        for (String nome1 : nomes) {
//            String minus, maius;
//            minus = "" + nome1.charAt(0);
//            maius = "" + nome1.charAt(0);
//            n = nome1.toLowerCase().replaceFirst(minus.toLowerCase(), maius.toUpperCase());
//            nome += n + " ";
//        }
//        n = nome;
//        nome = "";
//        for (int i = 0; i < 5; i++) {
//            nome += n + "/ ";
//            if (i == 4) {
//                nome += n;
//            }
//        }
//        System.out.println(nome);
    }

}
