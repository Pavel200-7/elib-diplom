package com.example.elib.copy.entity;

import com.example.elib.book.entity.Book;
import com.example.elib.common.entity.BaseEntity;
import com.example.elib.copy.enums.CopyStatus;
import com.example.elib.holder.entity.Holder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "copies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Copy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holder_id")
    private Holder holder;

    @Column(name = "inventory_number", nullable = false, unique = true, length = 50)
    private String inventoryNumber;

    @Column(name = "isbn", length = 20)
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private CopyStatus status;

    private Copy(String inventoryNumber, String isbn, Book book) {
        setInventoryNumber(inventoryNumber);
        setIsbn(isbn);
        this.book = book;
        this.status = CopyStatus.ADDED;
    }

    public static Copy addCopy(String inventoryNumber, String isbn, Book book) {
        return new Copy(inventoryNumber, isbn, book);
    }

    public void updateCopy(String inventoryNumber, String isbn) {
        setInventoryNumber(inventoryNumber);
        setIsbn(isbn);
    }

    public boolean isInStatus(CopyStatus status) {
        return this.status == status;
    }

    private void setInventoryNumber(String inventoryNumber) {
        validateInventoryNumber(inventoryNumber);
        this.inventoryNumber = inventoryNumber;
    }

    private void setIsbn(String isbn) {
        validateIsbn(isbn);
        this.isbn = isbn;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }

    public void setStatus(CopyStatus status) {
        this.status = status;
    }


    private void validateInventoryNumber(String inventoryNumber) {
        if (inventoryNumber == null || inventoryNumber.isBlank()) {
            throw new IllegalArgumentException("Инвентарный номер не может быть пустым.");
        }
        if (inventoryNumber.length() > 50) {
            throw new IllegalArgumentException("Инвентарный номер не может превышать 50 символов.");
        }
        if (!inventoryNumber.matches("^[A-Za-z0-9\\-\\/]+$")) {
            throw new IllegalArgumentException(
                    "Инвентарный номер может содержать только буквы (латиница), цифры, дефис и слеш."
            );
        }
    }

    private void validateIsbn(String isbn) {
        if (isbn != null && !isbn.isBlank()) {
            if (isbn.length() > 20) {
                throw new IllegalArgumentException("ISBN не может превышать 20 символов.");
            }
            String cleanIsbn = isbn.replaceAll("[\\s\\-]", "");
            if (!cleanIsbn.matches("^(97[89])?\\d{9}[\\dX]$")) {
                throw new IllegalArgumentException(
                        "Неверный формат ISBN. Допустимые форматы: ISBN-10 (XXXXXXXXXX) или ISBN-13 (XXXXXXXXXXXXX)"
                );
            }
        }
    }

}