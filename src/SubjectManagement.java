import model.Score;
import model.Student;
import model.Subject;

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

    // 과목 타입(필수, 선택)
    private static final String SUBJECT_TYPE_MANDATORY = "MANDATORY";
    private static final String SUBJECT_TYPE_CHOICE = "CHOICE";

    // 타입별 과목 리스트
    private static final List<String> subjectsMandatoryList = List.of("Java", "객체지향", "Spring", "JPA", "MySQL");
    private static final List<String> subjectsChoiceList = List.of("디자인 패턴", "Spring Security", "Redis", "MongoDB");

    // 싱글톤은 아니고 유사한 무언가의 동작...
    private SubjectManagement() {}
    public static Map<String, Subject> getStore() {
        if (subjectStore == null) {
            subjectStore = new HashMap<>();
            setSubjectList(subjectsMandatoryList, SUBJECT_TYPE_MANDATORY);
            setSubjectList(subjectsChoiceList, SUBJECT_TYPE_CHOICE);
        }
        return subjectStore;
    }

    // 과목 리스트 설정
    private static void setSubjectList(List<String> subjects, String type) {
        String subjectSeq = "";

        for (String subject : subjects) {
            subjectSeq = sequence();
            subjectIndex++;
            subjectStore.put(subjectSeq, new Subject(subjectSeq, subject, type));
        }
    }

    // 과목이름을 가지고 ID를 구하는 메서드, 실패 null 반환
    public static String getSubjectIdByName(String subjectName) {
        for (Map.Entry<String, Subject> entry : subjectStore.entrySet()) {
            if (entry.getValue().getSubjectName().equals(subjectName)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // 선택과목을 입력받는 메서드, 선택한 과목 리스트 반환
    public static List<String> getChoiceSubject() {
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

    //필수 과목 set으로 반환
    public static Set<String> getMandatorySubjectSet() {
        Set<String> mandatorySubjectSet = new HashSet<>();

        for (String subject : subjectsMandatoryList) {
            String subjectId = getSubjectIdByName(subject);
            if (subjectId != null) {
                mandatorySubjectSet.add(subjectId);
            }
        }

        return mandatorySubjectSet;
    }

    public static List<String> getStudentMandatorySubjectList(Set<String> studentSubject) {
        List<String> studentMandatorySubjectList = new ArrayList<String>();

        for(String subjectId : studentSubject) {
            String type = subjectStore.get(subjectId).getSubjectType();
            String name = subjectStore.get(subjectId).getSubjectName();

            if (SUBJECT_TYPE_MANDATORY.equals(type)) {
                studentMandatorySubjectList.add(name);
            }
        }

        return studentMandatorySubjectList;
    }

    public static List<String> getStudentChoiceSubject(Set<String> studentSubject) {
        List<String> studentChoiceSubjectList = new ArrayList<String>();

        for(String subjectId : studentSubject) {
            String type = subjectStore.get(subjectId).getSubjectType();
            String name = subjectStore.get(subjectId).getSubjectName();

            if (SUBJECT_TYPE_CHOICE.equals(type)) {
                studentChoiceSubjectList.add(name);
            }
        }

        return studentChoiceSubjectList;
    }
  
    public static String getSubjectNameById(String studentSubjectId) {
        return subjectStore.get(studentSubjectId).getSubjectName();
    }

    // 수강생이 수강하는 과목 중 한 가지를 선택하는 메서드, 취소 시 "" 리턴
    public static String inquireSubject(List<String> studentSubjectId) {
        String selectedSubject = "";
        int it = 1;
        while (true) {
            // 사용자의 과목 리스트 보여주기
            System.out.println("등록할 과목 선택:");
            System.out.print("(");
            for (String id : studentSubjectId) {
                System.out.print(it + "." + subjectStore.get(id).getSubjectName() + ", ");
                it++;
            }
            System.out.print(it + ".취소) ");

            try {
                int index = Integer.parseInt(sc.next());
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

    public static boolean isMandatory(String subjectId) {
        return SUBJECT_TYPE_MANDATORY.equals(subjectStore.get(subjectId).getSubjectType());
    }
}
