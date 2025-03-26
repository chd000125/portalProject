package edu.du.portalproject.controller;

import edu.du.portalproject.entity.Account;
import edu.du.portalproject.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // ✅ 회원가입 폼
    @GetMapping("/register")
    public String showRegisterForm() {
        return "portalAccount/accountRegister"; // register.html (회원가입 페이지)
    }

    // ✅ 회원가입 처리
    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String name,
                           @RequestParam String password,
                           Model model) {
        try {
            accountService.registerAccount(email, name, password);
            return "redirect:/account/login"; // 회원가입 성공 후 로그인 페이지로 이동
        } catch (Exception e) {
            model.addAttribute("error", "회원가입 실패: " + e.getMessage());
            return "/portalAccount/accountRegister";
        }
    }

    // ✅ 로그인 폼
    @GetMapping("/login")
    public String showLoginForm() {
        return "/portalAccount/accountLogin"; // login.html (로그인 페이지)
    }

    // ✅ 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam(value="email") String email,
                        @RequestParam(value="password") String password,
                        HttpSession session,
                        Model model) {
        System.out.println("email: " + email);
        System.out.println("password: " + password);
        Account account = accountService.login(email, password);
        if (account != null) {
            session.setAttribute("account", account); // 세션에 사용자 정보 저장
            return "/portalMain/index"; // 로그인 성공 후 메인으로 이동
        } else {
            model.addAttribute("error", "이메일 또는 비밀번호가 잘못되었습니다.");
            return "redirect:/portalAccount/accountLogin";
        }
    }

    // ✅ 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return "/portalMain/index";
    }

    @GetMapping("/accountInfo")
    public String showAccountInfo(Model model) {
        return "portalAccount/accountInfo";
    }
}
