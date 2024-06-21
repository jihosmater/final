package com.team.demo.service;

import com.team.demo.domain.dto.UserDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface FileService {
    ResponseEntity<Resource> getThumbnailResource(String systemname) throws Exception;
    ResponseEntity<Resource> downloadFile(String systemname,String orgname) throws Exception;

    UserDTO getUserByUserid(String loginUser);
}
