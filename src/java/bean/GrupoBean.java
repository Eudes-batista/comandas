/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import modelo.Lapt51;
import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import servico.GrupoServico;

@ManagedBean(name = "grupoBean")
@ViewScoped
@Getter
@Setter
public class GrupoBean implements Serializable {

    @ManagedProperty(value = "#{grupoServico}")
    private GrupoServico grupoServico;
    private List<Lapt51> grupos;
    private Lapt51 grupo;

    public void init() {
        listarGrupos();
    }

    private void listarGrupos() {
        grupos = grupoServico.listarGrupos();
    }

    public void upload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        String realPath = pegarCaminho();
        if (grupo != null) {
            String nameTipo[] = uploadedFile.getFileName().split(Pattern.quote("."));
            if (nameTipo.length > 0) {
                realPath += grupo.getT51dsgrp() + "." + nameTipo[1];
            }
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(realPath);
                outputStream.write(uploadedFile.getContents());
                outputStream.flush();
            } catch (IOException ex) {
                Messages.addGlobalError(ex.getMessage());
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException ex) {
                        Messages.addGlobalError(ex.getMessage());
                    }
                }
            }
        }
    }

    private String pegarCaminho() {
        return FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/imagens/");
    }

}
