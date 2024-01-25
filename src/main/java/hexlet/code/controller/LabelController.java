package hexlet.code.controller;

import hexlet.code.dto.LabelRequest;
import hexlet.code.dto.LabelResponse;
import hexlet.code.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelResponse updateById(@PathVariable Long id, @RequestBody LabelRequest labelRequest) {
        return labelService.updateById(id, labelRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelResponse save(@RequestBody LabelRequest labelRequest) {
        return labelService.save(labelRequest);
    }

    @GetMapping("/{id}")
    public LabelResponse findById(@PathVariable Long id) {
        return labelService.findById(id);
    }

    @GetMapping
    public ResponseEntity<List<LabelResponse>> findAll() {
        List<LabelResponse> labels = labelService.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(labels.size()));

        return new ResponseEntity<>(labels, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        labelService.deleteById(id);
    }
}
