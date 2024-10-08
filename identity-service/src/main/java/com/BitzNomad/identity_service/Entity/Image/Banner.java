package com.BitzNomad.identity_service.Entity.Image;

import com.BitzNomad.identity_service.Entity.BaseImage;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Banner extends BaseImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
}
