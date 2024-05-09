import model.Score;
import model.Student;
import model.Subject;

import java.util.*;

// updated 2024/05/09 11:00

/**
 * 구현 메모
 * -
 * 1. 숫자가 아닌 값 입력되면 오류로 프로그램 종료됨
 * 프로그램은 종료되지 않게 하고, 다시 값을 받게 하거나 수정단계만 빠져나가도록 하기
 * switch 변수를 String 형으로 작성  or 예외 try-catch
 */

public class CampManagementApplication {
    // 데이터 저장소
    private static Map<String, Student> studentStore;
    private static Map<String, Subject> subjectStore;
    private static Map<String, Score> scoreStore;

    // 과목 타입(필수, 선택)
    private static final String SUBJECT_TYPE_MANDATORY = "MANDATORY";
    private static final String SUBJECT_TYPE_CHOICE = "CHOICE";

    // 타입별 과목 리스트
    private static final List<String> subjectsMandatoryList = List.of("Java", "객체지향", "Spring", "JPA", "MySQL");
    private static final List<String> subjectsChoiceList = List.of("디자인 패턴", "Spring Security", "Redis", "MongoDB");

    // index 관리 필드
    private static int subjectIndex;
    private static final String INDEX_TYPE_SUBJECT = "SU";

    // 학생 상태 분류
    private static final String STUDENT_STATE_VERYGOOD = "아주좋음";
    private static final String STUDENT_STATE_GOOD = "좋음";
    private static final String STUDENT_STATE_NORMAL = "보통";
    private static final String STUDENT_STATE_BAD = "나쁨";
    private static final String STUDENT_STATE_VERYBAD = "아주나쁨";

    // 학생 상태 표현 리스트
    private static final List<String> stateList = List.of(
            STUDENT_STATE_VERYGOOD,
            STUDENT_STATE_GOOD,
            STUDENT_STATE_NORMAL,
            STUDENT_STATE_BAD,
            STUDENT_STATE_VERYBAD);

    // 스캐너
    private static final Scanner sc = new Scanner(System.in);

    private static StudentManagement studentManagement;
    private static ScoreManagement scoreManagement;

    public static void main(String[] args) {
        setInitData();
        studentManagement = new StudentManagement(studentStore, subjectStore, scoreStore, SUBJECT_TYPE_MANDATORY, SUBJECT_TYPE_CHOICE, subjectsMandatoryList, subjectsChoiceList, stateList);
        scoreManagement = new ScoreManagement(studentStore, subjectStore, scoreStore, SUBJECT_TYPE_MANDATORY, SUBJECT_TYPE_CHOICE, stateList);
        try {
            displayMainView();
        } catch (Exception e) {
            System.out.println("\n오류 발생!\n프로그램을 종료합니다.");
        }
    }

    // 초기 데이터 생성
    private static void setInitData() {
        studentStore = new HashMap<>();
        subjectStore = new HashMap<>();
        setSubjectList(subjectsMandatoryList, SUBJECT_TYPE_MANDATORY);
        setSubjectList(subjectsChoiceList, SUBJECT_TYPE_CHOICE);
        scoreStore = new HashMap<>();
    }

    // 과목 리스트 설정
    private static void setSubjectList(List<String> subjects, String type) {
        String subjectSeq = "";

        for (String subject : subjects) {
            subjectSeq = sequence(INDEX_TYPE_SUBJECT, subjectIndex);
            subjectIndex++;
            subjectStore.put(subjectSeq, new Subject(subjectSeq, subject, type));
        }
    }

    // index 자동 증가
    public static String sequence(String type, int index) {
        return type + index;
    }

    private static void displayMainView() throws InterruptedException {
        boolean flag = true;
        while (flag) {
            System.out.println("\n==================================");
            System.out.println("내일배움캠프 수강생 관리 프로그램 실행 중...");
            System.out.println("1. 수강생 관리");
            System.out.println("2. 점수 관리");
            System.out.println("3. 프로그램 종료");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();

            switch (input) {
                case 1 -> StudentManagement.displayStudentView(); // 수강생 관리
                case 2 -> ScoreManagement.displayScoreView(); // 점수 관리
                case 3 -> flag = false; // 프로그램 종료
                default -> {
                    System.out.println("잘못된 입력입니다.\n되돌아갑니다!");
                    Thread.sleep(2000);
                }
            }
        }
        System.out.println("프로그램을 종료합니다.");
    }
}