package etu1946.framework.servlet;

import etu1946.framework.Mapping;
import etu1946.framework.annotation.Url;
import etu1946.framework.annotation.Scope;
import etu1946.framework.annotation.Auth;
import etu1946.framework.view.ModelView;
import etu1946.framework.utils.Utils;

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
        HashMap<String, Object> singleton = new HashMap<>();
        HashMap<String, Mapping> mappingUrls;
        String session, profil;

        private boolean checkAuth(HttpServletRequest request, Method method) {
            Auth authentification = method.getAnnotation(Auth.class);
            if (!method.isAnnotationPresent(Auth.class))
                return false;
            if (request.getSession().getAttribute(session) == null)
                return false;
            if (((boolean) request.getSession().getAttribute(session)) == false)
                return false;

            String type = "";
            if (request.getSession().getAttribute(profil) != null) {
                type = request.getSession().getAttribute(profil).toString();
            }
            if (!authentification.type().equals(type))
                return false;
            return true;
        }

        public void init() throws ServletException {
            super.init();
            this.setMappingUrls(analyzeModelsDirectory());
            session = getServletConfig().getInitParameter("session");
            profil = getServletConfig().getInitParameter("profil");
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
                        Class<?> cls = Class.forName("etu1946.framework.models." + getClassNameFromFile(file));

                        if (((Scope) cls.getAnnotation(Scope.class)).value().equals("singleton")) {
                            singleton.put(cls.getName(), cls.newInstance());
                        }

                        Method[] methods = cls.getDeclaredMethods();
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(Url.class)) {
                                String annotationName = method.getAnnotation(Url.class).value();
                                Mapping mapping = new Mapping(getClassNameFromFile(file), method.getName());
                                mappings.put(annotationName, mapping);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
            return mappings;
        }

                

        private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PrintWriter out = resp.getWriter();
            try {
                String urlPattern = getURLPattern(req);
                Mapping mapping = mappingUrls.get(urlPattern);
                if (mapping != null) {
                    Map<String, String[]> params = req.getParameterMap();
                    ModelView mv = getMethodeMV(mapping, out, params);

                    if (mv.getData() != null) {
                        for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }
                    }

                    if (mv._session()) {
                        for (String key : mv.getSession().keySet()) {
                            req.getSession().setAttribute(key, mv.getSession().get(key));
                        }
                    }

                    Object obj = To_Object(mapping.getClassName(), params,out);
                    req.setAttribute(mapping.getMethod(), obj);
                    RequestDispatcher dispat = req.getRequestDispatcher(mv.getView());
                    dispat.forward(req, resp);

                } else {
                    out.println("none");
                }
            } catch (Exception e) {
                e.printStackTrace(out);
            }
        }

        public Object To_Object(String className, Map<String, String[]> params, PrintWriter out) throws Exception {
            Object obj = null;
            try {
                if (singleton.containsKey(className)) {
                    obj = singleton.get(className);
                    Utils.resetFieldsToDefaultValues(obj);
                } else {
                    Class<?> cls = Class.forName("etu1946.framework.models." + className);
                    obj = cls.getDeclaredConstructor().newInstance();
                }

                Field[] fields = obj.getClass().getDeclaredFields();
                String setterName;
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (params.containsKey(fieldName)) {
                        String[] values = params.get(fieldName);
                        Class<?> fieldType = field.getType();
                        Object fieldValue = Utils.castValueWithType(fieldType,values[0]);
                        setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                        Method setterMethod = obj.getClass().getDeclaredMethod(setterName, fieldType);
                        setterMethod.invoke(obj, fieldValue);
                    }
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new Exception("Error creating object: " + e.getMessage(), e);
            }
            return obj;
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
                        arguments[i] = Utils.castValueWithType(parameterType,values[0]);
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
    }
