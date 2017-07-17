package scalaadaptive.core.configuration.blocks.persistence

import scalaadaptive.core.configuration.BaseLongConfiguration
import scalaadaptive.core.logging.Logger
import scalaadaptive.core.runtime.history.historystorage.PersistentHistoryStorage

/**
  * Created by Petr Kubat on 7/17/17.
  *
  * Block that disables persistent storage for the framework. The functions with Storage.Persistent setup will use
  * the Storage.Global in such a case.
  *
  */
trait NoPersistence extends BaseLongConfiguration {
  override def createPersistentHistoryStorage(log: Logger): Option[PersistentHistoryStorage[TMeasurement]] = None
}
