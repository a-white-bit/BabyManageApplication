import model.Score;
import model.Student;
import model.Subject;

import java.util.*;

// updated 2024/05/07 16:00

/**
 * 구현 메모
 * -
 * 1. 숫자가 아닌 값 입력되면 오류로 프로그램 종료됨
 * 프로그램은 종료되지 않게 하고, 다시 값을 받게 하거나 수정단계만 빠져나가도록 하기
 * switch 변수를 String 형으로 작성  or 예외 try-catch
 * -
 * 2. 학생이 수강하는 과목리스트 등록
 * 중복 x, 최대 선택 개수 지정
 * -
 * 3. 학생이 수강하는 과목리스트 출력
 * 필수과목 - 선택과목 분류해서 예쁘게 출력하기
 */

public class CampManagementApplication {
    // 데이터 저장소  *(변경 전) List<Object> ------> Map<Id, Object> (변경 후)
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
    private static int studentIndex;
    private static final String INDEX_TYPE_STUDENT = "ST";
    private static int subjectIndex;
    private static final String INDEX_TYPE_SUBJECT = "SU";
    private static int scoreIndex;
    private static final String INDEX_TYPE_SCORE = "SC";

    // 스캐너
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        setInitData();
        // 임시 점수 데이터 추가
        temperaryCreateScore();
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
            subjectSeq = sequence(INDEX_TYPE_SUBJECT);
            subjectStore.put(subjectSeq, new Subject(subjectSeq, subject, type));
        }
    }

    // index 자동 증가
    private static String sequence(String type) {
        switch (type) {
            case INDEX_TYPE_STUDENT -> {
                studentIndex++;
                return INDEX_TYPE_STUDENT + studentIndex;
            }
            case INDEX_TYPE_SUBJECT -> {
                subjectIndex++;
                return INDEX_TYPE_SUBJECT + subjectIndex;
            }
            default -> {
                scoreIndex++;
                return INDEX_TYPE_SCORE + scoreIndex;
            }
        }
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
                case 1 -> displayStudentView(); // 수강생 관리
                case 2 -> displayScoreView(); // 점수 관리
                case 3 -> flag = false; // 프로그램 종료
                default -> {
                    System.out.println("잘못된 입력입니다.\n되돌아갑니다!");
                    Thread.sleep(2000);
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
            int input = sc.nextInt();

            switch (input) {
                case 1 -> createStudent(); // 수강생 등록
                case 2 -> displayStudentListView(); // 수강생 목록 조회
                case 3 -> updateStudent(); // 수강생 정보 수정
                case 4 -> deleteStudent(); //수강생 삭제
                case 5 -> flag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n다시 입력해주세요...");
                    Thread.sleep(800);
                }
            }
        }
    }

    // 수강생 등록
    private static void createStudent() {
        System.out.println("\n수강생을 등록합니다...");

        /* 이 메서드에서 구현해야할 것:
         * 다음 정보를 학생 목록(studentStore)에 저장하기
         * 수강생 ID, 이름, 상태, 과목 목록
         * 수강생 ID는 사용자에게서 임의로 입력받지 않습니다. 저희 프로그램이 내부적으로 등록합니다. ("ST1", "ST2", "ST3", ... 으로 등록됨)
         * 과목 목록은, 사용자에게서 여러개의 과목을 입력받아 컬렉션에 저장되어야 합니다.
         * 필수 과목과 선택 과목이 존재하는데, 필수 과목은 무조건 컬렉션에 저장하고
         * 선택 과목을 사용자에게 물어보고 입력받은 것들을 컬렉션에 넣어주면 될 것 같습니다!
         */

        System.out.print("수강생 이름 입력: ");
        String studentName = sc.next();

        boolean state = true;
        String studentState = "";
        while (state) {
            System.out.print("수강생 상태 입력:\n(1.아주좋음, 2.좋음, 3.보통, 4.나쁨, 5.아주나쁨) ");
            studentState = sc.next();
            switch (studentState) {
                case "1":
                    studentState = "아주좋음";
                    state = false;
                    break;
                case "2":
                    studentState = "좋음";
                    state = false;
                    break;
                case "3":
                    studentState = "보통";
                    state = false;
                    break;
                case "4":
                    studentState = "나쁨";
                    state = false;
                    break;
                case "5":
                    studentState = "아주나쁨";
                    state = false;
                    break;

                default:
                    System.out.println("올바른 값을 입력해주세요.");
            }
        }
        Set<String> studentSubject = new HashSet<>();
        studentSubject.add("Java");
        studentSubject.add("객체지향");
        studentSubject.add("Spring");
        studentSubject.add("JPA");
        studentSubject.add("MySQL");
        boolean flag = true;
        while (flag) {
            System.out.print("수강할 선택 과목:\n(1.디자인 패턴, 2.Spring security, 3.Redis, 4.MongoDB, 5.종료) ");
            switch (sc.next()) {
                case "1":
                    studentSubject.add("디자인 패턴");
                    break;
                case "2":
                    studentSubject.add("Spring Security");
                    break;
                case "3":
                    studentSubject.add("Redis");
                    break;
                case "4":
                    studentSubject.add("MongoDB");
                    break;
                case "5":
                    flag = false;
                    break;
                default:
                    System.out.println("올바른 값을 입력해주세요.");
            }
        }

        // 수강생 ID 시퀀스 생성
        String studentId = sequence(INDEX_TYPE_STUDENT);

        // 수강생 인스턴스 생성 예시 코드
        Student student = new Student(studentId, studentName, studentState, studentSubject);
        // 학생 목록(Map)에 저장
        studentStore.put(studentId, student);
        System.out.println("수강생 등록 성공!\n");
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
            int input = sc.nextInt();

            switch (input) {
                case 1 -> inquireStudent(); // 전체 목록 조회
                case 2 -> inquireStudentInfo(); // 상세 정보 조회
                case 3 -> inquireStudentByState(); // 상태별 목록 조회
                case 4 -> flag = false; // 수강 관리 기능으로
                default -> {
                    System.out.println("잘못된 입력입니다.\n다시 입력해주세요..");
                    Thread.sleep(800);
                }
            }
        }
    }

    // 수강생 전체 목록 조회
    private static void inquireStudent() {
        System.out.println("\n수강생 목록을 조회합니다...");
        /*
         * 수강생 목록은 studentStore에 있고, Map 컬렉션을 사용합니다.
         * Map의 모든 요소들을 돌면서 학생들의 ID와 이름으로 출력해주세요.
         * 아래 참고하시면 좋을 것 같습니다!
         * https://velog.io/@woply/HashMap-%EC%A0%84%EC%B2%B4-%EA%B0%92%EC%9D%84-%EC%B6%9C%EB%A0%A5%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95
         * https://coding-factory.tistory.com/556
         */
        if (!studentStore.isEmpty()) {
            String studentId = "";
            String studentName = "";
            for (Map.Entry<String, Student> entry : studentStore.entrySet()) {
                studentId = String.valueOf(entry.getValue().getStudentId());
                studentName = String.valueOf(entry.getValue().getStudentName());
                System.out.println("학생 ID: " + studentId + ", 이름: " + studentName);
            }

            System.out.println("\n수강생 목록 조회 성공!");
        } else {
            System.out.println("아무 정보도 없습니다!!");
        }
    }

    // 수강생 상세 정보 조회
    private static void inquireStudentInfo() {
        System.out.println("\n수강생 상세 정보를 조회합니다...");
        /*
         * 조회하고 싶은 수강생 ID를 입력받습니다. (예시: "ST1")
         * Map에서 key값인 수강생 ID를 가지고 그것에 해당하는 Student 객체를 얻을 수 있습니다.
         * 얻은 Student 객체로  다음을 출력해주세요.
         * 학생 ID, 학생이름, 상태(state), 과목리스트
         * 과목리스트는 컬렉션이므로 for문으로 돌면서 이름들을 출력해주세요.
         */
        System.out.println("\n수강생 고유번호를 입력해주세요: ");
        String studentId = sc.next();
        Student student = studentStore.get(studentId);

        // 수강생 조회
        if (student != null) {
            System.out.println("학생 ID: " + student.getStudentId());
            System.out.println("학생 이름 : " + student.getStudentName());
            System.out.println("상태 : " + student.getStudentState());

            Set<String> subjects = student.getStudentSubject();

            /*
             * 학생이 수강하는 과목리스트 출력
             * 필수과목 / 선택과목 분류해서 예쁘게 출력 예정
             */
            System.out.println("과목 리스트 : ");
            for (String subject : subjects) {
                System.out.print("{" + subject + "} ");
            }

        } else { // 조회한 수강생이 없을 경우
            System.out.println("등록되지 않은 수강생입니다");
            return;
        }

        System.out.println("\n수강생 상세 정보 조회 성공!");
    }

    // 수강생 상태별 목록 조회
    private static void inquireStudentByState() {
        /*
         * 조회하고 싶은 상태를 사용자에게 입력받습니다. (예시: "아주좋음")
         * 수강생 목록은 studentStore에 있고, Map 컬렉션을 사용합니다. Entry set문법으로 Map의 키, Value 를 받아옴
         * Map의 모든 요소들을 돌면서  통일성있게 swich 문으로 입력받은 상태 "아주좋음"과 일치하는 요소들의
         *  ID와 이름으로 출력해주세요.
         * 아래 참고하시면 좋을 것 같습니다!
         * https://velog.io/@woply/HashMap-%EC%A0%84%EC%B2%B4-%EA%B0%92%EC%9D%84-%EC%B6%9C%EB%A0%A5%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95
         * https://coding-factory.tistory.com/556
         */

        /*기능구현
         * 학생들의 상태값을 스캐너로 입력받아 swich ~ case 문으로 상태별 조건을 할당해준다.
         * Map의 값을 가져오기위해 Entry 문을 사용하여 studentStore 의 값을 entryset 으로 할당해준 후
         * for문으로 Map 을 전체적으로 돌면서 사용자가 입력한 key를 입력받아 학생 ID와 이름을 value 값으로 넘겨준다
         * 출력단에서 studentStore 에서 직접 뽑아오는게 아닌 if 문에서의 조건에 해당하는 밸류값을
         * studentStore 를 할당받은 entryset 에서 출력해준다.
         */
        System.out.println("\n수강생을 상태별로 조회합니다...");
        System.out.print("조회할 학생들의 상태를 입력해주세요 \n(1.아주좋음, 2.좋음, 3.보통, 4.나쁨, 5.아주나쁨)\n수강생 상태 입력 : ");
        String studentState = sc.next();
        switch (studentState) {
            case "1" -> studentState = "아주좋음";
            case "2" -> studentState = "좋음";
            case "3" -> studentState = "보통";
            case "4" -> studentState = "나쁨";
            case "5" -> studentState = "아주나쁨";
            default -> {
                System.out.println("올바른 값을 입력해주세요");
                return;
            }
        }

        Map<String, String> studentSortByState = new HashMap<>();
        for (Map.Entry<String, Student> entryset : studentStore.entrySet()) {
            if (studentState.equals(entryset.getValue().getStudentState())) {
                studentSortByState.put(entryset.getValue().getStudentId(), entryset.getValue().getStudentName());
            }
        }

        System.out.println("<상태: " + studentState + ">");
        if (studentSortByState.isEmpty()) {
            System.out.println("해당하는 학생이 없습니다.");
        } else {
            studentSortByState.forEach((studentId, studentName) -> {
                System.out.println("ID: " + studentId + ", 이름: " + studentName);
            });
        }
    }

    // 수강생 정보 수정
    private static void updateStudent() {
        System.out.println("\n수강생 정보를 수정합니다...");
        System.out.println("\n수강생 고유번호를 입력해주세요: ");
        String studentId = sc.next();

        Student student = studentStore.get(studentId);

        if (student != null) {
            String studentState = "";

            System.out.println("변경하는 수강생 이름을 입력하세요: (미 입력시 기존 정보가 유지됩니다.)");
            String studentName = sc.next();

            System.out.println("변경하는 수강생 상태를 입력하세요: \n 1.아주좋음, 2.좋음, 3.보통, 4.나쁨, 5.아주나쁨, 6.기존 정보 유지");
            int stateNumber = Integer.parseInt(sc.next());

            switch (stateNumber) {
                case 1 -> studentState = "아주좋음";
                case 2 -> studentState = "좋음";
                case 3 -> studentState = "보통";
                case 4 -> studentState = "나쁨";
                case 5 -> studentState = "아주나쁨";
                case 6 -> studentState = "";
                default -> System.out.println("잘못된 입력입니다");
            }

            //미 입력시 기존 정보 유지
            if (!"".equals(studentName)) {
                student.setStudentName(studentName); //수강생 이름 set
            }
            if (!"".equals(studentState)) {
                student.setStudentState(studentState); //수강생 상태 set
            }

        } else {
            System.out.println("해당 수강생이 없습니다.");
        }
    }

    private static void deleteStudent() {
        System.out.println("\n수강생 정보를 삭제합니다...");
        System.out.println("\n수강생 고유번호를 입력해주세요: ");
        String studentId = sc.next();
        Student deleteStudent = studentStore.get(studentId);
        if (deleteStudent != null) {
            System.out.print("정말로 " + deleteStudent.getStudentName() + "님의 정보를 삭제하시겠습니까?:\n ('네' 입력시 삭제) ");
            if ("네".equals(sc.next())) {
                studentStore.remove(studentId);
                System.out.println("삭제되었습니다.");
            } else {
                System.out.println("\n삭제가 취소되었습니다.");
            }
        } else {
            System.out.println("해당 수강생이 없습니다.");
        }


        /*
         * 삭제된 수강생에 관련된 점수 목록도 전부 삭제 (예정)
         */

    }

    private static void displayScoreView() {
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
        if (studentStore.get(studentId) != null) {
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

        /*
        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        if ("".equals(studentId)) {
            System.out.println("등록되지 않은 학생 ID입니다. 되돌아갑니다..");
            return;
        }
        System.out.println("시험 점수를 등록합니다...");

        boolean flag = false;
        Set<String> studentSubjects = studentStore.get(studentId).getStudentSubject();
        String subjectId = "";
        while (!flag) {
            System.out.println("등록할 과목 선택:");
            System.out.print(studentSubjects + " ");
            subjectId = sc.next();
            for (String subject : studentSubjects) {
                if (subjectId.equals(subject)) {
                    flag = true;
                }
            }
            if (!flag) {
                System.out.println("등록된 수강 과목이 아닙니다. 다시 입력해주세요.");
            }
        }

        System.out.println("회차: ");
        int roundNumber = Integer.parseInt(sc.next());
        // 잘못된 입력 (1~10 이외의 입력) 처리

        System.out.println("점수: ");
        int studentScore = Integer.parseInt(sc.next());
        // 잘못된 입력 (0~100 이외의 입력) 처리
*/
        // 점수 ID 시퀀스 생성
        //String scoreId = sequence(INDEX_TYPE_SCORE);
        // 점수 등록 예시
//        Score score = new Score(scoreId, studentId, subjectId, roundNumber, studentScore);


        System.out.println("\n점수 등록 성공!");
    }

    private static void temperaryCreateScore() {
        String scoreId1 = sequence(INDEX_TYPE_SCORE);
        String scoreId2 = sequence(INDEX_TYPE_SCORE);
        String scoreId3 = sequence(INDEX_TYPE_SCORE);
        String scoreId4 = sequence(INDEX_TYPE_SCORE);
        String scoreId5 = sequence(INDEX_TYPE_SCORE);
        Score score1 = new Score(scoreId1, "ST1", "SU2", 1, 80);
        score1.setGradeMandatoryScore();
        Score score2 = new Score(scoreId2, "ST1", "SU3", 1, 75);
        score2.setGradeChoiceScore();
        Score score3 = new Score(scoreId3, "ST1", "SU2", 2, 90);
        score3.setGradeMandatoryScore();
        Score score4 = new Score(scoreId4, "ST2", "SU1", 1, 40);
        score4.setGradeMandatoryScore();
        Score score5 = new Score(scoreId5, "ST2", "SU1", 2, 77);
        score5.setGradeMandatoryScore();
        scoreStore.put(scoreId1, score1);
        scoreStore.put(scoreId2, score2);
        scoreStore.put(scoreId3, score3);
        scoreStore.put(scoreId4, score4);
        scoreStore.put(scoreId5, score5);
    }

    // 수강생의 과목별 회차 점수 수정
    private static void updateRoundScoreBySubject() {
        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        String subjectId = scoreStore.get(studentId).getSubjectId();
        int roundNumber = scoreStore.get(subjectId).getRoundNumber();

        System.out.println("시험 점수를 수정합니다...");
        for (Map.Entry<String, Student> entryset : studentStore.entrySet()) {
            // 수정할 과목 이름 입력받기
            System.out.print(entryset.getValue().getStudentSubject() + "\n수정할 과목을 입력해주세요 : ");
            subjectId  = sc.next();
            // 수정할 과목 회차 입력받기
            System.out.print("수정할 회차를 선택해주세요 : ");
            roundNumber = sc.nextInt();
        }
        for (Map.Entry<String, Score> entryset : scoreStore.entrySet()) {
            if (studentId.equals(entryset.getValue().getStudentId()) && subjectId.equals(entryset.getValue().getSubjectId())) {
            }
        }
    }
//
//        for (Map.Entry<String, Student> entryset : studentStore.entrySet()) {
//        if (studentState.equals(entryset.getValue().getStudentState())) {
//            studentSortByState.put(entryset.getValue().getStudentId(), entryset.getValue().getStudentName());

//            if (subjectName.equals("Java")) {
//                System.out.print("수정할 회차를 입력해주세요 : ");
//                roundNumber = sc.nextInt();
//            } else if (subjectName.equals("디자인 패턴")) {
//                System.out.print("수정할 회차를 입력해주세요 : ");
//                roundNumber = sc.nextInt();
//            } else if (subjectName.equals("MySQL")) {
//                System.out.print("수정할 회차를 입력해주세요 : ");
//                roundNumber = sc.nextInt();
//            } else if (subjectName.equals("Redis")) {
//                System.out.print("수정할 회차를 입력해주세요 : ");
//                roundNumber = sc.nextInt();
//            } else if (subjectName.equals("객체지향")) {
//                System.out.print("수정할 회차를 입력해주세요 : ");
//                roundNumber = sc.nextInt();
//            } else if (subjectName.equals("JPA")) {
//                System.out.print("수정할 회차를 입력해주세요 : ");
//                roundNumber = sc.nextInt();
//            } else if (subjectName.equals("Spring")) {
//                System.out.print("수정할 회차를 입력해주세요 : ");
//                roundNumber = sc.nextInt();
//            } else if (subjectName.equals("Spring Security")) {
//                System.out.print("수정할 회차를 입력해주세요 : ");
//                roundNumber = sc.nextInt();
//            } else if (subjectName.equals("MongoDB")) {
//                System.out.print("수정할 회차를 입력해주세요 : ");
//                roundNumber = sc.nextInt();
//            } else {
//                System.out.println("올바른 값을 입력해주세요");
//            }
//        }
//    }


//        }
//        System.out.println("\n점수 수정 성공!");
//    }
        // 기능 구현 (수정할 과목 및 회차, 점수)

        // 1. 수정할 과목 이름 입력받기

        // 2. 수정할 회차 입력받기

        // 3. scoreStore를 돌면서 [studentId, 과목이름, 회차] 이 세가지가 일치하는 score 객체 찾기

        // 4. 찾은 score 객체의 .setStudentScore() 메서드로 점수 수정

        // 5. 수정 과목이 mandatory인지 choice인지에 따라 .setGradeMandatoryScore(), .setGradeChoiceScore() 둘 중 하나 실행하여 등급(A,B,..) 설정
        // 5-1. 과목 타입(mandatory, choice)은 확인하려면 subjectStore에서 subjectId로 해당하는 subject 객체를 찾고 멤버변수 subjectType으로 알 수있음



    // 수강생의 특정 과목 회차별 등급 조회
    private static void inquireRoundGradeBySubject() {
        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        System.out.println("회차별 등급을 조회합니다...");

        // 기능 구현 (조회할 특정 과목)
        // 1. 과목 이름 입력받기

        // 2. 시험본 회차 입력받기

        // 3. scoreStore를 돌면서 [studentId, 과목이름, 회차] 이 세가지가 일치하는 score 객체 찾기

        // 4. score 객체의 studentGrade 멤버변수 출력

        System.out.println("\n등급 조회 성공!");
    }

    // 수강생의 과목별 평균 등급 조회
    private static void inquireAvgGrades() {
        String studentId = getStudentId(); // 과목별 평균 등급을 보고싶은 수강생ID 입력
        System.out.println("과목별 평균 등급을 조회합니다...");

        // 기능 구현 (조회할 특정 과목)
        // 1. 과목 이름 입력받기
        // 1-1. subjectStore를 돌면서 입력받은 과목 이름과 일치하는 subject 객체 찾기
        // 1-2. 해당 객체의 subjectId, subjectType 가져오기 (이후 연산에 필요함)

        // 2. scoreStore를 돌면서 [studentId, subjectId(1-2에서 가져왔던 것)] 이 두 가지가 일치하는 score 객체를 찾고
        // 찾은 score 객체마다의 studentScore 멤버변수들을 가지고 평균점수 구하기

        // 3. 평균 점수를  등급으로 환산하기
        //   (1-2.)에서 구했던 subjectType 이 MANDATORY인지 CHOICE인지에 따라 averageScoreToGrade() 메서드 활용
        // --> String grade = averageScoreToGrade(평균점수, subjectType); 또는
        // --> String grade = averageScoreToGrade(점수리스트, subjectType); 선택해서 사용..

        // 4. 평균 등급 출력

        System.out.println("\n평균 등급 조회 성공!");
    }

    // 특정 상태 수강생들의 필수 과목 평균 등급 조회
    private static void inquireMandatoryAvgGradeByStudentState() {
        // 기능 구현
        // 1. 필수 과목 평균 등급을 가져올 수강생들의 상태 입력
        System.out.println("조회하고싶은 수강생들의 상태를 입력: ");
        String studentState = sc.next();
        System.out.println("상태가 [" + studentState + "]인 수강생들의 필수 과목 평균 등급을 조회합니다...");

        // 2. studentStore 맵을 돌면서 studentState가 일치하는 student 객체 찾기

        // 특정 상태 수강생들의 필수 과목 점수들을 넣는 리스트
        List<Integer> scores = new ArrayList<Integer>();

        // 3. 해당하는 student 객체들의 필수 과목들만 찾기

        // 4. (3.)에 해당하는 과목들의 회차별 점수들 모두 scores 리스트에 담기

        // 5. 다음 메서드 활용
        // averageScoreToGrade(scores, SUBJECT_TYPE_MANDATORY);
        String avgGrade = averageScoreToGrade(scores, SUBJECT_TYPE_MANDATORY);

        System.out.println("\n필수 과목 평균 등급 조회 성공!");
    }

    private static String averageScoreToGrade(List<Integer> scores, String type) {
        String grade = "N";
        int avg = (int) scores.stream().mapToInt(x -> x).average().orElse(0);

        if (Objects.equals(type, SUBJECT_TYPE_MANDATORY)) {
            grade = (avg < 60) ? "N" :
                    (avg < 70) ? "F" :
                            (avg < 80) ? "D" :
                                    (avg < 90) ? "C" :
                                            (avg < 95) ? "B" : "A";
        } else if (Objects.equals(type, SUBJECT_TYPE_CHOICE)) {
            grade = (avg < 50) ? "N" :
                    (avg < 60) ? "F" :
                            (avg < 70) ? "D" :
                                    (avg < 80) ? "C" :
                                            (avg < 90) ? "B" : "A";
        }

        return grade;
    }

    private static String averageScoreToGrade(int score, String type) {
        String grade = "N";

        if (Objects.equals(type, SUBJECT_TYPE_MANDATORY)) {
            grade = (score < 60) ? "N" :
                    (score < 70) ? "F" :
                            (score < 80) ? "D" :
                                    (score < 90) ? "C" :
                                            (score < 95) ? "B" : "A";
        } else if (Objects.equals(type, SUBJECT_TYPE_CHOICE)) {
            grade = (score < 50) ? "N" :
                    (score < 60) ? "F" :
                            (score < 70) ? "D" :
                                    (score < 80) ? "C" :
                                            (score < 90) ? "B" : "A";
        }

        return grade;
    }

}