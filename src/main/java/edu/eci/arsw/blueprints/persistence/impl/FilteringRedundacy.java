/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.FilteringPersistence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 *
 * @author daferrotru
 */
//@Service
public class FilteringRedundacy implements FilteringPersistence {

    private final Map<Tuple<String, String>, Blueprint> blueprints = new HashMap<>();

    public FilteringRedundacy() {
        Point[] pts = new Point[]{new Point(140, 140), new Point(115, 115)};
        Blueprint bp = new Blueprint("_authorname_", "_bpname_ ", pts);
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);

    }

    @Override
    public Blueprint filterBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        
        
        Blueprint res=new Blueprint(bp.getAuthor(), bp.getName());
        
        for(int i=0;i<bp.getPoints().size()-1;i++){
            if(bp.getPoints().get(i).equals(bp.getPoints().get(i+1))){
                res.addPoint(bp.getPoints().get(i+1));
            }else{
                res.addPoint(bp.getPoints().get(i));
            }
        }
       
        bp=res;
                
        return bp;
    }

}
