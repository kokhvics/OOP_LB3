package org.example.oop_lb3;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

@WebServlet(name = "registration", value = "/registration")
public class registration extends HttpServlet {
    private static final String filePath = "tutors.json";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String lastname = request.getParameter("lastname");
        String subject = request.getParameter("subject");
        int age = Integer.parseInt(request.getParameter("age"));
        String contact = request.getParameter("contact");

        JSONObject tutor = new JSONObject();
        tutor.put("name", name);
        tutor.put("lastname", lastname);
        tutor.put("subject", subject);
        tutor.put("age", age);
        tutor.put("contact", contact);

        JSONArray tutorsList = new JSONArray();
        try {
            JSONParser parser = new JSONParser();
            File file = new File(filePath);
            String fullPath = file.getAbsolutePath();
            System.out.println(fullPath);
            if (!file.exists()){
                file.createNewFile();
            }

            if (file.exists()){
                FileReader reader = new FileReader(filePath);
                tutorsList = (JSONArray) parser.parse(reader);
                reader.close();
            }
            tutorsList.add(tutor);
            FileWriter writer = new FileWriter(filePath);
            writer.write(tutorsList.toJSONString());
            writer.close();
        } catch (IOException | ParseException e){
            e.printStackTrace();
        }
        response.sendRedirect("index.jsp");
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)  {

        try {
            JSONParser parser = new JSONParser();
            JSONArray tutorsList = (JSONArray) parser.parse(new FileReader(filePath));

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Таблица</title><link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"></head><body><div class=\"container mt-5\"><h2>Данные о репетиторах школы</h2><table class=\"table table-bordered\"><thead><tr><th style=\"width: 20px\">#</th><th>Имя</th><th>Фамилия</th><th>Предмет</th><th>Возраст</th><th>Контактная информация</th></tr></thead>");

            int counter = 1;
            for (Object obj : tutorsList) {
                JSONObject tutor = (JSONObject) obj;
                out.println("<tbody><tr><td>" + counter + "</td><td>" + tutor.get("name") + "</td><td>" + tutor.get("lastname") + "</td><td>" + tutor.get("subject") + "</td><td>" + tutor.get("age") + "</td><td>" + tutor.get("contact") + "</td></tr>");
                counter++;
            }

            out.println("</tbody></table></div><script src=\"js/bootstrap.bundle.min.js\"></script></body></html>");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

}