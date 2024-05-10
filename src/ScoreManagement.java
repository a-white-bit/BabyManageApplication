import model.Score;
import model.Student;
import model.Subject;
import model.SubjectType;

import java.util.*;

public class ScoreManagement {
    // 스캐너
    private static final Scanner sc = new Scanner(System.in);

    // 데이터 저장소
    private static Map<String, Score> scoreStore;

    // index 관리 필드
    private static int scoreIndex;
    private static final String INDEX_TYPE_SCORE = "SC";

    // index 자동 증가
    private static String sequence() {
        scoreIndex++;
        return INDEX_TYPE_SCORE + scoreIndex;
    }

    // 싱글톤은 아니고 유사한 무언가의 동작...
    private ScoreManagement() {
    }

    public static Map<String, Score> getStore() {
        if (scoreStore == null) {
            scoreStore = new HashMap<>();
        }
        return scoreStore;
    }


    // 2.1.수강생의 과목별 시험 회차 및 점수 등록
    public static void createScore(Map<String, Student> studentStore, Map<String, Subject> subjectStore) {
        /*
         * 회차 번호(Score 클래스의 roundNumber 멤버변수)가 1 ~ 10 값만 저장되어야 합니다.  * 1회차 ~ 10회차
         * 등록하려는 점수(Score 클래스의 studentScore 멤버변수)가 0 ~ 100 값만 저장되어야 합니다.
         * 등록하려는 과목의 회차 점수가 이미 등록되어 있다면 등록할 수 없습니다.
         * (= Score 클래스에서 studentId, subjectId, roundNumber 이 세 멤버변수가 "모두 일치"하는 score 객체가 존재한다면 등록하면 안됩니다.)
         */

        // 등록된 수강생이 아무도 없으면 종료합니다.
        if (isEmptyStudent(studentStore)) {
            System.out.println("\n등록된 수강생이 없습니다.");
            return;
        }

        String studentId = getStudentId(studentStore); // 관리할 수강생 고유 번호
        if (studentId == null) {
            return;
        }
        System.out.println("시험 점수를 등록합니다...");

        // 등록할 과목 Id, 이름 리스트
        List<String> studentSubjectId = getStudentSubjectId(studentStore, studentId);

        // 등록할 과목 입력
        System.out.print("등록할 ");
        String subjectId = inquireSubject(subjectStore, studentSubjectId);
        if ("".equals(subjectId)) {
            System.out.println("점수 등록을 취소합니다.");
            return;
        }

        // 회차 입력
        int roundNumber = 0;
        for (Score score : scoreStore.values()) {
            if (score.getSubjectId().equals(subjectId)) {
                roundNumber = Math.max(roundNumber, score.getRoundNumber());
            }
        }
        if (roundNumber >= 10) {
            System.out.println("\n더 이상 점수를 등록할 수 없습니다.");
            return;
        } else {
            roundNumber++;
        }

        // 점수 입력
        System.out.print(roundNumber + "회차 ");
        Integer studentScore = inquireScore();
        if (studentScore == null) {
            System.out.println("점수 등록을 취소합니다.");
            return;
        }

        // 점수 ID 시퀀스 생성
        String scoreId = sequence();

        // 점수 객체 생성
        Score score = new Score(scoreId, studentId, subjectId, roundNumber, studentScore);

        // 과목 타입에 따른 등급 등록
        if (isMandatory(subjectStore, subjectId)) {
            score.setGradeMandatoryByScore();
        } else {
            score.setGradeChoiceByScore();
        }

        // 점수 등록
        scoreStore.put(scoreId, score);
        System.out.println("\n점수 등록 성공!");
    }

    // 2.2.수강생의 과목별 회차 점수 수정
    public static void updateRoundScoreBySubject(Map<String, Student> studentStore, Map<String, Subject> subjectStore) {
        // 등록된 수강생이 아무도 없으면 종료합니다.
        if (isEmptyStudent(studentStore)) {
            System.out.println("\n등록된 수강생이 없습니다.");
            return;
        }

        // 관리할 수강생 고유 번호
        String studentId = getStudentId(studentStore);
        if (studentId == null) {
            return;
        }
        System.out.println("시험 점수를 수정합니다...");

        // 수강생의 수강 과목 id 리스트
        List<String> studentSubjectId = getStudentSubjectId(studentStore, studentId);

        // 수정할 과목 id 선택
        System.out.print("수정할 ");
        String subjectId = inquireSubject(subjectStore, studentSubjectId);
        if (Objects.equals("", subjectId) || subjectId == null) {
            System.out.println("점수 수정을 취소합니다.");
            return;
        }

        // 수정할 과목 회차 입력
        System.out.print("수정할 ");
        Integer round = inquireRound();
        if (round == null) {
            System.out.println("점수 수정을 취소합니다.");
            return;
        }

        // 수정할 점수 입력
        System.out.print("수정할 ");
        Integer updateScore = inquireScore();
        if (updateScore == null) {
            System.out.println("점수 수정을 취소합니다.");
            return;
        }

        // 점수 수정
        for (Score score : scoreStore.values()) {
            if (score.getStudentId().equals(studentId)
                    && score.getSubjectId().equals(subjectId)
                    && score.getRoundNumber() == round) {
                int originalScore = score.getStudentScore();

                // 점수 세팅 후 등급 조정
                score.setStudentScore(updateScore);
                if (isMandatory(subjectStore, subjectId)) {
                    score.setGradeMandatoryByScore();
                } else {
                    score.setGradeChoiceByScore();
                }
                System.out.println("바뀐 점수 : [" + originalScore + "]점 -> [" + updateScore + "]점");
                System.out.println("\n점수 수정 성공!");
                return;
            }
        }
    }

    // 2.3.수강생의 특정 과목 회차별 등급 조회
    public static void inquireRoundGradeBySubject(Map<String, Student> studentStore, Map<String, Subject> subjectStore) {
        // 등록된 수강생이 아무도 없으면 종료합니다.
        if (isEmptyStudent(studentStore)) {
            System.out.println("\n등록된 수강생이 없습니다.");
            return;
        }

        // 관리할 수강생 고유 번호
        String studentId = getStudentId(studentStore);
        if (studentId == null) {
            return;
        }
        System.out.println("회차별 등급을 조회합니다...");

        // 수강생의 수강 과목 id 리스트
        List<String> studentSubjectId = getStudentSubjectId(studentStore, studentId);

        // 기능 구현 (조회할 특정 과목)
        // 1. 과목 id 입력받기
        System.out.print("조회할 ");
        String subjectId = inquireSubject(subjectStore, studentSubjectId);
        if (Objects.equals("", subjectId) || subjectId == null) {
            System.out.println("점수 조회를 취소합니다.");
            return;
        }

        // 2. scoreStore를 돌면서 [studentId, subjectId] 이 두 가지가 일치하는 score 저장하기
        String[] studentGrade = new String[10];
        int count = 0;
        for (Score score : scoreStore.values()) { // scoreStore에 있는 각 Score 객체에 대해 다음 작업을 수행합니다.
            // [studentId, 과목이름] 이 두 가지가 일치하는 score 저장하기
            if (score.getStudentId().equals(studentId)
                    && score.getSubjectId().equals(subjectId)) {
                studentGrade[score.getRoundNumber() - 1] = score.getStudentGrade();
                count++;
            }
        }
        // 3. 2번에서 일치하는 score 객체를 찾지 못한 경우
        if (count == 0) {
            System.out.println("\n해당하는 정보를 찾을 수 없습니다.");
            return;
        }

        // 4. score 객체의 studentGrade 멤버변수 출력
        String name = getStudentName(studentStore, studentId);
        String subject = getSubjectNameById(subjectStore, subjectId);
        System.out.println("\n수강생 " + name + "님의 " + subject + " 과목 등급");
        for (int i = 0; i < studentGrade.length; i++) {
            if (studentGrade[i] != null) {
                System.out.println("- " + (i + 1) + "회차 [" + studentGrade[i] + "]");
            }
        }

        System.out.println();
    }

    // 2.4.수강생의 과목별 평균 등급 조회
    public static void inquireAvgGrades(Map<String, Student> studentStore, Map<String, Subject> subjectStore) {
        // 등록된 수강생이 아무도 없으면 종료합니다.
        if (isEmptyStudent(studentStore)) {
            System.out.println("\n등록된 수강생이 없습니다.");
            return;
        }

        // 과목별 평균 등급을 보고싶은 수강생 ID 입력
        String studentId = getStudentId(studentStore);
        if (studentId == null) {
            return;
        }
        System.out.println("\n과목별 평균 등급을 조회합니다...");
        System.out.println("\n<과목별 평균 등급>");

        // 수강생의 수강 과목 id 리스트
        List<String> studentSubjectId = getStudentSubjectId(studentStore, studentId);
        String grade;
        int roundCount = 0;
        for (int i = 0; i < studentSubjectId.size(); i++) {
            grade = settingGrade(subjectStore, studentId, studentSubjectId.get(i));
            String subjectName = getSubjectNameById(subjectStore, studentSubjectId.get(i));
            if (!Objects.equals("", grade)) {
                System.out.println(" - [" + grade + "] " + subjectName);
                roundCount++;
            }
        }
        if (roundCount != 0) {
            System.out.println("\n평균 등급 조회 성공!");
        } else {
            System.out.println("시험 정보가 없습니다.");
        }
    }

    // 2.4.특정 학생의 특정 과목 평균 등급 조회, 없을 시 "" 반환
    private static String settingGrade(Map<String, Subject> subjectStore, String studentId, String subjectId) {
        List<Integer> scoreList = new ArrayList<>();

        for (Map.Entry<String, Score> entry : scoreStore.entrySet()) {
            if (studentId.equals(entry.getValue().getStudentId())
                    && subjectId.equals(entry.getValue().getSubjectId())) {
                scoreList.add(entry.getValue().getStudentScore());
            }
        }

        String grade = "";
        if (!scoreList.isEmpty()) {
            if (isMandatory(subjectStore, subjectId)) {
                grade = Score.getGradeMandatoryByScore(scoreList);
            } else {
                grade = Score.getGradeChoiceByScore(scoreList);
            }
        }

        return grade;
    }

    // 2.5.특정 상태 수강생들의 필수 과목 평균 등급 조회, 포괄적인 총 평균
    public static void inquireMandatoryAvgGradeByStudentState(Map<String, Student> studentStore, Map<String, Subject> subjectStore) {
        // 등록된 수강생이 없으면 종료
        if (isEmptyStudent(studentStore)) {
            System.out.println("\n등록된 수강생이 없습니다.");
            return;
        }
        System.out.println("\n특정 상태 수강생들의 필수 과목 평균 등급을 조회합니다...");

        // 상태 입력
        String studentState;
        System.out.print("\n점수를 조회하고 싶은 ");
        studentState = StudentManagement.getStudentState();
        if (Objects.equals("", studentState)) {
            return;
        }

        // 입력받은 상태가 일치하는 학생 객체 리스트
        List<Student> studentByState = getStudentByState(studentStore, studentState);

        // 위 학생들의 모든 점수 ID를 리스트에 저장
        List<String> scoreId = new ArrayList<>();
        for (Student student : studentByState) {
            for (Score score : scoreStore.values()) {
                if (Objects.equals(score.getStudentId(), student.getStudentId())) {
                    scoreId.add(score.getScoreId());
                }
            }
        }

        // 과목 타입이 필수 과목인 것만 점수 저장
        String subjectId;
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < scoreId.size(); i++) {
            // 과목 타입이 필수인지 확인
            subjectId = scoreStore.get(scoreId.get(i)).getSubjectId();
            if (isMandatory(subjectStore, subjectId)) {
                scores.add(scoreStore.get(scoreId.get(i)).getStudentScore());
            }
        }

        // 총 평균등급 계산
        String avgGrade = Score.getGradeMandatoryByScore(scores);
        if (!"none".equals(avgGrade)) {
            System.out.println("\n상태가 [" + studentState + "]인 수강생들의 필수 과목 평균 등급: " + avgGrade);
            System.out.println("\n필수 과목 평균 등급 조회 성공!");
        } else {
            System.out.println("조회할 점수가 없습니다.");
        }
    }

    // 회차 입력
    private static Integer inquireRound() {
        int roundNumber;
        while (true) {
            try {
                System.out.print("회차(1~10) 입력: ");
                String round = sc.nextLine();
                if (Objects.equals("", round)) {
                    return null;
                }

                roundNumber = Integer.parseInt(round);
                if (1 > roundNumber || roundNumber > 10) {
                    System.out.println("알맞지 않은 숫자입니다.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
        return roundNumber;
    }

    // 점수 입력
    private static Integer inquireScore() {
        int studentScore;
        while (true) {
            try {
                System.out.print("점수(0~100) 입력: ");
                String score = sc.nextLine();
                if (Objects.equals("", score)) {
                    return null;
                }

                studentScore = Integer.parseInt(score);
                if (0 > studentScore || studentScore > 100) {
                    System.out.println("알맞지 않은 숫자입니다.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
        return studentScore;
    }

    // studentStore가 비어있는지 확인
    private static boolean isEmptyStudent(Map<String, Student> studentStore) {
        return studentStore.isEmpty();
    }

    // ** 메모있음 **
    // 수강생 ID를 입력받는 메서드, 실패시 null
    private static String getStudentId(Map<String, Student> studentStore) {
        /*
         * StudentManagement 클래스의 getStudentId와 완전히 동일한 기능을 함
         * 리팩토링 과정에서 각 클래스의 private으로 숨겨두었지만 다른 방법을 고려해야 할 듯 함.
         */

        /*
         * 수강생 Id를 입력받는 메서드
         * 정규화 사용, id 숫자만 입력해도 검색됨
         * ST1, st1, sT1, St1, 1, 01, 001, .. 가능
         * 존재하지 않는 id값 입력시 null 반환
         */
        System.out.print("\n수강생 ID를 입력해주세요: ");
        String studentId = sc.nextLine().toUpperCase();
        if (studentId.matches("^[0-9]+$")) {
            studentId = "ST" + Integer.parseInt(studentId);
        }
        if (!studentStore.containsKey(studentId)) {
            System.out.println("등록되지 않은 수강생입니다. 되돌아갑니다..");
            return null;
        } else {
            return studentId;
        }
    }

    private static List<String> getStudentSubjectId(Map<String, Student> studentStore, String studentId) {
        return studentStore.get(studentId).getStudentSubject().stream().toList();
    }

    private static String getStudentName(Map<String, Student> studentStore, String studentId) {
        return studentStore.get(studentId).getStudentName();
    }

    private static List<Student> getStudentByState(Map<String, Student> studentStore, String state) {
        List<Student> studentByState = new ArrayList<>();
        for (Student student : studentStore.values()) {
            if (student.getStudentState().equals(state)) {
                studentByState.add(student);
            }
        }
        return studentByState;
    }

    // 수강생이 수강하는 과목 중 한 가지를 선택하는 메서드, 취소 시 "" 리턴
    private static String inquireSubject(Map<String, Subject> subjectStore, List<String> studentSubjectId) {
        String selectedSubject = "";
        while (true) {
            // 사용자의 과목 리스트 보여주기
            System.out.println("과목 선택:");
            System.out.print("(");
            int it = 1;
            for (String id : studentSubjectId) {
                System.out.print(it + "." + subjectStore.get(id).getSubjectName() + ", ");
                it++;
            }
            System.out.print(it + ".취소) ");

            try {
                int index = Integer.parseInt(sc.nextLine());
                if (index == it) {
                    return selectedSubject;
                } else if (index > it || index <= 0) {
                    System.out.println("잘못된 입력입니다.\n");
                    continue;
                }
                selectedSubject = studentSubjectId.get(index - 1);
                return selectedSubject;

            } catch (NumberFormatException e) {
                System.out.println("번호를 입력해주세요.\n");
            }
        }
    }

    private static boolean isMandatory(Map<String, Subject> subjectStore, String subjectId) {
        return SubjectType.SUBJECT_TYPE_MANDATORY.equals(subjectStore.get(subjectId).getSubjectType());
    }

    private static String getSubjectNameById(Map<String, Subject> subjectStore, String studentSubjectId) {
        return subjectStore.get(studentSubjectId).getSubjectName();
    }
}
