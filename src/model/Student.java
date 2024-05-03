package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student {
    // HashMap 추가
    Map<String, String> strMap = new HashMap<>();

    private String studentId;
    private String studentName;
    //학생 과목 추가
    private String studentSubject;

    public Student(String seq, String studentName, String studentSubject) {
        this.studentId = seq;
        this.studentName = studentName;
        this.studentSubject = studentSubject;

        this.strMap.put(studentId, studentName);
    }

    // Getter
    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentSubject() {
        return studentSubject;
    }
}