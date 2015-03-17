package model.json;

/**
 * Bean containing the information about a sink (motes acting as sink).
 * Created by Mathieu on 23/01/2015.
 */
public class Sink {
    /**
     * Version number of the DODAG in the RPL protocol (see the RFC 6650 for further precisions :
     * https://tools.ietf.org/html/rfc6550 ).
     */
    private int dodagVersionNumber;
    /**
     * ID of the mote.
     */
    private int id;
    /**
     * IP address version 6 of the sink.
     */
    private String ipv6;
    /**
     * MAC address of the sink.
     */
    private String mac;
    /**
     * Latitude of the geographical position of the sink.
     */
    private double lat;
    /**
     * Longitude of the geographical position of the sink.
     */
    private double lon;

    /**
     * Returns the DODAG version number of the sink.
     * @return the DODAG version number of the sink.
     */
    public int getDodagVersionNumber() {
        return dodagVersionNumber;
    }

    /**
     * Sets the DODAG version number of the sink.
     * @param dodagVersionNumber the new version number.
     */
    public void setDodagVersionNumber(int dodagVersionNumber) {
        this.dodagVersionNumber = dodagVersionNumber;
    }

    /**
     * Returns the sink's ID.
     * @return sink's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Changes the sink's ID.
     * @param id the new ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the IPv6 of the sink.
     * @return sink's IPv6.
     */
    public String getIpv6() {
        return ipv6;
    }

    /**
     * Changes the IPv6 of the sink.
     * @param ipv6 the new address.
     */
    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    /**
     * Returns the MAC address of the sink.
     * @return the sink's MAC address.
     */
    public String getMac() {
        return mac;
    }

    /**
     * Sets a new MAC address for the sink.
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
