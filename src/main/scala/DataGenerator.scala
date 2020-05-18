import java.io.File
import java.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object DataGenerator {
  def main(args: Array[String]): Unit = {
    val doc = Jsoup.parse(new File("./src/main/resources/notes/dreamnotes.html"), null)
    val elements = doc.select("h1")
    val notes = new util.ArrayList[Note]
    elements.forEach(element => addToNotes(element, notes))

//    println(notes)
  }

  def addToNotes(value: Element, target: util.List[Note]): Unit = {
    val note = new Note()
    val name = value.html()

    if (!name.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9].*")) {
      println(name)
    }

//    note.setName(name)
//    note.setCreatedDate(name.substring(0, 7))

    target.add(note)
  }
}