package com.ImmunizationTracker.api.controllers;

import com.ImmunizationTracker.api.models.StudentRecordsViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/lists")
public class ListsController {

    @GetMapping("/gender")
    @ResponseBody
    public String[] getGenderList() {
        String[] genders = new String[]{"Male", "Female", "Intersex"};
        return genders;
    }

    @GetMapping("/grade")
    @ResponseBody
    public String[] getGradeList() {
        String[] grades = new String[]{"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th"};
        return grades;
    }

    @GetMapping("/homeroom")
    @ResponseBody
    public String[] getHomeroomList() {
        String[] homerooms = new String[]{"Ms. Honey", "Ms. Trunchbull"};
        return homerooms;
    }
}