/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence;
import edu.eci.arsw.blueprints.model.Blueprint;

/**
 *
 * @author 2104835
 */
public interface FilteringPersistence {
    
    /**
     *
     * @param bp
     * @throws BlueprintPersistenceException
     */
    public Blueprint filterBlueprint(Blueprint bp) throws BlueprintPersistenceException;
    
}
