package com.BitzNomad.identity_service.Service.CloudiaryService;


import com.BitzNomad.identity_service.DtoReponese.ImageDTOReponese;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface ImageOfProductService {
    Set<ImageDTOReponese> saveImageOfProduct(MultipartFile[] imageOfRestaurant, Long ProductID, String typeofImage) throws Exception;
}
