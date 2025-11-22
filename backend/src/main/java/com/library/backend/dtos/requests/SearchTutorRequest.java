package com.library.backend.dtos.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchTutorRequest {

    String subject;
    
    String location;
    
    Double minRate;
    
    Double maxRate;
    
    Double minRating;
    
    String specialization;
    
    String verificationStatus;
}
