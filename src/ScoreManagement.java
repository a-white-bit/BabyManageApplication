import model.Score;
import model.Student;
import model.Subject;

import java.util.*;

public class ScoreManagement {
    // 스캐너
    private static final Scanner sc = new Scanner(System.in);

    // 데이터 저장소
    private static Map<String, Student> studentStore;
    private static Map<String, Subject> subjectStore;
    private static Map<String, Score> scoreStore;

    // 과목 타입(필수, 선택)
    private static String SUBJECT_TYPE_MANDATORY;
    private static String SUBJECT_TYPE_CHOICE;

    //index관리 필드
    private static int scoreIndex;
    private static final String INDEX_TYPE_SCORE = "SC";

    // 학생 상태 표현 리스트
    private static List<String> stateList = new ArrayList<>();

    public ScoreManagement(Map<String, Student>studentStore, Map<String, Subject> subjectStore, Map<String, Score> scoreStore, String Mandatory, String Choice, List<String> stateList){
        ScoreManagement.studentStore = studentStore;
        ScoreManagement.subjectStore = subjectStore;
        ScoreManagement.scoreStore = scoreStore;
        ScoreManagement.SUBJECT_TYPE_MANDATORY = Mandatory;
        ScoreManagement.SUBJECT_TYPE_CHOICE = Choice;
        ScoreManagement.stateList = stateList;
    }

    static void displayScoreView() {
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
            int input = sc.nextInt();

            switch (input) {
                case 1 -> createScore(); // 수강생의 과목별 시험 회차 및 점수 등록
                case 2 -> updateRoundScoreBySubject(); // 수강생의 과목별 회차 점수 수정
                case 3 -> inquireRoundGradeBySubject(); // 수강생의 특정 과목 회차별 등급 조회
                case 4 -> inquireAvgGrades(); // 수강생의 과목별 평균 등급 조회
                case 5 -> inquireMandatoryAvgGradeByStudentState(); // 특정 상태 수강생들의 필수 과목 평균 등급 조회
                case 6 -> flag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n메인 화면 이동...");
                    flag = false;
                }
            }
        }
    }

    private static String getStudentId() {
        System.out.print("\n관리할 수강생의 번호를 입력하시오...");
        // 잘못된 Id 입력 처리 필요
        String studentId = sc.next();
        if (studentStore.get(studentId) == null) {
            studentId = "";
        }
        return studentId;
    }

    // 수강생의 과목별 시험 회차 및 점수 등록
    private static void createScore() {
        /*
         * 회차 번호(Score 클래스의 roundNumber 멤버변수)가 1 ~ 10 값만 저장되어야 합니다.  * 1회차 ~ 10회차
         * 등록하려는 점수(Score 클래스의 studentScore 멤버변수)가 0 ~ 100 값만 저장되어야 합니다.
         * 등록하려는 과목의 회차 점수가 이미 등록되어 있다면 등록할 수 없습니다. (Score 클래스에서
         * studentId, subjectId, roundNumber 이 세 멤버변수가 "모두 일치"하는 score 객체가 존재한다면 등록하면 안됩니다.)
         */

        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        if ("".equals(studentId)) {
            System.out.println("등록되지 않은 학생 ID입니다. 되돌아갑니다..");
            return;
        }
        System.out.println("시험 점수를 등록합니다...");

        // 등록할 과목 이름 리스트
        Set<String> studentSubjects = studentStore.get(studentId).getStudentSubject();
        Set<String> studentSubjectNames = new HashSet<>();
        for (String id : studentSubjects) {
            String name = subjectStore.get(id).getSubjectName();
            studentSubjectNames.add(name);
        }
        String subjectName = "";
        String subjectId = null;
        while (true) {
            System.out.println("등록할 과목 선택:");
            System.out.print(studentSubjectNames + " ");
            subjectName = sc.next();
            if (studentSubjectNames.contains(subjectName)) {
                subjectId = StudentManagement.getSubjectIdByName(subjectName);
            }

            if (subjectId == null) {
                System.out.println("등록된 수강 과목이 아닙니다. 다시 입력해주세요.");
            } else {
                break;
            }
        }

        while (true) {
            System.out.println("회차(1~10) 입력: ");
            int roundNumber = Integer.parseInt(sc.next());
            if (1 <= roundNumber && roundNumber <= 10) {
                System.out.println("점수(0~100) 입력: ");
                int studentScore = Integer.parseInt(sc.next());
                if (0 <= studentScore && studentScore <= 100) {
                    // 점수 ID 시퀀스 생성
                    String scoreId = CampManagementApplication.sequence(INDEX_TYPE_SCORE, scoreIndex);
                    scoreIndex++;
                    // 점수 객체 생성
                    Score score = new Score(scoreId, studentId, subjectId, roundNumber, studentScore);
                    if (SUBJECT_TYPE_CHOICE.equals(subjectStore.get(subjectId).getSubjectType()))
                        score.setGradeChoiceByScore();
                    else if (SUBJECT_TYPE_MANDATORY.equals(subjectStore.get(subjectId).getSubjectType()))
                        score.setGradeMandatoryByScore();

                    // 점수 등록
                    scoreStore.put(scoreId, score);
                    System.out.println("\n점수 등록 성공!");
                    break;
                } else {
                    // 잘못된 입력 (0~100 이외의 입력) 처리
                    System.out.println("알맞지 않은 숫자입니다.");
                    continue;
                }
            } else {
                // 잘못된 입력 (1~10 이외의 입력) 처리
                System.out.println("알맞지 않은 숫자입니다");
                continue;
            }
        }
    }

    // 수강생의 과목별 회차 점수 수정
    private static void updateRoundScoreBySubject() {
        // 관리할 수강생 고유 번호
        String studentId = getStudentId();
        System.out.println(studentId);
        //getStudentId 메소드에서의 studentId값이 null일때 리턴시켜주는 조건문
        if ("".equals(studentId)) {
            System.out.println("등록되지 않은 학생 ID입니다. 되돌아갑니다..");
            return;
        }
        System.out.println("시험 점수를 수정합니다...");
        Set<String> studentSubjects = studentStore.get(studentId).getStudentSubject();
        // stream을 이용한 subjectID를 Name로변경
        List<String> studentSubjectNames = studentSubjects.stream().map(x -> {
            return x = subjectStore.get(x).getSubjectName();
        }).toList();

        for (Map.Entry<String, Student> entryset : studentStore.entrySet()) {
            System.out.print(studentSubjectNames + "\n수정할 과목을 입력해주세요 : ");
            // 수정할 과목 이름 입력받기
            String subjectName = sc.next();
            System.out.print("수정할 회차를 선택해주세요 : ");
            // 수정할 과목 회차 입력받기,
            int roundNumber = Integer.parseInt(sc.next());

            for (Score score : scoreStore.values()) {
                if (score.getStudentId().equals(studentId)
                        && score.getSubjectId().equals(StudentManagement.getSubjectIdByName(subjectName))
                        && score.getRoundNumber() == roundNumber) {
                    System.out.print("바꿀 점수를 입력해주세요 : ");
                    int changeScore = Integer.parseInt(sc.next());
                    score.setStudentScore(changeScore);
                    System.out.println("바뀐 점수 : " + changeScore);
                }
            }
            System.out.println("\n점수 수정 성공!");
        }
    }

    // 수강생의 특정 과목 회차별 등급 조회
    private static void inquireRoundGradeBySubject() {
        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        System.out.println("회차별 등급을 조회합니다...");

        // 기능 구현 (조회할 특정 과목)
        // 1. 과목 이름 입력받기
        // stream lambda 수정하기**
        Set<String> subjects = studentStore.get(studentId).getStudentSubject();
        List<String> studentSubjectNames = subjects.stream().map(x -> {
            return x = subjectStore.get(x).getSubjectName();
        }).toList();
        System.out.print("과목을 입력해주세요 " + studentSubjectNames + "\n과목 입력 : ");
        String studentSubject = sc.next();

        // 이상한 값 안 받기 위한 코드 짜기
        // 유효한 과목 ID를 저장할 변수 초기화
        String subjectId = null;
        // 사용자가 유효한 과목 이름을 입력할 때까지 반복
        while (subjectId == null) {
            // 사용자가 입력한 과목 이름을 사용하여 해당 과목의 ID를 가져옴
            subjectId = StudentManagement.getSubjectIdByName(studentSubject);
            // 만약 입력된 과목 이름이 유효하지 않으면 다시 입력 요청
            if (subjectId == null) {
                // 잘못된 입력임을 알리는 메시지 출력
                System.out.println("잘못된 과목입니다. 다시 입력하세요.");
                // 사용자가 선택할 수 있는 유효한 과목 목록을 출력하고 다시 입력 요청
                System.out.print("과목을 입력해주세요 " + studentSubjectNames + "\n과목 입력 : ");
                studentSubject = sc.next();
            }
        }

        // 2. 시험본 회차 입력받기
        System.out.println("시험본 회차를 입력해주세요 : ");
        int examRound;
        while (true) {
            try {
                examRound = Integer.parseInt(sc.next());
                break;
            } catch (NumberFormatException e) {
                System.out.print("올바른 숫자를 입력해주세요: ");
            }
        }

        // 3. scoreStore를 돌면서 [studentId, subjectId, examRound] 이 세가지가 일치하는 score 객체 찾기
        // scoreStore에 있는 각 Score 객체에 대해 다음 작업을 수행합니다.
        for (Score score : scoreStore.values()) {
            //[studentId, 과목이름, 회차] 이 세가지가 일치하는 score 객체 찾기
            if (score.getStudentId().equals(studentId)
                    && score.getSubjectId().equals(StudentManagement.getSubjectIdByName(studentSubject))
                    && score.getRoundNumber() == examRound) {
                // 4. score 객체의 studentGrade 멤버변수 출력
                System.out.println("수강생 " + studentStore.get(studentId).getStudentName() + "의 " + studentSubject + " 과목의 " + examRound + "회차 등급은 " + score.getStudentGrade() + "입니다.");
                return;
            }
        }
        // 3번에서 일치하는 score 객체를 찾지 못한 경우
        System.out.println("해당하는 정보를 찾을 수 없습니다.");
    }

    // 수강생의 과목별 평균 등급 조회
    private static void inquireAvgGrades() {
        String studentId = getStudentId(); // 과목별 평균 등급을 보고싶은 수강생ID 입력
        System.out.println("과목별 평균 등급을 조회합니다...");

        Set<String> subjects = new HashSet<>();

        for (Map.Entry<String, Student> entry : studentStore.entrySet()) {
            if (entry.getKey().equals(studentId)) {
                subjects = entry.getValue().getStudentSubject();
            }
        }

        for (String str : subjects) {
            String subjectName = subjectStore.get(str).getSubjectName();
            String grade = settingGrade(studentId, str);

            if (!"none".equals(grade)) {
                System.out.println(subjectName + " 과목 평균 등급 : " + grade);
            }
        }

        System.out.println("\n평균 등급 조회 성공!");
    }

    // 특정 학생의 특정 과목 평균 등급 조회
    private static String settingGrade(String studentId, String subjectId) {
        String subjectType = subjectStore.get(subjectId).getSubjectType();
        List<Integer> scoreList = new ArrayList<>();
        String grade = "";

        for (Map.Entry<String, Score> entry : scoreStore.entrySet()) {
            if (studentId.equals(entry.getValue().getStudentId())
                    && subjectId.equals(entry.getValue().getSubjectId())) {
                scoreList.add(entry.getValue().getStudentScore());
            }
        }

        if (scoreList.isEmpty()) {
            grade = "none";
        } else {
            if (subjectType.equals(SUBJECT_TYPE_CHOICE)) {
                grade = Score.getGradeChoiceByScore(scoreList);
            } else {
                grade = Score.getGradeMandatoryByScore(scoreList);
            }
        }

        return grade;
    }

    // 특정 상태 수강생들의 필수 과목 평균 등급 조회
    private static void inquireMandatoryAvgGradeByStudentState() {
        String studentState;
        while (true) {
            System.out.println("조회하고싶은 수강생들의 상태를 입력: ");
            studentState = sc.next();
            if (stateList.contains(studentState)) {
                break;
            }
            System.out.println("상태가 [" + studentState + "]인 수강생들의 필수 과목 평균 등급을 조회합니다...");

            // studentStore 맵을 돌면서 studentState가 일치하는 student 객체를 리스트에 저장
            List<Student> studentByState = new ArrayList<>();
            for (Student student : studentStore.values()) {
                if (student.getStudentState().equals(studentState)) {
                    studentByState.add(student);
                }
            }

            // 특정 상태 수강생들(studentByState)의 모든 점수 ID를 리스트에 저장
            List<String> scoreId = new ArrayList<String>();
            for (Student student : studentByState) {
                for (Score score : scoreStore.values()) {
                    if (Objects.equals(score.getStudentId(), student.getStudentId())) {
                        scoreId.add(score.getScoreId());
                    }
                }
            }

            // 저장한 점수들 중에 과목 타입이 필수 과목인 것만 저장
            // 필수 과목들의 회차별 점수들 모두 scores 리스트에 담기
            String getId;
            List<Integer> scores = new ArrayList<>();
            for (String id : scoreId) {
                getId = scoreStore.get(id).getSubjectId();
                if (SUBJECT_TYPE_MANDATORY.equals(subjectStore.get(getId).getSubjectType())) {
                    scores.add(scoreStore.get(id).getStudentScore());
                }
            }

            // 평균등급 계산
            String avgGrade = Score.getGradeMandatoryByScore(scores);
            if (!"none".equals(avgGrade)) {
                System.out.println("필수 과목 평균 등급: " + avgGrade);
                System.out.println("\n필수 과목 평균 등급 조회 성공!");
            } else {
                System.out.println("조회할 점수가 없습니다.");
            }
        }
    }
}
