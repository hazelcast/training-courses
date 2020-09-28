package hazelcast;

import java.io.Serializable;

public class Employee implements Serializable {
    private int salary;
    private int age;

    public Employee( int age,int salary) {
        this.salary = salary;
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
}
