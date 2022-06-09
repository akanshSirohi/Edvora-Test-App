package com.akansh.edvora;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Utils {

    public HashMap<ListData, Integer> sortByValue(HashMap<ListData, Integer> hm)
    {
        List<Map.Entry<ListData, Integer> > list =
                new LinkedList<>(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<ListData, Integer>>() {
            public int compare(Map.Entry<ListData, Integer> o1,
                               Map.Entry<ListData, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<ListData, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<ListData, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}
