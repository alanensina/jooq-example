package com.alanensina.school.repositories.impl;

import com.alanensina.school.jooq.generated.tables.Student;
import com.alanensina.school.jooq.generated.tables.records.StudentRecord;
import com.alanensina.school.repositories.StudentRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alanensina.school.jooq.generated.Tables.STUDENT;
import static com.alanensina.school.jooq.generated.tables.Enrollment.ENROLLMENT;

@Service
public class StudentRepositoryImpl implements StudentRepository {

    private final DSLContext dsl;

    public StudentRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<StudentRecord> findAll() {
        return dsl.selectFrom(STUDENT).fetch();
    }

    public void createStudent(String firstName, String lastName, int age, String email, String phone) {
        var studentRecord = dsl.newRecord(STUDENT);
        studentRecord.setFirstname(firstName);
        studentRecord.setLastname(lastName);
        studentRecord.setAge(age);
        studentRecord.setEmail(email);
        studentRecord.setPhone(phone);
        studentRecord.store();
    }

    public void updateStudent(Long id, String firstName, String lastName, int age, String email, String phone) {
        dsl.update(STUDENT)
                .set(STUDENT.FIRSTNAME, firstName)
                .set(STUDENT.LASTNAME, lastName)
                .set(STUDENT.AGE, age)
                .set(STUDENT.EMAIL, email)
                .set(STUDENT.PHONE, phone)
                .where(STUDENT.ID.eq(id))
                .execute();
    }

    public void deleteStudent(Long id) {
        dsl.deleteFrom(STUDENT)
                .where(STUDENT.ID.eq(id))
                .execute();
    }

    public List<StudentRecord> listStudentsByCourse(Long courseId) {
        return dsl.select(Student.STUDENT.fields())
                .from(Student.STUDENT)
                .join(ENROLLMENT).on(Student.STUDENT.ID.eq(ENROLLMENT.STUDENT_ID))
                .where(ENROLLMENT.COURSE_ID.eq(courseId))
                .fetchInto(StudentRecord.class);
    }

    public StudentRecord getById(Long id){
        return dsl.selectFrom(STUDENT)
                .where(STUDENT.ID.eq(id))
                .fetchOne();
    }
}
