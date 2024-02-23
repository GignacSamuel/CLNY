package com.example.clny.mapper;

import com.example.clny.dto.ProfileDTO;
import com.example.clny.model.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDTO profileToProfileDTO(Profile profile);

    Profile profileDTOToProfile(ProfileDTO profileDTO);

}
