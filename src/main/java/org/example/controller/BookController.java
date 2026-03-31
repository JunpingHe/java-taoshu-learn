package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.request.BookCreateRequest;
import org.example.dto.request.BookUpdateRequest;
import org.example.dto.response.BookResponse;
import org.example.service.BookService;
import org.example.util.UserContext;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品Controller
 * 对比Django: Django的ViewSet或GenericAPIView
 */
@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    
    private final BookService bookService;
    
    /**
     * 创建商品
     * POST /api/book
     */
    @PostMapping
    public Result<BookResponse> createBook(
            @Valid @RequestBody BookCreateRequest request,
            HttpServletRequest httpRequest) {
        Long sellerId = UserContext.getUserId(httpRequest);
        BookResponse book = bookService.createBook(request, sellerId);
        return Result.success("商品创建成功", book);
    }
    
    /**
     * 更新商品
     * PUT /api/book/{id}
     */
    @PutMapping("/{id}")
    public Result<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request,
            HttpServletRequest httpRequest) {
        Long sellerId = UserContext.getUserId(httpRequest);
        BookResponse book = bookService.updateBook(id, request, sellerId);
        return Result.success("商品更新成功", book);
    }
    
    /**
     * 删除商品
     * DELETE /api/book/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteBook(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long sellerId = UserContext.getUserId(httpRequest);
        bookService.deleteBook(id, sellerId);
        return Result.success("商品删除成功");
    }
    
    /**
     * 查询商品详情
     * GET /api/book/{id}
     */
    @GetMapping("/{id}")
    public Result<BookResponse> getBook(@PathVariable Long id) {
        BookResponse book = bookService.getBookById(id);
        return Result.success(book);
    }
    
    /**
     * 分页查询商品列表
     * GET /api/book/list?page=0&size=10
     */
    @GetMapping("/list")
    public Result<Page<BookResponse>> getBookList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookResponse> books = bookService.getBookList(page, size);
        return Result.success(books);
    }
    
    /**
     * 搜索商品
     * GET /api/book/search?keyword=Java&page=0&size=10
     */
    @GetMapping("/search")
    public Result<Page<BookResponse>> searchBooks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookResponse> books = bookService.searchBooks(keyword, page, size);
        return Result.success(books);
    }
    
    /**
     * 查询卖家的商品列表
     * GET /api/book/my
     */
    @GetMapping("/my")
    public Result<List<BookResponse>> getMyBooks(HttpServletRequest request) {
        Long sellerId = UserContext.getUserId(request);
        List<BookResponse> books = bookService.getBooksBySeller(sellerId);
        return Result.success(books);
    }
}
