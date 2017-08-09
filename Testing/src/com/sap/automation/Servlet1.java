package com.sap.automation;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.sap.jsonmodels.InputData;

/**
* Servlet implementation class Servlet1
*/
@WebServlet("/Servlet1")
public class Servlet1 extends HttpServlet {
       private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet1() {
        super();
        // TODO Auto-generated constructor stub
    }

       /**
       * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
       */
       protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              // TODO Auto-generated method stub
              String inputData = request.getParameter("createData");
              System.out.println(inputData);
              Gson gson = new Gson();
              InputData requestParameters = gson.fromJson(inputData, InputData.class);
              CodeGeneration.generateFiles(requestParameters);
              response.getWriter().append("Served at: ").append(request.getContextPath());
       }

       /**
       * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
       */
       protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              // TODO Auto-generated method stub
              doGet(request, response);
       }

}
