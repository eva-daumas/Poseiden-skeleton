package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
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
public class RuleNameController {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @RequestMapping("/ruleName/list")
    public String home(Model model, Authentication authentication) {
        String githubLogin = getGithubUsername(authentication);

        model.addAttribute("githubLogin", githubLogin);
        model.addAttribute("rules", ruleNameRepository.findAll());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName, Model model, Authentication authentication) {
        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model, Authentication authentication) {
        if (!result.hasErrors()) {
            ruleNameRepository.save(ruleName);
            return "redirect:/ruleName/list";
        }
        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        RuleName ruleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RuleName Id:" + id));

        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            ruleName.setId(id);
            String githubLogin = getGithubUsername(authentication);
            model.addAttribute("githubLogin", githubLogin);
            return "ruleName/update";
        }
        ruleName.setId(id);
        ruleNameRepository.save(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        RuleName ruleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RuleName Id:" + id));
        ruleNameRepository.delete(ruleName);
        return "redirect:/ruleName/list";
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