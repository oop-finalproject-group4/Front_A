package application;


public class RentRecord {
    private String date;
    private double start_lat;
    private double end_lat; 
    private double start_lng;
    private double end_lng; 
    private String startTime;
    private String endTime;
    private double totalMinutes;
    private int chargeCount;
    private int bill;
    private double distance;
    private String usedCoupon;

    // Getters and Setters

    public String getDate() {
        return date;
    }

    public void setStart_lat(double start_lat) {
        this.start_lat = start_lat;
    }

    public void setStart_lng(double start_lng) {
        this.start_lng = start_lng;
    }

    public void setEnd_lat(double end_lat) {
        this.end_lat = end_lat;
    }

    public void setEnd_lng(double end_lng) {
        this.end_lng = end_lng;
    }
    public double getStart_lat() {
        return start_lat;
    }

    public double getStart_lng() {
        return start_lng;
    }

    public double getEnd_lat() {
        return end_lat;
    }

    public double getEnd_lng() {
        return end_lng;
    }



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(double totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getChargeCount() {
        return chargeCount;
    }

    public void setChargeCount(int chargeCount) {
        this.chargeCount = chargeCount;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getUsedCoupon() {
        return usedCoupon;
    }

    public void setUsedCoupon(boolean usedCoupon) {
        if (usedCoupon) {
            this.usedCoupon = "Yes";
        } else {
            this.usedCoupon = "No";
        }
    }
}
