package com.example.demo;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.lambda.model.Environment;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Controller
public class profileController  {
	
	String s;
	@Value("#{environment.accesskey}")
	String accesskey;
	@Value("#{environment.secretkey}")
	String secretkey;
    @GetMapping(value="/")
     public ModelAndView renderpage()
     {
        ModelAndView indexPage= new ModelAndView();
        indexPage.setViewName("index");
    	return  indexPage;
     }
     
    @PostMapping(value="/upload")
    public ModelAndView uploads3(@RequestParam("file") MultipartFile image)
    {  
    	  
       
       ModelAndView indexPage= new ModelAndView();
       BasicAWSCredentials cred= new BasicAWSCredentials(accesskey,secretkey);
      // AmazonS3Client client=AmazonS3ClientBuilder.standard().withCredentials(new AWSCredentialsProvider(cred)).with
       AmazonS3 client=AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(cred)).withRegion(Regions.US_EAST_1).build();
       try {
		PutObjectRequest put= new PutObjectRequest("firstravi",image.getOriginalFilename(),image.getInputStream(), new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead);
		client.putObject(put);

        String imgSrc = "http://"+"firstravi"+".s3.amazonaws.com/"+image.getOriginalFilename();
        indexPage.addObject("imgSrc",imgSrc);
        indexPage.setViewName("profile");
        return indexPage;
       } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       
      indexPage.setViewName("error");
      return indexPage;
       
      
    }
    
}
