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
import java.lang.reflect.Parameter;
import java.lang.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FrontServlet extends HttpServlet
    {

        HashMap<String, Mapping> mappingUrls;

        public void init() throws ServletException {
            super.init();
            this.setMappingUrls(analyzeModelsDirectory());
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

        

        private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws  ServletException, IOException {
            PrintWriter out = resp.getWriter();
        try{
            String urlPattern = getURLPattern(req);
                Mapping mapping = mappingUrls.get(urlPattern);
                
                if(mapping != null) {
                Map<String, String[]> params = req.getParameterMap();
                ModelView mv = getMethodeMV(mapping,out,params);

                if (mv.getData() != null) {
                    for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
                
                if (!params.isEmpty()){
                    for (String param : params.keySet()) {
                    String[] values = params.get(param);
                        for (int i = 0; i < values.length; i++) {
                            req.setAttribute(param, values[i]);
                        }
                    }
                }
                
                RequestDispatcher dispat = req.getRequestDispatcher(mv.getView());
                dispat.forward(req, resp);
                    
            
                }else{
                    out.println("none");
                }
            }catch(Exception e){
                out.println(e);
            }
        }

        private ModelView getMethodeMV(Mapping mapping, PrintWriter out, Map<String, String[]> params) throws Exception {
            String className = mapping.getClassName();
            String methodName = mapping.getMethod();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> load = loader.loadClass("etu1946.framework.models." + className);
            Method method = null;

            // Recherche de la méthode par nom
            for (Method m : load.getMethods()) {
                if (m.getName().equals(methodName)) {
                    method = m;
                    break;
                }
            }


            Url annotation = method.getAnnotation(Url.class);
            Object[] arguments = new Object[annotation.params().length];
            String[] valeurs = annotation.params();
            String[] args = annotation.params();
            
            
            Parameter[] parameters = method.getParameters();

            for (int i = 0; i < args.length; i++) {
                Class<?> parameterType = parameters[i].getType();
                boolean found = false;
                for (String param : params.keySet()) {
                    if (args[i].equals(param)) {
                        String[] values = params.get(param);
                        arguments[i] = castValueWithType(parameterType, values[0]);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    arguments[i] = null;
                }
            }

            Object obj = load.getConstructor().newInstance();
            ModelView mv = (ModelView) method.invoke(obj, arguments);
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

        private Object castValueWithType(Class<?> parameterType, String value) {
            if (parameterType == int.class || parameterType == Integer.class) {
                return Integer.parseInt(value);
            } else if (parameterType == double.class || parameterType == Double.class) {
                return Double.parseDouble(value);
            } else if (parameterType == float.class || parameterType == Float.class) {
                return Float.parseFloat(value);
            } else if (parameterType == long.class || parameterType == Long.class) {
                return Long.parseLong(value);
            } else if (parameterType == boolean.class || parameterType == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (parameterType == String.class) {
                return value;
            } else if (parameterType == Date.class) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return dateFormat.parse(value);
                } catch (ParseException e) {
                    // Handle the parse exception if needed
                    e.printStackTrace();
                    return null;
                }
            } else {
                // Handle other types if needed
                return null; // Return null for unsupported types
            }
        }


    }
