package com.BitzNomad.identity_service.DtoRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequestDTO {
    Long id;
    String name;
    String description;
    Long FoodStoreID;
    Long CatagoryID;
}
