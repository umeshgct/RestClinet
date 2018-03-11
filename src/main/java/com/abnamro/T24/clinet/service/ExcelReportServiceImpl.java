package com.abnamro.T24.clinet.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Umesh
 *
 */
@Service
public class ExcelReportServiceImpl implements ExcelReportService {

	private static final Logger logger = LoggerFactory.getLogger(ExcelReportServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate ;

	/**
	 * To search and get the files based on the given input keyword
	 */

	@Override
	public ResponseEntity<InputStreamResource> downloadFile() throws IOException {

		HttpHeaders header = new HttpHeaders();

		InputStream stream = null;

		//String jsonString = "{\"infile\": [{\"field1\": 11,\"field2\": 12,\"field3\": 13},{\"field1\": 21,\"field2\": 22,\"field3\": 23},{\"field1\": 31,\"field2\": 32,\"field3\": 33}]}";

		//JSONObject output;
		try {
			//output = new JSONObject(jsonString);

			//JSONArray docs = output.getJSONArray("infile");
			
			String jsonString = getJsonFromT24();
			
			JSONArray docs = new JSONArray(jsonString);
			

			ArrayList<Object> list = new ArrayList<Object>();

			if (docs != null) {
				for (int i = 0; i < docs.length(); i++) {
					list.add(docs.get(i));
				}
			}

			ExcelReporter excelReporter = new ExcelReporter();

			Workbook rpWorkbook = excelReporter.writeRecordsToExcel(list);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			rpWorkbook.write(bos);
			stream = new ByteArrayInputStream(bos.toByteArray());

			header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TableData.xls");

			logger.info("Execel generated");

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok().contentLength(stream.available()).headers(header)
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(new InputStreamResource(stream));

	}
	
	private String getJsonFromT24() {
		
		
        
        String consumeJSONString = restTemplate.getForObject("http://localhost:8081/get", String.class);
		
		return consumeJSONString;
	}

}
