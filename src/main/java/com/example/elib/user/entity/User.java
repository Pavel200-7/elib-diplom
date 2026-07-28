package com.example.elib.user.entity;

import com.example.elib.common.entity.base.BaseEntity;
import com.example.elib.user.enums.UserStatus;
import com.example.elib.user.vo.Contact;
import com.example.elib.user.vo.PersonalData;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(name = "reader_book_number", nullable = false, length = 7)
    private String readerBookNumber;

    @Embedded
    @Setter(AccessLevel.NONE)
    private Contact contact;

    @Embedded
    private PersonalData personalData;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    private User(Contact contact, UUID id, String readerBookNumber) {
        this.id = id;
        this.contact = contact;
        this.status = UserStatus.CREATED;
        this.readerBookNumber = readerBookNumber;
    }

    public static User register(Contact contact, UUID id, String readerBookNumber) {
        return new User(contact, id, readerBookNumber);
    }

    public void activate() {
        if (status != UserStatus.CREATED && status != UserStatus.ACTIVATED) {
            throw new IllegalStateException("Нельзя изменить данные в статусе " + status);
        }

        if (this.personalData == null) {
            throw new IllegalStateException("Для активации аккаунта требуются персональные данные.");
        }

        this.status = UserStatus.ACTIVATED;
    }

    public String getEmail() {
        return contact != null ? contact.getEmail() : null;
    }

    public String getPhone() {
        return contact != null ? contact.getPhone() : null;
    }

    public String getFirstName() {
        return personalData != null ? personalData.getFirstName() : null;
    }

    public String getLastName() {
        return personalData != null ? personalData.getLastName() : null;
    }

    public String getPatronymic() {
        return personalData != null ? personalData.getPatronymic() : null;
    }

    public LocalDate getBirthDate() {
        return personalData != null ? personalData.getBirthDate() : null;
    }

}
