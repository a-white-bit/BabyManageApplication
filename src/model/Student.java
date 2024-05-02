package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
    private List<Map<String, Object>> studentList; // 과목을 List로 받기위해서 Map<String, Object>로 선언

    public Student() {
        studentList = new ArrayList<>();
    }

    public List<Map<String, Object>> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Map<String, Object>> studentList) {
        this.studentList = studentList;
    }
}
