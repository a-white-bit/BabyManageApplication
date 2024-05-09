import model.Score;
import model.Student;
import model.Subject;

import java.util.*;

public class StudentManagement {
    // 스캐너
    private static final Scanner sc = new Scanner(System.in);

    // 데이터 저장소
    private static Map<String, Student> studentStore;

    //index관리 필드
    private static int studentIndex;
    private static final String INDEX_TYPE_STUDENT = "ST";

    // index 자동 증가
    private static String sequence() {
        studentIndex++;
        return INDEX_TYPE_STUDENT + studentIndex;
    }

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

    // 싱글톤은 아니고 유사한 무언가의 동작...
    private StudentManagement() {}
    public static Map<String, Student> getStore() {
        if (studentStore == null) {
            studentStore = new HashMap<>();
        }
        return studentStore;
    }

    // 수강생 등록
    public static void createStudent() {
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
            String subjectId = SubjectManagement.getSubjectIdByName(subject);
            if (subjectId != null) {
                studentSubject.add(subjectId);
            }
        }
        // 선택 과목 리스트
        List<String> choiceSubjectNames = SubjectManagement.getChoiceSubject();
        if (choiceSubjectNames == null) {
            System.out.println("등록이 취소되었습니다.");
            return;
        }
        // 선택 과목 입력
        for (String subject : choiceSubjectNames) {
            String subjectId = SubjectManagement.getSubjectIdByName(subject);
            if (subjectId != null) {
                studentSubject.add(subjectId);
            }
        }

        // 수강생 ID 시퀀스 생성
        String studentId = sequence();
        studentIndex++;
        // 수강생 인스턴스 생성 예시 코드
        Student student = new Student(studentId, studentName, studentState, studentSubject);
        // 학생 목록(Map)에 저장
        studentStore.put(studentId, student);
        System.out.println("수강생 등록 성공!\n");
    }



    // 수강생 전체 목록 조회
    public static void inquireStudent() {
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
    public static void inquireStudentInfo() {
        System.out.println("\n수강생 상세 정보를 조회합니다...");
        /*
         * 조회하고 싶은 수강생 ID를 입력받습니다. (예시: "ST1")
         * Map에서 key값인 수강생 ID를 가지고 그것에 해당하는 Student 객체를 얻을 수 있습니다.
         * 얻은 Student 객체로  다음을 출력해주세요.
         * 학생 ID, 학생이름, 상태(state), 과목리스트
         * 과목리스트는 컬렉션이므로 for문으로 돌면서 이름들을 출력해주세요.
         */
        System.out.print("\n수강생 고유번호를 입력해주세요: ");
        // 정규화 사용, id 숫자만 입력해도 검색됨
        // ST1, st1, sT1, St1, 1, 01, 001, .. 가능
        String studentId = sc.next().toUpperCase();
        if (studentId.matches("^[0-9]+$")) {
            studentId = "ST" + Integer.parseInt(studentId);
        }
        Student student = studentStore.get(studentId);

        // 수강생 조회
        if (student != null) {
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

        } else { // 조회한 수강생이 없을 경우
            System.out.println("등록되지 않은 수강생입니다");
            return;
        }

        System.out.println("\n수강생 상세 정보 조회 성공!");
    }

    // 수강생 상태별 목록 조회
    public static void inquireStudentByState() {
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
    public static void updateStudent() {
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
    public static void deleteStudent(Map<String, Score> scoreStore) {
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


    // 수강생 상태를 입력받는 메서드, 취소 시 "" 리턴
    public static String getStudentState() {
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

    public static String getStudentId() {
        System.out.print("\n관리할 수강생의 번호를 입력하시오...");
        // 잘못된 Id 입력 처리 필요
        String studentId = sc.next();
        if (studentStore.get(studentId) == null) {
            studentId = "";
        }
        return studentId;
    }
}
