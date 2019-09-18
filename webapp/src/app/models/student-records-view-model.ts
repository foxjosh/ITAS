import { StudentRecord } from "./student-record";

export class StudentRecordsViewModel {
    studentRecords: StudentRecord[];

    constructor(studentRecords: StudentRecord[]) {
        this.studentRecords = studentRecords;
    }
}
