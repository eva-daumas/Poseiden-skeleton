package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;

    @RequestMapping("/rating/list")
    public String home(Model model, Authentication authentication) {
        String githubLogin = getGithubUsername(authentication);

        model.addAttribute("githubLogin", githubLogin);
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating, Model model, Authentication authentication) {
        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model, Authentication authentication) {
        if (!result.hasErrors()) {
            ratingRepository.save(rating);
            return "redirect:/rating/list";
        }
        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        return "rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id:" + id));

        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            rating.setId(id);
            String githubLogin = getGithubUsername(authentication);
            model.addAttribute("githubLogin", githubLogin);
            return "rating/update";
        }
        rating.setId(id);
        ratingRepository.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id:" + id));
        ratingRepository.delete(rating);
        return "redirect:/rating/list";
    }

    private String getGithubUsername(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Guest";
        }

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

            String login = (String) attributes.get("login");
            if (login != null) {
                return login;
            }

            String name = (String) attributes.get("name");
            if (name != null) {
                return name;
            }
        }

        return authentication.getName();
    }
}