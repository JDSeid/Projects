package jrails;

import java.lang.reflect.*;
import java.util.*;

import javax.management.RuntimeErrorException;

import java.io.*;

//TODO: Handle commas within column names

public class Model {

    private static HashMap<Class<?>, File> modelFiles = new HashMap<>();
    private int id = 0;
    private ArrayList<Integer> usedIds = new ArrayList<>();
    private static int maxId = -1;

    public void save() {
        
        /* this is an instance of the current model */
        ArrayList<Object> columns = getColumns();
        BufferedWriter writer;
        
        if(maxId == -1){
            maxId = lastId();
        }
        try{
            if(this.id == 0){;
                this.id = maxId + 1;
                maxId++;
                writer = getWriter(this.getClass(), true);
                writer.write(buildLine(id, columns));
            }
            else{
                
                HashMap<Integer, String> currFile = storeFile(this.getClass());
                String newLine = buildLine(id, columns);
                if(findLine(this.getClass(), id) == null){
                    throw new RuntimeException("Cannot update nonexistance model");
                }
                currFile.put(id, newLine);
                writer = getWriter(this.getClass(), false);
                for(Integer i : currFile.keySet()){
                    writer.write(currFile.get(i));
                }
            }
            writer.close();
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    
    private String buildLine(int id, ArrayList<Object> columns){
        String line = "";
        line += this.id + ", ";
        for(int i = 0; i < columns.size(); i++){
            Object c = columns.get(i);
            line += removeCommas(c.toString());
                if(i != columns.size() - 1){
                    line += ", ";
                }
        }
        line += "\n";
        return line;
    }

    private static HashMap<Integer, String> storeFile(Class<?> c){
        BufferedReader br = getReader(c);
        HashMap<Integer, String> fileMap = new HashMap<>();
        String result = "";
        String line;
        int id;
        try{
            while((line = br.readLine()) != null){
                id = Integer.parseInt(line.substring(0, line.indexOf(",")));
                fileMap.put(id, line + "\n");
                
            }
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        
        return fileMap;
    }



    private static BufferedWriter getWriter(Class<?> c, boolean append){
        if(!modelFiles.containsKey(c)){
            modelFiles.put(c, new File(c.toString() + ".txt"));
        }
        try{
            return new BufferedWriter(new FileWriter(modelFiles.get(c), append));
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private static BufferedReader getReader(Class<?> c){
        File f;
        if(!modelFiles.containsKey(c)){
            modelFiles.put(c, new File(c + ".txt"));
            
        }
        try{
            f = modelFiles.get(c);
            f.createNewFile();
            return new BufferedReader(new FileReader(modelFiles.get(c)));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public int id() {
        return this.id;
    }


    public static <T> T find(Class<T> c, int id) {
        String line = findLine(c, id);
        if(line == null){
            return null;
        }
        //returns a fresh instance of the object with the specified id
        return makeInstance(c, line.substring((id + "").length() + 2), id);
    }

    private static ArrayList<String> getLineInfo(String line){
        ArrayList<String> columns = new ArrayList<>();
        int index = 0;
        int nextComma;
        while (index < line.length()){
            nextComma = line.indexOf(",", index);
            if(nextComma == -1){
                if(line.contains("\n")){
                    columns.add(addCommas(line.substring(index, line.indexOf("\n"))));
                }
                else{
                    columns.add(addCommas(line.substring(index)));
                }
                
                break;
            }
            else{
                columns.add(line.substring(index, nextComma));
                index = nextComma + 2;
            } 
        }
        return columns;
    }

    private static <T> List<Field> getFields(Class<T> c){
        try{
            List<Field> fields = Arrays.asList(c.getFields());
            for(Field f : fields){
                if(!f.isAnnotationPresent(Column.class)){
                    fields.remove(f);
                }
            }
            return fields;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        
    }

    private static <T> T makeInstance(Class<T> c, String line, int id){
        ArrayList<String> columns = getLineInfo(line);
        List<Field> fields = getFields(c);
        try{
            Constructor con = c.getConstructor();
            T model = (T) con.newInstance();
            Model m = (Model) model;
            Field idField = Model.class.getDeclaredField("id");
            idField.set(m, id);
            model = (T) m;
            for(int i = 0; i < fields.size(); i++){
                Class<?> t = fields.get(i).getType();
                if(t.equals(int.class)){
                    fields.get(i).set(model, Integer.parseInt(columns.get(i)));
                }
                else if (t.equals(boolean.class)){
                    fields.get(i).set(model, Boolean.parseBoolean(columns.get(i)));
                }
                else{
                    fields.get(i).set(model, columns.get(i));
                }
            }
            return model;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    private static String findLine(Class<?> c, int id){
        BufferedReader reader = getReader(c);
        String line;
        
        try{
            while((line = reader.readLine()) != null){
                if(Integer.parseInt(line.substring(0, line.indexOf(","))) == id){
                    return addCommas(line);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public static <T> List<T> all(Class<T> c) {
        List<T> models = new ArrayList<>();
        HashMap<Integer, String> map = storeFile(c);
        for(Integer i : map.keySet()){
            models.add(makeInstance(c, map.get(i).substring((i + "").length() + 2), i));
        }
        return models;
    }

    public void destroy() {
        HashMap<Integer, String> file = storeFile(this.getClass());
        if (!file.containsKey(id)){
            throw new RuntimeException("model not found");
        }
        BufferedWriter writer = getWriter(this.getClass(), false);
        try{
            for(Integer i : file.keySet()){
                if(i != this.id){
                    writer.write(file.get(i));
                }
            }
            writer.close();
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
       
    }

    public static void reset() {
        maxId = -1;
        try{
            for(Class<?> c : modelFiles.keySet()){
                getWriter(c, false).close();
            }
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
        
        
    }

    private  ArrayList<Object> getColumns(){
        Field[] fields = this.getClass().getFields();
        ArrayList<Object> columns = new ArrayList<>();
        try{
            for(Field f : fields){
                if(f.getAnnotation(Column.class) != null){
                    Object val = f.get(this);
                    if((val instanceof String) || (val instanceof Integer) || (val instanceof Boolean) || val == null){
                        columns.add(val);
                    }
                    else{
                        throw new IllegalArgumentException();
                    }
                    
                }
            }
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return columns;
    }
    

    private static int lastId(){
        int max = 0;
        BufferedReader reader;
        String line;
        
        for(Class<?> c : modelFiles.keySet()){
            reader = getReader(c);
            try {
                while((line = reader.readLine()) != null){
                    int curr = Integer.parseInt(line.substring(0, line.indexOf(",")));
                    if(curr > max){
                        max = curr;
                    }
                }
            }
            catch (Exception e){
                System.out.println("error calling max id");
                return 100;
            }
        }
        return max;
    }

    private static String removeCommas(String input){
        return input.replace(",", "~!@#$%^&*()_+~");
    }

    private static String addCommas(String input){
        return input.replace("~!@#$%^&*()_+~", ",");
    }
}
