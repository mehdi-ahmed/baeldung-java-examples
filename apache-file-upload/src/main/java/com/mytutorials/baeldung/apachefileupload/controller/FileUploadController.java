package com.mytutorials.baeldung.apachefileupload.controller;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

// TODO
// Multipart examples

@RestController
public class FileUploadController {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleUpload(HttpServletRequest servletRequest) throws IOException, FileUploadException {

        System.out.println(System.getProperty("java.io.tmpdir"));

        //  We need to check if the request contains a multipart content using the isMultipartContent
        boolean isMultiPart = ServletFileUpload.isMultipartContent(servletRequest);
        System.out.println("Is Multi Part = " + isMultiPart);

        // Create a factory for disk-based file items
        // setting the directory where our files are going to be saved
        // The threshold in which the library decides to write to disk and if files should be...
        // deleted after the request ends.

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        // setSizeThreshold sets a maximum file size
        factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        // setFileCleaningTracker, when set to null, leaves the temporary files untouched.
        // By default, it deletes them after the request has finished.
        factory.setFileCleaningTracker(null);

        // Configure a repository (to ensure a secure temp location is used)
        ServletFileUpload upload = new ServletFileUpload(factory);

/*        try {
            // Parse the request and generate a list of FileItem
            List<FileItem> items = upload.parseRequest(servletRequest);

            // Process the uploaded items
            Iterator<FileItem> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem fileItem = iter.next();

                // extract the InputStream and to call the useful copy method
                if (!fileItem.isFormField()) {
                    try (InputStream uploadStream = fileItem.getInputStream();
                         OutputStream out = new FileOutputStream("file.mov")) {
                        // Now we have our file stored in the necessary folder
                        IOUtils.copy(uploadStream, out);
                    }
                }
            }*/
        // Now, with the Streaming API
        // The streaming API is easy to use, making it a great way to
        // process large files simply by not copying to a temporary location
        upload = new ServletFileUpload();

        // FileItemIterator, doesn't read anything until we extract them from the request with the next method.
        FileItemIterator iterStream = upload.getItemIterator(servletRequest);
        while (iterStream.hasNext()) {
            FileItemStream item = iterStream.next();
            String name = item.getName();
            System.out.println(name);
            InputStream inputStream = item.openStream();
            if (!item.isFormField()) {
                // process the inputStream
            } else {
                // process the fields
                String formFieldValue = Streams.asString(inputStream);
                System.out.println(formFieldValue);
            }
        }
        return "success!";
    } /*catch (IOException | FileUploadException ex) {
            return "failed: " + ex.getMessage();
        }*/
}

