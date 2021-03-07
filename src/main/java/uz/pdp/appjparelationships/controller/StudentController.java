package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    SubjectRepository subjectRepository;

    // Get 1.Vazirlik
    @GetMapping("/forMinistry")
    public Page<Student> getStudentsMinistry(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAll(pageable);
    }

    // Get 2.Universitet
    @GetMapping("/forUniversity/{id}")
    public Page<Student> getStudentsUniversity(@RequestParam int page, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_Faculty_UniversityId(id, pageable);
    }

    // Get 3.Fakultet
    @GetMapping("/forFaculty/{id}")
    public Page<Student> getStudentsFaculty(@RequestParam int page, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_FacultyId(id, pageable);
    }

    // Get 4.Guruh
    @GetMapping("/forGroup/{id}")
    public Page<Student> getStudentsGroup(@RequestParam int page, @PathVariable Integer id) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroupId(id, pageable);
    }

    // Create
    @PostMapping
    public String add(@RequestBody StudentDto studentDto) {
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (!optionalAddress.isPresent()) return studentDto.getAddressId() + " address not found !";
        student.setAddress(optionalAddress.get());
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()) return studentDto.getGroupId() + " group not found !";
        student.setGroup(optionalGroup.get());
        List<Subject> subjectList = new ArrayList<>();
        for (Integer id : studentDto.getSubjectIds()) {
            Optional<Subject> optionalSubject = subjectRepository.findById(id);
            if (!optionalSubject.isPresent()) return id + " subject not found !";
            subjectList.add(optionalSubject.get());
        }
        student.setSubjectList(subjectList);
        studentRepository.save(student);
        return "Student successfully saved !";
    }

    // Update
    @PutMapping("/{id}")
    public String edit(@PathVariable Integer id, @RequestBody StudentDto studentDto) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()) return id + " student not found !";
        Student student = optionalStudent.get();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()) return studentDto.getGroupId() + " group not found !";
        student.setGroup(optionalGroup.get());
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (!optionalAddress.isPresent()) return studentDto.getAddressId() + " address not found !";
        student.setAddress(optionalAddress.get());
        List<Subject> subjectList = new ArrayList<>();
        for (Integer subjectId : studentDto.getSubjectIds()) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            if (!optionalSubject.isPresent()) return subjectId + " subject not found !";
            subjectList.add(optionalSubject.get());
        }
        student.setSubjectList(subjectList);
        studentRepository.save(student);
        return id + " student successfully edited !";
    }

    // Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()) return id + " student not found !";
        studentRepository.deleteById(id);
        return id + " student successfully deleted !";
    }
}
