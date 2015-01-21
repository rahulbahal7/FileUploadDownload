package hello;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public @ResponseBody String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	//Get the name of the file here and store in variable "name"
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public @ResponseBody String handleFileUpload(@RequestParam("name") String name, 
			@RequestParam("file") MultipartFile file){
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				
				//Create a directory if it does'nt exists
				File f = new File("\\uploads");
				if(!f.exists()){
					if(f.mkdir())
						System.out.println("directory created for upload files");
					System.out.println("error in creating the file directory.");
				}

				//Upload the file
				BufferedOutputStream stream = 
						new BufferedOutputStream(new FileOutputStream(new File("\\uploads\\"+name)));
				stream.write(bytes);
				stream.close();

				return "You successfully uploaded the file" + name;
			} 
			catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		} 
		else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}

}
