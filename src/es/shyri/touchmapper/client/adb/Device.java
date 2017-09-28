package es.shyri.touchmapper.client.adb;

/**
 * Created by shyri on 12/09/17.
 */
public class Device {
    private String id;
    private String name;

    Device(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
