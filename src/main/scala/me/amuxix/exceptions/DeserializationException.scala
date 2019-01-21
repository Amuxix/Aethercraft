package me.amuxix.exceptions

case class DeserializationException(what: String) extends Exception(s"Failed to deserialize $what.")
