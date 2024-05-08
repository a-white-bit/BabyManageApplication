package model;

import java.util.List;

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

    // Setter
    public void setStudentScore(int studentScore) {
        this.studentScore = studentScore;
    }

    public void setGradeChoiceByScore() {
        this.studentGrade = getGradeChoiceByScore(List.of(this.studentScore));
    }

    public void setGradeMandatoryByScore() {
        this.studentGrade = getGradeMandatoryByScore(List.of(this.studentScore));
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

    public static String getGradeMandatoryByScore(List<Integer> scores) {
        String grade = "none";
        int avg = (int)scores.stream().mapToInt(x -> x).average().orElse(-1);
        if (avg == -1) return grade;
        if (avg < 60) {
            grade = "N";
        }
        else if (avg < 70) {
            grade = "F";
        }
        else if (avg < 80) {
            grade = "D";
        }
        else if (avg < 90) {
            grade = "C";
        }
        else if (avg < 95) {
            grade = "B";
        }
        else if (avg <= 100) {
            grade = "A";
        }
        return grade;
    }

    public static String getGradeChoiceByScore(List<Integer> scores) {
        String grade = "none";
        int avg = (int) scores.stream().mapToInt(x -> x).average().orElse(-1);
        if (avg == -1) return grade;
        if (avg < 50) {
            grade = "N";
        }
        else if (avg < 60) {
            grade = "F";
        }
        else if (avg < 70) {
            grade = "D";
        }
        else if (avg < 80) {
            grade = "C";
        }
        else if (avg < 90) {
            grade = "B";
        }
        else if (avg <= 100){
            grade = "A";
        }
        return grade;
    }

}