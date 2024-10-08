package com.BitzNomad.identity_service.Service.AuthService.AuthImpl;

import com.BitzNomad.identity_service.DtoReponese.PermissionReponese;
import com.BitzNomad.identity_service.DtoRequest.PermissionRequest;
import com.BitzNomad.identity_service.Exception.AppException;
import com.BitzNomad.identity_service.Exception.ErrorCode;
import com.BitzNomad.identity_service.Mapper.Auth.PermissionMapper;
import com.BitzNomad.identity_service.Service.AuthService.PermissionService;
import com.BitzNomad.identity_service.Entity.Auth.Permission;
import com.BitzNomad.identity_service.Respository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class PermissionImpl implements PermissionService {
    @Autowired
    PermissionRepository permissionRepository;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PermissionReponese create(PermissionRequest request) {
        Permission permission = PermissionMapper.ConvertToEntity(request);
        permissionRepository.save(permission);
        return PermissionMapper.convertToReponese(permission);
    }


    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(String permissionid) {
        permissionRepository.findById(permissionid)
                .ifPresentOrElse(
                        p -> {
                            p.setDeleted(true);
                            permissionRepository.save(p);
                        },
                        () -> {
                            throw new AppException(ErrorCode.USER_NOT_EXISTED);
                        }
                );
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PermissionReponese> findAll() {
        return permissionRepository.findAll().stream()
                .map(PermissionMapper::convertToReponese).collect(Collectors.toList());
    }
}
