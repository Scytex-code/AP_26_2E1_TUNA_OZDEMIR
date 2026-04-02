package com.catalog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Catalog implements Serializable {

    private List<Resource> resources = new ArrayList<>();

    public void add(Resource r){
        resources.add(r);
    }

    public List<Resource> getResources(){
        return resources;
    }

    public Resource findById(String id){
        for(Resource r : resources)
            if(r.getId().equals(id))
                return r;
        return null;
    }
}