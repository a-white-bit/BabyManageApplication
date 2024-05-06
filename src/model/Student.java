package model;

public class Student {
    private String studentId;
    private String studentName;
    private String studentState;

    public Student(String seq, String studentName, String studentCondition) {
        this.studentId = seq;
        this.studentName = studentName;
        this.studentState = studentState;
    }
    // Getter
    public String getStudentId() {
        return studentId;
    }
    public String getStudentName() {
        return studentName;
    }
    public String getStudentState() {
        return studentState;
    }
//    public String studentState() {
//        if (studentState.equals("1")){
//    } else if (studentState.equals("2")){
//        } else if (studentState.equals("3")){
//        }; return studentState;
//    }
}
