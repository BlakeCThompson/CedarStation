package WeatherStation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Launcher {
public static File baseDir;
public static File tokenFile;
    public static void main(String[] args){
        try {
            if (initialize()) {

                WeatherCaller.main(args);
            }
            else System.out.println("Error! Could not initialize.");
        }catch(IOException ioException){
            ioException.printStackTrace();
        }

    }

    private static boolean initialize() throws IOException {
        baseDir = new File(String.valueOf(Paths.get(System.getProperty("user.home"),"cedarStation","token")));
        tokenFile = new File(baseDir.getPath()+"/.token");
        if(baseDir.exists() && tokenFile.exists()){
            return true;
        }
        else{
        baseDir.mkdirs();
        if(!tokenFile.createNewFile()){
            System.out.println("Could not create token file at "+tokenFile.getPath()+". May not have permission.");
            return false;
        }
        return true;
        }
    }
}

