package edu.du.portalproject.service;

import edu.du.portalproject.entity.Account;
import edu.du.portalproject.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // ✅ 회원가입
    public Account registerAccount(String email, String name, String password) {
        Account account = new Account();
        account.setEmail(email);
        account.setName(name);
        account.setPassword(password); // 비밀번호를 그대로 저장 (암호화 X)
        return accountRepository.save(account);
    }

    // ✅ 로그인 (이메일과 비밀번호 검증)
    public Account login(String email, String password) {
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (account.getPassword().equals(password)) { // 단순 문자열 비교 (보안 취약)
                return account;
            }
        }
        return null; // 로그인 실패
    }

    // ✅ 특정 유저 조회
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }
}
