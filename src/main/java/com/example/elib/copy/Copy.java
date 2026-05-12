package com.example.elib.copy;

import com.example.elib.book.entity.Book;
import com.example.elib.common.entity.BaseEntity;
import com.example.elib.holder.entity.Holder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "copies")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Copy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "holder_id")
    private Holder holder;

    @Column(name = "inventory_number", nullable = false, unique = true, length = 50)
    private String inventoryNumber;

    @Column(name = "isbn", length = 20)
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "status", nullable = false, length = 30)
    private String status;


}