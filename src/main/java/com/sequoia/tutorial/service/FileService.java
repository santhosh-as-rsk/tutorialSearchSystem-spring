package com.sequoia.tutorial.service;

import com.sequoia.tutorial.models.TutorialModel;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


public interface FileService {
    void getExcelSheet(HttpServletResponse response) throws IOException;

    byte[] getAllTutorialsAsExcel();

    byte[] getTutorialsAsExcel(List<TutorialModel> tutorials);

    byte[] getTutorialsAsCsv(List<TutorialModel> tutorialModels);
}