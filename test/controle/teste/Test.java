package controle.teste;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


public class Test {
  
    public static void main(String[] args) {
        String pedido ="789454,789454,789454,789453";
        Set<String> collect = Arrays.asList(pedido.split(",")).stream().collect(Collectors.toSet());
        String pedidos = collect.stream().collect(Collectors.joining(","));
        System.out.println("pedidos = " + pedidos);
    }
    
}