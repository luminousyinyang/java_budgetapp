package budgetapp.java_budgetapp.HelperClasses;

public class TransactionClass {
    private int TransID;
    private String TransName;
    private String TransCategory;
    private String TransDate;
    private Double TransAmt;

    public TransactionClass(int TransID, String TransName, String TransCategory, String TransDate, Double TransAmt) {
        this.TransID = TransID;
        this.TransName = TransName;
        this.TransCategory = TransCategory;
        this.TransDate = TransDate;
        this.TransAmt = TransAmt;
    }

    public int getTransID() {
        return TransID;
    }

    public String getTransName() {
        return TransName;
    }

    public String getTransCategory() {
        return TransCategory;
    }

    public String getTransDate() {
        return TransDate;
    }

    public Double getTransAmt() {
        return TransAmt;
    }
}
