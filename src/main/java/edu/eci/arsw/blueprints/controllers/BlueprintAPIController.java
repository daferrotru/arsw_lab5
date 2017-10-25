/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.boot.Banner.Mode.LOG;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    BlueprintsServices bps = null;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoBlueprints() {
        //Obtener datos que se enviaran a traves del api
        return new ResponseEntity<>(bps.getAllBlueprints(), HttpStatus.ACCEPTED);

    }

    @RequestMapping(value = "/{author}", method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoBlueprintsPorAutor(@PathVariable String author) {
        try {
            //Obtener datos que se enviaran a traves del api
            if (bps.getBlueprintsByAuthor(author).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(bps.getBlueprintsByAuthor(author), HttpStatus.ACCEPTED);
            }

        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @RequestMapping(value = "/{author}/{bprintname}", method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoBlueprintsPorAutorYNombre(@PathVariable String author, @PathVariable String bprintname) {
        try {
            //Obtener datos que se enviaran a traves del api
            if (bps.getBlueprint(author, bprintname) == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(bps.getBlueprint(author, bprintname), HttpStatus.ACCEPTED);
            }

        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> manejadorPostRecursoBlueprint(@RequestBody Blueprint bp) {

        try {
            if (bps.getBlueprint(bp.getAuthor(), bp.getName()) == null) {
                bps.addNewBlueprint(bp);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(value = "/{author}/{bprintname}" , method = RequestMethod.PUT)
    public ResponseEntity<?> manejadorPutRecursoBlueprint(@RequestBody Blueprint bp) {

        try {
            if (bps.getBlueprint(bp.getAuthor(), bp.getName()) == null) {
                bps.addNewBlueprint(bp);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else if(bps.getBlueprint(bp.getAuthor(), bp.getName()) != null){
                bps.updateBlueprint(bp);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
    
    @RequestMapping(value = "/{author}/{bprintname}" , method = RequestMethod.DELETE)
    public ResponseEntity<?>deleteBP(@PathVariable String author, @PathVariable String bprintname){
        try {
            bps.deleteBP(author, bprintname);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    
    
}
