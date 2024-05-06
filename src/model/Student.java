package model;

import java.util.Set;

public class Student {
    private String studentId;
    private String studentName;
    private String studentState;
    private Set<String> studentSubject;

    public Student(String seq, String studentName, String studentState, Set<String> studentSubject) {
        this.studentId = seq;
        this.studentName = studentName;
        this.studentState = studentState;
        this.studentSubject = studentSubject;
    }

    // Getter
    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentState() { return studentState;  }

    public Set<String> getStudentSubject() { return studentSubject; }

    // Setter
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public void setStudentName(String studentName) { this.studentName = studentName; }

    public void setStudentState(String studentState) { this.studentState = studentState; }

    public void setStudentSubject(Set<String> studentSubject) { this.studentSubject = studentSubject; }

}
