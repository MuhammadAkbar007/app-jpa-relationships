package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    // Create subject
    @RequestMapping(method = RequestMethod.POST)
    public String addSubj(@RequestBody Subject subject) {
        if (subjectRepository.existsByName(subject.getName())) {
            return "This subject already exists !";
        } else {
            subjectRepository.save(subject);
            return "Subject successfully saved !";
        }
    }

    // Read subject
    @GetMapping // @RequestMapping(method = RequestMethod.GET)
    public List<Subject> getSubjects() {
        return subjectRepository.findAll();
    }
}
