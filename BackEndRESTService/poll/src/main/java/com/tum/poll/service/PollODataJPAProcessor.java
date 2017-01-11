package com.tum.poll.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAProcessor;
import org.apache.olingo.odata2.jpa.processor.core.ODataJPAProcessorDefault;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.tum.poll.model.Pollutiondata;


public class PollODataJPAProcessor extends ODataJPAProcessorDefault{

	Random rn;
	GeoApiContext context;
	
	public PollODataJPAProcessor(final ODataJPAContext oDataJPAContext) {
		super(oDataJPAContext);
		rn = new Random();
		context = new GeoApiContext().setApiKey("AIzaSyCL-8DTsu2wRhly1LU_GDiLglW3tmkXfdg");
	
		
	}
	
	private void postProcess() {}

	private void preprocess() {}

	@Override
	public ODataResponse readEntitySet(final GetEntitySetUriInfo uriParserResultView,
			final String contentType) throws ODataException {

		oDataJPAContext.setPageSize(100);
		
		preprocess();

		final List<Object> jpaEntities = jpaProcessor.process(uriParserResultView);

		
		postProcess();

		final ODataResponse oDataResponse = responseBuilder.build(uriParserResultView, jpaEntities,
				contentType);

		return oDataResponse;
	}
	
	
	  @Override
	  public ODataResponse createEntity(final PostUriInfo uriParserResultView, final InputStream content,
	      final String requestContentType, final String contentType) throws ODataException {
	    ODataResponse oDataResponse = null;
	    InputStream localcontent = content;
	      oDataJPAContext.setODataContext(getContext());
	      System.out.println("Current  page size is "+ oDataJPAContext.getPageSize());
	      //create JsonReader object
	      JsonReader jsonReader = null;
	      try {
			jsonReader = new JsonReader(new InputStreamReader((InputStream) localcontent, "ISO-8859-1"));
	      } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	      }
	      JsonParser parser = new JsonParser();
	      com.google.gson.JsonObject jsonObj = (com.google.gson.JsonObject) parser.parse(jsonReader);
	      
	      Double lat =jsonObj.get("Latitude").getAsDouble();
	      Double lng = jsonObj.get("Longitude").getAsDouble();
	      LatLng l = new LatLng(lat,lng);
	      try {
				GeocodingResult[] results = GeocodingApi.reverseGeocode(context, l).await();
				System.out.println(results[0].formattedAddress);
				jsonObj.addProperty("Name", results[0].formattedAddress);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      Calendar mfDate = Calendar.getInstance();
	      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	      SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
	      String formattedDateAndTime = df.format(mfDate.getTime()) + "T" + tf.format(mfDate.getTime());
	      System.out.println(formattedDateAndTime);
	      //Add current date and time
	      jsonObj.addProperty("Datetime", formattedDateAndTime);
	      
	      if(!jsonObj.has("Id")){
	    	  System.out.println("Json does not have id property");
	    	  int i = rn.nextInt();
	    	  jsonObj.addProperty("Id", Integer.toString(i));
	    	  String newstr = jsonObj.toString();
	    	  System.out.println("Json string is  " + newstr);
	    	  localcontent = new ByteArrayInputStream(newstr.getBytes());
	    	  System.out.println("Creating new entity with random ID");
		      Object createdJpaEntity = jpaProcessor.process(uriParserResultView, localcontent, requestContentType);
		     	      
		      oDataResponse =
		          responseBuilder.build(uriParserResultView, createdJpaEntity, contentType);	   
		    return oDataResponse;
	      }
	      String requeststr = jsonObj.toString();
	      localcontent = new ByteArrayInputStream(requeststr.getBytes());
	      System.out.println("Creating new entity with ID from payload");
	      Object createdJpaEntity = jpaProcessor.process(uriParserResultView, localcontent, requestContentType);
	     	      
	      oDataResponse =
	          responseBuilder.build(uriParserResultView, createdJpaEntity, contentType);	   
	      return oDataResponse;
	      
	  }	
}