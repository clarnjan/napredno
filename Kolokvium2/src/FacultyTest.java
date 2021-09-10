//package mk.ukim.finki.vtor_kolokvium;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Grade{
    String courseName;
    int grade;

    public Grade(String courseName, int grade) {
        this.courseName = courseName;
        this.grade = grade;
    }
}

class Student{
    String id;
    int yearsOfStudies;
    Map<Integer,Set<Grade>> grades;
    int numberOfCourses;

    public Student(String id, int yearsOfStudies) {
        this.id = id;
        this.yearsOfStudies = yearsOfStudies;
        grades = new HashMap<>();
        numberOfCourses = 0;
    }
    public double getAverageGrade(){
        int sum = 0;
        int num = 0;
        for(Integer t: grades.keySet()){
            int i =0;
            for(Grade g:grades.get(t)){
                i+= g.grade;
            }
            sum+=i;
            num+=grades.get(t).size();
        }
        if(num==0)
            return 5;
        return sum/(float)num;
    }

    public String getReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student: %s%n",id));
        int sum = 0;
        int num = 0;
        for(Integer t: grades.keySet()){
            sb.append(String.format("Term %d:%n",grades.get(t).size()));
            int i =0;
            for(Grade g:grades.get(t)){
                i+= g.grade;
            }
            sum+=i;
            num+=grades.get(t).size();
            sb.append(String.format("Average grade for term: %.2f%n",i/(float)grades.get(t).size()));
        }
        sb.append(String.format("Average grade: %.2f%n",sum/(float)num));
        for(Integer t: grades.keySet()){
            for(Grade g:grades.get(t)){
                sb.append(String.format("%s, ",g.courseName));
            }
        }
        return sb.toString();
    }

    public int getNumberOfCourses() {
        return numberOfCourses;
    }

    @Override
    public String toString() {
        return String.format("Student: %s Courses passed: %d Average grade: %.2f",id,numberOfCourses,getAverageGrade());
    }

    public String getId() {
        return id;
    }
    public void addGrade(int term, String courseName, int grade){
        grades.computeIfPresent(term,(k,v)->{
            v.add(new Grade(courseName,grade));
            return v;
        });
        grades.computeIfAbsent(term,(k)->{
            HashSet<Grade> temp = new HashSet<>();
            temp.add(new Grade(courseName,grade));
            return temp;
        });
        numberOfCourses+=1;
    }
}

class Faculty {
    Map<String,Student> students;
    String log="";

    public Faculty() {
        students = new HashMap<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        students.computeIfAbsent(id,(k)-> new Student(id,yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        Set<Grade> grades = new HashSet<>();
        Student st = students.get(studentId);
        if((st.yearsOfStudies==3 && term>6)||(st.yearsOfStudies==4 && term>8))
            throw new OperationNotAllowedException(term,studentId);
        if(st.grades.containsKey(term)){
            grades = st.grades.get(term);
            if(grades.size()>=3)
                throw new OperationNotAllowedException(studentId,term);
            st.addGrade(term,courseName,grade);
        }
        else{
            st.addGrade(term,courseName,grade);
        }
        if((st.yearsOfStudies==3 && st.numberOfCourses>=18)||(st.yearsOfStudies==4 && st.numberOfCourses>=24)) {
            if(!log.equals(""))
                log+=String.format("%n");
            log += String.format("Student with ID %s graduated with average grade 7.50 in %d years", studentId, st.yearsOfStudies);
            students.remove(studentId);
        }
    }

    String getFacultyLogs() {
        return log;
    }

    String getDetailedReportForStudent(String id) {
        return students.get(id).getReport();
    }

    void printFirstNStudents(int n) {
        List<Student> x = students.values().stream().sorted(Comparator.comparing(Student::getNumberOfCourses).thenComparing(Student::getAverageGrade).thenComparing(Student::getId).reversed()).collect(Collectors.toList());
        for(int i=0;i<n;i++) {
            if(i<x.size())
            System.out.println(x.get(i));
        }
    }

    void printCourses() {

    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}

class OperationNotAllowedException extends Exception{
    OperationNotAllowedException(int term,String id){
        super(String.format("Term %d is not possible for student with ID %s",term,id));
    }
    OperationNotAllowedException(String id,int term){
        super(String.format("Student %s already has 3 grades in term %d",id,term));
    }
}
