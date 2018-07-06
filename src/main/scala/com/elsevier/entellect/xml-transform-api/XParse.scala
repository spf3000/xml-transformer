package io.github.spf3000.hutsapi

import scala.xml._

object XParse {
  case class XTag(name: String, start: Int, end: Int)

  def fx(xs: String) = {
    val xml = XML.loadString(xs)
    flatX(xml)
  }


  def flatX(x: Node): Seq[XTag] = {
   def loop(nodes: Seq[Node], pos: Int, acc: Seq[XTag]): Seq[XTag] =
     if (nodes.isEmpty) acc else nodes.head match {
     case e: Elem => {
       val xt = XTag(e.label, pos, pos + e.text.length)
       loop(nodes.tail, pos, acc :+ xt)
     }
     case t: Text => loop(nodes.tail, pos + t.data.length, acc)
   }
  loop(x.descendant_or_self,0,Seq.empty[XTag])
  }
}

