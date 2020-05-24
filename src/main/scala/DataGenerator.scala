import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util

import org.jsoup.Jsoup

import scala.collection.JavaConverters._

object DataGenerator {
  val inputDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")

  def main(args: Array[String]): Unit = {
    val doc = Jsoup.parse(new File("./src/main/resources/notes/dreamnotes.html"), null)
    val notes = new util.ArrayList[Note]
    val body = doc.select("body").html()
    val x = body.split("<hr>").length
    body.split("<hr>").foreach(
      s => addToNotes(s, notes))

    generateReportByKeyword(notes, "清醒梦")
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

  def generateReportByKeyword(target: util.List[Note], keyword: String): Unit = {
    for(note <- target.asScala) {
      val name = note.getName
      val tags = note.getTags
      if (name.matches("""^\d{8}.*""") && tags.contains(keyword)) {
        println(name + "; " + LocalDate.parse(name.substring(0, 8), inputDateFormat).atStartOfDay().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))
      }
    }
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