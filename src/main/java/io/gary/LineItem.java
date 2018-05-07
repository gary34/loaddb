package io.gary;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pugang on 2018/5/7.
 */
public class LineItem {
    public Date tradeDate ;
    public String stockCode;
    public Double itemValue;
    public LineItem(Date tradeDate, String stockCode,Double itemValue){
        this.tradeDate = tradeDate;
        this.stockCode = stockCode;
        this.itemValue = itemValue;
    }
    public static LineItem[] parseItem(Date d,String line) {
        String[] array = line.split("\t");
        LineItem[] items = new LineItem[0];
        if (array.length < 2){
            Framework.getLogger().info("item size less 2 {},{}",array.length,line);
            return items;
        }
        List<LineItem> itemList = new ArrayList<>(array.length-1);
        String stockCode = array[0];
        for ( int i = 1 ; i < array.length ; i++ ){
            try{
                double v = Double.parseDouble(array[i]);
                itemList.add(new LineItem(d,stockCode,v));
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        return itemList.toArray(items);
    }
}
