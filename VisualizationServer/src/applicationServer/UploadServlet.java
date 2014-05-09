package applicationServer;
// Import required java libraries
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import parsers.IParse;
import parsers.planParser.PlanViewParser;
import parsers.temporalParser.TemporalViewParser;




@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {

	private boolean isMultipart;
	private String filePath;
	private int maxFileSize = 50 * 1024;
	private int maxMemSize = 4 * 1024;
	private File file ;
	private IParse parser;

	public void init( ){
		// Get the file location where it would be stored.
		filePath = 
				getServletContext().getInitParameter("file-upload"); 
	}
	public void doPost(HttpServletRequest request, 
			HttpServletResponse response)
					throws ServletException, java.io.IOException {
		// Check that we have a file upload request
		request.getInputStream();
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");

		java.io.PrintWriter out = response.getWriter( );

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Location to save data that is larger than maxMemSize.
		// factory.setRepository(new File("e:/data"));
	

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		try{ 
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);
			// Process the uploaded file items
			Iterator i = fileItems.iterator();
			while ( i.hasNext () ) 
			{
				FileItem fi = (FileItem)i.next();
				if ( !fi.isFormField () )	
				{
					InputStream dataInputStream = fi.getInputStream();

					StringWriter writer = new StringWriter();
					IOUtils.copy(dataInputStream, writer);
					String dataAsString = writer.toString();        	
					String fileName = fi.getName();
					String extension = fileName.substring(fileName.lastIndexOf('.'));
					if (extension.equals(".xml")){
						response.setHeader("fileExt", "xml");        		
						parser = new PlanViewParser();
						JSONObject jsonAns = parser.parse(dataAsString);
						out.println(jsonAns.toString());
					}
					else if(extension.equals(".log")){
						response.setHeader("fileExt", "log");        		
						parser = new TemporalViewParser();
						JSONObject jsonAns = parser.parse(dataAsString);
						out.println(jsonAns.toString());

					} else { // unknown file extension
						response.setStatus(400);
						response.setHeader("invalidFile", "Invalid file type");
						JSONObject jsonAns = new JSONObject();
						out.println(jsonAns.toString());						
					}


				}
			}
		}catch(Exception ex) {
			response.setStatus(400);
			response.setHeader("invalidFile", "Corrupted file");
			JSONObject jsonAns = new JSONObject();
			out.println(jsonAns.toString());		
		}
	}
	public void doGet(HttpServletRequest request, 
			HttpServletResponse response)
					throws ServletException, java.io.IOException {

		throw new ServletException("GET method used with " +
				getClass( ).getName( )+": POST method required.");
	} 
}