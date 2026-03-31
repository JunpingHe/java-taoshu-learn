package org.example.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品创建请求DTO
 */
@Data
public class BookCreateRequest {
    
    @NotBlank(message = "书名不能为空")
    @Size(max = 100, message = "书名最长100位")
    private String title;
    
    @NotBlank(message = "作者不能为空")
    @Size(max = 50, message = "作者名最长50位")
    private String author;
    
    @Size(max = 100, message = "出版社名最长100位")
    private String publisher;
    
    @Size(max = 20, message = "ISBN最长20位")
    private String isbn;
    
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;
    
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock = 0;
    
    @Size(max = 255, message = "封面图片URL最长255位")
    private String coverImage;
    
    private String description;
}
