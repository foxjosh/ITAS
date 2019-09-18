package com.ImmunizationTracker.api.controllers;

import com.ImmunizationTracker.api.entities.StudentEntity;
import com.ImmunizationTracker.api.models.*;
import com.ImmunizationTracker.api.repository.StudentEntityRepository;
import com.ImmunizationTracker.api.services.StudentService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping(path="/studentrecords")
public class StudentRecordsController {

    @Autowired
    private StudentService studentService;


    @GetMapping("")
    @ResponseBody
    public StudentRecordsViewModel getStudentRecordsViewModel() {
        StudentRecordsViewModel vm = new StudentRecordsViewModel();
        vm.setStudentRecords(studentService.getAllStudents());
        return vm;
    }

    @GetMapping("/{studentId}")
    @ResponseBody
    public StudentRecord getStudentRecordsForStudent(@PathVariable(value="studentId") Integer studentId) throws NotFoundException {
        StudentRecord studentRecord = studentService.getStudentByStudentId(studentId);
        return studentRecord;
    }

    @PostMapping("/create")
    @ResponseBody
    public StudentRecord createStudentRecord(@RequestBody StudentRecord newStudent) {
        StudentRecord createdStudent = studentService.createStudent(newStudent);
        return createdStudent;
    }

    @PutMapping("/save/{studentId}")
    @ResponseBody
    public StudentRecord saveStudentRecord(@RequestBody StudentRecord updatedStudent, @PathVariable Integer studentId) throws NotFoundException {
        StudentRecord student = studentService.updateStudent(updatedStudent);
        return student;
    }
}
