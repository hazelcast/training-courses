package com.hztraining.inv;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.io.Serializable;

/** Inventory database and IMap have a compound key consisting of SKU and Location */
public class InventoryKey implements Serializable, IdentifiedDataSerializable {
    private String SKU;
    private String location;

    public InventoryKey(String sku, String location) {
        this.SKU = sku;
        this.location = location;
    }

    // no-arg constructor required by IDSFactory
    public InventoryKey() {}

    public String sku() { return SKU; }
    public String location() { return location; }

    @Override
    public String toString() {
        return SKU + location;
    }

    public int getFactoryId() {
        return IDSFactory.FACTORY_ID;
    }

    public int getId() {
        return IDSFactory.IDS_INVENTORY_KEY;
    }

    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(SKU);
        objectDataOutput.writeUTF(location);
    }

    public void readData(ObjectDataInput objectDataInput) throws IOException {
        SKU = objectDataInput.readUTF();
        location = objectDataInput.readUTF();
    }
}
