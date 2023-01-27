package budgetapp.java_budgetapp.HelperClasses;

public class RecurringClass {
    private int RecurrID;
    private String RecurrStartDate;
    private Double RecurrAmount;
    private String RecurrMorY;
    private String RecurrName;
    private String RecurrCat;

    public RecurringClass(int RecurrID, String RecurrName, String RecurrCat, String RecurrStartDate, String RecurrMorY, Double RecurrAmount) {
        this.RecurrID = RecurrID;
        this.RecurrName = RecurrName;
        this.RecurrCat = RecurrCat;
        this.RecurrStartDate = RecurrStartDate;
        this.RecurrAmount = RecurrAmount;
        this.RecurrMorY = RecurrMorY;

    }

    public int getRecurrID() {
        return RecurrID;
    }

    public String getRecurrCat() {
        return RecurrCat;
    }
    public String getRecurrStartDate() {
        return RecurrStartDate;
    }

    public Double getRecurrAmount() {
        return RecurrAmount;
    }

    public String getRecurrMorY() {
        return RecurrMorY;
    }

    public String getRecurrName() {
        return RecurrName;
    }
}
