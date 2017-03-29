package entities;

/**
 * Created by uizen on 3/29/2017.
 */

public class Settings {
    private String printerIp, nameLine1, nameLine2, addrLine1, addrLine2, telLine, taxLine, extraLine;

    public Settings(String printerIp, String nameLine1, String nameLine2, String addrLine1, String addrLine2, String telLine, String taxLine, String extraLine) {
        this.printerIp = printerIp;
        this.nameLine1 = nameLine1;
        this.nameLine2 = nameLine2;
        this.addrLine1 = addrLine1;
        this.addrLine2 = addrLine2;
        this.telLine = telLine;
        this.taxLine = taxLine;
        this.extraLine = extraLine;
    }

    public String getNameLine2() {
        return nameLine2;
    }

    public void setNameLine2(String nameLine2) {
        this.nameLine2 = nameLine2;
    }

    public String getPrinterIp() {
        return printerIp;
    }

    public void setPrinterIp(String printerIp) {
        this.printerIp = printerIp;
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
}
