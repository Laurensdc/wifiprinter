package entities;

/**
 * Created by uizen on 3/29/2017.
 */

public class Settings {
    private String nameLine1, nameLine2, addrLine1, addrLine2, telLine, taxLine, extraLine, waiter;
    private int waiter_id;

    public Settings(String nameLine1, String nameLine2, String addrLine1, String addrLine2, String telLine, String taxLine, String extraLine, String waiter, int waiter_id) {
        this.nameLine1 = nameLine1;
        this.nameLine2 = nameLine2;
        this.addrLine1 = addrLine1;
        this.addrLine2 = addrLine2;
        this.telLine = telLine;
        this.taxLine = taxLine;
        this.extraLine = extraLine;
        this.waiter = waiter;
        this.waiter_id = waiter_id;
    }

    public Settings() {
        this.nameLine1 = "";
        this.nameLine2 = "";
        this.addrLine1 = "";
        this.addrLine2 = "";
        this.telLine = "";
        this.taxLine = "";
        this.extraLine = "";
        this.waiter = "";
        this.waiter_id = 0;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public String getNameLine2() {
        return nameLine2;
    }

    public void setNameLine2(String nameLine2) {
        this.nameLine2 = nameLine2;
    }

    public String getNameLine1() {
        return nameLine1;
    }

    public void setNameLine1(String nameLine1) {
        this.nameLine1 = nameLine1;
    }

    public String getAddrLine1() {
        return addrLine1;
    }

    public void setAddrLine1(String addrLine1) {
        this.addrLine1 = addrLine1;
    }

    public String getAddrLine2() {
        return addrLine2;
    }

    public void setAddrLine2(String addrLine2) {
        this.addrLine2 = addrLine2;
    }

    public String getTelLine() {
        return telLine;
    }

    public void setTelLine(String telLine) {
        this.telLine = telLine;
    }

    public String getTaxLine() {
        return taxLine;
    }

    public void setTaxLine(String taxLine) {
        this.taxLine = taxLine;
    }

    public String getExtraLine() {
        return extraLine;
    }

    public void setExtraLine(String extraLine) {
        this.extraLine = extraLine;
    }

    public void setWaiter_id(int waiter_id) {
        this.waiter_id = waiter_id;
    }

    public int getWaiter_id() {
        return waiter_id;
    }
}
