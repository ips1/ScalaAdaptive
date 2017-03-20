package runtime

/**
  * Created by pk250187 on 3/19/17.
  */
trait OptionSelector[TRecordItem] {
  def selectOption(records: Seq[Bucket[TRecordItem]]): Bucket[TRecordItem]
}
