import model.Subject;
import model.SubjectType;

import java.util.*;

public class SubjectManagement {
    // 스캐너
    private static final Scanner sc = new Scanner(System.in);

    // 데이터 저장소
    private static Map<String, Subject> subjectStore;

    // index 관리 필드
    private static int subjectIndex;
    private static final String INDEX_TYPE_SUBJECT = "SU";

    // index 자동 증가
    private static String sequence() {
        subjectIndex++;
        return INDEX_TYPE_SUBJECT + subjectIndex;
    }

//    // **SubjectType Enum Class로 이동**
//    // 과목 타입(필수, 선택)
//    private static final String SUBJECT_TYPE_MANDATORY = "MANDATORY";
//    private static final String SUBJECT_TYPE_CHOICE = "CHOICE";

    // 과목 타입 최소 선택 개수
    private static final int SUBJECT_MANDATORY_MIN = 3;
    private static final int SUBJECT_CHOICE_MIN = 2;

    // 타입별 과목 리스트
    private static final List<String> subjectsMandatoryList = List.of("Java", "객체지향", "Spring", "JPA", "MySQL");
    private static final List<String> subjectsChoiceList = List.of("디자인 패턴", "Spring Security", "Redis", "MongoDB");

    // 싱글톤은 아니고 유사한 무언가의 동작...
    private SubjectManagement() {}
    public static Map<String, Subject> getStore() {
        if (subjectStore == null) {
            subjectStore = new HashMap<>();
            setSubjectList(subjectsMandatoryList, SubjectType.SUBJECT_TYPE_MANDATORY);
            setSubjectList(subjectsChoiceList, SubjectType.SUBJECT_TYPE_CHOICE);
        }
        return subjectStore;
    }

    // 과목 리스트 설정
    private static void setSubjectList(List<String> subjects, SubjectType type) {
        String subjectSeq = "";

        for (String subject : subjects) {
            subjectSeq = sequence();
            subjectStore.put(subjectSeq, new Subject(subjectSeq, subject, type));
        }
    }


    private static String getSubjectNameById(String studentSubjectId) {
        return subjectStore.get(studentSubjectId).getSubjectName();
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

    // ** 메모 있음 **
    // 선택과목을 입력받는 메서드, 선택한 과목 리스트 반환, 취소시 null 반환
    public static List<String> inquireChoiceSubject() {
        /*
         * subjectsChoiceList, SUBJECT_CHOICE_MIN, .. 등을
         * enum 클래스로 관리하는 것이 좋아 보임
         * 우선은 이 메서드를 public으로 다른 클래스들이 사용할 수 있도록 함
         */
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
        int count = 0;

        while (true) {
            if (choiceSubject.size() == subjectsChoiceList.size()) {
                System.out.println("\n모든 수강을 선택하셨습니다.");
                break;
            }
            // 사용자가 선택한 과목 출력
            System.out.print("수강할 선택 과목(" + SUBJECT_CHOICE_MIN + "개 이상): ");
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
                int index = Integer.parseInt(sc.nextLine()) - 1;
                if (index == subjectsChoiceList.size()) {
                    if (count >= SUBJECT_CHOICE_MIN) { break; } // 등록 완료
                    else {
                        System.out.println("\n최소 " + SUBJECT_CHOICE_MIN + "개 선택이 필요합니다.");
                        continue;
                    }
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
                    count++;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n번호를 입력해주세요.");
            }
        }
        return choiceSubject;
    }

    // ** 메모 있음 **
    // 필수과목을 입력받는 메서드, 선택한 과목 리스트 반환, 취소시 null 반환
    public static List<String> inquireMandatorySubject() {
        /*
         * subjectsMandatoryList, SUBJECT_MANDATORY_MIN, .. 등을
         * enum 클래스로 관리하는 것이 좋아 보임
         * 우선은 이 메서드를 public으로 다른 클래스들이 사용할 수 있도록 함
         */
        List<String> mandatorySubject = new ArrayList<>();
        Boolean[] selected = new Boolean[subjectsMandatoryList.size()]; // 고른 과목인지 체크하는 배열
        Arrays.fill(selected, false);
        StringBuilder subjectsChoice = new StringBuilder();
        int count = 0;

        while (true) {
            if (mandatorySubject.size() == subjectsMandatoryList.size()) {
                System.out.println("\n모든 수강을 선택하셨습니다.");
                break;
            }
            // 사용자가 선택한 과목 출력
            System.out.print("수강할 필수 과목(" + SUBJECT_MANDATORY_MIN + "개 이상): ");
            if (!mandatorySubject.isEmpty()) {
                System.out.print(mandatorySubject);
            }
            subjectsChoice.append("\n(");
            for (int i = 0; i < subjectsMandatoryList.size(); i++) {
                if (!selected[i]) {
                    subjectsChoice.append(i + 1);
                } else {
                    subjectsChoice.append("x");
                }
                subjectsChoice.append(".").append(subjectsMandatoryList.get(i)).append(", ");
            }
            subjectsChoice.append(subjectsMandatoryList.size() + 1).append(". 등록 완료, ");
            subjectsChoice.append(subjectsMandatoryList.size() + 2).append(". 취소) ");
            System.out.print(subjectsChoice.toString());
            subjectsChoice.delete(0, subjectsChoice.length());

            // 선택과목 입력
            try {
                int index = Integer.parseInt(sc.nextLine()) - 1;
                if (index == subjectsMandatoryList.size()) {
                    if (count >= SUBJECT_MANDATORY_MIN) { break; } // 등록 완료
                    else {
                        System.out.println("\n최소 " + SUBJECT_MANDATORY_MIN + "개 선택이 필요합니다.");
                        continue;
                    }
                } else if (index == subjectsMandatoryList.size() + 1) {
                    mandatorySubject = null;
                    break; // 등록 취소
                } else if (index < 0 || index > subjectsMandatoryList.size() + 1) {
                    System.out.println("\n잘못된 번호입니다.");
                    continue;
                }

                String choiceNumber = subjectsMandatoryList.get(index);
                if (mandatorySubject.contains(choiceNumber)) {
                    System.out.println("\n이미 선택된 과목입니다.");
                } else {
                    mandatorySubject.add(choiceNumber);
                    selected[index] = true;
                    count++;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n번호를 입력해주세요.");
            }
        }
        return mandatorySubject;
    }

    //필수 과목 set으로 반환
    private static Set<String> getMandatorySubject() {
        Set<String> mandatorySubjectSet = new HashSet<>();

        for (String subject : subjectsMandatoryList) {
            String subjectId = getSubjectIdByName(subject);
            if (subjectId != null) {
                mandatorySubjectSet.add(subjectId);
            }
        }

        return mandatorySubjectSet;
    }

    private static List<String> getStudentMandatorySubjectList(Set<String> studentSubject) {
        List<String> studentMandatorySubjectList = new ArrayList<String>();

        for(String subjectId : studentSubject) {
            SubjectType type = subjectStore.get(subjectId).getSubjectType();
            String name = subjectStore.get(subjectId).getSubjectName();

            if (SubjectType.SUBJECT_TYPE_MANDATORY.equals(type)) {
                studentMandatorySubjectList.add(name);
            }
        }

        return studentMandatorySubjectList;
    }

    private static List<String> getStudentChoiceSubject(Set<String> studentSubject) {
        List<String> studentChoiceSubjectList = new ArrayList<String>();

        for(String subjectId : studentSubject) {
            SubjectType type = subjectStore.get(subjectId).getSubjectType();
            String name = subjectStore.get(subjectId).getSubjectName();

            if (SubjectType.SUBJECT_TYPE_CHOICE.equals(type)) {
                studentChoiceSubjectList.add(name);
            }
        }

        return studentChoiceSubjectList;
    }

    // 수강생이 수강하는 과목 중 한 가지를 선택하는 메서드, 취소 시 "" 리턴
    private static String inquireSubject(List<String> studentSubjectId) {
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

    private static boolean isMandatory(String subjectId) {
        return SubjectType.SUBJECT_TYPE_MANDATORY.equals(subjectStore.get(subjectId).getSubjectType());
    }
}
