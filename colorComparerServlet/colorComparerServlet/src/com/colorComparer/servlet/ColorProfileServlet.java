package com.colorComparer.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.colorComparer.ColorComparer;

@MultipartConfig
public class ColorProfileServlet extends HttpServlet {

	private static final long serialVersionUID = -3765738018606119921L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean profileReturned = false;

		for (Part part : request.getParts()) {
			String filename = getFilename(part);
			if (filename == null) {
				// Handle "normal" fields here
			} else if (!filename.isEmpty()) {
				filename = filename.substring(filename.lastIndexOf('/') + 1)
						.substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
				InputStream fileContent = part.getInputStream();
				BufferedImage image = ImageIO.read(fileContent);
				ServletOutput.write(response,
						ColorComparer.getRgbProfile(image));
				profileReturned = true;
			}
		}

		if (!profileReturned) {
			throw new IOException("Ok...");
		}
	}

	// Yucky
	private static String getFilename(Part part) {
		for (String contentDisposition : part.getHeader("content-disposition")
				.split(";")) {
			if (contentDisposition.trim().startsWith("filename")) {
				return contentDisposition
						.substring(contentDisposition.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;
	}

}
