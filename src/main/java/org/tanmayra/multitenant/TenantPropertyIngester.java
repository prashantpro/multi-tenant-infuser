package org.tanmayra.multitenant;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/multi-tenant-config")
public class TenantPropertyIngester extends HttpServlet {
	private static final Logger logger = Logger.getLogger(TenantPropertyIngester.class.getName());
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String htmlForm = "<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<title>Tenant Config</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<form action=\"multi-tenant-config\" method=\"POST\">\n" + 
				"	<select name=\"tenant-selection\">\n" + 
				"		$SELECT_ITEMS\n" + 
				"	</select>\n" + 
				"	\n" + 
				"	<button type=\"submit\" value=\"Submit\">Submit</button>\n" + 
				"</form>\n" + 
				"</body>\n" + 
				"</html>";
		htmlForm = buildForm(htmlForm);
			
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(htmlForm);
		out.flush();
		out.close();
	}

	private String buildForm(String htmlForm) {
		String selectOptions = buildOptions();
		return htmlForm.replace("$SELECT_ITEMS", selectOptions);
	}

	private String buildOptions() {
		StringBuilder options = new StringBuilder();
		for(String tenant : WebTenantResolver.getTenants()) {
			options.append("<option value=\""+ tenant + "\">" + tenant + "</option>");
		}
		return options.toString();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		String value = request.getParameter("tenant-selection");
		addIDonlyForSingleTenant(value);
		response.getWriter().println("done");
	}

	private void addIDonlyForSingleTenant(String identifier) {
		WebTenantResolver.addIDonlyForSingleTenant(identifier, "/travel");
	}

}
