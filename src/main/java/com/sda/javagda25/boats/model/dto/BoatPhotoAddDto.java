package com.sda.javagda25.boats.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoatPhotoAddDto {
    private Long boatId;
    private MultipartFile photo;
}
