package com.library.backend.mappers;

import com.library.backend.dtos.responses.CertificateResponse;
import com.library.backend.dtos.responses.TutorProfileResponse;
import com.library.backend.dtos.responses.UserResponse;
import com.library.backend.entities.Certificate;
import com.library.backend.entities.Tutor;
import com.library.backend.entities.TutorProfile;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TutorMapper {

    @Mapping(target = "user", source = "tutor.tutor")
    @Mapping(target = "certificates", source = "tutor.tutor.certificates")
    TutorProfileResponse toTutorProfileResponse(TutorProfile tutor);

    UserResponse toUserResponse(Tutor tutor);

    CertificateResponse toCertificateResponse(Certificate certificate);

    List<CertificateResponse> toCertificateResponseList(List<Certificate> certificates);
}
