package edu.du.portalproject.service;

import edu.du.portalproject.entity.Board;
import edu.du.portalproject.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // ✅ 게시글 목록 조회
    public List<Board> getAllPosts() {
        return boardRepository.findAll();
    }

    // ✅ 게시글 저장
    public void savePost(Board post) {
        boardRepository.save(post);
    }

    // ✅ 특정 게시글 조회
    public Board getPostById(Long id) {
        Optional<Board> post = boardRepository.findById(id);
        return post.orElse(null);
    }

    // ✅ 게시글 수정
    public void updatePost(Board post) {
        Board posts = getPostById(post.getId());
        posts.setTitle(post.getTitle());
        posts.setContent(post.getContent());
        posts.setUpdatedAt(LocalDateTime.now());
        boardRepository.save(posts);
    }

    // ✅ 게시글 삭제
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    // ✅
    public Page<Board> getPostsPaged(int page) {
        Pageable pageable = PageRequest.of(page, 10); // 10개씩 페이징
        return boardRepository.findAll(pageable);
    }
}
