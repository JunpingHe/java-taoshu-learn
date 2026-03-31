package org.example.dto.response;

import lombok.Data;
import org.example.entity.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品响应DTO
 */
@Data
public class BookResponse {
    
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    private String coverImage;
    private String description;
    private Integer status;
    private Long sellerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static BookResponse fromEntity(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setPublisher(book.getPublisher());
        response.setIsbn(book.getIsbn());
        response.setPrice(book.getPrice());
        response.setStock(book.getStock());
        response.setSales(book.getSales());
        response.setCoverImage(book.getCoverImage());
        response.setDescription(book.getDescription());
        response.setStatus(book.getStatus());
        response.setSellerId(book.getSellerId());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        return response;
    }
}
