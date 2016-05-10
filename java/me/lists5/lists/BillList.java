package me.lists5.lists;

import java.util.ArrayList;

/**
 * Created by porko on 4/27/16.
 */
public class BillList {
    ArrayList<BillListItem> list;
    BillList () {
        list = new ArrayList<BillListItem>();
    }

    public ArrayList<BillListItem> getList() {
        return list;
    }
    public void insert(String name, String price) {
        BillListItem newListItem = new BillListItem(name, price);
        list.add(newListItem);
    }

    public void remove(BillListItem item) {
        list.remove(item);
    }

    class BillListItem {
        String name;
        String price;
        BillListItem(String iname, String iprice) {
            this.name = iname;
            this.price = iprice;
        }
    }
}
