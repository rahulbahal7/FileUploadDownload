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
			@RequestParam("file1") MultipartFile file){
		if (!file.isEmpty()) {
			try {
				int filename_type=0;
				byte[] bytes = file.getBytes();
				String original_filename = file.getOriginalFilename();
				String extension = "";
				String return_msg ="";

				//Create a directory if it does'nt exists
				File f = new File("\\uploads");
				if(!f.exists()){
					if(f.mkdir())
						System.out.println("directory created for upload files");
					System.out.println("error in creating the file directory.");
				}

				//Upload the file with it's original name if Name not entered
				BufferedOutputStream stream =null;
				if(name.isEmpty()){
					stream = new BufferedOutputStream(new FileOutputStream(new File("\\uploads\\"+original_filename)));

					//Close "stream" - (BufferedOutputStream object), before returning to prevent resource leak					
					stream.write(bytes);
					stream.close();
					return "You successfully uploaded the file " + original_filename;
				}

				//Upload the file with the supplied file name

				else
				{
					//The user entered a filename with extension
					int i = name.lastIndexOf('.');
					if (i > 0) {
						filename_type=1;
						stream = new BufferedOutputStream(new FileOutputStream(new File("\\uploads\\"+name)));
					}

					//The user entered a filename without an extension, use the extension of original file
					else {
						filename_type=2;
						i = original_filename.lastIndexOf('.');
						extension = original_filename.substring(i+1);
						stream = new BufferedOutputStream(new FileOutputStream(new File("\\uploads\\"+ name + '.' + extension)));
					}
				}
				stream.write(bytes);
				stream.close();

				// Print the appropriate return message
				switch(filename_type){
				case 1: return_msg = "You successfully uploaded the file " + name;
				case 2: return_msg = "You successfully uploaded the file " + name + '.' + extension;
				}

				return return_msg;
			} 
			// Print the original name of the file with exception message
			catch (Exception e) {
				return "You failed to upload " + file.getName() + " => " + e.getMessage();
			}
		} 
		//Print the original name of the file if the file is empty
		else {
			return "You uploaded an empty file. Please make sure that there are contents present in your file!";
		}
	}
}