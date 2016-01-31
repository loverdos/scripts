/*
 * Copyright (c) 2013-2014 Christos KK Loverdos
 */

import java.io.{FileFilter, File}
import scala.language.implicitConversions


def ext(f: File): String = {
  val name = f.getName
  f.getName.lastIndexOf('.') match {
    case 0 ⇒ ""
    case i if i == name.length - 1 ⇒ ""
    case i ⇒ name.substring(i + 1)
  }
}

def sum2sD(sumForExt: Long): String =
  if(sumForExt < 1024)
    s"$sumForExt B"
  else if(sumForExt < 1024 * 1024)
    s"${(sumForExt + 1023) / 1024} KB"
  else if(sumForExt < 1024 * 1024 * 1024)
    s"${(sumForExt + 1024 * 1024 - 1) / (1024 * 1024)} MB"
  else
    s"${(sumForExt + 1024 * 1024 * 1024 - 1) / (1024 * 1024 * 1024)} GB"

def sum2sF(sumForExt: Long): String =
  if(sumForExt < 1024)
    s"$sumForExt B"
  else if(sumForExt < 1024 * 1024)
    f"${sumForExt / 1024.0}%.3f KB"
  else if(sumForExt < 1024 * 1024 * 1024)
    f"${sumForExt / (1024.0 * 1024)}%.3f MB"
  else
    f"${sumForExt / (1024.0 * 1024 * 1024)}%.3f GB"

val files =
  args match {
    case Array() ⇒
      val files = new File(".").
        listFiles(new FileFilter { def accept(file: File) = file.isFile })
      Option(files).getOrElse(Array()).toSeq

    case _ ⇒
      args.toSeq.
        map(new File(_)).
        filter(_.isFile)
  }

files.
  groupBy(ext).
  map { case (ext, filesByExt) ⇒
    ext → filesByExt.map(_.length()).sum
  }.
  toSeq.
  sortWith(_._2 < _._2).
  foreach { case (ext, sumForExt) ⇒
    val sumS = sum2sF(sumForExt)
    println(s"$ext\t$sumS\t($sumForExt)")
  }