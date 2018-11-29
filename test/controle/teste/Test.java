package controle.teste;

import java.util.Random;

public class Test {
  
    public static void main(String[] args) {
 
        Random random = new Random();
        
        int valor1 = random.ints(100, 255).findFirst().getAsInt();
        int valor2 = random.ints(100, 255).findFirst().getAsInt();
        int valor3 = random.ints(100, 255).findFirst().getAsInt();
        String rgba ="rgba("+valor1+","+valor2+","+valor3+",0.6)";
        System.out.println(rgba);
    }
    
}