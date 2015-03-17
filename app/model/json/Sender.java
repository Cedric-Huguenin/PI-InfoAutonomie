package model.json;

/**
 * Bean containing the information about a data sender (motes acting as sender).
 * Created by Mathieu on 23/01/2015.
 */
public class Sender {
    /**
     * ID of the sender.
     */
    private int id;
    /**
     * IP address version 6 of the sender.
     */
    private String ipv6;
    /**
     * MAC address of the sender.
     */
    private String mac;
    /**
     * Latitude of the geographical position of the sender.
     */
    private double lat;
    /**
     * Longitude of the geographical position of the sender.
     */
    private double lon;

    /**
     * Returns the sender's ID.
     * @return sender's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Changes the sender ID.
     * @param id the new ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the IPv6 of the sender.
     * @return sender's IPv6.
     */
    public String getIpv6() {
        return ipv6;
    }

    /**
     * Changes the IPv6 of the sender.
     * @param ipv6 the new address.
     */
    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    /**
     * Returns the MAC address of the sender.
     * @return the sender's MAC address.
     */
    public String getMac() {
        return mac;
    }

    /**
     * Sets a new MAC address for the sender.
     * @param mac new address.
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * Returns the latitude of the mote.
     * @return the latitude of the mote.
     */
    public double getLat() {
        return lat;
    }

    /**
     * Sets the latitude of the mote.
     * @param lat the new latitude.
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Returns the longitude of the mote.
     * @return the longitude of the mote.
     */
    public double getLon() {
        return lon;
    }

    /**
     * Sets the longitude of the mote.
     * @param lon the new longitude.
     */
    public void setLon(double lon) {
        this.lon = lon;
    }
}
