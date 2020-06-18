package utils;

import java.util.List;

import database.pojo.Record;


public class Converter {

    public static Record maxRecord(List<Record> recordList){
        Record recMax = recordList.get(0);

       for (int i = 0; i <recordList.size() ;i++){
           if(recordList.get(i).getValue() < recMax.getValue()){
               recMax = recordList.get(i);
           }
       }
        return recMax;
    }

    public static Record minRecord(List<Record> recordList){
        Record recMin = recordList.get(0);

        for (int i = 0; i <recordList.size() ;i++){
            if(recordList.get(i).getValue() > recMin.getValue()){
                recMin = recordList.get(i);
            }
        }
        return recMin;
    }

}
