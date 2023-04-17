package etu1946.framework.servlet;

import etu1946.framework.Mapping;
import etu1946.framework.annotation.Url;
import etu1946.framework.view.ModelView;


import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.HashMap;
import java.util.Map;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class FrontServlet extends HttpServlet
    {

        HashMap<String, Mapping> mappingUrls;

        public void init() throws ServletException {
            super.init();
            this.setMappingUrls(analyzeModelsDirectory());
        }



        private HashMap<String, Mapping> analyzeModelsDirectory() {
            HashMap<String, Mapping> mappings = new HashMap<>();
            String MODELS_DIR = getServletContext().getRealPath("/WEB-INF/classes/etu1946/framework/models");
            File dir = new File(MODELS_DIR);
            File[] files = dir.listFiles();

            for (File file : files) {
                if (file.isFile()) {
                    try {
                        Class<?> cls = Class.forName("etu1946.framework.models."+getClassNameFromFile(file));
                        Method[] methods = cls.getDeclaredMethods();
                        for (Method method : methods) {
                                String annotationName = method.getAnnotation(Url.class).value();
                                Mapping mapping = new Mapping(getClassNameFromFile(file), method.getName());
                                mappings.put(annotationName, mapping);
                        }
                    } catch (ClassNotFoundException e) {

                    }
                }
            }
            return mappings;
        }

        private String getClassNameFromFile(File file) {
            String fileName = file.getName();
            return fileName.substring(0, fileName.lastIndexOf("."));
        }

        public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
            this.mappingUrls = mappingUrls;
        }

        public HashMap<String, Mapping> getMappingUrls() {
            return this.mappingUrls;
        }

        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                processRequest(req, resp);
            } catch (Exception ex) {

            }
        }

        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                processRequest(req, resp);
            } catch (Exception ex) {

            }
        }

        private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws  ServletException, IOException {
            PrintWriter out = resp.getWriter();
        try{
            String urlPattern = "TEST/hello";
                Mapping mapping = mappingUrls.get(urlPattern);
                if(mapping != null) {

                ModelView mv = getMethodeMV(mapping);
                out.println("classe selected "+mapping.getClassName());
                out.println("Method selected"+mapping.getMethod());
                out.println("modelView.getView -> "+mv.getView());

                if (mv.getData() != null) {
                    for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
                
                RequestDispatcher dispat = req.getRequestDispatcher("WEB-INF/" + mv.getView());
                dispat.forward(req, resp);

                }else{
                    out.println("none");
                }
            }catch(Exception e){
                out.println(e);
            }
        }

        private ModelView getMethodeMV(Mapping mapping) throws Exception {
            String className = mapping.getClassName();
            String methodName = mapping.getMethod();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class load = loader.loadClass("etu1946.framework.models."+className);
            Method methode = load.getMethod(methodName);
            Object obj = load.getConstructor().newInstance();
            ModelView mv = (ModelView) methode.invoke(obj);
            return mv;
        }

        public static String getURLPattern(HttpServletRequest request) throws Exception {
            String rep = request.getServletPath();
            if(rep == null){
                rep = "/";
                return rep;
            }
            return rep.substring(1);
        }
    }
