package com.zzwarn.servlets;


import net.iharder.Base64;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.Paths;

@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        String filename =request.getParameter("filename");
        response.setHeader("content-Type", "application/octet-stream");
        String agent =request.getHeader("User-agent");

        if(agent.toLowerCase().indexOf("firefox")!=-1){
            response.setHeader("content-Disposition", "attachment;filename==?UTF-8?B?"+ Base64.encodeBytes(filename.getBytes()) +"?=");//这里文件名包含后缀
        }else{
            response.setHeader("content-Disposition", "attachment;filename="+ URLEncoder.encode(filename, "UTF-8"));//这里文件名包含后缀
        }

        InputStream input = getServletContext().getResourceAsStream("/img/disposed/"+filename);
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] bs = new byte[1024];
        int len =-1;

        while((len=input.read(bs))!=-1){
            outputStream.write(bs,0, len);
        }

        System.out.println(filename +" Successfully download!");
        outputStream.close();
        input.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
