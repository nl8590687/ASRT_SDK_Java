package me.ailemon.asrt.webdemo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ailemon.asrt.webdemo.classes.ApiUtils;
import me.ailemon.asrt.webdemo.classes.wav;



/**
 * Servlet implementation class upload
 */
@WebServlet("/upload")
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public upload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("UTF-8");
		
		String blob_base64 = request.getParameter("upfile_b64");
		byte[] bytes_wav = ApiUtils.Convert_Base64ToBytes(blob_base64);
		wav wavefile = new wav();
		wavefile.GetFromBytes(bytes_wav);
		//response.getWriter().append(new String(bytes_wav, "utf-8"));
		//System.out.println(wavefile.fs);
		//System.out.println(wavefile.channels);
		//System.out.println(wavefile.sample_width);
		//System.out.print(wavefile.samples.length);
		String result = ApiUtils.SendToASRTServer("qwertasd", wavefile.fs, wavefile.samples);
		//System.out.println(result);
		//response.getWriter().append("Served at: ").append(request.getContextPath()).append(blob_base64);
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().append(result);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
