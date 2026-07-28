package com.example.elib.test.controller.service.impl.helper;

import com.example.elib.common.service.ReaderNumberGenerator;
import com.example.elib.test.controller.service.Generator;
import com.example.elib.user.entity.User;
import com.example.elib.user.vo.Contact;
import com.example.elib.user.vo.PersonalData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserGenerator implements Generator<com.example.elib.user.entity.User> {

    private final NameGenerator nameGenerator;
    private final EmailGenerator emailGenerator;
    private final PhoneGenerator phoneGenerator;
    private final Random random = new Random();

    private final ReaderNumberGenerator readerNumberGenerator;

    @Override
    public com.example.elib.user.entity.User Generate() {
        UUID id = UUID.randomUUID();
        String name = nameGenerator.Generate();
        String email = emailGenerator.Generate();
        String phone = phoneGenerator.Generate();

        // Создаем пользователя через фабричный метод
        Contact contact = Contact.of(email, phone);
        String readerBookNumber = readerNumberGenerator.generate();

        User user = User.register(contact, id, readerBookNumber);

        // Активируем пользователя (заполняем PersonalData)
        PersonalData personalData = getPersonalData(name);
        user.setPersonalData(personalData);
        user.activate();

        return user;
    }

    private PersonalData getPersonalData(String name) {
        String[] parts = name.split(" ");
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : "Иванов";
        String patronymic = parts.length > 2 ? parts[2] : "Иванович";

        // Дата рождения (от 18 до 80 лет)
        int currentYear = Year.now().getValue();
        int birthYear = currentYear - (18 + random.nextInt(63));
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28);
        LocalDate birthDate = LocalDate.of(birthYear, month, day);

        return PersonalData.of(firstName, lastName, patronymic, birthDate);
    }
}