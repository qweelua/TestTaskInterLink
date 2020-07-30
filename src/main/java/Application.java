import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Application {
    public static void main(String[] args) throws IOException {
        String filePath = "." + File.separator + "src" + File.separator +
                "main" + File.separator + "resources" + File.separator + "task.csv";
        try (FileInputStream fis = new FileInputStream(filePath);
             InputStreamReader isr = new InputStreamReader(fis);
             CSVReader reader = new CSVReader(isr)) {
            String[] nextLine;
            List<String> names = new ArrayList<>();
            List<String> dates = new ArrayList<>();
            List<String> workTime = new ArrayList<>();
            while ((nextLine = reader.readNext()) != null) {
                names.add(nextLine[0]);
                dates.add(nextLine[1]);
                workTime.add(nextLine[2]);
            }

            Map<String, Map<String, String>> nameWorkTimeMap = new HashMap<>();
            for (int i = 1; i < names.size(); i++) {
                Map<String, String> workTimesByName = nameWorkTimeMap.get(names.get(i));
                if (workTimesByName == null) {
                    HashMap<String, String> dateWorkTimeMap = new LinkedHashMap<>();
                    dateWorkTimeMap.put(dates.get(i), workTime.get(i));
                    nameWorkTimeMap.put(names.get(i), dateWorkTimeMap);
                } else {
                    workTimesByName.put(dates.get(i), workTime.get(i));
                    nameWorkTimeMap.put(names.get(i), workTimesByName);
                }
            }

            String fileName = "." + File.separator + "src" + File.separator +
                    "main" + File.separator + "resources" + File.separator + "result.csv";
            try (FileOutputStream fos = new FileOutputStream(fileName);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 CSVWriter writer = new CSVWriter(osw)) {
                String[] datesArray = dates.stream().distinct().toArray(String[]::new);
                datesArray[0] = "Name/Date";
                writer.writeNext(datesArray);
                for (int i = 1; i < names.size(); i++) {
                    ArrayList<String> line = new ArrayList<>();
                    String s = names.get(i);
                    line.add(s);
                    for (int j = 1; j < datesArray.length; j++) {
                        String date = nameWorkTimeMap.get(s).get(datesArray[j]);
                        if (date == null)
                            line.add("0");
                        else line.add(date);
                    }
                    writer.writeNext(line.stream().toArray(String[]::new));
                }
            }
        }
    }
}