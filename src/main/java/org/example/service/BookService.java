package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.dto.request.BookCreateRequest;
import org.example.dto.request.BookUpdateRequest;
import org.example.dto.response.BookResponse;
import org.example.entity.Book;
import org.example.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品Service
 */
@Service
@RequiredArgsConstructor
public class BookService {
    
    private final BookRepository bookRepository;
    
    /**
     * 创建商品
     */
    @Transactional(rollbackFor = Exception.class)
    public BookResponse createBook(BookCreateRequest request, Long sellerId) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());
        book.setCoverImage(request.getCoverImage());
        book.setDescription(request.getDescription());
        book.setSellerId(sellerId);
        book.setStatus(1);
        book.setSales(0);
        
        book = bookRepository.save(book);
        return BookResponse.fromEntity(book);
    }
    
    /**
     * 更新商品
     */
    @Transactional(rollbackFor = Exception.class)
    public BookResponse updateBook(Long bookId, BookUpdateRequest request, Long sellerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException("商品不存在"));
        
        // 检查权限: 只能修改自己的商品
        if (!book.getSellerId().equals(sellerId)) {
            throw new BusinessException("无权修改此商品");
        }
        
        // 更新字段
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getPublisher() != null) {
            book.setPublisher(request.getPublisher());
        }
        if (request.getIsbn() != null) {
            book.setIsbn(request.getIsbn());
        }
        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            book.setStock(request.getStock());
        }
        if (request.getCoverImage() != null) {
            book.setCoverImage(request.getCoverImage());
        }
        if (request.getDescription() != null) {
            book.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            book.setStatus(request.getStatus());
        }
        
        book = bookRepository.save(book);
        return BookResponse.fromEntity(book);
    }
    
    /**
     * 删除商品
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(Long bookId, Long sellerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException("商品不存在"));
        
        // 检查权限
        if (!book.getSellerId().equals(sellerId)) {
            throw new BusinessException("无权删除此商品");
        }
        
        bookRepository.delete(book);
    }
    
    /**
     * 查询商品详情
     */
    public BookResponse getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException("商品不存在"));
        return BookResponse.fromEntity(book);
    }
    
    /**
     * 分页查询商品列表
     * 对比Django: Paginator
     */
    public Page<BookResponse> getBookList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> bookPage = bookRepository.findByStatus(1, pageable);
        return bookPage.map(BookResponse::fromEntity);
    }
    
    /**
     * 搜索商品
     */
    public Page<BookResponse> searchBooks(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> bookPage = bookRepository.findByTitleContaining(keyword, pageable);
        return bookPage.map(BookResponse::fromEntity);
    }
    
    /**
     * 查询卖家的商品列表
     */
    public List<BookResponse> getBooksBySeller(Long sellerId) {
        List<Book> books = bookRepository.findBySellerId(sellerId);
        return books.stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
