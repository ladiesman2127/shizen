package jp.aqua.shizen.item.model
import java.util.UUID


data class TocEntry(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val href: String,
    val date: Long? = null,
    var status: LoadingStatus = LoadingStatus.Loading
)