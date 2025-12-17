package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class BidListController {

    private final BidListRepository bidListRepository;

    @Autowired
    public BidListController(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    @GetMapping("/bidList/list")
    public String home(Model model, Authentication authentication) {
        String githubLogin = getGithubUsername(authentication);

        model.addAttribute("githubLogin", githubLogin);
        model.addAttribute("bidLists", bidListRepository.findAll());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bidList, Model model, Authentication authentication) {
        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bidList, BindingResult result, Model model, Authentication authentication) {
        if (!result.hasErrors()) {
            bidListRepository.save(bidList);
            return "redirect:/bidList/list";
        }
        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        BidList bidList = bidListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));

        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        model.addAttribute("bidList", bidList);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            String githubLogin = getGithubUsername(authentication);
            model.addAttribute("githubLogin", githubLogin);
            return "bidList/update";
        }

        bidList.setBidListId(id);
        bidListRepository.save(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        BidList bidList = bidListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));
        bidListRepository.delete(bidList);
        return "redirect:/bidList/list";
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