package com.example.clny.mapper;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.model.Credentials;
import org.mapstruct.Mapper;

@Mapper
public interface CredentialsMapper {

    CredentialsDTO credentialsToCredentialsDTO(Credentials credentials);

    Credentials credentialsDTOToCredentials(CredentialsDTO credentialsDTO);

}
