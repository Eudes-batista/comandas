/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Administrador
 */
public class Empresa implements Serializable {

    private String cnpj;
    private String inscricaoEstadual;
    private String razao;
    private String nomeFantasia;
    private String cep;
    private String cidade;
    private String bairro;
    private String endereco;
    private String uf;
    private String telefone;
    private String numero;
    private String codigo;

    public Empresa(String cnpj) {
        this.cnpj = cnpj;
    }

    public Empresa(String cnpj, String inscricaoEstadual, String razao, String nomeFantasia, String cep, String cidade, String bairro, String endereco, String uf, String telefone, String numero) {
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.razao = razao;
        this.nomeFantasia = nomeFantasia;
        this.cep = cep;
        this.cidade = cidade;
        this.bairro = bairro;
        this.endereco = endereco;
        this.uf = uf;
        this.telefone = telefone;
        this.numero = numero;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.cnpj);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Empresa other = (Empresa) obj;
        return Objects.equals(this.cnpj, other.cnpj);
    }

    @Override
    public String toString() {
        return "Empresa{" + "cnpj=" + cnpj + ", inscricaoEstadual=" + inscricaoEstadual + ", razao=" + razao + ", nomeFantasia=" + nomeFantasia + ", cep=" + cep + ", cidade=" + cidade + ", bairro=" + bairro + ", endereco=" + endereco + ", uf=" + uf + ", telefone=" + telefone + ", numero=" + numero + '}';
    }

}
