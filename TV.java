package MeresaMeron_Lab2;
// this represents a Television

public class TV implements FuelUsed {
    //screen size in inches
    private double screenSize; 
    //is this flat screen TV or not
    private boolean isFlat; 
    //supported video type (HD, FHD, QHD, 2K etc)
    private String videoType; 

    //constructor

    public TV(double screenSize, boolean isFlat, String videoType) {

        this.screenSize = screenSize;

        this.isFlat = isFlat;

        this.videoType = videoType;

    }

    //getters and setters
    /*
    
    */
    public double getScreenSize() {

        return screenSize;

    }
    /*
    
    */
    public void setScreenSize(double screenSize) {

        this.screenSize = screenSize;

    }

    public boolean isFlat() {

        return isFlat;

    }

    public void setFlat(boolean isFlat) {

        this.isFlat = isFlat;

    }

    public String getVideoType() {

        return videoType;

    }

    public void setVideoType(String videoType) {

        this.videoType = videoType;

    }

    //overriden methods

    @Override

    public boolean needsElectricity() {
        //needs electricity to work
        return true; 

    }

    @Override

    public boolean needsHeat() {
        //does not need heat
        return false; 

    }

    @Override

    public boolean needsWater() {
        //does not need water
        return false; 

    }
    
    @Override

    public String toString() {

        return "TV{" + "screenSize=" + screenSize + ", isFlat=" + isFlat + ", videoType=" + videoType + '}';

    }

}
