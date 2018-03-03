package com.example.demo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Controller
@Component
public class profileController {

	@Value("#{environment.AWS_ACCESS_KEY_ID}")
	String accessKey; 

	
	@Value("#{environment.AWS_SECRET_KEY}")
	String secretKey;
	
	
	@GetMapping(value="/")
	public ModelAndView renderPage()
	{
		ModelAndView objModelAndView = new ModelAndView();
		
		objModelAndView.setViewName("index");
		return objModelAndView;
		
	}
	
	 
	
	
	@PostMapping(value="/upload")
	public ModelAndView uploadFileToS3(@RequestParam("file") MultipartFile img) throws IOException {
		
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
		
		AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();
		
		
		PutObjectRequest request = new PutObjectRequest("khushboo1234", img.getOriginalFilename(), img.getInputStream(), new ObjectMetadata())
				.withCannedAcl(CannedAccessControlList.PublicRead);
		
		amazonS3.putObject(request);
		
		String imgSrcFromController = "http://"+"khushboo1234"+".s3.amazonaws.com/"+img.getOriginalFilename();
		
		
		ModelAndView objModelAndView = new ModelAndView();
		
		objModelAndView.addObject("imgSrc",imgSrcFromController);
		
		objModelAndView.setViewName("profilePage");
		return objModelAndView;
		
	}
}
