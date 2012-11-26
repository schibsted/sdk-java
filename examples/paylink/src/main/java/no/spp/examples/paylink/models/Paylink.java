package no.spp.examples.paylink.models;

import org.apache.wicket.util.io.IClusterable;

public class Paylink implements IClusterable {

    private String title;
    private String description;
    private String flow;
    private Integer price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String toString() {
        return "Paylink(" + title + "," + flow + "," + description + "," + price + ")";
    }
}