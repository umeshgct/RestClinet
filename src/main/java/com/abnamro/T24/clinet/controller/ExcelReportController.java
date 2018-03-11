package com.abnamro.T24.clinet.controller;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/download")
public interface ExcelReportController {		
		
		@RequestMapping(value = "/excel", produces= {"application/vnd.ms-excel"},method = RequestMethod.GET)		
		public @ResponseBody
		 ResponseEntity<InputStreamResource> downloadFile() throws IOException;
		
	}
