import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ReadFiles {
    public static String readMesas(){
        int mesas[][];
        String result ="";


        try {
            File file = new File("mesas.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while((line = reader.readLine()) != null) {
                String[] linhas = line.split(",");

                result.concat("Mesa: " + linhas[0] + " Numero de pessoas: " + linhas[1] + "Estado: " + linhas[2]);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}