package report;

import evernote.Note;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class NoteNameByKeywordReport implements Report {
    private List<Note> notes;
    private String keyword;

    public NoteNameByKeywordReport(List<Note> notes, String keyword) {
        this.notes = notes;
        this.keyword = keyword;
    }

    @Override
    public void toCsv() {
        try {
            File target = new File("./target/reports/noteNameByKeywordReport.csv");
            target.getParentFile().mkdirs();
            target.createNewFile();
            FileWriter csvWriter = new FileWriter(target);
            DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
            int relatedNoteNumber = 0;
            for (Note note : notes) {
                String name = note.getName();
                Set<String> tags = note.getTags();
                if (name.matches("^\\d{8}.*") && tags.contains(keyword)) {
                    csvWriter.append(name + "; " + LocalDate.parse(name.substring(0, 8), inputDateFormat)
                            .atStartOfDay().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + "\n");
                    relatedNoteNumber++;
                }
            }
            csvWriter.flush();
            csvWriter.close();
            System.out.println("Report generated at " + target.getAbsolutePath());
            System.out.println("Printed " + relatedNoteNumber + " notes with keyword " + keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print() {
        DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        int relatedNoteNumber = 0;
        for (Note note : notes) {
            String name = note.getName();
            Set<String> tags = note.getTags();
            if (name.matches("^\\d{8}.*") && tags.contains(keyword)) {
                System.out.println(name + "; " + LocalDate.parse(name.substring(0, 8), inputDateFormat)
                        .atStartOfDay().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                relatedNoteNumber++;
            }
        }
        System.out.println("Printed " + relatedNoteNumber + " notes with keyword " + keyword);
    }
}
