package RMI;

import java.io.Serializable;

public class Employee implements Serializable {

    private static final long serialVersionUID = 20241119L;

    private String id;
    private String name;
    private double baseSalary;
    private int experienceYears;
    private double finalSalary;

    public Employee() {}

    public Employee(String id, String name,
                    double baseSalary, int experienceYears) {
        this.id = id;
        this.name = name;
        this.baseSalary = baseSalary;
        this.experienceYears = experienceYears;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getBaseSalary() { return baseSalary; }
    public int getExperienceYears() { return experienceYears; }
    public double getFinalSalary() { return finalSalary; }

    public void setFinalSalary(double finalSalary) {
        this.finalSalary = finalSalary;
    }
}