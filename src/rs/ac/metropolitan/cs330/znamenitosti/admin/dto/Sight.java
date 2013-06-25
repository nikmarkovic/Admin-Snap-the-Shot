package rs.ac.metropolitan.cs330.znamenitosti.admin.dto;

/**
 *
 * @author nikola
 */
public class Sight {

    private Long id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private City city;

    public Sight() {
    }

    public Sight(String name, String description, double latitude, double longitude, City city) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public enum SightForPosting {

        INSTANCE;
        public Sight sight;
    }
}
