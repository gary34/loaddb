import io.gary.LineItem;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by pugang on 2018/5/7.
 */
public class TestLineItem {
    @Test
    public void testParseLine(){
        String line = "00000x\t0.11\t2.11\t0.21";
        Date date = Date.valueOf("2017-08-01");
        LineItem[] items = LineItem.parseItem(date,line);
        assertEquals(items.length,3);
        assertEquals(items[0].stockCode,"00000x");
        assertEquals(items[0].itemValue,0.11,0);
    }
}
