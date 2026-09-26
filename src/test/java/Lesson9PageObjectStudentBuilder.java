public class Lesson9PageObjectStudentBuilder {
    private Lesson9PageObjectStudent student;
    public Lesson9PageObjectStudentBuilder() {
        this.student = new Lesson9PageObjectStudent();
    }

    public Lesson9PageObjectStudentBuilder setName(String name) {
        student.name = name;

        return this;
    }

    public Lesson9PageObjectStudentBuilder setSecondName(String name) {
        student.secondName = name;

        return this;
    }

    public Lesson9PageObjectStudent build() {
        return student;
    }
}
