import model.Score;
import model.Student;
import model.Subject;

import java.util.*;

// updated 2024/05/10 01:00

/**
 * 구현 메모
 *
 *
 *
 *
 */

public class CampManagementApplication {
    // 스캐너
    private static final Scanner sc = new Scanner(System.in);

    // 데이터 저장소
    private static Map<String, Student> studentStore;
    private static Map<String, Subject> subjectStore;
    private static Map<String, Score> scoreStore;


    public static void main(String[] args) {
        setInitData();
        try {
            displayMainView();
        } catch (Exception e) {
            System.out.println("\n오류 발생!\n프로그램을 종료합니다.");
        }
    }

    // 초기 데이터 생성
    private static void setInitData() {
        studentStore = StudentManagement.getStore();
        subjectStore = SubjectManagement.getStore();
        scoreStore = ScoreManagement.getStore();
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
            String input = sc.nextLine();

            switch (input) {
                case "1" -> displayStudentView(); // 수강생 관리
                case "2" -> displayScoreView(); // 점수 관리
                case "3" -> flag = false; // 프로그램 종료
                default -> {
                    System.out.println("잘못된 입력입니다.\n되돌아갑니다!");
                    Thread.sleep(800);
                }
            }
        }
        System.out.println("프로그램을 종료합니다.");
    }

    private static void displayStudentView() throws InterruptedException {
        boolean flag = true;
        while (flag) {
            System.out.println("==================================");
            System.out.println("수강생 관리 실행 중...");
            System.out.println("1. 수강생 등록");
            System.out.println("2. 수강생 조회");
            System.out.println("3. 수강생 정보 수정");
            System.out.println("4. 수강생 삭제");
            System.out.println("5. 메인 화면 이동");
            System.out.print("관리 항목을 선택하세요...");
            String input = sc.nextLine();

            switch (input) {
                case "1" -> StudentManagement.createStudent(); // 수강생 등록
                case "2" -> displayStudentListView(); // 수강생 목록 조회
                case "3" -> StudentManagement.updateStudent(); // 수강생 정보 수정
                case "4" -> StudentManagement.deleteStudent(); //수강생 삭제
                case "5" -> flag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n다시 입력해주세요...");
                    Thread.sleep(800);
                }
            }
        }
    }

    private static void displayStudentListView() throws InterruptedException {
        boolean flag = true;
        while (flag) {
            System.out.println("\n==================================");
            System.out.println("수강생 조회 실행 중...");
            System.out.println("1. 수강생 전체 목록");
            System.out.println("2. 상세 정보 조회");
            System.out.println("3. 상태별 목록 조회");
            System.out.println("4. 수강생 관리로 이동");
            System.out.print("조회 항목을 선택하세요...");
            String input = sc.nextLine();

            switch (input) {
                case "1" -> StudentManagement.inquireStudent(); // 전체 목록 조회
                case "2" -> StudentManagement.inquireStudentInfo(); // 상세 정보 조회
                case "3" -> StudentManagement.inquireStudentByState(); // 상태별 목록 조회
                case "4" -> flag = false; // 수강 관리 기능으로
                default -> {
                    System.out.println("잘못된 입력입니다.\n다시 입력해주세요..");
                    Thread.sleep(800);
                }
            }
        }
    }

    private static void displayScoreView() throws InterruptedException {
        boolean flag = true;
        while (flag) {
            System.out.println("==================================");
            System.out.println("점수 관리 실행 중...");
            System.out.println("1. 수강생의 과목별 시험 회차 및 점수 등록");
            System.out.println("2. 수강생의 과목별 회차 점수 수정");
            System.out.println("3. 수강생의 특정 과목 회차별 등급 조회");
            System.out.println("4. 수강생의 과목별 평균 등급 조회");
            System.out.println("5. 특정 상태 수강생들의 필수 과목 평균 등급 조회");
            System.out.println("6. 메인 화면 이동");
            System.out.print("관리 항목을 선택하세요...");
            String input = sc.nextLine();

            switch (input) {
                case "1" -> ScoreManagement.createScore(); // 수강생의 과목별 시험 회차 및 점수 등록
                case "2" -> ScoreManagement.updateRoundScoreBySubject(); // 수강생의 과목별 회차 점수 수정
                case "3" -> ScoreManagement.inquireRoundGradeBySubject(); // 수강생의 특정 과목 회차별 등급 조회
                case "4" -> ScoreManagement.inquireAvgGrades(); // 수강생의 과목별 평균 등급 조회
                case "5" -> ScoreManagement.inquireMandatoryAvgGradeByStudentState(); // 특정 상태 수강생들의 필수 과목 평균 등급 조회
                case "6" -> flag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n메인 화면 이동...");
                    Thread.sleep(800);
                }
            }
        }
    }
}