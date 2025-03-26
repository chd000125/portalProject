package edu.du.portalproject.controller;

import edu.du.portalproject.entity.Board;
import edu.du.portalproject.entity.Account;
import edu.du.portalproject.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // ✅ 게시글 목록 조회, 페이징
    @GetMapping("/list")
    public String showBoard(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Board> board = boardService.getPostsPaged(page);
        model.addAttribute("board", board);
        return "portalBoard/boardList";
    }
    @GetMapping("/list/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", boardService.getPostById(id));
        return "portalBoard/boardDetail";
    }

    // ✅ 글쓰기 페이지 (로그인한 사용자만)
    @GetMapping("/write")
    public String writePostForm(HttpSession session, RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account"); // 통일
        if (account == null) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/portalAccount/accountLogin"; // 로그인 페이지 이동
        }
        return "portalBoard/boardWrite"; // Thymeleaf에서 boardWrite.html 열기
    }

    // ✅ 게시글 작성
    @PostMapping("/write")
    public String writePost(@RequestParam String title,
                            @RequestParam String content,
                            HttpSession session,
                            Model model) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/account/login";
        }
        try{
            Board post = new Board();
            post.setTitle(title);
            post.setContent(content);
            post.setUserName(account.getName());
            post.setCreatedAt(LocalDateTime.now());

            boardService.savePost(post);
            return "redirect:/board/list";
        }
        catch(Exception e){
            model.addAttribute("error", "글쓰기 실패: " + e.getMessage());
            return "redirect:/board/list";
        }
    }

    // ✅ 게시글 수정 (본인만 가능)
    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/account/login";
        }

        Board post = boardService.getPostById(id);
        if (post == null || !post.getUserName().equals(account.getName())) {
            redirectAttributes.addFlashAttribute("message", "본인만 수정할 수 있습니다.");
            return "/portalBoard/boardList";
        }

        model.addAttribute("post", post);
        return "portalBoard/boardEdit";
    }

    // ✅ 게시글 수정 처리
    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Long id, @RequestParam String title, @RequestParam String content, HttpSession session, RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/account/login";
        }

        Board post = boardService.getPostById(id);
        if (post == null || !post.getUserName().equals(account.getName())) {
            redirectAttributes.addFlashAttribute("message", "본인만 수정할 수 있습니다.");
            return "redirect:/board/list";
        }
        // 변경된 값으로 적용하기
        Board udPost = Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .userName(account.getName())
                .createdAt(LocalDateTime.now())
                .build();
        boardService.updatePost(udPost);
        return "redirect:/board/list/{id}";
    }

    // ✅ 게시글 삭제 (본인만 가능)
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/login";
        }

        Board post = boardService.getPostById(id);
        if (post == null || !post.getUserName().equals(account.getName())) {
            redirectAttributes.addFlashAttribute("message", "본인만 삭제할 수 있습니다.");
            return "redirect:/board/list";
        }

        boardService.deletePost(id);
        return "redirect:/board/list";
    }


}
