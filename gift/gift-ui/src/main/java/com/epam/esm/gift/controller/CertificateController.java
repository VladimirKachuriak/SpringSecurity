package com.epam.esm.gift.controller;

import com.epam.esm.gift.model.Certificate;
import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.User;
import com.epam.esm.gift.model.exception.ResourceNotFoundException;
import com.epam.esm.gift.service.CertificateService;
import com.epam.esm.gift.service.UserService;
import com.epam.esm.gift.service.UserServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("certificate")
public class CertificateController {
    private final CertificateService certificateService;
    private final UserService userService;

    @Autowired
    public CertificateController(CertificateService certificateService, UserService userService) {
        this.certificateService = certificateService;
        this.userService = userService;
    }

    @GetMapping
    public List<Certificate> getByTags(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                       @RequestParam List<String> tags) throws Exception {
        List<Certificate> certificates = certificateService.getAll(page, tags);
        for (Certificate certificate : certificates) {
            Link selfLink = linkTo(methodOn(CertificateController.class)
                    .getCertificateById(certificate.getId())).withSelfRel().withType("GET");
            Link delete = linkTo(methodOn(CertificateController.class)
                    .deleteById(certificate.getId())).withRel("delete").withType("DELETE");
            Link put = linkTo(methodOn(CertificateController.class)
                    .update(certificate.getId(), certificate)).withRel("put").withType("PUT");
            certificate.add(selfLink);
            certificate.add(delete);
            certificate.add(put);
        }
        return certificates;
    }

    @GetMapping("/{id}")
    public Certificate getCertificateById(@PathVariable int id) throws Exception {
        Certificate certificate = certificateService.getById(id);
        if (certificate == null) throw new ResourceNotFoundException(id);
        Link delete = linkTo(methodOn(CertificateController.class)
                .deleteById(certificate.getId())).withRel("delete").withType("DELETE");
        Link put = linkTo(methodOn(CertificateController.class)
                .update(certificate.getId(), certificate)).withRel("put").withType("PUT");
        for (Tag tag : certificate.getTags()) {
            Link selfLinkTag = linkTo(methodOn(TagController.class)
                    .getTagById(tag.getId())).withSelfRel().withType("GET");
            Link deleteTag = linkTo(methodOn(TagController.class)
                    .getTagById(tag.getId())).withRel("delete").withType("DELETE");
            Link putTag = linkTo(methodOn(TagController.class)
                    .getTagById(tag.getId())).withRel("put").withType("PUT");
            tag.add(selfLinkTag);
            tag.add(deleteTag);
            tag.add(putTag);
        }
        certificate.add(delete);
        certificate.add(put);
        return certificate;
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody Certificate certificate, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        certificateService.create(certificate);
        return ResponseEntity.ok("Resource created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Certificate certificate) throws Exception {
        Certificate certificateDb = certificateService.getById(id);
        if (certificateDb == null) throw new ResourceNotFoundException(id);
        certificate.setId(id);
        if (!certificateService.update(certificate)) {
            return ResponseEntity.ok("The certificate does not differ");
        }
        return ResponseEntity.ok("Resource updated successfully");
    }

    @PutMapping("/{id}/updateDuration")
    public ResponseEntity<String> updateDuration(@PathVariable int id, @RequestParam int duration) throws Exception {
        Certificate certificate = certificateService.getById(id);
        if (certificate == null) throw new ResourceNotFoundException(id);
        certificate.setDuration(duration);
        if (!certificateService.update(certificate)) {
            return ResponseEntity.ok("The certificate does not differ");
        }
        return ResponseEntity.ok("Resource updated successfully");
    }

    @PutMapping("/{id}/updatePrice")
    public ResponseEntity<String> updatePrice(@PathVariable int id, @RequestParam int price) throws Exception {
        Certificate certificate = certificateService.getById(id);
        if (certificate == null) throw new ResourceNotFoundException(id);
        certificate.setPrice(price);
        if (!certificateService.update(certificate)) {
            return ResponseEntity.ok("The certificate does not differ");
        }
        return ResponseEntity.ok("Resource updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) throws Exception {
        certificateService.delete(id);

        return ResponseEntity.ok("Resource deleted successfully");
    }

    @PostMapping("/{id}/buy")
    public ResponseEntity<String> makePurchase(@PathVariable(name = "id") int certificateId, @AuthenticationPrincipal User user) {
        if (!userService.makePurchase(user.getId(), certificateId)) {
            return ResponseEntity.ok("user haven't enough money");
        }
        return ResponseEntity.ok("Purchase was made successfully");
    }
}
