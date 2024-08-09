package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {
    @InjectMocks
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);;

    @Test
    void toDtoTest_ValidUser(){
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .lastName("TANNER")
                .firstName("Jean")
                .createdAt(now)
                .updatedAt(now)
                .admin(true)
                .password("test123")
                .email("test@gmail.com")
                .build();

        UserDto userDto = userMapper.toDto(user);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.isAdmin(), userDto.isAdmin());
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt());
    }

    @Test
    void toDtoTest_UserNull(){
        User user = null;
        UserDto userDto = userMapper.toDto(user);
        assertNull(userDto);
    }

    @Test
    void toEntityTest_ValidUser(){
        LocalDateTime now = LocalDateTime.now();
        UserDto userDto = new UserDto(1L,"test@gmail.com","TANNER","Jean",true,"test123",now,now);

        User user = userMapper.toEntity(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.isAdmin(), user.isAdmin());
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt());
    }

    @Test
    void toEntityTest_UserNull(){
        UserDto userDto = null;
        User user = userMapper.toEntity(userDto);
        assertNull(user);
    }
}
