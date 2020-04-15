package com.hazelcast.inv;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

public class Inventory implements Serializable /*, IdentifiedDataSerializable*/ {

    private String sku;
    private String description;
    private String location;
    private Character locationType; // W=Warehouse, S=store
    private int quantity;

    private Random random = new Random();


    public void setSKU(String sku) { this.sku = sku; };
    public String getSKU() { return sku; }

    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return description; }

    public void setLocation(String location) { this.location = location; }
    public String getLocation() { return location; }

    public void setLocationType(Character lt) { this.locationType = lt; }
    public Character getLocationType() { return locationType; }

    public void setQuantity(int qty) { this.quantity = qty; }
    public int getQuantity() { return quantity; }

    // Create a random entry for the given primary key (compound)
    public Inventory(String sku, char locationType, String location) {
        this.sku = sku;
        this.locationType = locationType;
        this.location = location;
        this.description = sku + " @ " + locationType + location;
        this.quantity = random.nextInt(1000);
    }

    // No arg constructor used when creating from JDBC ResultSet
    public Inventory() {}


    public Inventory(String sku, String description, String location, char locationType, int quantity) {
        this.sku = sku;
        this.description = description;
        this.location = location;
        this.locationType = locationType;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return sku + " [" + description + "] " + locationType + location + " " + quantity;
    }


    ////////////////
    // IdentifiedDataSerializable implementation
    ////////////////

    public int getFactoryId() {
        return IDSFactory.FACTORY_ID;
    }

    public int getId() {
        return IDSFactory.IDS_INVENTORY;
    }

    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(sku);
        objectDataOutput.writeUTF(description);
        objectDataOutput.writeUTF(location);
        objectDataOutput.writeChar(locationType);
        objectDataOutput.writeInt(quantity);
    }

    public void readData(ObjectDataInput objectDataInput) throws IOException {
        sku = objectDataInput.readUTF();
        description = objectDataInput.readUTF();
        location = objectDataInput.readUTF();
        locationType = objectDataInput.readChar();
        quantity = objectDataInput.readInt();
    }
}
