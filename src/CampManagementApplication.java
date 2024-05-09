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

    // 과목이름을 가지고 ID를 구하는 메서드, 실패 null 반환
    private static String getSubjectIdByName(String subjectName) {
        for (Map.Entry<String, Subject> entry : subjectStore.entrySet()) {
            if (entry.getValue().getSubjectName().equals(subjectName)) {
                return entry.getKey();
            }
        }
        return null;
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

        // 이름 입력
        System.out.print("수강생 이름 입력 : ");
        sc.nextLine();
        String studentName = sc.nextLine();
        if (Objects.equals(studentName, "") || studentName == null) {
            System.out.println("등록이 취소되었습니다.");
            return;
        }

        // 상태 입력
        String studentState = getStudentState();
        if (Objects.equals(studentState, "") || studentState == null) {
            System.out.println("등록이 취소되었습니다.");
            return;
        }

        // 학생 과목 리스트 생성
        // 필수 & 선택 과목 추가될 리스트
        Set<String> studentSubject = new HashSet<>();

        // 필수 과목 입력
        for (String subject : subjectsMandatoryList) {
            String subjectId = getSubjectIdByName(subject);
            if (subjectId != null) {
                studentSubject.add(subjectId);
            }
        }
        // 선택 과목 리스트
        List<String> choiceSubjectNames = getChoiceSubject();
        if (choiceSubjectNames == null) {
            System.out.println("등록이 취소되었습니다.");
            return;
        }
        // 선택 과목 입력
        for (String subject : choiceSubjectNames) {
            String subjectId = getSubjectIdByName(subject);
            if (subjectId != null) {
                studentSubject.add(subjectId);
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

    // 수강생 상태를 입력받는 메서드, 취소 시 "" 리턴
    private static String getStudentState() {
        String studentState = "";
        int listSize = stateList.size();
        while (true) {
            System.out.println("수강생 상태 입력:");
            System.out.print("(");
            for (int i = 0; i < listSize; i++) {
                System.out.print(i + 1 + "." + stateList.get(i) + ", ");
            }
            System.out.print(listSize + 1 + ".취소) ");

            try {
                int index = Integer.parseInt(sc.next());
                if (index == listSize + 1) {
                    return studentState;
                } else if (index > listSize + 1 || index <= 0) {
                    System.out.println("잘못된 입력입니다.\n");
                    continue;
                }
                studentState = stateList.get(index - 1);

            } catch (NumberFormatException e) {
                System.out.println("번호를 입력해주세요.\n");
                continue;
            }
            return studentState;
        }
    }

    // 수강생 등록 -> 선택과목을 입력받는 메서드, 선택한 과목 리스트 반환
    private static List<String> getChoiceSubject() {
        /*
         * 생각보다 많은 코드가 기술되어서 따로 메서드를 작성했습니다.
         * 1) 사용자가 선택 중인 과목 리스트 표시함
         * 2) 선택한 과목은 "x.OOOO" 으로 사용할 수 없는 번호임을 명시함 -> StringBuilder 사용
         * 3) 예외처리 완료
         */
        List<String> choiceSubject = new ArrayList<>();
        Boolean[] selected = new Boolean[subjectsChoiceList.size()]; // 고른 과목인지 체크하는 배열
        Arrays.fill(selected, false);
        StringBuilder subjectsChoice = new StringBuilder();

        while (true) {
            if (choiceSubject.size() == subjectsChoiceList.size()) {
                System.out.println("\n모든 수강을 선택하셨습니다.");
                break;
            }
            // 사용자가 선택한 과목 출력
            System.out.print("수강할 선택 과목: ");
            if (!choiceSubject.isEmpty()) {
                System.out.print(choiceSubject);
            }
            subjectsChoice.append("\n(");
            for (int i = 0; i < subjectsChoiceList.size(); i++) {
                if (!selected[i]) {
                    subjectsChoice.append(i + 1);
                } else {
                    subjectsChoice.append("x");
                }
                subjectsChoice.append(".").append(subjectsChoiceList.get(i)).append(", ");
            }
            subjectsChoice.append(subjectsChoiceList.size() + 1).append(". 등록 완료, ");
            subjectsChoice.append(subjectsChoiceList.size() + 2).append(". 취소) ");
            System.out.print(subjectsChoice.toString());
            subjectsChoice.delete(0, subjectsChoice.length());

            // 선택과목 입력
            try {
                int index = Integer.parseInt(sc.next()) - 1;
                if (index == subjectsChoiceList.size()) {
                    break; // 등록 완료
                } else if (index == subjectsChoiceList.size() + 1) {
                    choiceSubject = null;
                    break; // 등록 취소
                } else if (index < 0 || index > subjectsChoiceList.size() + 1) {
                    System.out.println("\n잘못된 번호입니다.");
                    continue;
                }

                String choiceNumber = subjectsChoiceList.get(index);
                if (choiceSubject.contains(choiceNumber)) {
                    System.out.println("\n이미 선택된 과목입니다.");
                } else {
                    choiceSubject.add(choiceNumber);
                    selected[index] = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n번호를 입력해주세요.");
            }
        }
        return choiceSubject;
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
                System.out.println("ID: " + studentId + ", 이름: " + studentName);
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
        String studentId = getStudentId();
        if (studentId == null) { // 조회한 수강생이 없을 경우
            System.out.println("등록되지 않은 수강생입니다. 되돌아갑니다..");
            return;
        }
        Student student = studentStore.get(studentId);

        // 수강생 조회
        System.out.println("ID: " + student.getStudentId());
        System.out.println("이름: " + student.getStudentName());
        System.out.println("상태: " + student.getStudentState());

        Set<String> studentSubject = student.getStudentSubject();

        // 학생이 수강하는 과목리스트 출력
        List<String> mandatoryList = new ArrayList<>();
        List<String> optionalList = new ArrayList<>();
        for (String subject : studentSubject) {
            String type = subjectStore.get(subject).getSubjectType();
            String name = subjectStore.get(subject).getSubjectName();
            if (SUBJECT_TYPE_MANDATORY.equals(type)) {
                mandatoryList.add(name);
            } else if (SUBJECT_TYPE_CHOICE.equals(type)) {
                optionalList.add(name);
            }
        }
        System.out.println("필수 과목: " + mandatoryList);
        if (!optionalList.isEmpty()) {
            System.out.println("선택 과목: " + optionalList);
        } else {
            System.out.println("선택 과목: 없음");
        }
        System.out.println("\n수강생 상세 정보 조회 성공!");
    }

    // 수강생 ID를 입력받는 메서드, 실패시 null
    private static String getStudentId() {
        /*
         * 수강생 Id를 입력받는 메서드
         * 정규화 사용, id 숫자만 입력해도 검색됨
         * ST1, st1, sT1, St1, 1, 01, 001, .. 가능
         * 존재하지 않는 id값 입력시 null 반환
         */
        System.out.print("수강생 ID를 입력해주세요: ");
        String studentId = sc.next().toUpperCase();
        if (studentId.matches("^[0-9]+$")) {
            studentId = "ST" + Integer.parseInt(studentId);
        }
        if (!studentStore.containsKey(studentId)) {
            return null;
        } else {
            return studentId;
        }
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
        System.out.print("조회할 ");
        String studentState = getStudentState();
        if (Objects.equals(studentState, "") || studentState == null) {
            System.out.println("조회가 취소되었습니다.");
            return;
        }

        // studentStore 돌면서 상태가 studentState인 학생을 해시맵에 저장
        Map<String, String> studentSortByState = new HashMap<>();
        for (Map.Entry<String, Student> entryset : studentStore.entrySet()) {
            if (studentState.equals(entryset.getValue().getStudentState())) {
                studentSortByState.put(entryset.getKey(), entryset.getValue().getStudentName());
            }
        }

        System.out.println("\n<상태: " + studentState + ">");
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
            sc.nextLine();

            // 이름 변경, 미 입력시 기존 정보 유지
            System.out.print("변경될 이름을 입력하세요: (미 입력시 기존 정보가 유지됩니다.) ");
            String studentName = sc.nextLine();

            if (!"".equals(studentName)) {
                System.out.print("[" + studentStore.get(studentId).getStudentName() + "]에서 ");
                student.setStudentName(studentName); //수강생 이름 set
                System.out.println("[" + studentName + "]으로 변경되었습니다.");
            } else {
                System.out.println("학생 이름이 [" + studentStore.get(studentId).getStudentName() + "]으로 유지됩니다.");
            }

            // 상태 변경, 상태 변경 메서드 사용
            System.out.print("변경될 ");
            String studentState = getStudentState();

            if (!"".equals(studentState)) {
                System.out.print("[" + studentStore.get(studentId).getStudentState() + "]에서 ");
                student.setStudentState(studentState); //수강생 상태 set
                System.out.println("[" + studentState + "]으로 변경되었습니다.");
            } else {
                System.out.println("학생 상태는 [" + studentStore.get(studentId).getStudentState() + "]으로 유지됩니다.");
            }

        } else {
            System.out.println("해당 수강생이 없습니다.");
        }
    }

    // 수강생 정보 삭제
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

        // 해당하는 수강생 점수 정보 삭제
        Iterator<Map.Entry<String, Score>> entries = scoreStore.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Score> entry = entries.next();
            if (entry.getValue().getStudentId().equals(studentId)) {
                scoreStore.remove(entry.getKey());
            }
        }
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

    // 수강생의 과목별 시험 회차 및 점수 등록
    private static void createScore() {
        /*
         * 회차 번호(Score 클래스의 roundNumber 멤버변수)가 1 ~ 10 값만 저장되어야 합니다.  * 1회차 ~ 10회차
         * 등록하려는 점수(Score 클래스의 studentScore 멤버변수)가 0 ~ 100 값만 저장되어야 합니다.
         * 등록하려는 과목의 회차 점수가 이미 등록되어 있다면 등록할 수 없습니다. (Score 클래스에서
         * studentId, subjectId, roundNumber 이 세 멤버변수가 "모두 일치"하는 score 객체가 존재한다면 등록하면 안됩니다.)
         */

        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        if (studentId == null) {
            System.out.println("등록되지 않은 수강생입니다. 되돌아갑니다..");
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
                subjectId = getSubjectIdByName(subjectName);
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
                    String scoreId = sequence(INDEX_TYPE_SCORE);
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

        //getStudentId 메소드에서의 studentId값이 null일때 리턴시켜주는 조건문
        if (studentId == null) {
            System.out.println("등록되지 않은 수강생입니다. 되돌아갑니다..");
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
                        && score.getSubjectId().equals(getSubjectIdByName(subjectName))
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
        System.out.println("회차별 등급을 조회합니다...");

        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        if (studentId == null) {
            System.out.println("등록되지 않은 수강생입니다. 되돌아갑니다..");
            return;
        }

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
            subjectId = getSubjectIdByName(studentSubject);
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
                    && score.getSubjectId().equals(getSubjectIdByName(studentSubject))
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
        System.out.println("과목별 평균 등급을 조회합니다...");
        String studentId = getStudentId(); // 과목별 평균 등급을 보고싶은 수강생ID 입력
        if (studentId == null) {
            System.out.println("등록되지 않은 수강생입니다. 되돌아갑니다..");
            return;
        }

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