package report;

import evernote.Note;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class TagReport implements Report {
    private List<Note> notes;

    public TagReport(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public void toCsv() {
        try {
            File target = new File(outputFolder + "/tagReport.csv");
            target.getParentFile().mkdirs();
            target.createNewFile();
            FileWriter csvWriter = new FileWriter(target);

            csvWriter.append("tag,date\n");

            DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
            int relatedRowNumber = 0;
            for (Note note : notes) {
                String name = note.getName();
                Set<String> tags = note.getTags();

                if (!name.matches("^\\d{8}.*")) {
                    System.out.println("Name is " +name);
                    continue;
                }

                String date = LocalDate.parse(name.substring(0, 8), inputDateFormat)
                        .atStartOfDay().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                for (String tag : tags) {
                    csvWriter.append(tag + "," + date + "\n");
                    relatedRowNumber++;
                }
            }
            csvWriter.flush();
            csvWriter.close();
            System.out.println("Report generated at " + target.getAbsolutePath());
            System.out.println("Printed " + relatedRowNumber + " rows of tags");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
