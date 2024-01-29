package hexlet.code.controller;

import hexlet.code.dto.LabelRequest;
import hexlet.code.dto.LabelResponse;
import hexlet.code.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    /**
     * Updates a label by its ID.
     *
     * @param id           The ID of the label to be updated.
     * @param labelRequest The request containing updated label information.
     * @return The updated label response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LabelResponse> updateById(@PathVariable Long id, @RequestBody LabelRequest labelRequest) {
        return ResponseEntity.ok(labelService.updateById(id, labelRequest));
    }

    /**
     * Creates a new label.
     *
     * @param labelRequest The request containing label information.
     * @return The created label response.
     */
    @PostMapping
    public ResponseEntity<LabelResponse> save(@RequestBody LabelRequest labelRequest) {
        return new ResponseEntity<>(labelService.save(labelRequest), HttpStatus.CREATED);
    }

    /**
     * Finds a label by its ID.
     *
     * @param id The ID of the label to be retrieved.
     * @return The label response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LabelResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(labelService.findById(id));
    }

    /**
     * Finds all labels.
     *
     * @return ResponseEntity with a list of label responses and headers including total count.
     */
    @GetMapping
    public ResponseEntity<List<LabelResponse>> findAll() {
        List<LabelResponse> labels = labelService.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(labels.size()));
        return new ResponseEntity<>(labels, headers, HttpStatus.OK);
    }

    /**
     * Deletes a label by its ID.
     *
     * @param id The ID of the label to be deleted.
     * @return ResponseEntity indicating the success of the deletion (HTTP 204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        labelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
