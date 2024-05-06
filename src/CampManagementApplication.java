import model.Score;
import model.Student;
import model.Subject;

import java.util.*;

/**
 * 구현 메모
 *
 * 1. 숫자가 아닌 값 입력되면 오류로 프로그램 종료됨
 * 프로그램은 종료되지 않게 하고, 다시 값을 받게 하거나 수정단계만 빠져나가도록 하기
 * switch 변수를 String 형으로 작성  or 예외 try-catch
 */

public class CampManagementApplication {
    // 데이터 저장소  *(변경 전) List<Object> ------> Map<Id, Object> (변경 후)
    private static Map<String, Student> studentStore;
    private static Map<String, Subject> subjectStore;
    private static Map<String, Score> scoreStore;

    // 과목 타입
    private static String SUBJECT_TYPE_MANDATORY = "MANDATORY";
    private static String SUBJECT_TYPE_CHOICE = "CHOICE";

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
        List<String> subjectsMandatory = List.of("Java", "객체지향", "Spring", "JPA", "MySQL");
        List<String> subjectsChoice = List.of("디자인 패턴", "Spring Security", "Redis", "MongoDB");
        setSubjectList(subjectsMandatory, SUBJECT_TYPE_MANDATORY);
        setSubjectList(subjectsChoice, SUBJECT_TYPE_CHOICE);
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

        System.out.print("(1.아주좋음, 2.좋음, 3.보통, 4.나쁨, 5.아주나쁨)\n수강생 상태 입력: ");
        String studentState = sc.next();

        System.out.print("(1.디자인 패턴, 2.Spring security, 3.Redis, 4.MongoDB, 5.종료)\n수강할 과목 선택: ");
        Set<String> studentSubject = new HashSet<>();

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
        System.out.println(studentStore);

        System.out.println("\n수강생 목록 조회 성공!");
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
        System.out.println("이름: " + student.getStudentName());

        Set<String> subjects = student.getStudentSubject();

        System.out.println("\n수강생 상세 정보 조회 성공!");
    }

    // 수강생 상태별 목록 조회
    private static void inquireStudentByState() {
        System.out.println("\n수강생을 상태별로 조회합니다...");
        /*
         * 조회하고 싶은 상태를 사용자에게 입력받습니다. (예시: "아주좋음")
         * 수강생 목록은 studentStore에 있고, Map 컬렉션을 사용합니다.
         * Map의 모든 요소들을 돌면서  if문으로 입력받은 상태 "아주좋음"과 일치하는 요소들의
         *  ID와 이름으로 출력해주세요.
         * 아래 참고하시면 좋을 것 같습니다!
         * https://velog.io/@woply/HashMap-%EC%A0%84%EC%B2%B4-%EA%B0%92%EC%9D%84-%EC%B6%9C%EB%A0%A5%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95
         * https://coding-factory.tistory.com/556
         */
        System.out.println("\n조회하고 싶은 상태를 입력해주세요: ");
        String studentState = sc.next();

        System.out.println(studentStore);

        System.out.println("\n수강생 상태별 목록 조회 성공!");
    }

    // 수강생 정보 수정
    private static void updateStudent() {
        System.out.println("\n수강생 정보를 수정합니다...");
        System.out.println("\n수강생 고유번호를 입력해주세요: ");
        String studentId = sc.next();

        /* 컬렉션이 list 일 때 코드
        for (Student student : studentStore) {
            if (studentId.equals(student.getStudentId())) {
                String studentState = "";

                System.out.println("변경하는 수강생 이름을 입력하세요: ");
                String studentName = sc.next();

                System.out.println("변경하는 수강생 상태를 입력하세요: \n 1.아주좋음, 2.좋음, 3.보통, 4.나쁨, 5.아주나쁨");
                int stateNumber = Integer.parseInt(sc.next());

                switch (stateNumber){
                    case 1 -> studentState = "아주좋음";
                    case 2 -> studentState = "좋음";
                    case 3 -> studentState = "보통";
                    case 4 -> studentState = "나쁨";
                    case 5 -> studentState = "아주나쁨";
                    default -> System.out.println("잘못된 입력입니다");
                }

                student.setStudentName(studentName); //수강생 이름 set
                student.setStudentName(studentState); //수강생 상태 set

                //수강생 정보 List로 반영 하는 메소드 추가 예정

            }else{
                System.out.println("해당 수강생이 없습니다.");
            }
        }
         */

        Student student = studentStore.get(studentId);
        if (student != null) {
            String studentState = "";

            System.out.println("변경하는 수강생 이름을 입력하세요: ");
            String studentName = sc.next();

            System.out.println("변경하는 수강생 상태를 입력하세요: \n 1.아주좋음, 2.좋음, 3.보통, 4.나쁨, 5.아주나쁨");
            int stateNumber = Integer.parseInt(sc.next());

            switch (stateNumber){
                case 1 -> studentState = "아주좋음";
                case 2 -> studentState = "좋음";
                case 3 -> studentState = "보통";
                case 4 -> studentState = "나쁨";
                case 5 -> studentState = "아주나쁨";
                default -> System.out.println("잘못된 입력입니다");
            }

            student.setStudentName(studentName); //수강생 이름 set
            student.setStudentName(studentState); //수강생 상태 set

            //수강생 정보 List로 반영 하는 메소드 추가 예정

        } else {
            System.out.println("해당 수강생이 없습니다.");
        }
    }

    private static void deleteStudent() {
        System.out.println("\n수강생 정보를 삭제합니다...");
        System.out.println("\n수강생 고유번호를 입력해주세요: ");
        String studentId = sc.next();
        /* 컬렉션이 list 일 때 코드
        Iterator<Student> it = studentStore.iterator();
        while (it.hasNext()) {
            Student studentToDelete = (Student)it.next();
            if (studentToDelete.getStudentId().equals(studentId)) {
                System.out.print("정말로 " + studentToDelete.getStudentName() + "님의 정보를 삭제하시겠습니까?:\n ('네' 입력시 삭제) ");
                if ("네".equals(sc.next())) {
                    it.remove();
                    System.out.println("삭제되었습니다.");
                } else {
                    System.out.println("\n삭제가 취소되었습니다.");
                }
                return;
            }
        }
        System.out.println("해당 수강생이 없습니다.");
        */
        Student deleteStudent = studentStore.get(studentId);
        if (deleteStudent != null) {
            System.out.print("정말로 " + deleteStudent.getStudentName() + "님의 정보를 삭제하시겠습니까?:\n ('네' 입력시 삭제) ");
            if ("네".equals(sc.next())) {
                studentStore.remove(studentId);
                System.out.println("삭제되었습니다.");
            } else {
                System.out.println("\n삭제가 취소되었습니다.");
            }
        }
        else {
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
            System.out.println("4. 메인 화면 이동");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();

            switch (input) {
                case 1 -> createScore(); // 수강생의 과목별 시험 회차 및 점수 등록
                case 2 -> updateRoundScoreBySubject(); // 수강생의 과목별 회차 점수 수정
                case 3 -> inquireRoundGradeBySubject(); // 수강생의 특정 과목 회차별 등급 조회
                case 4 -> flag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n메인 화면 이동...");
                    flag = false;
                }
            }
        }
    }

    private static String getStudentId() {
        System.out.print("\n관리할 수강생의 번호를 입력하시오...");
        return sc.next();
    }

    // 수강생의 과목별 시험 회차 및 점수 등록
    private static void createScore() {
        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        System.out.println("시험 점수를 등록합니다...");
        // 기능 구현
        System.out.println("\n점수 등록 성공!");
    }

    // 수강생의 과목별 회차 점수 수정
    private static void updateRoundScoreBySubject() {
        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        // 기능 구현 (수정할 과목 및 회차, 점수)
        System.out.println("시험 점수를 수정합니다...");
        // 기능 구현
        System.out.println("\n점수 수정 성공!");
    }

    // 수강생의 특정 과목 회차별 등급 조회
    private static void inquireRoundGradeBySubject() {
        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        // 기능 구현 (조회할 특정 과목)
        System.out.println("회차별 등급을 조회합니다...");
        // 기능 구현
        System.out.println("\n등급 조회 성공!");
    }

}
