package com.team.demo.controller;

import com.team.demo.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/file/*")
public class FileController {
    @Autowired
    private FileService service;

    @GetMapping("thumbnail")
    public ResponseEntity<Resource> thumbnail(String systemname) throws Exception {
        return service.getThumbnailResource(systemname);
    }

    @GetMapping("download")
    public ResponseEntity<Resource> download(String systemname, String orgname) throws Exception {
        return service.downloadFile(systemname, orgname);
    }

    @GetMapping("loginprofile")
    public ResponseEntity<Resource> loginprofile(HttpServletRequest req) throws Exception {
        Object temp = req.getSession().getAttribute("loginUser"); // 속성 이름 통일
        String loginUser = (temp!=null)?(String)temp:"";
        if(loginUser!=""){
            String userprofile = service.getUserByUserid(loginUser).getUserprofile();
            userprofile = (userprofile.equals("planz")?"planz.webp":userprofile);
            return service.getThumbnailResource(userprofile);
        }else{
            return service.getThumbnailResource("planz.webp");
        }
    }
}
