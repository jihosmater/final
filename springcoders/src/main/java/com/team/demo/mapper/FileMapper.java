package com.team.demo.mapper;

import com.team.demo.domain.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
	//C
	int insertFile(FileDTO file);
	//R
	FileDTO getFileBySystemname(String systemname);
	List<FileDTO> getFiles(String fileid);
	FileDTO getFileByFileid(String fileid);
	//D
	int deleteFileBySystemname(String systemname);
	int deleteFilesByFileid(String fileid);
	
	boolean userDeleteFile(String userfile);

}
