import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by XNBB on 2017/6/15.
 */
public class Test6 {
    public static void main(String[] args)throws Exception {
        String path = "C:\\testData\\input\\data.txt";
        String context = "hello word";

        String newPath = path.substring(0,path.lastIndexOf(".")-1);
        File file = new File(newPath);
        if (!file.exists()){
            file.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
        bw.write(context);
        bw.newLine();
        bw.flush();
        bw.close();



    }
}
