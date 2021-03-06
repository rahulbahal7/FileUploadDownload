package hello;

import java.io.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileDownloadController {

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public @ResponseBody String handleFileDownload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("dname") String file) throws Exception {

		File folder = new File("C:\\uploads\\");
		File[] listOfFiles = folder.listFiles();


		//Get a list of all files (and directories) in the main directory

		boolean filePresent = false;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile())
			{
				if(listOfFiles[i].getName().equalsIgnoreCase(file));
				{
					filePresent=true;
					break;
				}	
			}
		}

		// Section to Download File
		final int BUFFER_SIZE = 2048;

		if(filePresent && !file.isEmpty()){
			try
			{
				//Path of the file
				String filePath = new String("C:\\uploads\\"+file);

				ServletContext context = request.getServletContext();

				File downloadFile = new File(filePath);
				FileInputStream inputStream = new FileInputStream(downloadFile);

				//retrieve MIME type of the file
				String mimeType = context.getMimeType(filePath);
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/octet-stream";
				}
				System.out.println("MIME type: " + mimeType);

				//set content attributes for the response
				response.setContentType(mimeType);
				response.setContentLength((int) downloadFile.length());

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"",
						downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// get output stream of the response
				OutputStream outStream = response.getOutputStream();

				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				// write bytes read from the input stream into the output stream
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
				inputStream.close();
				outStream.close();
				return "File Downloaded Successfully";

			}catch (Exception e) {
				return "File does not exist. Please check the filename (and extension)";
			}
		}
		else return "File does not exist. Please check the filename (and extension)!";
	}
}