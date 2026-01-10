package org.example.mapper;

import org.example.dto.CreateUserDTO;
import org.example.dto.UpdateUserDTO;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    User toEntity(CreateUserDTO createUserDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "newUsername")
    @Mapping(target = "role", source = "newRole")

    @Mapping(target = "password", source = "newPassword")
    User toEntity(UpdateUserDTO updateUserDTO);
}
