package com.abnamro.T24.clinet.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.abnamro.T24.clinet.service.ExcelReportService;

@RestController
public class ExcelReportControllerImpl implements ExcelReportController {

	@Autowired
	private ExcelReportService excelReportService;

	@Override
	public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
		return excelReportService.downloadFile();

	}
}