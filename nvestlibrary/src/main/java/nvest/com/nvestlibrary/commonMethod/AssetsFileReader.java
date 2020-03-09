package nvest.com.nvestlibrary.commonMethod;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AssetsFileReader {

    public String readFile(AssetManager assets, String fileName) {
        StringBuilder fileContentStringBuilder = new StringBuilder();
        BufferedReader fileBufferedreader = null;
        try {
            fileBufferedreader = new BufferedReader(new InputStreamReader(assets.open(fileName)));
            String line;
            while ((line = fileBufferedreader.readLine()) != null) {
                fileContentStringBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileBufferedreader != null) {
                    fileBufferedreader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileContentStringBuilder.toString();
    }



}
