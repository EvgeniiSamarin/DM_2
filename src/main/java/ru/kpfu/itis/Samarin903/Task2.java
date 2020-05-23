package ru.kpfu.itis.Samarin903;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task2 {
    private static String prodecode_id;
    private static String basket_id;


    private static Map<String, Map<Integer, ArrayList<String>>> map;
    public static void main(String[] args) throws FileNotFoundException {
        CsvToBean csv = new CsvToBean();
        CSVReader reader = new CSVReader(new FileReader("src/transactions.csv"), ';', '"', 1);
        List list = csv.parse(setColumMapping(), reader);
        for (Object object : list) {

           /* Task2 bckt = (Task2) object;
            System.out.println(bckt.getBasket_id() + bckt.basket_id);

            */
           map = new HashMap<String, Map<Integer, ArrayList<String>>>();
           if(map.containsKey(prodecode_id)) {

           } else {
               ArrayList<String> bckt = new ArrayList<String>();
               bckt.add(basket_id);
               Map<Integer, ArrayList<String>> mp  =new HashMap<Integer, ArrayList<String>>();
               mp.put(1, bckt);
               map.put(prodecode_id, mp);
           }
        }
    }

    private static ColumnPositionMappingStrategy setColumMapping() {
        ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
        strategy.setType(Task2.class);
        String[] columns = new String[] {"prodecode_id", "basket_id"};
        strategy.setColumnMapping(columns);
        return strategy;
    }


    public String getProdecode_id() {
        return prodecode_id;
    }

    public String getBasket_id() {
        return basket_id;
    }

    public static void setProdecode_id(String prodecode_id) {
        Task2.prodecode_id = prodecode_id;
    }

    public static void setBasket_id(String basket_id) {
        Task2.basket_id = basket_id;
    }
}
