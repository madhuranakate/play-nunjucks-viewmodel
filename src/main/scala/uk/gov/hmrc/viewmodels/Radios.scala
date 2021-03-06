package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.i18n.Messages
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Radios {

  final case class Radio(label: Text, value: String)
  final case class Item(id: String, text: Text, value: String, checked: Boolean)

  object Item {
    implicit def writes(implicit messages: Messages): OWrites[Item] =
      Json.writes[Item]
  }

  def apply(field: Field, items: Seq[Radio]): Seq[Item] = {

    val head = items.headOption.map {
      item =>
        Item(
          id      = field.id,
          text    = item.label,
          value   = item.value,
          checked = field.values.contains(item.value)
        )
    }

    val tail = items.zipWithIndex.tail.map {
      case (item, i) =>
        Item(
          id      = s"${field.id}_$i",
          text    = item.label,
          value   = item.value,
          checked = field.values.contains(item.value)
        )
    }

    head.toSeq ++ tail
  }

  def yesNo(field: Field)(implicit messages: Messages): Seq[Item] = Seq(
    Item(id = field.id, text = msg"site.yes", value = "true", checked = field.value.contains("true")),
    Item(id = s"${field.id}-no", text = msg"site.no", value = "false", checked = field.value.contains("false"))
  )
}
