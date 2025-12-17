package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
public class TradeController {

    @Autowired
    private TradeRepository tradeRepository;

    @RequestMapping("/trade/list")
    public String home(Model model, Authentication authentication) {
        String githubLogin = getGithubUsername(authentication);

        model.addAttribute("githubLogin", githubLogin);
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addTradeForm(Trade trade, Model model, Authentication authentication) {
        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model, Authentication authentication) {
        if (!result.hasErrors()) {
            tradeRepository.save(trade);
            return "redirect:/trade/list";
        }
        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        return "trade/add";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Trade Id:" + id));

        String githubLogin = getGithubUsername(authentication);
        model.addAttribute("githubLogin", githubLogin);
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            trade.setTradeId(id);
            String githubLogin = getGithubUsername(authentication);
            model.addAttribute("githubLogin", githubLogin);
            return "trade/update";
        }
        trade.setTradeId(id);
        tradeRepository.save(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Trade Id:" + id));
        tradeRepository.delete(trade);
        return "redirect:/trade/list";
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