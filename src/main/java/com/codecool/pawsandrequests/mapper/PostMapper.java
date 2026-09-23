package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.PostRequest;
import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = Picture.class)
public interface PostMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "age", source = "animal.age")
    @Mapping(target = "gender", source = "animal.gender")
    @Mapping(target = "species", source = "animal.species")
    @Mapping(target = "picture", expression = "java(post.getPictures()"
            + ".stream().map(Picture::getUrl).findFirst().get())")
    PostSummaryResponse toPostSummaryResponse(Post post);
    @Mapping(target = "shelterName", source = "user.shelter.shelterName")
    @Mapping(target = "address", source = "user.shelter.address")
    @Mapping(target = "age", source = "animal.age")
    @Mapping(target = "gender", source = "animal.gender")
    @Mapping(target = "species", source = "animal.species")
    @Mapping(target = "animalName", source = "animal.name")
    @Mapping(target = "url", expression = "java(post.getPictures().stream()."
            + "map(picture -> \"/pictures/\" + picture.getId()).toList())")
    PostResponse toPostResponse(Post post);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "pictures", ignore = true)
    Post toPost(PostRequest postRequest, User user, Animal animal);

}
