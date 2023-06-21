package application;

public class cars {
    private String no;
    private double lat;
    private double lng;
    private double power;
    private String carStatus;
    private boolean isRiding;
    
    
	cars(String no, double lat, double lng, double power, String carstatus, boolean isriding)
	{
		this.no = no;
		this.lat = lat;
		this.lng = lng;
		this.power = power;
		this.carStatus = carstatus;
		this.isRiding = isriding;
		
	}
	cars(){
		
	}

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public boolean isRiding() {
        return isRiding;
    }

    public void setRiding(boolean riding) {
        isRiding = riding;
    }

}


	
	
	
	

