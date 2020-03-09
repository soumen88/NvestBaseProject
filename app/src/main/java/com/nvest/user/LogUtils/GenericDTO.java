package com.nvest.user.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericDTO {

    private static volatile Map<String, List<Object>> resultSetMap = new HashMap<String, List<Object>>() ;

    public static void addAttribute(String attributeName, Object attributeValue) {
        attributeName = attributeName.toUpperCase();
        if(resultSetMap.containsKey(attributeName)) {
            resultSetMap.get(attributeName).add(attributeValue);
        } else {
            List<Object> list = new ArrayList<Object>();
            list.add(attributeValue);
            resultSetMap.put(attributeName, list);
        }
    }

    public static Object getAttributeValue(String key) {
        return (resultSetMap.get(key) == null) ? null : resultSetMap.get(key).get(0);
    }

    public static List<Object> getAttributeValues(String key) {
        return resultSetMap.get(key);
    }

}