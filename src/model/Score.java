package model;

public class Score {
    private String scoreId;
    private String studentId;
    private String subjectId;
    private int roundNumber;
    private int studentScore;
    private String studentGrade;

    public Score(String seq, String studentId, String subjectId, int roundNumber, int studentScore) {
        this.scoreId = seq;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.roundNumber = roundNumber;
        this.studentScore = studentScore;
    }


    public void setGradeMandatoryScore() {
        studentGrade = (studentScore < 60) ? "N" :
                (studentScore < 70) ? "F" :
                        (studentScore < 80) ? "D" :
                                (studentScore < 90) ? "C" :
                                        (studentScore < 95) ? "B" : "A";
    }

    public void setGradeChoiceScore() {
        studentGrade = (studentScore < 50) ? "N" :
                (studentScore < 60) ? "F" :
                        (studentScore < 70) ? "D" :
                                (studentScore < 80) ? "C" :
                                        (studentScore < 90) ? "B" : "A";
    }

    // Getter
    public String getScoreId() {
        return scoreId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getStudentScore() {
        return studentScore;
    }

    public String getStudentGrade() {
        return studentGrade;
    }


    // Setter
    public void setStudentScore(int studentScore) {
        this.studentScore = studentScore;
    }

}