package ru.kpfu.itis.Samarin903;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bucket {
    private String prodCode;
    private String basketID;

    public static void main(String[] args) throws IOException {
        CsvToBean csv = new CsvToBean();
        String csvFilename = "src/transactions.csv";
        CSVReader csvReader = new CSVReader(new FileReader(csvFilename), ';', '"', 1);
        List list = csv.parse(setColumnMapping(), csvReader);
        Map<String, Map<Integer, List<String>>> products = new Bucket().addAllProducts(list); // [ PRDxxxxxxx [ number_of_orders list_of_baskets ] ]
        writeToCSV(products);
    }

    private static void writeToCSV(Map<String, Map<Integer, List<String>>> products) throws IOException {
        String csv = "target/data.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv));
        String[] record = new String[2];
        for (Map.Entry<String, Map<Integer, List<String>>> entry: products.entrySet()) {
            record[0] = entry.getKey();
            for (Map.Entry<Integer, List<String>> innerEntry : entry.getValue().entrySet()) {
                record[1] = String.valueOf(innerEntry.getKey());
            }
            writer.writeNext(record);
        }
        writer.close();
    }

    private static ColumnPositionMappingStrategy<Bucket> setColumnMapping() {
        ColumnPositionMappingStrategy<Bucket> strategy = new ColumnPositionMappingStrategy<Bucket>();
        strategy.setType(Bucket.class);
        String[] columns = new String[] {"prodCode", "basketID"};
        strategy.setColumnMapping(columns);
        return strategy;
    }

    private Map<String, Map<Integer, List<String>>> addAllProducts(List list) {
        Map<String, Map<Integer, List<String>>> products = new HashMap<String, Map<Integer, List<String>>>();
        List<Bucket> transactionsList = new ArrayList<Bucket>();
        for (Object object : list) {
            Bucket transactions = (Bucket) object;
            transactionsList.add(transactions);
            prodCode = transactions.getProdCode();
            basketID = transactions.getBasketID();
            if (products.containsKey(prodCode)) {
                Map<Integer, List<String>> innerMap = products.get(prodCode);
                int periodicity = 1;
                List<String> oldBaskets = null;
                for (Map.Entry<Integer, List<String>> entry : innerMap.entrySet()) {
                    periodicity = entry.getKey() + 1;
                    oldBaskets = entry.getValue();
                    oldBaskets.add(basketID);
                }
                innerMap.clear();
                innerMap.put(periodicity, oldBaskets);
                products.put(prodCode, innerMap);
            } else {
                List<String> baskets = new ArrayList<String>();
                baskets.add(basketID);
                Map<Integer, List<String>> innerMap = new HashMap<Integer, List<String>>();
                innerMap.put(1, baskets);
                products.put(prodCode, innerMap);
            }
        }
        return products;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getBasketID() {
        return basketID;
    }

    public void setBasketID(String basketID) {
        this.basketID = basketID;
    }
}
