package hazelcast;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableFactory;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;

import java.io.IOException;

public class Employee implements Portable {

    public static final int CLASS_ID = 1;

    public static final int FACTORY_ID = 5;
    private int salary;
    private int age;

    public Employee( int age,int salary) {
        this.salary = salary;
        this.age = age;
    }

    public Employee() {
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public int getSalary() {
        return salary;
    }

    public void incSalary(int delta) {
        salary += delta;
    }

    @Override
    public int getFactoryId() {
        return FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return CLASS_ID;
    }

    @Override
    public void writePortable(PortableWriter portableWriter) throws IOException {
        portableWriter.writeInt("age", age);
        portableWriter.writeInt("salary", salary);
    }

    @Override
    public void readPortable(PortableReader portableReader) throws IOException {
        age = portableReader.readInt("age");
        salary = portableReader.readInt("salary");
    }

    public static final class EmployeeFactory implements PortableFactory{

        @Override
        public Portable create(int classId) {
            if (Employee.CLASS_ID == classId)
                return new Employee();
            else
                return null;
        }
    }

    @Override
    public String toString(){
        return "Employee with age: " + age + " and salary: " + salary;
    }
}

