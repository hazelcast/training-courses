package com.hztraining.inv;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.io.Serializable;

/** Inventory database and IMap have a compound key consisting of SKU and Location */
public class InventoryKey implements Serializable {
    private String sku;
    private String location;

    public InventoryKey(String sku, String location) {
        this.sku = sku;
        this.location = location;
    }

    // no-arg constructor required by IDSFactory
    public InventoryKey() {}

    public String sku() { return sku; }
    public String location() { return location; }

    @Override
    public String toString() {
        return sku + location;
    }

    // IdentifiedDataSerializable implementation (not in use at the moment)

    public int getFactoryId() {
        return IDSFactory.FACTORY_ID;
    }

    public int getId() {
        return IDSFactory.IDS_INVENTORY_KEY;
    }

    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(sku);
        objectDataOutput.writeUTF(location);
    }

    public void readData(ObjectDataInput objectDataInput) throws IOException {
        sku = objectDataInput.readUTF();
        location = objectDataInput.readUTF();
    }
}
