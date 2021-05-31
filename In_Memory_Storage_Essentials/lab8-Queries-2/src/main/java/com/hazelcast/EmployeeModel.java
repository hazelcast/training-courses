package com.hazelcast;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EmployeeModel implements DataSerializable {
    private String name;
    private int age;
    private double salary;
    private boolean active;

    public EmployeeModel() {
    }

    public EmployeeModel(String name, int age, double salary, boolean active) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeModel)) {
            return false;
        }
        EmployeeModel employeeModel = (EmployeeModel) o;
        return name == employeeModel.name &&
                age == employeeModel.age &&
                Objects.equals(name, employeeModel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, salary, active);
    }

    @Override
    public String toString() {
        return "EmployeeModel{" + "name='" + name + '\'' + ", age=" + age + ", salary=" + salary + ", active=" + active + '}';
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(age);
        out.writeDouble(salary);
        out.writeBoolean(active);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        // Simulating extremely slow deserialization
//        sleep(5);
        name = in.readUTF();
        age = in.readInt();
        salary = in.readDouble();
        active = in.readBoolean();
    }

    private void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }

    private static Faker faker = new Faker();

    public static EmployeeModel fake(){
        EmployeeModel result = new EmployeeModel();
        Name n = faker.name();
        result.setName(n.firstName() + " " + n.lastName());
        result.setAge(faker.number().numberBetween(1, 100));
        result.setSalary(faker.number().randomDouble(0,1000, 10000000));
        result.setActive(faker.bool().bool());

        return result;
    }
}
