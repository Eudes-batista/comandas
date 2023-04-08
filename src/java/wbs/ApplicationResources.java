/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wbs;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/relatorio")
public class ApplicationResources extends ResourceConfig{

    public ApplicationResources() {
        packages("wbs.controller");
    }
    
    
}
