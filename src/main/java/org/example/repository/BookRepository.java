package org.example.repository;

import org.example.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品Repository
 * 对比Django: Book.objects
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    /**
     * 根据卖家ID查询商品
     * 对比Django: Book.objects.filter(seller_id=sellerId)
     */
    List<Book> findBySellerId(Long sellerId);
    
    /**
     * 根据状态查询商品(分页)
     * 对比Django: Book.objects.filter(status=status).order_by('-created_at')
     */
    Page<Book> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据标题模糊查询
     * 对比Django: Book.objects.filter(title__contains=keyword)
     */
    Page<Book> findByTitleContaining(String keyword, Pageable pageable);
    
    /**
     * 根据作者查询
     */
    List<Book> findByAuthor(String author);
}
