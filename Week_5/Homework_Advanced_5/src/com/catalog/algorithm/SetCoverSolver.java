package com.catalog.algorithm;

import com.catalog.model.Resource;
import java.util.*;

public class SetCoverSolver {

    public static List<Resource> solve(List<Resource> resources, Set<String> concepts){

        List<Resource> result = new ArrayList<>();
        Set<String> covered = new HashSet<>();

        while(!covered.containsAll(concepts)){

            Resource best = null;
            int max = 0;

            for(Resource r : resources){

                int count = 0;

                for(String k : r.getKeywords())
                    if(concepts.contains(k) && !covered.contains(k))
                        count++;

                if(count > max){
                    max = count;
                    best = r;
                }
            }

            if(best == null) break;

            result.add(best);
            covered.addAll(best.getKeywords());
        }

        return result;
    }
}