package hazelcast;

import com.hazelcast.map.EntryProcessor;

import java.io.Serializable;
import java.util.Map;

public class SalaryIncreaseEntryProcessor implements Serializable, EntryProcessor<String, Employee, Object> {

    @Override
    public Object process(Map.Entry<String, Employee> entry) {
        Employee value = entry.getValue();
        value.incSalary(10);
        entry.setValue(value);
        return null;
    }
}