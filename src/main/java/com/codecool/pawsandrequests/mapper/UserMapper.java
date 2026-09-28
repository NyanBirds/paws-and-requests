package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.UserResponse;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(CustomUserDetails user);
}
