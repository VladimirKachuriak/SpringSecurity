package com.epam.esm.gift.controller;

import com.epam.esm.gift.model.Certificate;
import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.User;
import com.epam.esm.gift.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/tag")
    public Tag getMostUsedTag(@PathVariable int id) {
        Tag tag = userService.getMostUsedTag(id);
        Link selfLink = linkTo(methodOn(TagController.class)
                .getTagById(tag.getId())).withSelfRel().withType("GET");
        Link delete = linkTo(methodOn(TagController.class)
                .getTagById(tag.getId())).withRel("delete").withType("DELETE");
        Link put = linkTo(methodOn(TagController.class)
                .getTagById(tag.getId())).withRel("put").withType("PUT");
        tag.add(selfLink);
        tag.add(delete);
        tag.add(put);
        return tag;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> getMostUsedTag(@RequestBody User user) {
        userService.create(user);
        return ResponseEntity.ok("user has been registered");
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getById(id);
    }

    @PostMapping("/{id}/makePurchase")
    public ResponseEntity<String> makePurchase(@PathVariable(name = "id") int userId, int certificateId) {
        if (!userService.makePurchase(userId, certificateId)) {
            return ResponseEntity.ok("user haven't enough money");
        }
        return ResponseEntity.ok("Purchase was made successfully");
    }
}
