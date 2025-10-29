package org.example

import types.LiteralValue.*
import types.Expr.*

object ResultPrinter {
  def printResult(interpreted: Option[Literal]): Unit = {
    val result = interpreted match {
      case Some(value) => value
      case None => throw Exception("")
    }
    result.value match {
      case LiteralInt(value) =>
        val reduced = value.toString.toList.map(_.toString).reduce(_ + _)
        println(reduced)
      case LiteralString(value) => println(value)
      case LiteralBool(value) => println(value)
      case LiteralNil => println("nil")
    }
  }
}
