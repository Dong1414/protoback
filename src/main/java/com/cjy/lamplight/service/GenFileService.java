package com.cjy.lamplight.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.cjy.lamplight.dao.GenFileDao;
import com.cjy.lamplight.dto.GenFile;
import com.cjy.lamplight.dto.ResultData;
import com.cjy.lamplight.util.Util;
import com.google.common.base.Joiner;

@Service
public class GenFileService {

	// 파일이 저장될 폴더 경로를 가져오는 명령어
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;

	@Autowired
	private GenFileDao genFileDao;

	public ResultData saveMeta(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo,
			String originFileName, String fileExtTypeCode, String fileExtType2Code, String fileExt, int fileSize,
			String fileDir) {

		Map<String, Object> param = Util.mapOf("relTypeCode", relTypeCode, "relId", relId, "typeCode", typeCode,
				"type2Code", type2Code, "fileNo", fileNo, "originFileName", originFileName, "fileExtTypeCode",
				fileExtTypeCode, "fileExtType2Code", fileExtType2Code, "fileExt", fileExt, "fileSize", fileSize,
				"fileDir", fileDir);
		genFileDao.saveMeta(param);

		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", "성공하였습니다.", "id", id);
	}

	public ResultData save(MultipartFile multipartFile) {
		String fileInputName = multipartFile.getName();
		// 'file__article__0__common__attachment__1'를 "__" 기준으로 쪼갠다.
		// 0 1 2 3 4 5
		String[] fileInputNameBits = fileInputName.split("__");

		if (fileInputNameBits[0].equals("file") == false) {
			return new ResultData("F-1", "파라미터명이 올바르지 않습니다.");
		}

		// getSize() : 파일 사이즈를 가져오는 명령어
		int fileSize = (int) multipartFile.getSize();

		// 파일 사이즈가 0이거나 0보다 작으면
		if (fileSize <= 0) {
			return new ResultData("F-2", "파일이 업로드 되지 않았습니다.");
		}

		String relTypeCode = fileInputNameBits[1];
		int relId = Integer.parseInt(fileInputNameBits[2]);
		String typeCode = fileInputNameBits[3];
		String type2Code = fileInputNameBits[4];
		int fileNo = Integer.parseInt(fileInputNameBits[5]);
		String originFileName = multipartFile.getOriginalFilename();
		String fileExtTypeCode = Util.getFileExtTypeCodeFromFileName(multipartFile.getOriginalFilename());
		String fileExtType2Code = Util.getFileExtType2CodeFromFileName(multipartFile.getOriginalFilename());
		String fileExt = Util.getFileExtFromFileName(multipartFile.getOriginalFilename()).toLowerCase();

		if (fileExt.equals("jpeg")) {
			fileExt = "jpg";
		} else if (fileExt.equals("htm")) {
			fileExt = "html";
		}

		String fileDir = Util.getNowYearMonthDateStr();

		if (relId > 0) {
			GenFile oldGenFile = getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);

			if (oldGenFile != null) {
				deleteGenFile(oldGenFile);
			}
		}

		ResultData saveMetaRd = saveMeta(relTypeCode, relId, typeCode, type2Code, fileNo, originFileName,
				fileExtTypeCode, fileExtType2Code, fileExt, fileSize, fileDir);
		int newGenFileId = (int) saveMetaRd.getBody().get("id");

		// 새 파일이 저장될 폴더(io파일) 객체 생성
		String targetDirPath = genFileDirPath + "/" + relTypeCode + "/" + fileDir;
		java.io.File targetDir = new java.io.File(targetDirPath);

		// 새 파일이 저장될 폴더가 존재하지 않는다면 생성
		if (targetDir.exists() == false) {
			targetDir.mkdirs();
		}

		String targetFileName = newGenFileId + "." + fileExt;
		String targetFilePath = targetDirPath + "/" + targetFileName;

		// 파일 생성(업로드된 파일을 지정된 경로로 옮김)
		try {
			multipartFile.transferTo(new File(targetFilePath));
		} catch (IllegalStateException | IOException e) {
			return new ResultData("F-3", "파일저장에 실패하였습니다.");
		}

		return new ResultData("S-1", "파일이 생성되었습니다.", "id", newGenFileId, "fileRealPath", targetFilePath, "fileName",
				targetFileName, "fileInputName", fileInputName);
	}

	public GenFile getGenFile(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo) {
		return genFileDao.getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);
	}

	public ResultData saveFiles(Map<String, Object> param, MultipartRequest multipartRequest) {
		// 업로드 시작
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		Map<String, ResultData> filesResultData = new HashMap<>();
		List<Integer> genFileIds = new ArrayList<>();

		// fileMap.keySet() : file__article__0__common__attachment__1
		for (String fileInputName : fileMap.keySet()) {
			// fileInputName : file__article__0__common__attachment__1
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				ResultData fileResultData = save(multipartFile);
				int genFileId = (int) fileResultData.getBody().get("id");
				genFileIds.add(genFileId);

				filesResultData.put(fileInputName, fileResultData);
			}
		}

		// genFileIds 리스트를 ","를 기준으로 문장형으로 조인(결합)시켜주는 구아바
		// ex) [1,2,3,4,5] => 1,2,3,4,5
		String genFileIdsStr = Joiner.on(",").join(genFileIds);

		/* 삭제 시작 */
		int deleteCount = 0;

		for (String inputName : param.keySet()) {
			String[] inputNameBits = inputName.split("__");

			if (inputNameBits[0].equals("deleteFile")) {
				String relTypeCode = inputNameBits[1];
				int relId = Integer.parseInt(inputNameBits[2]);
				String typeCode = inputNameBits[3];
				String type2Code = inputNameBits[4];
				int fileNo = Integer.parseInt(inputNameBits[5]);

				GenFile oldGenFile = getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);

				if (oldGenFile != null) {
					deleteGenFile(oldGenFile);
					deleteCount++;
				}
			}
		}

		return new ResultData("S-1", "파일을 업로드하였습니다.", "filesResultData", filesResultData, "genFileIdsStr",
				genFileIdsStr, "deleteCount", deleteCount);
	}

	public void changeRelId(int id, int relId) {
		genFileDao.changeRelId(id, relId);
	}

	public void deleteGenFiles(String relTypeCode, int relId) {
		// 게시물에 달려있는 genFile 리스트 불러오기
		List<GenFile> genFiles = genFileDao.getGenFilesByRelTypeCodeAndRelId(relTypeCode, relId);

		for (GenFile genFile : genFiles) {
			deleteGenFile(genFile);
		}
	}

	private void deleteGenFile(GenFile genFile) {
		// 1. genFile의 저장소 경로를 찾고 저장소에서 삭제(유틸 활용)
		String filePath = genFile.getFilePath(genFileDirPath);
		Util.delteFile(filePath);

		// 2. genFile정보를 DB상에서 삭제
		genFileDao.deleteFile(genFile.getId());
	}

	public List<GenFile> getGenFiles(String relTypeCode, int relId, String typeCode, String type2Code) {
		return genFileDao.getGenFiles(relTypeCode, relId, typeCode, type2Code);
	}

	public GenFile getGenFile(int id) {
		return genFileDao.getGenFileById(id);
	}

	public Map<Integer, Map<String, GenFile>> getFilesMapKeyRelIdAndFileNo(String relTypeCode, List<Integer> relIds,
			String typeCode, String type2Code) {
		
		List<GenFile> genFiles = genFileDao.getGenFilesRelTypeCodeAndRelIdsAndTypeCodeAndType2Code(relTypeCode, relIds,
				typeCode, type2Code);
		
		//Map<String, GenFile> map = new HashMap<>();
		Map<Integer, Map<String, GenFile>> rs = new LinkedHashMap<>();

		for (GenFile genFile : genFiles) {
			if (rs.containsKey(genFile.getRelId()) == false) {
				rs.put(genFile.getRelId(), new LinkedHashMap<>());
			}

			rs.get(genFile.getRelId()).put(genFile.getFileNo() + "", genFile);
		}

		return rs;
	}

	public void changeInputFileRelIds(Map<String, Object> param, int id) {
		String genFileIdsStr = Util.ifEmpty((String)param.get("genFileIdsStr"), null);

		if ( genFileIdsStr != null ) {
			List<Integer> genFileIds = Util.getListDividedBy(genFileIdsStr, ",");

			// 파일이 먼저 생성된 후에, 관련 데이터가 생성되는 경우에는, file의 relId가 일단 0으로 저장된다.
			// 순서: 파일업로드 => 글저장
			// 즉, ajax로 파일업로드는 하였지만, 파일업로드가 글작성보다 먼저 진행됐기 때문에 업로드된 파일들에는 relId가 일단 0으로 저장된 상태이다.
			// 글저장은 아직 진행이 안되었기 때문에 신규 글의 ID를 지금부터 가져와서 파일의 relId로 업데이트해주어야 한다
			// 따라서, 이것을 뒤늦게라도 다음 로직을 통해 고처야 한다.
			for (int genFileId : genFileIds) {
				changeRelId(genFileId, id);
			}
		}
		
	}

}
