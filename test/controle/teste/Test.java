package controle.teste;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public URI buscarCaminho() throws URISyntaxException{
        return getClass().getResource("/hibernate.cfg.xml").toURI();
    }
    
    public List<String> pegarInformacoesHibernateConfig() throws URISyntaxException,IOException{
        File file = new File(buscarCaminho());
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> dados = new ArrayList<>();
        while(br.ready()){
            dados.add(br.readLine());
        }
        return dados;
    }
    
    public List<String> mudarCaminhoDoBancoDeDados(List<String> dados,String caminhoDoBancoDeDados) {
        int count=0;
        for (String dado : dados) {
             if(dado.contains("<property name=\"hibernate.connection.url\">")){
                 String caminho = "    <property name=\"hibernate.connection.url\">jdbc:firebirdsql:"+caminhoDoBancoDeDados+"</property>;";
                 dados.set(count, caminho);
             }
             count++;
        }
        return dados;
    }
    
    public void salvarAsConfiguracoesDoBancoDeDados(String caminhoDoBancoDeDados) throws URISyntaxException, IOException {
        List<String> informacoesDoArquivoAtual = pegarInformacoesHibernateConfig();
        List<String> configuracoesAlteradas = mudarCaminhoDoBancoDeDados(informacoesDoArquivoAtual, caminhoDoBancoDeDados);
        PrintWriter pw = new PrintWriter(new File(buscarCaminho()));
        for (String configuracoesAlterada : configuracoesAlteradas) {
            pw.println(configuracoesAlterada);
            pw.flush();
        }
        pw.close();
    }
    
    
    public static void main(String[] args) {
 
        
        
    }
    
}