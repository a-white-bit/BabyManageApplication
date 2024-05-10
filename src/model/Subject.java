package model;

public class Subject {
    private String subjectId;
    private String subjectName;
    private SubjectType subjectType;


    public Subject(String seq, String subjectName, SubjectType subjectType) {
        this.subjectId = seq;
        this.subjectName = subjectName;
        this.subjectType = subjectType;
    }

    // Getter
    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public boolean isMandatory() {
        return subjectType.equals(SubjectType.SUBJECT_TYPE_MANDATORY);
    }

    public boolean isChoice() {
        return subjectType.equals(SubjectType.SUBJECT_TYPE_CHOICE);
    }

}