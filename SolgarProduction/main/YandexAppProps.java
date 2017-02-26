package main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

public class YandexAppProps {

	static final String PRODUCT_NAME = "YandexAddress";
    static final String LIB_DIRNAME = "properties";
    static final boolean isTraceError  =  false;
    static final String PROPERTIES_FILENAME = "Yandex.properties";
    private static final Properties properties = new Properties();
    private static String lib_path; // defaults to null


    static {
        try {
            setProperties();
           // System.out.println(getProperty("WUGatewayClient.Port"));
        } catch (Exception e) {
            String msg = "Warning: Unable to load " + PROPERTIES_FILENAME;
            if (lib_path != null)
                msg += " from directory " + lib_path;
            if (isTraceError) { 
                System.err.println(msg);
                e.printStackTrace();
            }
        }
    }
/**
 * WUGatewayProperties constructor comment.
 */
public YandexAppProps() {
    super();
}
    /**
 <pre>
 Returns library path.
 </pre>
 @servicename None
 @return String
 @sideeffects None
 @specialnotes None
 @algorithm None
*/
    static String getLibraryPath() throws IOException {
        if (lib_path == null) throw new IOException(PRODUCT_NAME + " library directory (" +
            LIB_DIRNAME + ") could not be found");
        return lib_path;
    }
/**
 <pre>
 Gets the given property from properties
 </pre>
 @servicename None
 @param key String
 @return String
 @sideeffects None
 @specialnotes None
 @algorithm None
*/  
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    /**
 <pre>
 Gets the given property from properties. If it can not find, it returns the default value
 </pre>
 @servicename None
 @param key String
 @param defaultValue String
 @return String
 @sideeffects None
 @specialnotes None
 @algorithm None
*/
    static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
/**
 <pre>
 Lists the contents of properties to given PrintStream
 </pre>
 @servicename None
 @param out java.io.PrintStream
 @return void
 @sideeffects None
 @specialnotes None
 @algorithm None
*/  
    static void list(PrintStream out) {
        properties.list(out);
    }
/**
 <pre>
 Lists the contents of properties to given PrintWriter
 </pre>
 @servicename None
 @param out java.io.PrintWriter
 @return void
 @sideeffects None
 @specialnotes None
 @algorithm None
*/  
    static void list(PrintWriter out) {
        properties.list(out);
    }
/**
 <pre>
 Returns the list of names of properties
 </pre>
 @servicename None
 @return java.util.Enumeration
 @sideeffects None
 @specialnotes None
 @algorithm None
*/  
    static Enumeration propertyNames() {
        return properties.propertyNames();
    }
/**
 <pre>
 Stores given comment in the output stream
 </pre>
 @servicename None
 @param out java.io.OutputStream
 @param comment String
 @return void
 @sideeffects None
 @specialnotes None
 @algorithm None
*/  
    static void save(OutputStream os, String comment) {
/*      try {
            properties.store(os, comment);
        } catch (IOException e) {
            System.out.println("Properties can not be stored!!!");
        }
*/      
        properties.save(os, comment); //E       
    }
/**
 <pre>
 Reads the contents of property file and put them to properties
 </pre>
 @servicename None
 @return void
 @sideeffects None
 @specialnotes None
 @algorithm <pre>
 Searches the property file specified in classpath,
 When it finds, loads the properties 
 </pre>
*/      
    private static void setProperties() {
   
        String classpath = System.getProperty("java.class.path");
        String ps = System.getProperty("path.separator");
        String fs = System.getProperty("file.separator");
       
        //System.out.println("classpath - " + classpath);
        
        StringTokenizer st = new StringTokenizer(classpath, ps);
        
        //boolean loaded = false;
        while (st.hasMoreTokens()) {
            String entry = st.nextToken();
            File f;

            if (entry.endsWith(".zip") || entry.endsWith(".jar")) {
                System.out.println("entry - " + entry);
                int i = entry.lastIndexOf(fs);
                if (i == -1)
                    continue; // ignore .zip/.jar files specified by relative paths

                f = new File(entry.substring(0, i), LIB_DIRNAME);
            } else {
                f = new File(entry, LIB_DIRNAME);
            }

            if (f.isDirectory()) {
                String path = f.getAbsolutePath();
                if (!path.endsWith(fs)) {
                    path += fs;
                }
                //System.out.print("path - " + entry);
                File pf = new File(path, PROPERTIES_FILENAME);
                if (pf.isFile()) {
                    try {
                        BufferedInputStream in =
                            new BufferedInputStream(new FileInputStream(pf));
                        properties.load(in);
                        in.close();
                        lib_path = path;
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (isTraceError) {
            System.err.println(
                "Warning: failed to load the " + PROPERTIES_FILENAME + " file.\n" +
                "Make sure that the CLASSPATH entry for " + PRODUCT_NAME + " is an absolute path.");
        }
        
    }


}