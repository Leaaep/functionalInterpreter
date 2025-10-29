package org.example

import interpreter.Interpreter.interpret
import lexer.Lexer.lex
import parser.Parser.parse
import ResultPrinter.printResult

import scala.annotation.tailrec
import scala.io.StdIn.readLine

object Main extends App {
  private val consoleToken = "> "

  @tailrec
  def loop(): Unit = {
    print(consoleToken)
    val input = readLine()
    input match {
      case "exit" => println("Program exiting...")
      case _ => {
        val tokens = lex(input)
        parse(tokens) match {
          case Some(parsed) => {
            val interpreted = interpret(parsed)
            printResult(interpreted)
          }
          case None => println("Invalid syntax!")
        }
        loop()
      }
    }
  }
  loop()
}
