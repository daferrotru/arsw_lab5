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
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
//import java.util.HashMap;
import java.util.HashSet;
//import java.util.Map;
import java.util.Map.Entry;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 */
@Service
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final ConcurrentHashMap<Tuple<String, String>, Blueprint> blueprints = new ConcurrentHashMap<>();

    public InMemoryBlueprintPersistence() {
        //load stub data

        Point[] pts = new Point[]{new Point(100, 100), new Point(100, 60), new Point(60,60), new Point(60,100), new Point(100, 100)};
        Blueprint bp = new Blueprint("daniel", "Cuadrado", pts);
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);

        Point[] pts2 = new Point[]{new Point(160, 160), new Point(120, 120)};
        Blueprint bp2 = new Blueprint("daniel", "Pintura2", pts2);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);

        Point[] pts3 = new Point[]{new Point(140, 100), new Point(200, 100), new Point(115, 115), new Point(140, 100)};
        Blueprint bp3 = new Blueprint("David", "trianguloDavid", pts3);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);

    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        } else {
            synchronized (blueprints) {
                blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
            }

        }
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        return blueprints.get(new Tuple<>(author, bprintname));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {

        Set<Blueprint> res = new HashSet<>();
        for (Entry<Tuple<String, String>, Blueprint> e : blueprints.entrySet()) {
            if (e.getValue().getAuthor().equals(author)) {
                res.add(e.getValue());
            }
        }
        return res;
    }
    private static final Logger LOG = Logger.getLogger(InMemoryBlueprintPersistence.class.getName());

    @Override
    public Set<Blueprint> getAllBlueprints() {
        synchronized(blueprints){
            return new HashSet<>(blueprints.values());
        }
        
    }

    @Override
    public Blueprint updateBlueprint(Blueprint bp) {
        synchronized (blueprints) {
            Blueprint res = blueprints.get(new Tuple<>(bp.getAuthor(), bp.getName()));
            res.setPoints(bp.getPoints());
            return res;
        }

    }

    @Override
    public void deleteBP(String author, String bpname) throws BlueprintNotFoundException {
        if (blueprints.containsKey(new Tuple<>(author, bpname))) {
            blueprints.remove(new Tuple<>(author, bpname));
        } 
    }

}
