package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
public class CurveController {

    @Autowired
    private CurvePointRepository curvePointRepository;

    @RequestMapping("/curvePoint/list")
    public String home(Model model, Authentication authentication) {
        String githubLogin = getGithubUsername(authentication);

        model.addAttribute("githubLogin", githubLogin);
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurveForm(CurvePoint curvePoint, Model model, Authentication authentication) {
        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model, Authentication authentication) {
        if (!result.hasErrors()) {
            curvePointRepository.save(curvePoint);
            return "redirect:/curvePoint/list";
        }
        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CurvePoint Id:" + id));

        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurve(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                              BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            curvePoint.setId(id);
            String githubLogin = getGithubUsername(authentication);
            model.addAttribute("githubLogin", githubLogin);
            return "curvePoint/update";
        }
        curvePoint.setId(id);
        curvePointRepository.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CurvePoint Id:" + id));
        curvePointRepository.delete(curvePoint);
        return "redirect:/curvePoint/list";
    }

    private String getGithubUsername(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Guest";
        }

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

            // GitHub stocke le nom d'utilisateur dans 'login'
            String login = (String) attributes.get("login");
            if (login != null) {
                return login;
            }

            // Fallback sur le name
            String name = (String) attributes.get("name");
            if (name != null) {
                return name;
            }
        }

        return authentication.getName();
    }
}