import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;


public class Parser {

    protected ArrayList<Data> parseCsv() {
        String csvFile = "sports.csv";
        String line;
        String csvSplitBy = ", \"";
        int count =0;
        ArrayList<Data> datas = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                count++;
                if (count == 1) { continue; }

                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (values.length != 5) { continue; }
                try {
                    int i = Integer.parseInt(values[0]);

                    if (hashSet.add(values[1])) {
                        Data data = new Data(i, values[1], values[2], values[3], values[4]);
                        datas.add(data);
                    }
                }  catch (Exception _) {
                };

            }
        } catch (IOException e) {
            System.out.println("ОШИБКА");
        }
        return datas;
    }



}



