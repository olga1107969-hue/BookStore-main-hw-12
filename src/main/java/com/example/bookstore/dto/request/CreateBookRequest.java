package com.example.bookstore.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {
    @NotBlank(message = "Название обязательно")
    @Size(min = 2, max = 200)
    private String title;

    @NotBlank(message = "Автор обязателен")
    @Size(min = 2, max = 100)
    private String author;

    @NotBlank(message = "ISBN обязателен")
    private String isbn;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть положительной")
    private BigDecimal price;

    @NotNull(message = "Количество обязательно")
    @Min(value = 0, message = "Количество не может быть отрицательным")
    private Integer stock;
}
