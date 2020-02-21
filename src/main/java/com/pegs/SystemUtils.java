package com.pegs;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * user: shailendra
 * Date: 21/02/20
 * Time: 5:52 PM
 */
public class SystemUtils {

    private static Properties properties ;

    static {
        try {
            loadProperties();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void loadProperties() throws Exception{
        InputStream input = SystemUtils.class.getClassLoader().getResourceAsStream("application.properties");
        properties = new Properties();
        properties.load(input);
    }

    public static String getValue(String key){
        return properties.getProperty(key);
    }

    public static String readFile(File file) throws Exception {
        String retVal = "";
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                retVal = retVal + line;
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return retVal;
    }

    public static void write(String input,String toLocation) throws Exception{
        FileUtils.writeStringToFile(new File(toLocation), input, StandardCharsets.UTF_8);
    }

    public static String convertInputStreamToString(InputStream inputStream)
            throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(StandardCharsets.UTF_8.name());

    }
}
