package com.library.backend.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateResponse {

    Long id;

    String title;

    String issuedBy;

    LocalDate issuedDate;

    String certificateUrl;

    Boolean verified;
}
