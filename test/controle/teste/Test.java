package controle.teste;

public class Test {

    public static void main(String[] args) {

        String nome = "WAGNER CAMINHA";

        String n = "";
        String[] nomes = nome.split(" ");
        nome = "";

        for (String nome1 : nomes) {
            String minus, maius;
            minus = "" + nome1.charAt(0);
            maius = "" + nome1.charAt(0);
            n = nome1.toLowerCase().replaceFirst(minus.toLowerCase(), maius.toUpperCase());
            nome += n + " ";
        }
        n = nome;
        nome = "";
        for (int i = 0; i < 5; i++) {
            nome += n + "/ ";
            if (i == 4) {
                nome += n;
            }
        }
        System.out.println(nome);
    }

}
