package com.example.elib.booking.mapper;

import com.example.elib.booking.dto.response.BookingDto;
import com.example.elib.booking.entity.Booking;
import com.example.elib.copy.mapper.CopyMapper;
import com.example.elib.user.mapper.UserMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UserMapper.class, CopyMapper.class})
public interface BookingMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "copy", target = "copy")
    BookingDto toDto(Booking booking);
}
