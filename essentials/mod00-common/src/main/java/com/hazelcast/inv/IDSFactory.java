package com.hazelcast.inv;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class IDSFactory implements DataSerializableFactory {
    public static final int IDS_INVENTORY_KEY = 101;
    public static final int IDS_INVENTORY     = 102;
    public static final int FACTORY_ID        = 42;

    @Override
    public IdentifiedDataSerializable create(int i) {
        switch (i) {
            case IDS_INVENTORY_KEY:
                //return new InventoryKey();
            case IDS_INVENTORY:
                //return new Inventory();
            default:
                throw new IllegalArgumentException("IDSFactory: Unexpected object key " + i);
        }
    }
}
