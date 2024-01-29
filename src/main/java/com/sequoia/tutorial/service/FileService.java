package com.sequoia.tutorial.service;

import com.sequoia.tutorial.models.ResponseData;
import com.sequoia.tutorial.models.TutorialModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface FileService {
    byte[] getTutorialsAsExcel(List<TutorialModel> tutorials);

    byte[] getTutorialsAsCsv(List<TutorialModel> tutorialModels);

    ResponseData importExcelFile(MultipartFile file);
}
