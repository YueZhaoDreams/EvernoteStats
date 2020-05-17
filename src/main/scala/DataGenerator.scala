import org.jsoup.Jsoup

object DataGenerator {
  def main(args: Array[String]): Unit = {
    println("hello")
    val doc = Jsoup.connect("http://example.com").get
    doc.select("p").forEach(println)
  }
}