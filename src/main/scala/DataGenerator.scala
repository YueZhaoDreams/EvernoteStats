import java.io.File
import java.util

import evernote.Note
import org.jsoup.Jsoup
import report.{NoteNameByKeywordReport, TagReport}

import scala.collection.JavaConverters._

object DataGenerator {
  def main(args: Array[String]): Unit = {

    val notes = loadNotes("./src/main/resources/notes/dreamnotes.html")
    val report = new NoteNameByKeywordReport(notes, "清醒梦")
    report.toCsv()

    val report2 = new TagReport(notes)
    report2.toCsv()
  }

  def loadNotes(fileLocation: String): util.List[Note] = {
    val doc = Jsoup.parse(new File(fileLocation), null)
    val notes = new util.ArrayList[Note]
    val body = doc.select("body").html()
    body.split("<hr>").foreach(
      s => addToNotes(s, notes))
    notes
  }

  def addToNotes(value: String, target: util.List[Note]): Unit = {
    val noteHtml = Jsoup.parse(value)

    val name = noteHtml.select("h1").html().trim
    val trs = noteHtml.select("tr")
    val note = new Note()
    val tags = if (trs.size() >= 3) {
      val tagArray: Array[String] = noteHtml.select("tr").get(2).select("i").html().split(",").map(s => s.trim)
      new util.HashSet[String](util.Arrays.asList[String](tagArray: _*))
    } else new util.HashSet[String]()

    val content = noteHtml.select("span").html().replaceAll("<div>", "").replaceAll("</div>", "")

    note.setContent(content)
    note.setName(name)
    note.setTags(tags)



    //    note.setName(name)
    //    note.setCreatedDate(name.substring(0, 7))

    target.add(note)
  }

  def filterNotesWithMissingTag(target: util.List[Note], tagName: String): util.List[Note] = {
    val filteredNotes = new util.ArrayList[Note]

    def addToFilterNotes(note: Note) = {
      val name = note.getName()
      val tags = note.getTags()
      val content = note.getContent
      if (name.matches("""^\d{8}.*""") &&
        (!tags.contains(tagName) && (name.contains(tagName) || content.contains(tagName) || content.contains(tagName)))) {
        filteredNotes.add(note)
      }
    }

    for (note: Note <- target.asScala) {
      addToFilterNotes(note)
    }
    filteredNotes
  }
}