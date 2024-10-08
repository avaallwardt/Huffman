package org.example;

import java.util.Comparator;

public class StudentComparator implements Comparator<Student> {
    @Override // always use @override with implements -- way of getting past with subclassing where you can only have 1 subclass
    public int compare(Student o1, Student o2) {
        return o1.getID().compareTo(o2.getID());
    }
    // the implemented class methods are always empty
    // if you implement an interface, you are required to use the methods in the interface
    // must use a comparator if you use a priority queue

}
