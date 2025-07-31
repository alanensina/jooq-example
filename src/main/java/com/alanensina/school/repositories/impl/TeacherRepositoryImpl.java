package com.alanensina.school.repositories.impl;

import com.alanensina.school.jooq.generated.tables.records.TeacherRecord;
import com.alanensina.school.repositories.TeacherRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alanensina.school.jooq.generated.Tables.TEACHER;

@Service
public class TeacherRepositoryImpl implements TeacherRepository {

    private final DSLContext dsl;

    public TeacherRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<TeacherRecord> findAll() {
        return dsl.selectFrom(TEACHER).fetch();
    }

    public TeacherRecord getById(Long id){
        return dsl.selectFrom(TEACHER)
                .where(TEACHER.ID.eq(id))
                .fetchOne();
    }

    public void createTeacher(String firstName, String lastName, int age, String email, String phone) {
        var teacherRecord = dsl.newRecord(TEACHER);
        teacherRecord.setFirstname(firstName);
        teacherRecord.setLastname(lastName);
        teacherRecord.setAge(age);
        teacherRecord.setEmail(email);
        teacherRecord.setPhone(phone);
        teacherRecord.store();
    }

    public void updateTeacher(Long id, String firstName, String lastName, int age, String email, String phone) {
        dsl.update(TEACHER)
                .set(TEACHER.FIRSTNAME, firstName)
                .set(TEACHER.LASTNAME, lastName)
                .set(TEACHER.AGE, age)
                .set(TEACHER.EMAIL, email)
                .set(TEACHER.PHONE, phone)
                .where(TEACHER.ID.eq(id))
                .execute();
    }

    public void deleteTeacher(Long id) {
        dsl.deleteFrom(TEACHER)
                .where(TEACHER.ID.eq(id))
                .execute();
    }
}
