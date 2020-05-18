import java.io.File
import java.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import collection.JavaConverters._

object DataGenerator {
  def main(args: Array[String]): Unit = {
    val doc = Jsoup.parse(new File("./src/main/resources/notes/dreamnotes.html"), null)
    val notes = new util.ArrayList[Note]
    val body = doc.select("body").html()
    val x = body.split("<hr>").length
    body.split("<hr>").foreach(
      s => addToNotes(s, notes))

    //    println(notes)
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

    val content = noteHtml.select("span").html()

    if (
      !tags.contains("清醒梦") && (name.contains("清醒梦") || content.contains("清醒梦") || content.contains("清明梦"))) {
      println(name)
    }

    //    note.setName(name)
    //    note.setCreatedDate(name.substring(0, 7))

    target.add(note)
  }
}