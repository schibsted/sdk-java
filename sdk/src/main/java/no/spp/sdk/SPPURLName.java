package no.spp.sdk;

/**
 * This is used to determine the what the type of URL to construct for VG Services.
 *
 * @author Martin Jonsson <martin.jonsson@gmail.com>
 */
public enum SPPURLName {
    API("api"),
    API_READ("api_read"),
    WWW("www");

    private String name;

    SPPURLName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}