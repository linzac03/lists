package me.lists5.lists;

import java.util.ArrayList;

/**
 * Created by porko on 4/27/16.
 */
public class BillItem {
        String name, amount, nextDue, frequency;

        BillItem(String iname, String iamount, String inext, String ifrequency) {
            this.name = iname;
            this.amount = iamount;
            this.nextDue = inext;
            this.frequency = ifrequency;
        }

    @Override
    public String toString() {
        String out = "{'type':'bill', ";
        out += "'name':" + this.name + ", ";
        out += "'amount':" + this.amount + ", ";
        out += "'nextdue':" + this.nextDue + ", ";
        out += "'frequency':" + this.frequency;
        out += "}";
        return out;
    }
}
