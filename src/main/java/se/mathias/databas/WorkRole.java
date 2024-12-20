package se.mathias.databas;

import java.util.Date;

public class WorkRole {
    private int roleId;
    private String title;
    private String description;
    private double salary;
    private Date creationDate;


    public WorkRole(Integer roleId, String title, String description, double salary, java.sql.Date creationDate) {
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.creationDate = creationDate;

    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
