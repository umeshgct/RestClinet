package com.abnamro.T24.clinet.service;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

public interface ExcelReportService {

	ResponseEntity<InputStreamResource> downloadFile() throws IOException;

}