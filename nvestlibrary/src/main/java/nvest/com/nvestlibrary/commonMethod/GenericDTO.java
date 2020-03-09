package nvest.com.nvestlibrary.commonMethod;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import nvest.com.nvestlibrary.nvestWebModel.DynamicParams;

public class GenericDTO {

    private static volatile Map<String, List> resultSetMap = new HashMap<>();
    private static volatile Map<String, List<DynamicParams>> dynamicresultSetMap = new HashMap<>();
    private static volatile int resourceId = 1;
    private static volatile HashMap<Integer, Map<String, DynamicParams>> combinedProductList = new HashMap();

    public static void addCombinedProduct(int productId, String key, DynamicParams dynamicParams) {
        Map<String, DynamicParams> dynamicParamsMap = new HashMap<>();
        if (combinedProductList.containsKey(productId)) {
            dynamicParamsMap = combinedProductList.get(productId);
        }

        if (dynamicParamsMap != null) {
            dynamicParamsMap.put(key, dynamicParams);
            combinedProductList.put(productId, dynamicParamsMap);
        }
    }

    public static Map<String, DynamicParams> getAllCombinedProducts(int productId) {
        Map<String, DynamicParams> dynamicParamsMap = new HashMap<>();
        if (combinedProductList.containsKey(productId)) {
            dynamicParamsMap = combinedProductList.get(productId);
        }
        return dynamicParamsMap;
    }

    public static HashMap<String, String> getAllCombinedProductsMap(int productId) {
        HashMap<String, String> combinedProductsMap = new HashMap<>();
        if (combinedProductList.containsKey(productId)) {
            Map<String, DynamicParams> dynamicParamsMap = combinedProductList.get(productId);
            if (dynamicParamsMap != null) {
                for (Map.Entry<String, DynamicParams> entry : dynamicParamsMap.entrySet()) {
                    String key = entry.getKey();
                    DynamicParams dynamicParams = entry.getValue();
                    String value = dynamicParams.getFieldKey();
                    combinedProductsMap.put(key, value);
                }
            }
        }
        return combinedProductsMap;
    }

    public static HashMap<Integer, Map<String, DynamicParams>> getAllCombinedProducts() {
        return combinedProductList;
    }

    /*public static HashMap<Integer, Map<String, String>> getAllCombinedProductsString() {
        HashMap<Integer, Map<String, String>> comboProductsString = new HashMap<>();
        for (Map.Entry<Integer, Map<String, DynamicParams>> entry : combinedProductList.entrySet()) {
            int key = entry.getKey();
            DynamicParams dynamicParams = entry.getValue();

        }
        //return combinedProductList;
        return comboProductsString;
    }*/

    public static void clearCombinedProducts() {
        combinedProductList.clear();
    }

    public static DynamicParams getCombinedProductDynamicParams(int id, String key) {
        Map<String, DynamicParams> dynamicParamsMap = new HashMap<>();
        if (combinedProductList.containsKey(id)) {
            dynamicParamsMap = combinedProductList.get(id);
        }
        if (dynamicParamsMap != null) {
            return dynamicParamsMap.get(key);
        }
        return null;
    }

    // Dynamic params section starts here

    public static void addListDynamicParams(String key, DynamicParams dynamicParams) {
        List<DynamicParams> dynamicParamsList;
        if (dynamicresultSetMap.containsKey(key)) {
            dynamicParamsList = dynamicresultSetMap.get(key);
            if (dynamicParamsList != null) {
                dynamicParamsList.add(dynamicParams);
            }
        } else {
            dynamicParamsList = new ArrayList<>();
            dynamicParamsList.add(dynamicParams);
        }

        if (dynamicParamsList != null) {
            dynamicresultSetMap.put(key, dynamicParamsList);
        }

    }

    public static void addDynamicParam(String key, DynamicParams dynamicParams) {
        key = key.toUpperCase();
        List<DynamicParams> list = new ArrayList<DynamicParams>();
        list.add(dynamicParams);
        dynamicresultSetMap.put(key, list);
    }

    public static DynamicParams getDynamicParamByKey(String key) {
        return (dynamicresultSetMap.get(key) == null) ? null : dynamicresultSetMap.get(key).get(0);
    }

    public static List<DynamicParams> getDynamicParamListByKey(String key) {
        return dynamicresultSetMap.get(key);
    }

    public static synchronized Map<String, List<DynamicParams>> getAllDynamicResultSetMap() {
        return dynamicresultSetMap;
    }

    public static int getResourceId() {
        return Integer.parseInt(String.valueOf(new AtomicInteger(resourceId + 1)));
    }

    public static List<String> getKeysBySubString(String key) {
        Map<String, List<DynamicParams>> allData = getAllDynamicResultSetMap();
        List<String> keysList = new ArrayList<>();
        for (Map.Entry<String, List<DynamicParams>> entry : allData.entrySet()) {
            if (entry.getKey().contains(key)) {
                keysList.add(entry.getKey());
            }
        }
        return keysList;
    }

    public static void clearDynamicParams() {
        dynamicresultSetMap.clear();
    }

    public static void removeDynamicParams(String key) {
        dynamicresultSetMap.remove(key);
    }

    public static void removeAllDynamicParamsStartsWith(String key) {
        Map<String, List<DynamicParams>> allData = getAllDynamicResultSetMap();
        List<String> keysList = new ArrayList<>();
        for (Map.Entry<String, List<DynamicParams>> entry : allData.entrySet()) {
            if (entry.getKey().startsWith(key)) {
                keysList.add(entry.getKey());
            }
        }

        for (String keyToBeRemoved : keysList) {
            removeDynamicParams(keyToBeRemoved);
        }
    }

    public static void removeAllDynamicParamsEndsWith(String key) {
        Map<String, List<DynamicParams>> allData = getAllDynamicResultSetMap();
        List<String> keysList = new ArrayList<>();
        for (Map.Entry<String, List<DynamicParams>> entry : allData.entrySet()) {
            if (entry.getKey().endsWith(key)) {
                keysList.add(entry.getKey());
            }
        }

        for (String keyToBeRemoved : keysList) {
            removeDynamicParams(keyToBeRemoved);
        }
    }

    public static void removeAllDynamicParamsContains(String key) {
        Map<String, List<DynamicParams>> allData = getAllDynamicResultSetMap();
        List<String> keysList = new ArrayList<>();
        for (Map.Entry<String, List<DynamicParams>> entry : allData.entrySet()) {
            if (entry.getKey().contains(key)) {
                keysList.add(entry.getKey());
            }
        }

        for (String keyToBeRemoved : keysList) {
            removeDynamicParams(keyToBeRemoved);
        }
    }

    public static boolean dynamicParamsContainsKey(String key) {
        Map<String, List<DynamicParams>> allData = getAllDynamicResultSetMap();
        List<String> keysList = new ArrayList<>();
        for (Map.Entry<String, List<DynamicParams>> entry : allData.entrySet()) {
            if (entry.getKey().contains(key)) {
                keysList.add(entry.getKey());
            }
        }
        return keysList.size() > 0;
    }

    // Dynamic params section ends here

    // Normal generic DTO section starts here

    public static <T> void addListAttribute(String key, T attributeValue) {
        List<T> attributeList;
        if (resultSetMap.containsKey(key)) {
            attributeList = resultSetMap.get(key);
            if (attributeList != null) {
                attributeList.add(attributeValue);
            }
        } else {
            attributeList = new ArrayList<>();
            attributeList.add(attributeValue);
        }

        if (attributeList != null) {
            resultSetMap.put(key, attributeList);
        }

        resultSetMap.put(key, attributeList);
    }


    public static <T> void addAttribute(String key, T attributeValue) {
        key = key.toUpperCase();
        List<T> list = new ArrayList<>();
        list.add(attributeValue);
        resultSetMap.put(key, list);

    }

    public static <T> T getAttributeValue(String key) {
        key = key.toUpperCase();
        return (resultSetMap.get(key) == null) ? null : (T) resultSetMap.get(key).get(0);
    }

    public static <T> List<T> getAttributeList(String key) {
        return resultSetMap.get(key);
    }

    public static Map<String, List> getAllResultSetMap() {
        return resultSetMap;
    }

    public static void clearResultSetMap() {
        resultSetMap.clear();
    }

    public static void removeAttribute(String attributeName) {
        resultSetMap.remove(attributeName);
    }

    public static void removeAllAttributeStartsWith(String key) {
        Map<String, List> allData = getAllResultSetMap();
        List<String> keysList = new ArrayList<>();
        for (Map.Entry<String, List> entry : allData.entrySet()) {
            if (entry.getKey().startsWith(key)) {
                keysList.add(entry.getKey());
            }
        }

        for (String keyToBeRemoved : keysList) {
            removeAttribute(keyToBeRemoved);
        }
    }

    public static void removeAllAttributeEndsWith(String key) {
        Map<String, List> allData = getAllResultSetMap();
        List<String> keysList = new ArrayList<>();
        for (Map.Entry<String, List> entry : allData.entrySet()) {
            if (entry.getKey().endsWith(key)) {
                keysList.add(entry.getKey());
            }
        }

        for (String keyToBeRemoved : keysList) {
            removeAttribute(keyToBeRemoved);
        }
    }

    public static void removeAllAttributeContains(String key) {
        Map<String, List> allData = getAllResultSetMap();
        List<String> keysList = new ArrayList<>();
        for (Map.Entry<String, List> entry : allData.entrySet()) {
            if (entry.getKey().contains(key)) {
                keysList.add(entry.getKey());
            }
        }

        for (String keyToBeRemoved : keysList) {
            removeAttribute(keyToBeRemoved);
        }
    }

    // Normal generic DTO section ends here

    public static void clearGenericDto() {
        clearCombinedProducts();
        clearDynamicParams();
        clearResultSetMap();
    }
}